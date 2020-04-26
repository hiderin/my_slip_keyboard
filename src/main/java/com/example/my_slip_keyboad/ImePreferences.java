/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.my_slip_keyboad;

//import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
//import com.android.inputmethodcommon.InputMethodSettingsFragment;
//import android.preference.PreferenceFragment;

/**
 * Displays the IME preferences inside the input method setting.
 */
public class ImePreferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		if(NewKeyboard.getInstance()==null){
			new NewKeyboard(this);
		}

		addPreferencesFromResource(R.xml.ime_preferences);

		//getFragmentManager().beginTransaction().replace(android.R.id.content, new Settings()).commit();
  }

  /*
   public static class Settings extends PreferenceFragment {
       @Override
       public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
//            setInputMethodSettingsCategoryTitle(R.string.language_selection_title);
//            setSubtypeEnablerTitle(R.string.select_language);

           // Load the preferences from an XML resource
           addPreferencesFromResource(R.xml.ime_preferences);
       }
   }
   */

}
