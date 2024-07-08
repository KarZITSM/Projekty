package com.spinteam.kosch.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.spinteam.kosch.R;

public class SettingsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	public static class PrefsFragment extends PreferenceFragmentCompat {

		SettingsActivity activity;

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			addPreferencesFromResource(R.xml.settings);
			activity = (SettingsActivity) getActivity();
		}

		@Override
		public boolean onPreferenceTreeClick(@NonNull Preference preference) {
			return super.onPreferenceTreeClick(preference);
		}

	}

}