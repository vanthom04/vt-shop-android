package com.vanthom04.vtshop.activities.products;

import static com.vanthom04.vtshop.utils.AppUtils.capitalize;
import static com.vanthom04.vtshop.utils.AppUtils.onClickGoToDetailProduct;
import static com.vanthom04.vtshop.utils.Server.GET_ALL_PRODUCTS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.adapters.ProductGridAdapter;
import com.vanthom04.vtshop.adapters.ProductLinearAdapter;
import com.vanthom04.vtshop.models.Product;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;
import com.vanthom04.vtshop.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView productsView;
    ProductLinearAdapter productLinearAdapter;
    ProductGridAdapter productGridAdapter;
    TextView titleCategory;
    List<Product> list = new ArrayList<>();
    List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        setBtnBack();

        handleAndSetViewProducts();
    }

    private void setBtnBack() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void handleAndSetViewProducts() {
        titleCategory = findViewById(R.id.title_products);

        Intent intent = getIntent();
        String brand = intent.getStringExtra(Constants.BRAND_NAME_KEY);
        boolean featured = intent.getBooleanExtra(Constants.FEATURED_NAME_KEY, false);

        String URL = "";

        if (brand != null) {
            URL = GET_ALL_PRODUCTS + "?brand=" + brand;
            titleCategory.setText(capitalize(brand));
        } else if (featured) {
            URL = GET_ALL_PRODUCTS + "?featured=" + true;
            titleCategory.setText("Sản phẩm nổi bật");
        } else {
            URL = GET_ALL_PRODUCTS + "?featured=" + false;
            titleCategory.setText("Gợi ý hôm nay");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectProducts = new JsonObjectRequest(
                Request.Method.GET,
                URL,
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
                            Log.e("error_load_data", Objects.requireNonNull(e.getMessage()));
                        }

                        setProductsGridView(list);
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

    private void setProductsLinearView(List<Product> list) {
        productsView = findViewById(R.id.recyclerview_products);
        productLinearAdapter = new ProductLinearAdapter(new IOnClickItemProductListener() {
            @Override
            public void onClickItemProduct(String productId) {
                onClickGoToDetailProduct(ProductsActivity.this, productId);
            }
        });
        productLinearAdapter.setData(list);
        productsView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        productsView.setAdapter(productLinearAdapter);
    }

    private void setProductsGridView(List<Product> list) {
        productsView = findViewById(R.id.recyclerview_products);
        productGridAdapter = new ProductGridAdapter(new IOnClickItemProductListener() {
            @Override
            public void onClickItemProduct(String productId) {
                onClickGoToDetailProduct(ProductsActivity.this, productId);
            }
        });
        productGridAdapter.setData(list);
        productsView.setLayoutManager(new GridLayoutManager(this, 2));
        productsView.setAdapter(productGridAdapter);
    }

}
