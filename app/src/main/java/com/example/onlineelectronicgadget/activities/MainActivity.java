package com.example.onlineelectronicgadget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.fragments.AccountFragment;
import com.example.onlineelectronicgadget.fragments.CartFragment;
import com.example.onlineelectronicgadget.fragments.HomeFragment;
import com.example.onlineelectronicgadget.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        initComponent();
        loadFragment(new HomeFragment());

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) Log.d("myTag", "user successfully login");
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_option) {
                    loadFragment(new HomeFragment());
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
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initComponent() {
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.navigation_layout);
    }
}