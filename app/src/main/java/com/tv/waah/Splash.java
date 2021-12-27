package com.tv.waah;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.tv.waah.RetrofitFiles.JsonPlaceHolderApi;
import com.tv.waah.RetrofitFiles.RetrofitClient;
import com.tv.waah.RetrofitFiles.Update;
import com.tv.waah.ui.MainActivity;
import com.tv.waah.ui.SettingsFragment;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Splash extends FragmentActivity {

    ImageView imageView;
    ProgressBar progressBar;
    Handler handler = new Handler();
    int check = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.logo);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        SharedPreferences sharedPreferences = getSharedPreferences("time", 0);
        String userValidityDate = sharedPreferences.getString("time", null);

        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);
        animFadeOut.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeOut);
        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);

        if (userValidityDate == null) {
            check = 0;
        } else {
            Calendar cal = Calendar.getInstance();

            int year = Integer.parseInt(userValidityDate.substring(6)); // this is deprecated
            int month = Integer.parseInt(userValidityDate.substring(3, 5));
            int day = Integer.parseInt(userValidityDate.substring(0, 2));

            Calendar validDate = Calendar.getInstance();
            validDate.set(year, month, day);

            Calendar currentDate = Calendar.getInstance();

            if (currentDate.after(validDate)) {
                check = 0;
            }

        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                    if (check != 0) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(Splash.this, Login.class);
                        startActivity(intent);
                        finish();
                }
            }
        }, 7000);
    }
}