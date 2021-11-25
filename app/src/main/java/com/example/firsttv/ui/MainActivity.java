package com.example.firsttv.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.widget.VerticalGridView;

import com.example.firsttv.R;

import java.util.LinkedHashMap;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {
    public static String PREFS_USE_STANDARD_BROWSE_FRAGMENT;
    public static String PREFS_ROOT;

    public void onBackPressed(){
        return;
    }

    public static boolean isUsingStandardBrowseFragment() { return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_browse_fragment, new MainFragment())
                            .commitNow();
                }
            }

    public VerticalGridView getVerticalGridView(CustomHeadersFragment customHeadersFragment) { return null;
    }

    public Object updateCurrentFragment(Fragment obj) { return null;
    }

    public LinkedHashMap<Integer, Fragment> getFragments() { return null;
    }
}
