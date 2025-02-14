package com.spinteam.kosch.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.Result;
import com.spinteam.kosch.Constants;
import com.spinteam.kosch.EspApi;
import com.spinteam.kosch.Prefs;
import com.spinteam.kosch.ProductsApi;
import com.spinteam.kosch.R;
import com.spinteam.kosch.SegregationRulesInformer;
import com.spinteam.kosch.Utils;
import com.spinteam.kosch.databinding.ActivityMainBinding;
import com.spinteam.kosch.listener.FinishResultListener;
import com.spinteam.kosch.model.SegregationRule;
import com.spinteam.kosch.model.SegregationRuleType;
import com.spinteam.kosch.model.Trash;
import com.spinteam.kosch.util.TtsUtils;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

@SuppressLint("DefaultLocale")
public class MainActivity extends BaseActivity implements TextToSpeech.OnInitListener, RecognitionListener, ZXingScannerView.ResultHandler {

	private static final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
	private static final Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

	private SegregationRulesInformer segregationRulesInformer;
	private ActivityMainBinding binding;
	private boolean initialized, cameraStarted;
	private final Handler handler = new Handler();
	private TtsUtils ttsUtils;
	private SpeechRecognizer speechRecognizer;
	boolean listeningNow = false;
	private final Runnable refreshRunnable = new Runnable() {
		@Override
		public void run() {
			refreshInfo();
			handler.postDelayed(refreshRunnable, 10000);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (hasPermissions())
			init();
		else
			requestPermission();
	}

	private void init() {
		segregationRulesInformer = new SegregationRulesInformer(this);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.paperButton.setOnClickListener(view -> openTrash(Trash.PAPER));
		binding.plasticButton.setOnClickListener(view -> openTrash(Trash.PLASTIC));
		binding.mixedButton.setOnClickListener(view -> openTrash(Trash.MIXED));
		binding.glassButton.setOnClickListener(view -> openTrash(Trash.GLASS));

		binding.whereShouldIThrowButton.setOnClickListener(view -> whereShouldIThrow());
		binding.scannerButton.setOnClickListener(view -> startActivityForResult(new Intent(this, ScannerActivity.class), Constants.REQUEST_CODE_EAN_SCAN));
		binding.settingsButton.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));

		//initEanScanner();

		ttsUtils = new TtsUtils(this, this);
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
		speechRecognizer.setRecognitionListener(this);

		/*commandEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_SEND) {
				processCommand(commandEditText.getText().toString());
				commandEditText.setText(null);
				return true;
			}
			return false;
		});*/

		handler.post(refreshRunnable);

		updateUI();
	}

	private void initEanScanner() {
		binding.zxingScannerView.setResultHandler(this);
		initialized = true;
		if (!cameraStarted)
			binding.zxingScannerView.startCamera(Utils.getFrontFacingCameraId(this));
	}

	private boolean hasPermissions() {
		for (String permission : permissions)
			if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
				return false;
		return true;
	}

	private void requestPermission() {
		if (!hasPermissions())
			ActivityCompat.requestPermissions(this, permissions, Constants.REQUEST_CODE_PERMISSIONS);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
			if (hasPermissions())
				init();
			else
				finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUEST_CODE_EAN_SCAN && resultCode == RESULT_OK && data != null) {
			String eanCode = data.getStringExtra(Constants.EXTRA_EAN_NUMBER);
			if (Prefs.get().getBoolean(Constants.PREF_DATABASE_ADDING_MODE, false)) {
				promptForDatabaseAdd(eanCode);
			} else {
				getTrashIdForEan(eanCode);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (initialized) {
			binding.zxingScannerView.startCamera(Utils.getFrontFacingCameraId(this));
			cameraStarted = true;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (initialized && cameraStarted) {
			binding.zxingScannerView.stopCamera();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(refreshRunnable);
	}

	private void updateUI() {
		updateTrashUI(Trash.PAPER, binding.paperTrashText, binding.paperTrashProgress);
		updateTrashUI(Trash.PLASTIC, binding.plasticTrashText, binding.plasticTrashProgress);
		updateTrashUI(Trash.MIXED, binding.mixedTrashText, binding.mixedTrashProgress);
		updateTrashUI(Trash.GLASS, binding.glassTrashText, binding.glassTrashProgress);
		binding.overlay.setVisibility(listeningNow ? View.VISIBLE : View.GONE);
	}

	private void updateTrashUI(Trash trash, TextView textView, ProgressBar progressBar) {
		int percent = trash.calculateGarbageFillPercent();
		if (percent == 100) {
			textView.setText("Błąd odczytu");
			progressBar.setProgress(0);
			progressBar.getProgressDrawable().setColorFilter(0xffffffff, android.graphics.PorterDuff.Mode.SRC_IN);
			return;
		}
		textView.setText(String.format("%d%%", percent));
		int color;
		if (percent < 50)
			color = Constants.green;
		else if (percent < 80)
			color = Constants.yellow;
		else
			color = Constants.red;
		progressBar.setProgress(percent);
		progressBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
	}

	private void openTrash(Trash trash) {
		Log.e("KOSCH", "isAllowedToOpen=" + trash.isAllowedToOpen());
		if (!trash.isAllowedToOpen()) {
			return;
		}
		trash.setLastOpen(System.currentTimeMillis());
		toast(trash.getName());
		speak(trash.getName(), null);
		Utils.runInBg(() -> {
			try {
				EspApi.openTrashes(trash.getId());
			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(() -> binding.errorText.setText(e.getMessage()));
			}
		});
	}

	private void refreshInfo() {
		Utils.runInBg(() -> {
			try {
				for (Trash remoteTrash : EspApi.getInfo()) {
					Trash trash = Trash.trashMap.get(remoteTrash.getId());
					if (trash != null) {
						trash.setOpened(remoteTrash.isOpened());
						trash.setDistance(remoteTrash.getDistance());
					}
				}
				runOnUiThread(this::updateUI);
			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(() -> binding.errorText.setText(e.getMessage()));
			}
		});
	}

	private void getTrashIdForEan(String eanNumber) {
		Utils.runInBg(() -> {
			try {
				Trash trash = ProductsApi.getTrashForEan(eanNumber);
				runOnUiThread(() -> {
					if (trash != null) {
						openTrash(trash);
					} else {
						speak(getString(R.string.productNotInDatabase), null);
						toast(R.string.productNotInDatabase);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(() -> binding.errorText.setText(e.getMessage()));
			}
		});
	}

	private void addTrashIdForEanToDatabase(String eanNumber, int trashId) {
		Utils.runInBg(() -> {
			try {
				boolean success = ProductsApi.addTrashIdForEanToDatabase(eanNumber, trashId);
				runOnUiThread(() -> toast(success ? R.string.addedToDatabase : R.string.errorAddingToDatabase));
			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(() -> binding.errorText.setText(e.getMessage()));
			}
		});
	}

	private void promptForDatabaseAdd(String eanNumber) {
		new MaterialDialog.Builder(this)
				.title(R.string.addToDatabase)
				.items(Trash.TRASH_NAMES)
				.itemsCallbackSingleChoice(-1, (dialog, itemView, which, text) -> {
					Trash selectedTrash = Trash.TRASHES[which];
					addTrashIdForEanToDatabase(eanNumber, selectedTrash.getId());
					return true;
				})
				.negativeText(R.string.cancel)
				.show();
	}

	private void whereShouldIThrow() {
		if (!listeningNow) {
			speakAndListen(getString(R.string.tellMeWhatYouWantToThrow));
		}
	}

	public void findSegregationRules(String inputText) {
		List<SegregationRule> segregationRules = segregationRulesInformer.findRulesByQuery(inputText);
		if (segregationRules.isEmpty())
			speak(getString(R.string.unknownGarbage), null);
		else if (segregationRules.size() == 1) {
			runSegregationRule(segregationRules.get(0));
		} else {
			speak(getString(R.string.selectFromList), null);
			new MaterialDialog.Builder(this)
					.title(R.string.selectFromList)
					.items(segregationRules)
					.itemsCallback((dialog, itemView, position, text) -> {
						runSegregationRule(segregationRules.get(position));
					})
					.negativeText(R.string.cancel)
					.show();
		}
	}

	public void runSegregationRule(SegregationRule segregationRule) {
		SegregationRuleType segregationRuleType = segregationRule.getType();
		if (segregationRuleType.getTrash() != null) {
			openTrash(segregationRuleType.getTrash());
		} else {
			toast(segregationRuleType.getOtherInfo());
			speak(segregationRuleType.getOtherInfo(), null);
		}
	}

	@Override
	public void handleResult(Result rawResult) {
		String eanNumber = rawResult.getText();
		getTrashIdForEan(eanNumber);
	}

	public void speak(String text, FinishResultListener listener) {
		ttsUtils.speak(text, () -> {
			updateUI();
			if (listener != null)
				listener.onFinished();
		});
		updateUI();
	}

	public void speakAndListen(String text) {
		speak(text, this::listen);
	}

	public void listen() {
		try {
			speechRecognizer.startListening(recognizerIntent);
			listeningNow = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateUI();
	}

	public void toast(int res) {
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	public void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResults(Bundle results) {
		listeningNow = false;
		ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		if (matches != null && !matches.isEmpty()) {
			String inputText = matches.get(0);
			findSegregationRules(inputText);
		}
		updateUI();
	}

	@Override
	public void onInit(int status) {
	}

	@Override
	public void onError(int error) {
		listeningNow = false;
		if (error == SpeechRecognizer.ERROR_NETWORK)
			speak(getString(R.string.networkError), null);
		if (error == SpeechRecognizer.ERROR_NO_MATCH)
			speak(getString(R.string.didntUnderstand), null);
		//toast(String.valueOf(error));
		updateUI();
	}

	@Override
	public void onRmsChanged(float rmsdB) {
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
	}

	@Override
	public void onBeginningOfSpeech() {
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
	}

	@Override
	public void onEndOfSpeech() {
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
	}

}