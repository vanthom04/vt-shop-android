package com.vanthom04.vtshop.activities.auth;

import static com.vanthom04.vtshop.utils.Server.VERIFY_ACCOUNT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.vanthom04.vtshop.MainActivity;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.utils.AppPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Objects;

public class VerifyAccountActivity extends AppCompatActivity {

    EditText inputVerifyCode;
    AppCompatButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        setBtnSubmit();
    }

    private void setBtnSubmit() {
        inputVerifyCode = findViewById(R.id.input_verify_code);
        btnSubmit = findViewById(R.id.btn_submit_verify_code);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inputVerifyCode.getText().toString().trim();
                if (code.length() != 6) {
                    Toast.makeText(VerifyAccountActivity.this, "Vui lòng nhập đủ mã code!", Toast.LENGTH_SHORT).show();
                } else {
                    handleVerify(code);
                }
            }
        });
    }

    private void handleVerify(String code) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject data = new JSONObject();
        try {
            data.put("verifyCode", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectVerifyCode = new JsonObjectRequest(
                Request.Method.POST,
                VERIFY_ACCOUNT,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (!success) {
                                String message = response.getString("message");
                                Toast.makeText(VerifyAccountActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                AppPreferences preferences = AppPreferences.getInstance(VerifyAccountActivity.this);
                                preferences.saveString("userId", response.getString("userId"));

                                String message = response.getString("message");
                                Toast.makeText(VerifyAccountActivity.this, message, Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(VerifyAccountActivity.this, MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_connect_server", Objects.requireNonNull(error.getMessage()));
                    }
                }
        );
        requestQueue.add(jsonObjectVerifyCode);
    }
}