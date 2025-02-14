package com.spinteam.kosch.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import com.spinteam.kosch.Constants;
import com.spinteam.kosch.Utils;
import com.spinteam.kosch.databinding.ActivityScannerBinding;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

	private ActivityScannerBinding binding;
	private boolean initialized, cameraStarted;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Utils.setOrientation(this);
		binding = ActivityScannerBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
			requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
		else
			init();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			init();
		else
			finish();
	}

	public void init() {
		binding.zxingScannerView.setResultHandler(this);
		initialized = true;
		if (!cameraStarted)
			binding.zxingScannerView.startCamera(Utils.getFrontFacingCameraId(this));
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
	public void handleResult(Result rawResult) {
		String eanNumber = rawResult.getText();
		/*new AlertDialog.Builder(this)
				.setMessage(eanNumber)
				.setOnDismissListener(dialogInterface -> binding.zxingScannerView.resumeCameraPreview(this))
				.show();*/
		setResult(RESULT_OK, new Intent().putExtra(Constants.EXTRA_EAN_NUMBER, eanNumber));
		finish();
	}

}