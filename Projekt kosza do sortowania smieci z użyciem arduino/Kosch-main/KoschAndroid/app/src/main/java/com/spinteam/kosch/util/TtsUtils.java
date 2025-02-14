package com.spinteam.kosch.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import androidx.core.util.Pair;

import com.spinteam.kosch.listener.FinishResultListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TtsUtils {

	Activity activity;
	TextToSpeech tts;
	boolean speakingNow;

	public TtsUtils(Activity activity, TextToSpeech.OnInitListener onInitListener) {
		this.activity = activity;
		tts = new TextToSpeech(activity, onInitListener);
	}

	public void speak(String string, FinishResultListener listener) {
		stopSpeaking();
		ArrayList<Pair<Integer, Integer>> contentParts = loadContentParts(string);
		HashMap<String, String> params = new HashMap<>();
		params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
		UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
			int position;

			@Override
			public void onStart(String utteranceId) {
				speakingNow = true;
			}

			@Override
			public void onDone(String utteranceId) {
				activity.runOnUiThread(() -> {
					if (position == contentParts.size()) {
						speakingNow = false;
						if (listener != null)
							listener.onFinished();
					} else {
						tts.speak(getTtsText(string, contentParts, position), TextToSpeech.QUEUE_FLUSH, params);
						position++;
					}
				});
			}

			@Override
			public void onError(String utteranceId) {
				activity.runOnUiThread(() -> {
					speakingNow = false;
					if (listener != null)
						listener.onFinished();
				});
			}
		};
		tts.setOnUtteranceProgressListener(utteranceProgressListener);
		tts.speak("", TextToSpeech.QUEUE_FLUSH, params);
	}

	public boolean isSpeakingNow() {
		return speakingNow;
	}

	public void stopSpeaking() {
		if (tts.isSpeaking())
			tts.stop();
	}

	public String getTtsText(String CONTENT, ArrayList<Pair<Integer, Integer>> contentParts, int position) {
		Pair<Integer, Integer> part_bounds = contentParts.get(position);
		return CONTENT.substring(part_bounds.first, part_bounds.second);
	}

	public ArrayList<Pair<Integer, Integer>> loadContentParts(String CONTENT) {
		ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>();
		ArrayList<Pair<Integer, Integer>> parts_tmp = new ArrayList<>();
		int index = Integer.MAX_VALUE, lastindex = 0;
		while (true) {
			String[] searches = {". ", "?", "!", ">", ": ", "”", "'", "\"", ";", "»", " - ", "..."};
			int search_len = 1;
			for (String search : searches) {
				int new_index = CONTENT.indexOf(search, lastindex);
				if (new_index != -1) {
					index = Math.min(index, new_index);
					if (index == new_index)
						search_len = search.length();
				}
			}
			if (index >= CONTENT.length() - search_len) {
				parts_tmp.add(new Pair<>(lastindex, CONTENT.length()));
				break;
			} else /*if (Math.abs(index - lastindex) > 1)*/ {
				parts_tmp.add(new Pair<>(lastindex, index + search_len));
			}
			lastindex = index + search_len;
			index = Integer.MAX_VALUE;
		}
		for (Pair<Integer, Integer> part_bounds : parts_tmp) {
			String text = CONTENT.substring(part_bounds.first, part_bounds.second).trim();
			if (!text.isEmpty() && text.matches(".*[A-Za-z0-9].*"))
				parts.add(part_bounds);
		}
		return parts;
	}

	public static void openTtsSettings(Context context) {
		context.startActivity(new Intent("com.android.settings.TTS_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	public static boolean isTtsInstalled(Context context) {
		return !new TextToSpeech(context, null).getEngines().isEmpty();
	}

}