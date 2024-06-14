package com.example.onlineelectronicgadget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.onlineelectronicgadget.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView userInfo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        initComponent();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) userInfo.setText("Welcome" + user.getEmail());
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initComponent() {
        mAuth = FirebaseAuth.getInstance();
        userInfo = findViewById(R.id.userInfo);
    }
}