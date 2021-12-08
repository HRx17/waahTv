package com.tv.waah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.provider.Settings.Secure;
import androidx.fragment.app.FragmentActivity;

import com.tv.waah.RetrofitFiles.LoginResponse;
import com.tv.waah.RetrofitFiles.RetrofitClient;
import com.tv.waah.ui.MainActivity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends FragmentActivity {

    EditText log_email, log_pass;
    Button login;
    Button skiptoo;
    ProgressBar progressBar;

    public void onBackPressed(){

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_email = findViewById(R.id.log_enroll);
        log_pass = findViewById(R.id.log_pass);
        login = findViewById(R.id.log_in);
        skiptoo = findViewById(R.id.skip);
        progressBar = findViewById(R.id.progress_log);
        progressBar.setVisibility(View.GONE);

        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                login.setBackgroundResource(R.drawable.onbutton_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });
        skiptoo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                login.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.onbutton_bg);
            }
        });
        log_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                login.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });
        log_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                login.setBackgroundResource(R.drawable.button_bg);
                skiptoo.setBackgroundResource(R.drawable.button_bg);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp1,tmp2;
                View view = getCurrentFocus();
                if(view != null && view.getWindowToken()!=null){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                tmp1 = log_email.getText().toString();
                tmp2 = log_pass.getText().toString();
                if(tmp1.isEmpty() || tmp2.isEmpty()){
                    Toast.makeText(Login.this, "Please enter email and password!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(userValid(log_email)) {
                        userLogin();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        skiptoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });


    }

    private boolean userValid(EditText log_email) {
        String emailToText = log_email.getText().toString();

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            return true;
        } else {
            Toast.makeText(this, "Please enter valid email address!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;
                        if (isIPv4)
                                return sAddr;
                        else {
                            int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                            return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private void userLogin() {

        String email = log_email.getText().toString();
        String password = log_pass.getText().toString();
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        String deviceId = Utils.getDeviceId(getApplicationContext());
        String ip = getIPAddress();

        LoginResponse loginResponse = new LoginResponse(email,password,ip, deviceId, Build.DEVICE + ":" +Build.MODEL, "");
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().login(loginResponse);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if(response.isSuccessful()){
                    if(loginResponse.getIsActive().equals("true")) {
                        // no need for success message
                       // Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("time", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("time", loginResponse.getExpiryDate());
                        editor.apply();
                        SharedPreferences sharedPreferences1 = getSharedPreferences("email", 0);
                        SharedPreferences.Editor editorr = sharedPreferences1.edit();
                        editorr.putString("email", email);
                        editorr.apply();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else{
                    try {
                        String errorMsg = response.errorBody().string();
                        Toast.makeText(Login.this, errorMsg, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}