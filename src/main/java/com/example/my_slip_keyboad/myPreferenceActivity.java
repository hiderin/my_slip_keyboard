package com.example.my_slip_keyboad;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

//package com.example.my_slip_keyboad.R;

/**
 * preference activity
 */

/*
public class myPreferenceActivity extends AppCompatActivity implements myPreferenceFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);
    }
}
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
