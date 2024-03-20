package com.vanthom04.vtshop.activities.accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.vanthom04.vtshop.R;

public class SettingsAccountActivity extends AppCompatActivity {

    AppCompatButton btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_account);

        setBtnLogOut();
    }

    private void setBtnLogOut() {
        btnLogOut = findViewById(R.id.btn_logout_account);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertMessageLogOut();
            }
        });
    }

    private void showAlertMessageLogOut() {
        CardView containerDialog = findViewById(R.id.container_dialog_alert_logout);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_logout, containerDialog);
        builder.setView(v);

        AppCompatButton btnSubmitLogOut = v.findViewById(R.id.btn_submit_logout);
        AppCompatButton btnClose = v.findViewById(R.id.btn_close);

        AlertDialog dialog = builder.create();

        btnSubmitLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: xu ly su kien
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}