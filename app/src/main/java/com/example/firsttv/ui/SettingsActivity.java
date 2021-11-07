package com.example.firsttv.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.firsttv.R;

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        FragmentManager manager = getFragmentManager();
        manager
            .beginTransaction()
            .replace(R.id.settings_container, new SettingsFragment())
            .commit();

    }
}
