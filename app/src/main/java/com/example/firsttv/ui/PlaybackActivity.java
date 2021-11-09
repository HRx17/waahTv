package com.example.firsttv.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

///**
// * Loads {@link PlaybackVideoFragment}.
// */
public class PlaybackActivity extends FragmentActivity {

    public static String URLL="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new PlaybackVideoFragment())
                    .commit();
        }
    }
}