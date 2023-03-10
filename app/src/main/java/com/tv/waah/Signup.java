package com.tv.waah;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.tv.waah.RetrofitFiles.RetrofitClient;
import com.tv.waah.RetrofitFiles.SignupResponse;
import com.tv.waah.RetrofitFiles.Validate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends FragmentActivity {

    EditText sign_email, sign_pass, validatee, confirmpass;
    Button sign;
    Button check;
    Button skiptoo;
    ProgressBar progressBar;

    public void onBackPressed(){
        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = findViewById(R.id.progsin);
        progressBar.setVisibility(View.GONE);
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

        check.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                check.setBackgroundResource(R.drawable.onbutton_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });
        validatee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                check.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });
        sign.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sign.setBackgroundResource(R.drawable.onbutton_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });
        skiptoo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sign.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.onbutton_bg);
                check.setBackgroundResource(R.drawable.button_bg);
            }
        });
        sign_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sign.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });
        sign_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sign.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });
        confirmpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sign.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getCurrentFocus();
                if(view != null && view.getWindowToken()!=null){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                progressBar.setVisibility(View.VISIBLE);
                String tmp = validatee.getText().toString();
                if ( tmp.isEmpty() || tmp.contains(" ") || !tmp.startsWith("WAAH"))
                {
                    Toast.makeText(Signup.this, "Invalid referral code!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Call<Validate> call = RetrofitClient.getInstance().getApi().validate(tmp);
                call.enqueue(new Callback<Validate>() {
                    @Override
                    public void onResponse(Call<Validate> call, Response<Validate> response) {
                        if(response.isSuccessful()){
                            Validate validate1 = response.body();
                            if(validate1.getValid().equals("true")){
                                // No need for success message
                                //Toast.makeText(Signup.this, "Verified!", Toast.LENGTH_SHORT).show();
                                Animation animFadeOut = AnimationUtils.loadAnimation(Signup.this, R.anim.fadeout);
                                Animation animFadeIn = AnimationUtils.loadAnimation(Signup.this, R.anim.fadein);
                                animFadeIn.reset();
                                check.setVisibility(View.GONE);
                                validatee.clearAnimation();
                                validatee.startAnimation(animFadeOut);
                                validatee.setVisibility(View.GONE);
                                confirmpass.clearAnimation();
                                sign_email.clearAnimation();
                                sign.clearAnimation();
                                sign_pass.clearAnimation();
                                confirmpass.startAnimation(animFadeIn);
                                sign_email.startAnimation(animFadeIn);
                                sign.startAnimation(animFadeIn);
                                sign_pass.startAnimation(animFadeIn);
                                animFadeOut.reset();
                                sign_email.setVisibility(View.VISIBLE);
                                sign_pass.setVisibility(View.VISIBLE);
                                sign.setVisibility(View.VISIBLE);
                                confirmpass.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(Signup.this, validate1.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            String errorMsg = "Server not reachable, please try after sometime!";
                            Toast.makeText(Signup.this, errorMsg, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Validate> call, Throwable t) {
                        Toast.makeText(Signup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
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
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String tmp1,tmp2;
                tmp1 = sign_email.getText().toString();
                tmp2 = sign_pass.getText().toString();
                if(tmp1.isEmpty() || tmp2.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter emailId and password!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(emailValid(sign_email)) {
                        if(sign_pass.getText().toString().equals(confirmpass.getText().toString())) {
                            userSignup();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(Signup.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Enter valid email address!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void userSignup() {
        String email = sign_email.getText().toString();
        String password = sign_pass.getText().toString();
        if (password.contains(" ") || password.length() < 6 )
        {
            Toast.makeText(Signup.this, "Password length must be more than 6 characters.", Toast.LENGTH_LONG).show();
            sign_pass.getText().clear();
            confirmpass.getText().clear();
            return;
        }
        String deviceId = Utils.getDeviceId(getApplicationContext());
        String ip = Utils.getIPAddress();
        SignupResponse signupResponse = new SignupResponse(email,password, deviceId, Build.DEVICE + ":" +Build.MODEL, ip);
        Call<SignupResponse> call = RetrofitClient.getInstance().getApi().Signup(signupResponse);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                //Toast.makeText(Signup.this, response.message(), Toast.LENGTH_SHORT).show();
                SignupResponse signupResponse = response.body();
                if(response.isSuccessful()){
                    Toast.makeText(Signup.this, signupResponse.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    try {
                        String errorMsg = response.errorBody().string();
                        Toast.makeText(Signup.this, errorMsg, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    sign_email.getText().clear();
                    sign_pass.getText().clear();
                    confirmpass.getText().clear();
                    sign_email.requestFocus();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(Signup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}