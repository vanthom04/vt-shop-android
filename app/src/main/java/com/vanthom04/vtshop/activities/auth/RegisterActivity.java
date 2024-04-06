package com.vanthom04.vtshop.activities.auth;

import static com.vanthom04.vtshop.utils.Apis.REGISTER_USER_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vanthom04.vtshop.MainActivity;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.utils.AppPreferences;
import com.vanthom04.vtshop.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText inputFullName, inputEmail, inputPassword;
    AppCompatButton btnRegister;
    TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // submit register
        submitRegister();

        changeLoginPage();
    }

    private void submitRegister() {
        btnRegister = findViewById(R.id.btn_submit_register);
        inputFullName = findViewById(R.id.input_full_name);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (fullName.isEmpty()) {
                    inputFullName.setHint("Vui lòng nhập họ, tên!");
                    inputFullName.setHintTextColor(getResources().getColor(R.color.red_light));
                    return;
                }  else if (email.isEmpty()) {
                    inputEmail.setHint("Vui lòng nhập email!");
                    inputEmail.setHintTextColor(getResources().getColor(R.color.red_light));
                    return;
                } else if (!AppUtils.isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đúng email!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    inputPassword.setHint("Vui lòng nhập password!");
                    inputPassword.setHintTextColor(getResources().getColor(R.color.red_light));
                    return;
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu tối thiểu 6 ký tự!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // handle register
                handleRegister(fullName, email, password);
            }
        });
    }

    private void handleRegister(String fullName, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        JSONObject data = new JSONObject();
        try {
            data.put("fullName", fullName);
            data.put("email", email);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectUser = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_USER_URL,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");

                            if (!user.getBoolean("success")) {
                                Toast.makeText(RegisterActivity.this, user.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                AppPreferences preferences = AppPreferences.getInstance(RegisterActivity.this);
                                preferences.saveString("userId", user.getString("userId"));
                                preferences.saveString("accessToken", user.getString("accessToken"));

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                        } catch (JSONException e) {
                            Log.e("error_load_data", Objects.requireNonNull(e.getMessage()));
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
        jsonObjectUser.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(jsonObjectUser);
    }

    private void changeLoginPage() {
        btnLogin = findViewById(R.id.btn_change_tab_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}