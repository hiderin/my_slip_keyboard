package com.example.my_slip_keyboad;

import android.content.Context;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

/**
 * preference fragment
 */
public class myPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.ime_preferences, rootKey);
    }

}
