package com.example.e7;

import android.os.Bundle;

import android.preference.PreferenceFragment;

public class ColourFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }


}