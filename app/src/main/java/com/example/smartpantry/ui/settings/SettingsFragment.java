package com.example.smartpantry.ui.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.example.smartpantry.R;

/**
 * Settings / Profile screen (the fifth screen). Uses the AndroidX Preference
 * library so toggles are persisted automatically in SharedPreferences.
 *   - "Expiring soon alerts" toggle
 *   - "Show Almost There list" toggle (drives the stretch feature)
 *   - Units preference (metric / imperial labels)
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
