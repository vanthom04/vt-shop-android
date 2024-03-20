package com.vanthom04.vtshop.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vanthom04.vtshop.R;

public class IntroActivity extends AppCompatActivity {

    AppCompatButton btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        changeLoginPage();
        changeRegistrationPage();
    }
    private void changeLoginPage() {
        btnLogin = findViewById(R.id.btn_to_change_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });
    }
    private void changeRegistrationPage() {
        btnRegister = findViewById(R.id.btn_to_change_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, RegisterActivity.class));
            }
        });
    }
}