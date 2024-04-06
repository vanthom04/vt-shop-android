package com.vanthom04.vtshop.fragments;

import static com.vanthom04.vtshop.utils.AppUtils.onClickGoToDetailProduct;
import static com.vanthom04.vtshop.utils.AppUtils.onClickToToActivityProductsFeatured;
import static com.vanthom04.vtshop.utils.AppUtils.onClickToToActivityProductsBrand;
import static com.vanthom04.vtshop.utils.Apis.GET_ALL_PRODUCTS;
import static com.vanthom04.vtshop.utils.Apis.GET_ALL_SLIDES;
import static com.vanthom04.vtshop.utils.Apis.GET_USER_BY_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.activities.sites.NotificationActivity;
import com.vanthom04.vtshop.activities.sites.SearchActivity;
import com.vanthom04.vtshop.adapters.SlideAdapter;
import com.vanthom04.vtshop.adapters.ProductGridAdapter;
import com.vanthom04.vtshop.animations.ZoomOutPageTransformer;
import com.vanthom04.vtshop.models.Slide;
import com.vanthom04.vtshop.models.Product;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;
import com.vanthom04.vtshop.utils.AppPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView viewWelcome;
    static List<Slide> listSlide = new ArrayList<>();
    static List<Product> listProductOutstanding = new ArrayList<>();
    static List<Product> listProductSuggest = new ArrayList<>();
    EditText btnSearch;
    LinearLayout btnApple, btnDell, btnHp, btnMsi, btnAsus;
    ViewPager2 viewSlide;
    CircleIndicator3 indicatorSlide;
    TextView btnShowProductOutstanding, btnShowProductSuggest;
    RecyclerView productsOutstandingView, productsSuggestView;
    ProductGridAdapter productGridAdapter;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            if (viewSlide.getCurrentItem() == listSlide.size() - 1) {
//                viewSlide.setCurrentItem(0);
//            } else {
//                viewSlide.setCurrentItem(viewSlide.getCurrentItem() + 1);
//            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // mapping
        mapping();

        setBtnSearch();

        // render view slides
        renderViewSlides();

        // mapping button
        mappingBtn();

        // render view products
        loadProductsSuggest();
        loadProductsOutstanding();

        // set view user
        setViewUser();
        return view;
    }

    private void mapping() {
        viewWelcome = view.findViewById(R.id.view_welcome);
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
                                if (!fullName.isEmpty()) {
                                    String name = fullName.split(" ")[fullName.split(" ").length - 1];
                                    viewWelcome.setText("Xin chào, " + name);
                                } else {
                                    viewWelcome.setText("Xin chào, " + user.getString("username"));
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
            viewWelcome.setText("Xin chào, Khách");
        }
    }

    private void setBtnSearch() {
        btnSearch = view.findViewById(R.id.btn_search_home);
        btnSearch.setFocusable(false);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));

                hideSoftKeyboard();
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
    }

    private void mappingBtn() {
        ConstraintLayout btnNotification = view.findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(this);
        btnApple = view.findViewById(R.id.btn_apple);
        btnApple.setOnClickListener(this);
        btnDell = view.findViewById(R.id.btn_dell);
        btnDell.setOnClickListener(this);
        btnHp = view.findViewById(R.id.btn_hp);
        btnHp.setOnClickListener(this);
        btnMsi = view.findViewById(R.id.btn_msi);
        btnMsi.setOnClickListener(this);
        btnAsus = view.findViewById(R.id.btn_asus);
        btnAsus.setOnClickListener(this);
        btnShowProductOutstanding = view.findViewById(R.id.btn_show_more_outstanding);
        btnShowProductOutstanding.setOnClickListener(this);
        btnShowProductSuggest = view.findViewById(R.id.btn_show_more_suggest);
        btnShowProductSuggest.setOnClickListener(this);
    }

    private void renderViewSlides() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectSlides = new JsonObjectRequest(
                Request.Method.GET,
                GET_ALL_SLIDES,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (listSlide.size() == 0) {
                            try {
                                JSONArray slides = response.getJSONArray("slides");
                                for (int i = 0; i < slides.length(); i++) {
                                    JSONObject slide = slides.getJSONObject(i);
                                    String slideId = slide.getString("_id");
                                    String title = slide.getString("title");
                                    String slug = slide.getString("slug");
                                    String thumbnail = slide.getString("thumbnail");

                                    listSlide.add(new Slide(slideId, title, slug, thumbnail));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setViewSlides(listSlide);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_connect_server", Objects.requireNonNull(error.getMessage()));
                    }
                }
        );
        requestQueue.add(jsonObjectSlides);
    }

    private void setViewSlides(List<Slide> list) {
        viewSlide = view.findViewById(R.id.view_slide_home);
        indicatorSlide = view.findViewById(R.id.circle_indicator_slide);
        SlideAdapter slideAdapter = new SlideAdapter();
        slideAdapter.setData(list);
        viewSlide.setAdapter(slideAdapter);
        indicatorSlide.setViewPager(viewSlide);
        handler.postDelayed(runnable, 5000);

        viewSlide.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
            }
        });
        viewSlide.setPageTransformer(new ZoomOutPageTransformer());
    }

    private void loadProductsOutstanding() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectProducts = new JsonObjectRequest(
                Request.Method.GET,
                GET_ALL_PRODUCTS + "?featured=" + true,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (listProductOutstanding.size() == 0) {
                            try {
                                JSONArray products = response.getJSONArray("products");
                                for (int i = 0; i < products.length(); i++) {
                                    JSONObject product = products.getJSONObject(i);
                                    String productId = product.getString("_id");
                                    String name = product.getString("name");
                                    int price = product.getInt("price");
                                    String thumbnail = product.getString("thumbnail");

                                    listProductOutstanding.add(new Product(productId, name, price, thumbnail));
                                }
                            } catch (JSONException e) {
                                Log.e("error_load_data", Objects.requireNonNull(e.getMessage()));
                            }
                        }

                        setProductsOutstandingView(listProductOutstanding);
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

    private void setProductsOutstandingView(List<Product> list) {
        productsOutstandingView = view.findViewById(R.id.products_outstanding_recyclerview);
        productGridAdapter = new ProductGridAdapter(new IOnClickItemProductListener() {
            @Override
            public void onClickItemProduct(String productId) {
                onClickGoToDetailProduct(getContext(), productId);
            }
        });
        productGridAdapter.setData(list);
        productsOutstandingView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productsOutstandingView.setNestedScrollingEnabled(false);
        productsOutstandingView.setAdapter(productGridAdapter);
    }

    private void loadProductsSuggest() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectProducts = new JsonObjectRequest(
                Request.Method.GET,
                GET_ALL_PRODUCTS + "?featured=" + false,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (listProductSuggest.size() == 0) {
                            try {
                                JSONArray products = response.getJSONArray("products");
                                for (int i = 0; i < products.length(); i++) {
                                    JSONObject product = products.getJSONObject(i);
                                    String productId = product.getString("_id");
                                    String name = product.getString("name");
                                    int price = product.getInt("price");
                                    String thumbnail = product.getString("thumbnail");

                                    listProductSuggest.add(new Product(productId, name, price, thumbnail));
                                }
                            } catch (JSONException e) {
                                Log.e("error_load_data", Objects.requireNonNull(e.getMessage()));
                            }
                        }

                        setProductsSuggestView(listProductSuggest);
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

    private void setProductsSuggestView(List<Product> list) {
        productsSuggestView = view.findViewById(R.id.products_suggest_recyclerview);
        productGridAdapter = new ProductGridAdapter(new IOnClickItemProductListener() {
            @Override
            public void onClickItemProduct(String productId) {
                onClickGoToDetailProduct(getContext(), productId);
            }
        });
        productGridAdapter.setData(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setInitialPrefetchItemCount(10);
        productsSuggestView.setLayoutManager(gridLayoutManager);
        productsSuggestView.setAdapter(productGridAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_notification:
                startActivity(new Intent(getContext(), NotificationActivity.class));
                break;
            case R.id.btn_apple:
                onClickToToActivityProductsBrand(getContext(), "apple");
                break;
            case R.id.btn_dell:
                onClickToToActivityProductsBrand(getContext(), "dell");
                break;
            case R.id.btn_hp:
                onClickToToActivityProductsBrand(getContext(), "hp");
                break;
            case R.id.btn_msi:
                onClickToToActivityProductsBrand(getContext(), "msi");
                break;
            case R.id.btn_asus:
                onClickToToActivityProductsBrand(getContext(), "asus");
                break;
            case R.id.btn_show_more_outstanding:
                onClickToToActivityProductsFeatured(getContext(), true);
                break;
            case R.id.btn_show_more_suggest:
                onClickToToActivityProductsFeatured(getContext(), false);
                break;
        }
    }
}