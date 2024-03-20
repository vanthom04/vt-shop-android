package com.vanthom04.vtshop.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.vanthom04.vtshop.activities.products.DetailProductActivity;
import com.vanthom04.vtshop.activities.products.ProductsActivity;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class AppUtils {
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null
                    && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    || (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
    }

    public static boolean isValidEmail(String email) {
        String regexEmail = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,4}$";
        return Pattern.compile(regexEmail).matcher(email).matches();
    }

    public static String capitalize(String value) {
        char firstLetter = value.charAt(0);
        char capitalFirstLetter = Character.toUpperCase(firstLetter);
        return value.replace(value.charAt(0), capitalFirstLetter);
    }

    public static String changeIntToString(int value) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
    }

    public static void onClickGoToDetailProduct(Context context, String productId) {
        Intent intent = new Intent(context, DetailProductActivity.class);
        intent.putExtra(Constants.PRODUCT_ID_KEY, productId);
        context.startActivity(intent);
    }

    public static void onClickToToActivityProductsBrand(Context context, String brand) {
        Intent intent = new Intent(context, ProductsActivity.class);
        intent.putExtra(Constants.BRAND_NAME_KEY, brand);
        context.startActivity(intent);
    }

    public static void onClickToToActivityProductsFeatured(Context context, boolean featured) {
        Intent intent = new Intent(context, ProductsActivity.class);
        intent.putExtra(Constants.FEATURED_NAME_KEY, featured);
        context.startActivity(intent);
    }
}
