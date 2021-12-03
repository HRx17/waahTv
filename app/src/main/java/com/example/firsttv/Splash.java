package com.example.firsttv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.firsttv.ui.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Splash extends FragmentActivity {

    ImageView imageView;
    ProgressBar progressBar;
    Handler handler = new Handler();
    int tmp=1;

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
        String tmp1 = sharedPreferences.getString("time",null);
        System.out.println(tmp1);

        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);
        animFadeOut.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeOut);
        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);

        Calendar cal = Calendar.getInstance();

        int year = Integer.parseInt(tmp1.substring(6)); // this is deprecated
        int month = Integer.parseInt(tmp1.substring(3,5));
        int day = Integer.parseInt(tmp1.substring(0,2));

        Calendar validDate = Calendar.getInstance();
        validDate.set(year, month, day);
        System.out.println(validDate);

        Calendar currentDate = Calendar.getInstance();

        if (currentDate.after(validDate)) {
            tmp = 0;
        }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                if(tmp != 0){
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                }
            }
        }, 7000);
    }
}