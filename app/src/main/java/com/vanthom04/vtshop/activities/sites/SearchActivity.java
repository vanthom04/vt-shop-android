package com.vanthom04.vtshop.activities.sites;

import static com.vanthom04.vtshop.utils.AppUtils.onClickGoToDetailProduct;
import static com.vanthom04.vtshop.utils.Apis.GET_ALL_PRODUCTS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.adapters.ProductLinearAdapter;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;
import com.vanthom04.vtshop.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private static final long DEBOUNCE_DELAY = 500;
    private final Handler debounceHandler = new Handler(Looper.getMainLooper());
    EditText inputSearch;
    ImageView btnBack, btnClear;
    RecyclerView recyclerViewSearch;
    ProductLinearAdapter productLinearAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnBack = findViewById(R.id.btn_back);
        inputSearch = findViewById(R.id.input_search);
        btnClear = findViewById(R.id.btn_clear);
        recyclerViewSearch = findViewById(R.id.recycler_view_search);

        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputSearch, InputMethodManager.SHOW_IMPLICIT);

        // btn back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // input search
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                debounceHandler.removeCallbacks(debounceRunnable);
                debounceHandler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);

                if (s.toString().length() > 0) {
                    btnClear.setVisibility(View.VISIBLE);
                    btnClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputSearch.setText("");
                        }
                    });
                } else {
                    btnClear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private final Runnable debounceRunnable = new Runnable() {
        @Override
        public void run() {
            String name = inputSearch.getText().toString();

            if (name.length() > 0) {
                loadDataByName(name);
            }
        }
    };

    private void loadDataByName(String name) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectProducts = new JsonObjectRequest(
                Request.Method.GET,
                GET_ALL_PRODUCTS + "?name=" + name,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Product> list = new ArrayList<>();
                        try {
                            JSONArray products = response.getJSONArray("products");
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject product = products.getJSONObject(i);
                                String productId = product.getString("_id");
                                String name = product.getString("name");
                                int price = product.getInt("price");
                                String thumbnail = product.getString("thumbnail");

                                list.add(new Product(productId, name, price, thumbnail));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        setRecyclerViewSearch(list);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_connect_server", Objects.requireNonNull(error.getMessage()));
                    }
                }
        );
        requestQueue.add(jsonObjectProducts);
    }

    private void setRecyclerViewSearch(List<Product> list) {
        productLinearAdapter = new ProductLinearAdapter(new IOnClickItemProductListener() {
            @Override
            public void onClickItemProduct(String productId) {
                onClickGoToDetailProduct(SearchActivity.this, productId);
            }
        });
        productLinearAdapter.setData(list);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false));
        recyclerViewSearch.setAdapter(productLinearAdapter);
    }
}