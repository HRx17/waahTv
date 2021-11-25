package com.example.firsttv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.firsttv.RetrofitFiles.LoginResponse;
import com.example.firsttv.RetrofitFiles.RetrofitClient;
import com.example.firsttv.ui.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends FragmentActivity {

    EditText log_email, log_pass;
    Button login;
    Button skiptoo;

    public void onBackPressed(){
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp1,tmp2;
                tmp1 = log_email.getText().toString();
                tmp2 = log_pass.getText().toString();
                if(tmp1.isEmpty() || tmp2.isEmpty()){
                    Toast.makeText(Login.this, "Please enter details!", Toast.LENGTH_SHORT).show();
                }
                else{
                    userLogin();
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

    private void userLogin() {

        String email = log_email.getText().toString();
        String password = log_pass.getText().toString();
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();

        LoginResponse loginResponse = new LoginResponse(email,password,address,android.os.Build.DEVICE,android.os.Build.PRODUCT,System.getProperty("os.version"));
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().login(loginResponse);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if(response.isSuccessful()){
                    if(loginResponse.getIsActive().equals("true")) {
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("time", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("time", loginResponse.getExpiryDate());
                        editor.apply();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(Login.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}