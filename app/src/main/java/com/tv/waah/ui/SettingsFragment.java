package com.tv.waah.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.tv.waah.Login;
import com.tv.waah.R;
import com.tv.waah.RetrofitFiles.JsonPlaceHolderApi;
import com.tv.waah.RetrofitFiles.Language;
import com.tv.waah.RetrofitFiles.LoginResponse;
import com.tv.waah.RetrofitFiles.RetrofitClient;
import com.tv.waah.RetrofitFiles.langs;
import com.tv.waah.Splash;
import com.tv.waah.Utils;
import com.tv.waah.model.ChannelList;
import com.tv.waah.model.Seasons;
import com.tv.waah.model.SubPost;

import org.apache.commons.codec.language.bm.Lang;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsFragment extends GuidedStepFragment {

    public static String HINDI = "";
    public static String SOUTH = "";
    public static String MARATHI = "";

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
                .title("Channel Languages:")
                .focusable(true)
                .build());

        actions.add(new GuidedAction.Builder()
                .id(R.id.settings_toggle_nav_id)
                .title("Hindi")
                .description("On")
                .build());
        actions.add(new GuidedAction.Builder()
                .id(R.id.southi)
                .title("South")
                .description("On")
                .build());
        actions.add(new GuidedAction.Builder()
                .id(R.id.marathi)
                .title("Marathi")
                .description("On")
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
            case R.id.southi:
                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("south", 0);
                System.out.println(findActionById(R.id.southi).getDescription().toString());
                if(findActionById(R.id.southi).getDescription().toString().equals("On")) {
                    findActionById(R.id.southi).setDescription("Off");
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString("South", "Off");
                    editor.apply();
                    System.out.println(findActionById(R.id.southi));
                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString("South", "On");
                    editor.apply();
                    findActionById(R.id.southi).setDescription("On");
                }
                SettingsFragment.SOUTH = sharedPreferences1.getString("south","On");
                break;

            case R.id.marathi:
                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("south", 0);
                if(findActionById(R.id.marathi).getDescription().toString().equals("On")) {
                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                    editor.putString("South", "Off");
                    editor.apply();
                    findActionById(R.id.marathi).setEditDescription("Off");
                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                    editor.putString("South", "On");
                    editor.apply();
                    findActionById(R.id.marathi).setDescription("On");
                }
                SettingsFragment.MARATHI = sharedPreferences2.getString("south","On");
                break;

            case R.id.settings_toggle_nav_id:
                SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences("south", 0);
                if(findActionById(R.id.settings_toggle_nav_id).getDescription().toString().equals("On")) {
                    findActionById(R.id.settings_toggle_nav_id).setDescription("Off");
                    SharedPreferences.Editor editor = sharedPreferences3.edit();
                    editor.putString("South", "Off");
                    editor.apply();
                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences3.edit();
                    editor.putString("South", "On");
                    editor.apply();
                    findActionById(R.id.settings_toggle_nav_id).setDescription("On");
                }
                SettingsFragment.HINDI = sharedPreferences3.getString("south","On");
                break;

            case R.id.logout:
                SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("time", 0);
                SharedPreferences.Editor editor2 = sharedPreferences4.edit();
                editor2.clear();
                editor2.apply();
                Intent intent = new Intent(getActivity(), Splash.class);
                startActivity(intent);
                // call the logout api
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email", 0);
                String email = sharedPreferences.getString("email",null);
                String pass = sharedPreferences.getString("password",null);
                LoginResponse loginResponse = new LoginResponse(email,pass,null, null, null);
                Call<LoginResponse> call = RetrofitClient.getInstance().getApi().logout(loginResponse);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (!response.isSuccessful())
                            Toast.makeText(getActivity().getApplicationContext(), "logged out.!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error when logging out!!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default :
                break;
        }

        super.onGuidedActionClicked(action);
    }

    /*
    String encryptedDeviceId = Utils.getEncryptedDeviceId(getActivity().getApplicationContext());
    //List<langs> list = new ArrayList<>(3);
    JSONStringer jsonStringer = null;
    try{
    JsonPlaceHolderApi jsonPlaceHolderApi = RetrofitClient.getInstance().getApi().setLang(encryptedDeviceId,);
    Call<Language> call = jsonPlaceHolderApi.setLang(encryptedDeviceId,);
        call.enqueue(new Callback<Language>() {
        @Override
        public void onResponse(Call<Language> call, Response<Language> response){
            if (!response.isSuccessful()) {
                Toast.makeText(getActivity(), "Failed to get Response!", Toast.LENGTH_SHORT).show();
            }
            List<ChannelList> posts = Objects.requireNonNull(response.body()).getChannelList();
            System.out.println(posts);
            for (ChannelList post : posts) {
                if (post == null) {
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();

                } else if (post.getChannelName().equals()) {
                    for (Seasons seasonsList : post.getSeasons()) {
                    }
                }
            }
        }
        };
}*/
}