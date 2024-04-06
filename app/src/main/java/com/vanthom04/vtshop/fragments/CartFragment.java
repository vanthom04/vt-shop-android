package com.vanthom04.vtshop.fragments;

import static com.vanthom04.vtshop.utils.AppUtils.changeIntToString;
import static com.vanthom04.vtshop.utils.Apis.GET_CART_BY_USER;
import static com.vanthom04.vtshop.utils.AppUtils.onClickGoToDetailProduct;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vanthom04.vtshop.MainActivity;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.adapters.CartProductAdapter;
import com.vanthom04.vtshop.models.ProductCart;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;
import com.vanthom04.vtshop.utils.AppPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment {

    private View view;
    ProgressBar loadingCart;
    LinearLayout emptyView;
    ConstraintLayout boxBottom;
    RecyclerView productCartView;
    CartProductAdapter cartProductAdapter;
    TextView totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        //
        mapping();

        AppPreferences preferences = AppPreferences.getInstance(requireContext());
        String userId = preferences.getString("userId", "");

        if (!userId.equals("")) {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            JsonObjectRequest jsonObjectCart = new JsonObjectRequest(
                    Request.Method.GET,
                    GET_CART_BY_USER + userId,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            List<ProductCart> list = new ArrayList<>();

                            try {
                                loadingCart.setVisibility(View.VISIBLE);

                                JSONObject cart = response.getJSONObject("cart");
                                JSONArray products = cart.getJSONArray("products");
                                for (int i = 0; i < products.length(); i++) {
                                    JSONObject product = products.getJSONObject(i);
                                    String productId = product.getString("productId");
                                    String name = product.getString("name");
                                    String thumbnail = product.getString("thumbnail");
                                    int price = product.getInt("price");
                                    int quantity = product.getInt("quantity");

                                    list.add(new ProductCart(productId, name, thumbnail, price, quantity));
                                }

                                totalPrice.setText(changeIntToString(cart.getInt("totalPrice")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loadingCart.setVisibility(View.GONE);

                            if (list.size() == 0) {
                                showEmptyView();
                            } else {
                                showDataView();
                                setViewProductsCart(list);
                            }

                            MainActivity.setAlertNotifyCart(String.valueOf(list.size()));
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
            Toast.makeText(getContext(), "Vui lòng đăng nhập để xem sản phẩm trong giỏ hàng!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void mapping() {
        loadingCart = view.findViewById(R.id.loading_cart);
        productCartView = view.findViewById(R.id.recyclerview_cart);
        totalPrice = view.findViewById(R.id.total_price);
        emptyView = view.findViewById(R.id.empty_view);
        boxBottom = view.findViewById(R.id.box_bottom);
    }

    private void showDataView() {
        productCartView.setVisibility(View.VISIBLE);
        productCartView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boxBottom.setVisibility(View.VISIBLE);;
        boxBottom.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        emptyView.getLayoutParams().height = 0;
        emptyView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        productCartView.setVisibility(View.GONE);
        productCartView.getLayoutParams().height = 0;
        boxBottom.getLayoutParams().height = 0;
        boxBottom.setVisibility(View.GONE);
    }

    private void setViewProductsCart(List<ProductCart> list) {
        cartProductAdapter = new CartProductAdapter(new IOnClickItemProductListener() {
            @Override
            public void onClickItemProduct(String productId) {
                onClickGoToDetailProduct(getContext(), productId);
            }
        });
        cartProductAdapter.setData(list);
        productCartView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        productCartView.setAdapter(cartProductAdapter);
    }

}