package com.spinteam.kosch;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Prefs {

   public static SharedPreferences get() {
      return PreferenceManager.getDefaultSharedPreferences(App.getApp());
   }

}
