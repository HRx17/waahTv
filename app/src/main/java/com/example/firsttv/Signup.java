package com.example.firsttv;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.firsttv.RetrofitFiles.RetrofitClient;
import com.example.firsttv.RetrofitFiles.SignupResponse;
import com.example.firsttv.RetrofitFiles.Validate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends FragmentActivity {

    EditText sign_email, sign_pass, validatee, confirmpass;
    Button sign;
    Button check;
    Button skiptoo;

    public void onBackPressed(){
        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        check = findViewById(R.id.Check);
        validatee = findViewById(R.id.validate);
        sign_email = findViewById(R.id.sign_email);
        sign_pass = findViewById(R.id.sign_pass);
        sign = findViewById(R.id.sign_up);
        skiptoo = findViewById(R.id.skip_login);
        confirmpass = findViewById(R.id.sign_pass_confirm);
        confirmpass.setVisibility(View.GONE);

        sign_email.setVisibility(View.GONE);
        sign_pass.setVisibility(View.GONE);
        sign.setVisibility(View.GONE);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = validatee.getText().toString();

                Call<Validate> call = RetrofitClient.getInstance().getApi().validate(tmp);
                call.enqueue(new Callback<Validate>() {
                    @Override
                    public void onResponse(Call<Validate> call, Response<Validate> response) {
                        if(response.isSuccessful()){
                            Validate validate1 = response.body();
                            if(validate1.getValid().equals("true")){
                                Toast.makeText(Signup.this, "Verified!", Toast.LENGTH_SHORT).show();
                                Animation animFadeOut = AnimationUtils.loadAnimation(Signup.this, R.anim.fadeout);
                                Animation animFadeIn = AnimationUtils.loadAnimation(Signup.this, R.anim.fadein);
                                animFadeIn.reset();
                                check.setVisibility(View.GONE);
                                validatee.clearAnimation();
                                validatee.startAnimation(animFadeOut);
                                validatee.setVisibility(View.GONE);
                                sign_email.clearAnimation();
                                sign.clearAnimation();
                                sign_pass.clearAnimation();
                                sign_email.startAnimation(animFadeIn);
                                sign.startAnimation(animFadeIn);
                                sign_pass.startAnimation(animFadeIn);
                                animFadeOut.reset();
                                sign_email.setVisibility(View.VISIBLE);
                                sign_pass.setVisibility(View.VISIBLE);
                                sign.setVisibility(View.VISIBLE);
                                confirmpass.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(Signup.this, validate1.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Validate> call, Throwable t) {
                        Toast.makeText(Signup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        skiptoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this,Login.class);
                startActivity(intent);
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp1,tmp2;
                tmp1 = sign_email.getText().toString();
                tmp2 = sign_pass.getText().toString();
                if(tmp1.isEmpty() || tmp2.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter details!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(emailValid(sign_email)) {
                        if(sign_pass.equals(confirmpass)) {
                            userSignup();
                        }
                        else{
                            Toast.makeText(Signup.this, "Please enter Correct Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private boolean emailValid(EditText sign_email) {
        String emailToText = sign_email.getText().toString();

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            return true;
        } else {
            Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void userSignup() {
        String email = sign_email.getText().toString();
        String password = sign_pass.getText().toString();
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();

        SignupResponse signupResponse = new SignupResponse(email,password,null,android.os.Build.DEVICE,android.os.Build.PRODUCT,System.getProperty("os.version"));
        Call<SignupResponse> call = RetrofitClient.getInstance().getApi().Signup(signupResponse);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                //Toast.makeText(Signup.this, response.message(), Toast.LENGTH_SHORT).show();
                SignupResponse signupResponse = response.body();
                if(response.isSuccessful()){
                    Toast.makeText(Signup.this, signupResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Signup.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(Signup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}