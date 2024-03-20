package com.vanthom04.vtshop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.vanthom04.vtshop.activities.auth.IntroActivity;
import com.vanthom04.vtshop.utils.AppPreferences;
import com.vanthom04.vtshop.utils.AppUtils;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadData();
    }

    private void loadData() {
        if (AppUtils.isNetworkAvailable(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppPreferences preferences = AppPreferences.getInstance(SplashActivity.this);
                    String userId = preferences.getString("userId", "");
                    if (userId.isEmpty()) {
                        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                }
            }, 3000);
        } else {
            Toast.makeText(SplashActivity.this, "Không có kết nối internet!", Toast.LENGTH_SHORT).show();
        }
    }
}