package com.tv.waah.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

            case R.id.logout:
                // call the logout api
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email", 0);
                String email = sharedPreferences.getString("email",null);
                String pass = sharedPreferences.getString("password",null);
                LoginResponse loginResponse = new LoginResponse(email,pass,null, null, null);
                Call<LoginResponse> call = RetrofitClient.getInstance().getApi().logout(loginResponse);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            Toast.makeText(getActivity().getApplicationContext(), "logged out.!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("time", 0);
                            SharedPreferences.Editor editor2 = sharedPreferences4.edit();
                            editor2.clear();
                            editor2.apply();
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
}