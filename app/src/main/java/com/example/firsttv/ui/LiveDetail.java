package com.example.firsttv.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.firsttv.R;

public class LiveDetail extends FragmentActivity {
    public static String LIVE = "Movie";
    public static String NOTIFICATION_ID = "0";
    public static String SHARED_ELEMENT_NAME = null ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livedetails);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.live_browse_fragment, new LiveDetailsFragment())
                    .commitNow();
        }
    }
}
