package com.example.my_slip_keyboad;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


/**
 * preference activity
 */

public class myPreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_preference_demo, new myPreferenceFragment())
                .commit();
    }
}
