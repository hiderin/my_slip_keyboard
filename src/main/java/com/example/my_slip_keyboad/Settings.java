package com.example.my_slip_keyboad;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Switch doubleKeySwitch;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        prefs = getSharedPreferences("NewKeyboardData", MODE_MULTI_PROCESS);
        editor = prefs.edit();

        doubleKeySwitch = (Switch) findViewById(R.id.doubleKey);
        doubleKeySwitch.setChecked(prefs.getBoolean("doubleKey", false));
        doubleKeySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("doubleKey", b).apply();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
