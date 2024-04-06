package com.vanthom04.vtshop.fragments;

import static com.vanthom04.vtshop.utils.Apis.GET_USER_BY_ID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.SplashActivity;
import com.vanthom04.vtshop.activities.accounts.MyProfileActivity;
import com.vanthom04.vtshop.activities.accounts.SettingsAccountActivity;
import com.vanthom04.vtshop.utils.AppPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private View view;
    private CircleImageView imageAvatar;
    private TextView viewFullName, viewEmail;
    RelativeLayout btnMyProfile;
    RelativeLayout btnSettingApp;
    AppCompatButton btnLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        // mapping
        mapping();

        setViewUser();

        changeViewMyProfile();

        // set btn settings app
        setBtnSettingApp();

        // set btn log out
        setBtnLogOut();

        return view;
    }

    private void mapping() {
        imageAvatar = view.findViewById(R.id.image_avatar);
        viewFullName = view.findViewById(R.id.view_full_name);
        viewEmail = view.findViewById(R.id.view_email);
    }

    @SuppressLint("SetTextI18n")
    private void setViewUser() {
        AppPreferences preferences = AppPreferences.getInstance(getContext());
        String userId = preferences.getString("userId", "");

        if (!userId.isEmpty()) {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            JsonObjectRequest jsonObjectUser = new JsonObjectRequest(
                    Request.Method.GET,
                    GET_USER_BY_ID + userId,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject user = response.getJSONObject("user");

                                String fullName = user.getString("fullName");
                                viewFullName.setText(fullName);
                                viewEmail.setText(user.getString("email"));

                                String photoURL = user.getString("photoURL");
                                if (photoURL.isEmpty()) {
                                    Glide.with(requireContext()).load(R.drawable.default_avatar).into(imageAvatar);
                                } else {
                                    Glide.with(requireContext()).load(photoURL).into(imageAvatar);
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
            requestQueue.add(jsonObjectUser);
        } else {
            viewFullName.setText("Khách!");
            viewEmail.setText("exmaple@gmail.com");
            Glide.with(requireContext()).load(R.drawable.default_avatar).into(imageAvatar);
        }
    }

    private void changeViewMyProfile() {
        btnMyProfile = view.findViewById(R.id.btn_my_profile);
        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyProfileActivity.class));
            }
        });
    }

    private void setBtnSettingApp() {
        btnSettingApp = view.findViewById(R.id.btn_setting_app);
        btnSettingApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingsAccountActivity.class));
            }
        });
    }

    private void setBtnLogOut() {
        btnLogOut = view.findViewById(R.id.btn_logout_account);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertMessageLogOut();
            }
        });
    }

    public void showAlertMessageLogOut() {
        CardView containerDialog = view.findViewById(R.id.container_dialog_alert_logout);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_logout, containerDialog);
        builder.setView(v);

        AppCompatButton btnSubmitLogOut = v.findViewById(R.id.btn_submit_logout);
        AppCompatButton btnClose = v.findViewById(R.id.btn_close);

        AlertDialog dialog = builder.create();

        btnSubmitLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: xu ly su kien
                AppPreferences preferences = AppPreferences.getInstance(getContext());
                preferences.clearAll();
                startActivity(new Intent(getContext(), SplashActivity.class));
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