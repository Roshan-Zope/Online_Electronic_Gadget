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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
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
        if (validateData()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            if (task.isSuccessful()) {
                                Log.d("myTag", "Authentication successful");
                                Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                                changeActivity(LoginActivity.this, MainActivity.class);
                            } else {
                                Log.d("myTag", "Authentication failed");
                                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("myTag", "Email is not verified");
                            Toast.makeText(LoginActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void changeActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
        finish();
    }

    private boolean validateData() {
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
        mAuth = FirebaseAuth.getInstance();
        tlEmail = findViewById(R.id.tlEmail);
        tlPassword = findViewById(R.id.tlPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
    }
}