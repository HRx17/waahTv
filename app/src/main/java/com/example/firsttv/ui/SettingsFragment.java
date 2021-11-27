package com.example.firsttv.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.example.firsttv.R;
import com.example.firsttv.Splash;

import java.util.List;

public class SettingsFragment extends GuidedStepFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        return new GuidanceStylist.Guidance(getString(R.string.settings_title), null, getString(R.string.app_name), null);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        actions.add(new GuidedAction.Builder()
                .id(R.id.settings_category_id)
                .infoOnly(true)
                .title(getString(R.string.settings_category))
                .build());

        actions.add(new GuidedAction.Builder()
                .id(R.id.settings_toggle_nav_id)
                .title(getString(R.string.settings_toggle_nav_title))
                .checked(!MainActivity.isUsingStandardBrowseFragment())
                .description(getString(R.string.settings_toggle_nav_desc))
                .build());
        actions.add(new GuidedAction.Builder()
                .id(R.id.logout)
                .title("Logout")
                .checked(!MainActivity.isUsingStandardBrowseFragment())
                .description( null)
                .build());

        super.onCreateActions(actions, savedInstanceState);
    }

    public void onGuidedActionClicked(final GuidedAction action) {
        switch ((int) action.getId()) {
            case R.id.settings_toggle_nav_id :
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Use custom navigation")
                        .setMessage("Restart the application?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.PREFS_ROOT, Context.MODE_PRIVATE);
                                prefs
                                        .edit()
                                        .putBoolean(MainActivity.PREFS_USE_STANDARD_BROWSE_FRAGMENT, action.isChecked())
                                        .apply();
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.logout:
                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("time", 0);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.clear();
                editor2.apply();
                Intent intent = new Intent(getActivity(), Splash.class);
                startActivity(intent);
                break;
            default :
                break;
        }

        super.onGuidedActionClicked(action);
    }
}