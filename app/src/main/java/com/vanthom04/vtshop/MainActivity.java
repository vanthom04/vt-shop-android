package com.vanthom04.vtshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.vanthom04.vtshop.fragments.AccountFragment;
import com.vanthom04.vtshop.fragments.CartFragment;
import com.vanthom04.vtshop.fragments.HomeFragment;
import com.vanthom04.vtshop.fragments.BrandFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @SuppressLint("StaticFieldLeak")
    static AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setBottomNavigationView();
        setAHBottomNavigationView();
    }

    private void setBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        replaceFragment(new HomeFragment());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.item_home_bottom_nav:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.item_category_bottom_nav:
                        replaceFragment(new BrandFragment());
                        break;
                    case R.id.item_cart_bottom_nav:
                        replaceFragment(new CartFragment());
                        break;
                    case R.id.item_account_bottom_nav:
                        replaceFragment(new AccountFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void setAHBottomNavigationView() {
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.home, R.drawable.nav_home, R.color.primary_color);
        AHBottomNavigationItem category = new AHBottomNavigationItem(R.string.category, R.drawable.nav_category, R.color.primary_color);
        AHBottomNavigationItem cart = new AHBottomNavigationItem(R.string.cart, R.drawable.nav_cart, R.color.primary_color);
        AHBottomNavigationItem account = new AHBottomNavigationItem(R.string.account, R.drawable.nav_user, R.color.primary_color);

        // Add items
        bottomNavigation.addItem(home);
        bottomNavigation.addItem(category);
        bottomNavigation.addItem(cart);
        bottomNavigation.addItem(account);

        // Change colors
        bottomNavigation.setAccentColor(getResources().getColor(R.color.primary_color));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.gray));

        // set state
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        setAlertNotifyCart("0");

        replaceFragment(new HomeFragment());
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        replaceFragment(new HomeFragment());
                        break;
                    case 1:
                        replaceFragment(new BrandFragment());
                        break;
                    case 2:
                        replaceFragment(new CartFragment());
                        break;
                    case 3:
                        replaceFragment(new AccountFragment());
                        break;
                }
                return true;
            }
        });
    }

    public static void setAlertNotifyCart(String title) {
        bottomNavigation.setNotification(title, 2);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}