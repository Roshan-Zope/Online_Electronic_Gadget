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

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private Auth auth;
    private TextInputLayout tlName;
    private TextInputLayout tlEmailR;
    private TextInputLayout tlPasswordR;
    private TextInputLayout tlConfirmPassword;
    private TextInputEditText nameEditText;
    private TextInputEditText emailEditTextR;
    private TextInputEditText passwordEditTextR;
    private TextInputEditText confirmPasswordEditText;
    private TextView loginLink;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponent();

        registerButton.setOnClickListener(v -> {
            if (validateData()) registerUser(nameEditText.getText().toString().trim(), emailEditTextR.getText().toString().trim(), passwordEditTextR.getText().toString().trim());
            else Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show();
        });

        loginLink.setOnClickListener(v -> {
            changeActivity(RegisterActivity.this, LoginActivity.class);
            Log.d("myTag", "to login activity");
        });

    }

    private void changeActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
        finish();
    }

    private boolean validateData() {
        List<Boolean> check = new ArrayList<>();

        if (nameEditText.getText().toString().trim().isEmpty()) {
            check.add(false);
            tlName.setError("Invalid name");
        } else check.add(true);

        if (emailEditTextR.getText().toString().trim().isEmpty()) {
            check.add(false);
            tlEmailR.setError("Invalid Email");
        } else check.add(true);

        if (passwordEditTextR.getText().toString().trim().isEmpty() && passwordEditTextR.getText().toString().trim().length() < 6) {
            check.add(false);
            tlPasswordR.setError("Invalid password. Password should be of 6 character");
        } else check.add(true);

        if (confirmPasswordEditText.getText().toString().trim().isEmpty()) {
            check.add(false);
            tlConfirmPassword.setError("Confirm password");
        } else {
            if (confirmPasswordEditText.getText().toString().trim().equals(passwordEditTextR.getText().toString().trim())) check.add(true);
            else {
                check.add(false);
                tlConfirmPassword.setError("password mismatch");
            }
        }

        return !check.contains(false);
    }

    private void registerUser(String username, String email, String password) {
        auth.registerUser(username, email, password);
    }

    private void initComponent() {
        auth = new Auth(this);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditTextR = findViewById(R.id.emailEditTextR);
        passwordEditTextR = findViewById(R.id.passwordEditTextR);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        tlName = findViewById(R.id.tlName);
        tlEmailR = findViewById(R.id.tlEmailR);
        tlPasswordR = findViewById(R.id.tlPasswordR);
        tlConfirmPassword = findViewById(R.id.tlConfirmPassword);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
    }
}