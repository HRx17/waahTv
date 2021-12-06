package com.tv.waah.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.tv.waah.R;
import com.tv.waah.Splash;

import java.util.List;

public class SettingsFragment extends GuidedStepFragment {

    public static String LANG = "";
    Switch aSwitch;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aSwitch = getActivity().findViewById(R.id.switch1);
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
                .focusable(true)
                .build());

        actions.add(new GuidedAction.Builder()
                .id(R.id.settings_toggle_nav_id)
                .title("Channels")
                .description("Channel Languages: "+LANG)
                .build());

        actions.add(new GuidedAction.Builder()
                .id(R.id.logout)
                .title("Logout")
                .checked(!MainActivity.isUsingStandardBrowseFragment())
                .description( null)
                .build());

        super.onCreateActions(actions, savedInstanceState);
    }

    @SuppressLint("ResourceType")
    public void onGuidedActionClicked(final GuidedAction action) {
        switch ((int) action.getId()) {
            case R.id.settings_toggle_nav_id :
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Channel language")
                        .setMessage("Preferred languages for channels")
                        .setPositiveButton("English, Hindi, South", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("language", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("language","Hindi+South");
                                LANG="English, Hindi, South";
                                editor.apply();
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                            }
                        })
                        .setNeutralButton("English, Hindi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("language", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("language","Hindi");
                                LANG="English, Hindi";
                                editor.apply();
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                            }
                        })
                        .setNegativeButton("English (default)", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("language", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("language",null);
                                editor.apply();
                                LANG="English (default)";
                                dialog.dismiss();
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.logout:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Logout")
                        .setMessage("Are You Sure You Want To Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("time", 0);
                                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                editor2.clear();
                                editor2.apply();
                                Intent intent = new Intent(getActivity(), Splash.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
            default :
                break;
        }

        super.onGuidedActionClicked(action);
    }
}