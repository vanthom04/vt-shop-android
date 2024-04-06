package com.vanthom04.vtshop.activities.products;

import static com.vanthom04.vtshop.utils.AppUtils.changeIntToString;
import static com.vanthom04.vtshop.utils.AppUtils.onClickGoToDetailProduct;
import static com.vanthom04.vtshop.utils.Apis.ADD_PRODUCT_TO_CART;
import static com.vanthom04.vtshop.utils.Apis.GET_ALL_PRODUCTS;
import static com.vanthom04.vtshop.utils.Apis.GET_PRODUCT_BY_ID;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.bumptech.glide.Glide;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.activities.sites.ChatMessageActivity;
import com.vanthom04.vtshop.adapters.ProductGridAdapter;
import com.vanthom04.vtshop.models.Product;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;
import com.vanthom04.vtshop.utils.AppPreferences;
import com.vanthom04.vtshop.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailProductActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack, imageProduct;
    TextView nameProduct, priceProduct;
    TextView viewCPU, viewRAM, viewStorage, viewGraphicCard, viewDisplay;
    TextView viewAudio, viewWebcam, viewWeight, viewSize, viewPin, viewColor;
    TextView viewPortConnection, viewWirelessConnectivity;
    RecyclerView viewProductsSuggest;
    TextView btnShowMoreProduct;
    TextView manufacturer;
    ImageView btnChatMessage;
    AppCompatButton btnAddCart, btnBuyNow;
    List<Product> productList = new ArrayList<>();
    Intent intent;
    String productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        // get product id
        intent = getIntent();
        productId = intent.getStringExtra(Constants.PRODUCT_ID_KEY);
        if (productId == null) return;

        // mapping
        mappingButton();
        setDetailProduct();
    }

    private void mappingButton() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnShowMoreProduct = findViewById(R.id.btn_show_more_product);
        btnShowMoreProduct.setOnClickListener(this);
        btnChatMessage = findViewById(R.id.btn_chat_message);
        btnChatMessage.setOnClickListener(this);
        btnAddCart = findViewById(R.id.btn_add_to_cart);
        btnAddCart.setOnClickListener(this);
        btnBuyNow = findViewById(R.id.btn_buy_now);
        btnBuyNow.setOnClickListener(this);
    }

    private void setDetailProduct() {
        imageProduct = findViewById(R.id.image_product);
        nameProduct = findViewById(R.id.name_product);
        priceProduct = findViewById(R.id.price_product);

        // view specifications
        viewCPU = findViewById(R.id.view_cpu);
        viewRAM = findViewById(R.id.view_ram);
        viewStorage = findViewById(R.id.view_storage);
        viewGraphicCard = findViewById(R.id.view_graphic_card);
        viewDisplay = findViewById(R.id.view_display);
        viewAudio = findViewById(R.id.view_audio);
        viewWebcam = findViewById(R.id.view_webcam);
        viewWeight = findViewById(R.id.view_weight);
        viewSize = findViewById(R.id.view_size);
        viewPortConnection = findViewById(R.id.view_port_connection);
        viewWirelessConnectivity = findViewById(R.id.view_wireless_connectivity);
        viewPin = findViewById(R.id.view_pin);
        viewColor = findViewById(R.id.view_color);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectProduct = new JsonObjectRequest(
                Request.Method.GET,
                GET_PRODUCT_BY_ID + productId,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject product = response.getJSONObject("product");

                            // load image product
                            String thumbnail = product.getString("thumbnail");
                            Glide.with(DetailProductActivity.this).load(thumbnail).into(imageProduct);

                            // load name and price product
                            nameProduct.setText(product.getString("name"));
                            priceProduct.setText(changeIntToString(product.getInt("price")));

                            // load specifications product
                            JSONObject specifications = product.getJSONObject("specifications");
                            viewCPU.setText(specifications.getString("cpu"));
                            viewRAM.setText(specifications.getString("ram"));
                            viewStorage.setText(specifications.getString("storage"));
                            viewGraphicCard.setText(specifications.getString("graphicCard"));
                            viewDisplay.setText(specifications.getString("display"));
                            viewAudio.setText("Audio: " + specifications.getString("audio"));
                            viewWebcam.setText("Webcam: " + specifications.getString("webcam"));
                            viewWeight.setText("Khối lượng: " + specifications.getString("weight"));
                            viewSize.setText("Kích thước: " + specifications.getString("size"));
                            viewPortConnection.setText(specifications.getString("portConnection"));
                            viewWirelessConnectivity.setText(specifications.getString("wirelessConnectivity"));
                            viewPin.setText(specifications.getString("pin"));
                            viewColor.setText(specifications.getString("color"));

                            // render view products suggest
                            loadViewProductsSuggest(product.getString("brand"));
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
        requestQueue.add(jsonObjectProduct);
    }

    private void loadViewProductsSuggest(String brand) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectProducts = new JsonObjectRequest(
                Request.Method.GET,
                GET_ALL_PRODUCTS + "?brand=" + brand,
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

                        setViewProductsSuggest(list);
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

    private void setViewProductsSuggest(List<Product> list) {
        viewProductsSuggest = findViewById(R.id.view_product_suggest);
        ProductGridAdapter girdAdapter = new ProductGridAdapter(new IOnClickItemProductListener() {
            @Override
            public void onClickItemProduct(String productId) {
                onClickGoToDetailProduct(DetailProductActivity.this, productId);
            }
        });
        girdAdapter.setData(list);
        viewProductsSuggest.setLayoutManager(new GridLayoutManager(this, 2));
        viewProductsSuggest.setAdapter(girdAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_show_more_product:
                startActivity(new Intent(DetailProductActivity.this, ProductsActivity.class));
                break;
            case R.id.btn_chat_message:
                startActivity(new Intent(DetailProductActivity.this, ChatMessageActivity.class));
                break;
            case R.id.btn_add_to_cart:
                addProductToCart();
                break;
            case R.id.btn_buy_now:
                break;
        }
    }

    private void addProductToCart() {
        AppPreferences preferences = AppPreferences.getInstance(this);
        String userId = preferences.getString("userId", "");
        if (!userId.isEmpty()) {
            // TOTO: Handle event add to cart
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectCart = new JsonObjectRequest(
                    Request.Method.POST,
                    ADD_PRODUCT_TO_CART + userId + "/" + productId,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject cart = response.getJSONObject("cart");
                                if (cart.length() > 0) {
                                    showAlertDialog();
                                } else {
                                    Toast.makeText(DetailProductActivity.this, "Thêm sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
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
            requestQueue.add(jsonObjectCart);
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm sản phẩm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog() {
        ConstraintLayout container = findViewById(R.id.container_dialog_alert_add_cart);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_alert_add_cart, container);
        builder.setView(view);

        AppCompatButton btnClose = view.findViewById(R.id.btn_close);
        AppCompatButton btnBuyNow = view.findViewById(R.id.btn_submit_buy_now);

        AlertDialog dialog = builder.create();

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TOTO: Handle event submit buy now

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