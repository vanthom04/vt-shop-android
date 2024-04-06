package com.vanthom04.vtshop.activities.auth;

import static com.vanthom04.vtshop.utils.Apis.LOGIN_USER_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
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

public class LoginActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    AppCompatButton btnSubmitLogin;
    TextView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submitLogin();

        // change tab register
        changeRegistrationPage();
    }

    private void submitLogin() {
        inputEmail = findViewById(R.id.input_email_login);
        inputPassword = findViewById(R.id.input_password_login);
        btnSubmitLogin = findViewById(R.id.btn_submit_login);

        btnSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    inputEmail.setHint("Vui lòng nhập email!");
                    inputEmail.setHintTextColor(getResources().getColor(R.color.red_light));
                    return;
                }  else if (!AppUtils.isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đúng email!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    inputPassword.setHint("Vui lòng nhập password!");
                    inputPassword.setHintTextColor(getResources().getColor(R.color.red_light));
                    return;
                } else if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu tối thiểu 6 ký tự!", Toast.LENGTH_SHORT).show();
                    return;
                }

                handleLogin(email, password);
            }
        });
    }
    private void handleLogin(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        JSONObject data = new JSONObject();
        try {
            data.put("email", email);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectLogin = new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_USER_URL,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            if (!user.getBoolean("success")) {
                                Toast.makeText(LoginActivity.this, user.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                AppPreferences preferences = AppPreferences.getInstance(LoginActivity.this);
                                preferences.saveString("userId", user.getString("userId"));
                                preferences.saveString("accessToken", user.getString("accessToken"));

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
        jsonObjectLogin.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(jsonObjectLogin);
    }
    private void changeRegistrationPage() {
        btnRegister = findViewById(R.id.btn_change_tab_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}