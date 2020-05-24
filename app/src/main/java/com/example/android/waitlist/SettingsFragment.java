package com.example.android.waitlist;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {
    SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_menu);

    }
}

