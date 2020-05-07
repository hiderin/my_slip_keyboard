package com.example.my_slip_keyboad;

import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
//import android.support.v7.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceFragmentCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;

//package com.example.my_slip_keyboad.R;

/**
 * preference fragment
 */
public class myPreferenceFragment extends PreferenceFragmentCompat {

    //private OnFragmentInteractionListener mListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.ime_preferences, rootKey);
    }

	/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // delete under line
        setDivider(new ColorDrawable(Color.TRANSPARENT));
        setDividerHeight(0);

        return view;
    }
	*/

	/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
	*/

}
