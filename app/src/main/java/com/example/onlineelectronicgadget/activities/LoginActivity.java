package com.example.onlineelectronicgadget.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.authentication.Auth;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Auth auth;
    private TextInputLayout tlEmail;
    private TextInputLayout tlPassword;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
        Log.d("myTag", "in login activity");

        loginButton.setOnClickListener(v -> login(etEmail.getText().toString(), etPassword.getText().toString()));

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void login(String email, String password) {
        auth.login(email, password);
    }

    public boolean validateData() {
        List<Boolean> check = new ArrayList<>();

        if (etEmail.getText().toString().trim().isEmpty()) {
            check.add(false);
            tlEmail.setError("Invalid Email");
        } else check.add(true);

        if (etPassword.getText().toString().trim().isEmpty()) {
            check.add(false);
            tlPassword.setError("Please enter password");
        } else check.add(true);

        return !check.contains(false);
    }

    private void initComponent() {
        auth = new Auth(this);
        tlEmail = findViewById(R.id.tlEmail);
        tlPassword = findViewById(R.id.tlPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
    }
}