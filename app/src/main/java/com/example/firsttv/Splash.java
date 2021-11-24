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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.firsttv.ui.MainActivity;

import java.util.Date;

public class Splash extends FragmentActivity {

    ImageView imageView;
    ProgressBar progressBar;
    Handler handler = new Handler();
    String tmp = "1";

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

        Date today = new Date();
        Toast.makeText(this, today.toString(), Toast.LENGTH_SHORT).show();
        System.out.println(today);
        String d,m,y;
        d = String.valueOf(today.getDay());
        m = String.valueOf(today.getMonth());
        y = String.valueOf(today.getYear());
        String tmp1 = sharedPreferences.getString("time", null);
        String dd = String.valueOf(tmp1.subSequence(0,2));
        String mm = String.valueOf(tmp1.subSequence(3,5));
        String yy = String.valueOf(tmp1.subSequence(6,9));
        int date= Integer.valueOf(dd);
        int month= Integer.valueOf(mm);
        int year= Integer.valueOf(yy);
        int dt= Integer.valueOf(d);
        int mt= Integer.valueOf(m);
        int yr= Integer.valueOf(y);


        if(year > yr){
            tmp = null;
        }
        else{
            if(month > mt){
                tmp=null;
            }
            else if(mm.equals(m)){
                if(date>dt){
                    tmp=null;
                }
            }
        }

        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);
        animFadeOut.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeOut);
        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);
        progressBar.setVisibility(View.GONE);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(tmp != null){
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Splash.this, Signup.class);
                    startActivity(intent);
                }
            }
        }, 7000);
    }
}