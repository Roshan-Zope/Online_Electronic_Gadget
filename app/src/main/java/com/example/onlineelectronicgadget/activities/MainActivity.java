package com.example.onlineelectronicgadget.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.authentication.Auth;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.fragments.AccountFragment;
import com.example.onlineelectronicgadget.fragments.AdminHomeScreen;
import com.example.onlineelectronicgadget.fragments.CartFragment;
import com.example.onlineelectronicgadget.fragments.HomeFragment;
import com.example.onlineelectronicgadget.fragments.SearchFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements CartFragment.OnCartItemCountChangeListener {
    private Auth auth;
    private BottomNavigationView bottomNavigationView;
    private String accType;
    private BadgeDrawable badgeDrawable;
    private DatabaseHelper db;
    private MutableLiveData<Integer> cartItemCount = new MutableLiveData<>();


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
        loadFragmentWithRespectToAccType(user);
        cartItemCount.setValue(0);
        observeCartItemCount();
    }

    private void observeCartItemCount() {
        cartItemCount.observe(this, count -> {
            updateCartItemCount(count);
        });
    }

    private void loadFragmentWithRespectToAccType(FirebaseUser user) {
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
            } else {
                Toast.makeText(this, "check your account type", Toast.LENGTH_SHORT).show();
            }

            bottomNavigationView.setOnItemSelectedListener(this::onBottomViewListener);
        } else {
            Log.d("myTag", "user is not login");
        }
    }

    private boolean onBottomViewListener(MenuItem item) {
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
        db = new DatabaseHelper();

        badgeDrawable = BadgeDrawable.create(this);
        badgeDrawable.setNumber(0);

        bottomNavigationView.getOrCreateBadge(R.id.cart_option).setVisible(true);
        bottomNavigationView.getOrCreateBadge(R.id.cart_option).setNumber(0);
    }

    @Override
    public void onCartItemCountChange(int count) {
        cartItemCount.setValue(count);
    }

    public void updateCartItemCount(int count) {
        bottomNavigationView.getOrCreateBadge(R.id.cart_option).setVisible(count > 0);
        bottomNavigationView.getOrCreateBadge(R.id.cart_option).setNumber(count);
    }
}