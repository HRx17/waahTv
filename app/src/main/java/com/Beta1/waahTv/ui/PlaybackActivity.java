package com.Beta1.waahTv.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

///**
// * Loads {@link PlaybackVideoFragment}.
// */
public class PlaybackActivity extends FragmentActivity {

    public static String URLL="1";
    public static String NAME="1";
    public static String EMAIL="1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new PlaybackVideoFragment())
                    .commit();
            SharedPreferences sharedPreferences = getSharedPreferences("email", 0);
            EMAIL = sharedPreferences.getString("email",null);
        }
    }
}