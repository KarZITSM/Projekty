package com.spinteam.kosch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.preference.PreferenceManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Utils {

	static final String PREF_ORIENTATION = "orientation";

	public static String loadAssetTextFile(Context context, String name) throws Exception {
		return IOUtils.toString(context.getAssets().open(name), StandardCharsets.UTF_8);
	}

	public static int getFrontFacingCameraId(Context context) {
		try {
			CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
			for (final String cameraId : cameraManager.getCameraIdList()) {
				CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
				int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
				if (cOrientation == CameraCharacteristics.LENS_FACING_BACK)
					return Integer.parseInt(cameraId);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return 0;
	}

	public static void runInBg(Runnable runnable) {
		new Thread(runnable).start();
	}

	public static void secureActivity(Activity activity) {
		Window window = activity.getWindow();
		window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		//window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
			window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		window.getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
			if (visibility == 0)
				new Handler().postDelayed(() -> systemui(window, false), 50);
		});
		systemui(window, false);
	}

	@SuppressLint("WrongConstant")
	public static void closeStatusBar(final Activity activity) {
		try {
			activity.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
			new Handler().postDelayed(() -> activity.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS")), 500);
		} catch (Exception ignored) {
		}
		try {
			Object service = activity.getSystemService("statusbar");
			Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
			try {
				Method collapse = statusbarManager.getMethod("collapse");
				collapse.setAccessible(true);
				collapse.invoke(service);
			} catch (Exception e) {
				Method collapse = statusbarManager.getMethod("collapsePanels");
				collapse.setAccessible(true);
				collapse.invoke(service);
			}
		} catch (Exception ignored) {
		}
	}

	public static void setOrientation(Activity activity) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		activity.setRequestedOrientation(Integer.parseInt(sp.getString(PREF_ORIENTATION, String.valueOf(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE))));
	}

	public static void systemui(Window window, boolean visible) {
		if (!visible)
			window.getDecorView().setSystemUiVisibility(
					//View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
							View.SYSTEM_UI_FLAG_FULLSCREEN// |
							//View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
							//View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
							//View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
							//View.SYSTEM_UI_FLAG_IMMERSIVE
			);
		else
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
							View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			);
	}

	public static Date getDate(long daysOffset) {
		return new Date(System.currentTimeMillis() + daysOffset * 24 * 60 * 60 * 1000);
	}

	public static double stringSimilarity(String s1, String s2) {
		s1 = StringUtils.stripAccents(s1);
		s2 = StringUtils.stripAccents(s2);
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) {
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();
		if (longerLength == 0)
			return 1;
		return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	}

	//Levenshtein Edit Distance
	public static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}

}