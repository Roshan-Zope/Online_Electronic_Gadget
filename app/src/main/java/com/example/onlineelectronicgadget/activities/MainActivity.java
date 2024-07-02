package com.example.onlineelectronicgadget.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.authentication.Auth;
import com.example.onlineelectronicgadget.fragments.AccountFragment;
import com.example.onlineelectronicgadget.fragments.AdminHomeScreen;
import com.example.onlineelectronicgadget.fragments.CartFragment;
import com.example.onlineelectronicgadget.fragments.HomeFragment;
import com.example.onlineelectronicgadget.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Auth auth;
    private BottomNavigationView bottomNavigationView;
    private String accType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        initComponent();

        Log.d("myTag", "before authenticate()");
        auth.authenticate();
        Log.d("myTag", "after authenticate()");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String id = user.getUid();
            SharedPreferences preferences = getSharedPreferences("myPref", MODE_PRIVATE);
            accType = preferences.getString(id + "_accType", null);
            if (accType == null) {
                accType = "Customer";
            }

            if (accType.equals("Customer")) {
                loadFragment(new HomeFragment());
            } else if (accType.equals("Retailer")) {
                loadFragment(new AdminHomeScreen());
            }

            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.home_option) {
                    if (accType.equals("Customer")) loadFragment(new HomeFragment());
                    else if (accType.equals("Retailer")) loadFragment(new AdminHomeScreen());
                }
                if (item.getItemId() == R.id.search_option) {
                    loadFragment(new SearchFragment());
                }
                if (item.getItemId() == R.id.cart_option) {
                    loadFragment(new CartFragment());
                }
                if (item.getItemId() == R.id.account_option) {
                    loadFragment(new AccountFragment());
                }

                return true;
            });
        } else {
            Log.d("myTag", "user is not login");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initComponent() {
        auth = new Auth(this);
        bottomNavigationView = findViewById(R.id.navigation_layout);
    }
}