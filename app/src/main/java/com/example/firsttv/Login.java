package com.example.firsttv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.firsttv.ui.MainActivity;

public class Login extends FragmentActivity {

    EditText log_enroll, log_pass;
    Button login;
    Button skiptoo;

    public void onBackPressed(){
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_enroll = findViewById(R.id.log_enroll);
        log_pass = findViewById(R.id.log_pass);
        login = findViewById(R.id.log_in);
        skiptoo = findViewById(R.id.skip);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp1,tmp2;
                tmp1 = log_enroll.getText().toString();
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
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}