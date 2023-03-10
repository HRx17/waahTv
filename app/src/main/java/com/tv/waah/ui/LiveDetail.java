package com.tv.waah.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.tv.waah.R;

public class LiveDetail extends FragmentActivity {
    public static String LIVE = "Movie";
    public static String NOTIFICATION_ID = "0";
    public static String SHARED_ELEMENT_NAME = null ;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_browse_fragment, new LiveDetailsFragment())
                    .commitNow();
        }
    }
}
