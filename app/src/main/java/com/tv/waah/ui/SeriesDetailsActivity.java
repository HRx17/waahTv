package com.tv.waah.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.tv.waah.R;

public class SeriesDetailsActivity extends FragmentActivity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String MOVIE = "Movie";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seriesdetail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.series_browse_fragment, new SeriesDetailFragment())
                    .commitNow();
        }
    }

}