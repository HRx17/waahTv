package com.example.firsttv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.firsttv.RetrofitFiles.RetrofitClient;
import com.example.firsttv.RetrofitFiles.SignupResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends FragmentActivity {

    EditText sign_email, sign_pass;
    Button sign;
    Button skiptoo;

    public void onBackPressed(){
        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sign_email = findViewById(R.id.sign_email);
        sign_pass = findViewById(R.id.sign_pass);
        sign = findViewById(R.id.sign_up);
        skiptoo = findViewById(R.id.skip_login);

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
                    userSignup();
                }
            }
        });
    }

    private void userSignup() {
        String email = sign_email.getText().toString();
        String password = sign_pass.getText().toString();

        Call<SignupResponse> call = RetrofitClient.getInstance().getApi().Signup(email,password);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Toast.makeText(Signup.this, response.message(), Toast.LENGTH_SHORT).show();
                SignupResponse signupResponse = response.body();
                if(response.isSuccessful()){
                    Toast.makeText(Signup.this, "Successfully Signed Up!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(Signup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}