package com.tv.waah.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

///**
// * Loads {@link PlaybackVideoFragment}.
// */
public class PlaybackActivity extends FragmentActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(SERIES.equals("1")) {
            Intent intent = new Intent(this, LiveDetail.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, SeriesDetailsActivity.class);
            startActivity(intent);
        }
    }

    public static String URLL="1";
    public static String SERIES="1";
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