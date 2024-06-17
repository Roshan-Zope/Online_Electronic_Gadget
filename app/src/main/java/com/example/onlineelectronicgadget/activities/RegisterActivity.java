package com.example.onlineelectronicgadget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextInputLayout tlName;
    private TextInputLayout tlEmailR;
    private TextInputLayout tlPasswordR;
    private TextInputLayout tlConfirmPassword;
    private TextInputLayout tlOtp;
    private TextInputEditText nameEditText;
    private TextInputEditText emailEditTextR;
    private TextInputEditText passwordEditTextR;
    private TextInputEditText confirmPasswordEditText;
    private TextInputEditText otpEditText;
    private TextView loginLink;
    private Button registerButton;
    private Button otpButton;

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
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private boolean validateData() {
        List<Boolean> check = new ArrayList<>();

        if (nameEditText.getText().toString().trim().equals("")) {
            check.add(false);
            tlName.setError("Invalid name");
        } else check.add(true);

        if (emailEditTextR.getText().toString().trim().equals("")) {
            check.add(false);
            tlEmailR.setError("Invalid Email");
        } else check.add(true);

        if (passwordEditTextR.getText().toString().trim().equals("") && passwordEditTextR.getText().toString().trim().length() < 6) {
            check.add(false);
            tlPasswordR.setError("Invalid password. Password should be of 6 character");
        } else check.add(true);

        if (confirmPasswordEditText.getText().toString().trim().equals("") && confirmPasswordEditText.getText().toString().trim().equals(passwordEditTextR.getText().toString().trim())) {
            check.add(false);
            tlConfirmPassword.setError("Password doesn't match");
        } else check.add(true);

        if (otpEditText.getText().toString().trim().equals("")) {
            check.add(false);
            tlOtp.setError("please enter otp");
        } else check.add(true);

        return !check.contains(false);
    }

    private void registerUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "Registration successful");
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        saveUser(username, email, password);
                    } else {
                        Log.d("myTag", "Registration failed");
                        Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUser(String username, String email, String password) {
        User user = new User(username, email, password);

        mDatabase.child("users").child(username).setValue(user)
                .addOnSuccessListener(e -> {
                    Log.d("myTag", "user saved");
                    Toast.makeText(RegisterActivity.this, "user saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.d("myTag", "user saved failed");
                    Toast.makeText(RegisterActivity.this, "user saved failed", Toast.LENGTH_SHORT).show();
                });

    }

    private void initComponent() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameEditText = findViewById(R.id.nameEditText);
        emailEditTextR = findViewById(R.id.emailEditTextR);
        passwordEditTextR = findViewById(R.id.passwordEditTextR);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        tlName = findViewById(R.id.tlName);
        tlEmailR = findViewById(R.id.tlEmailR);
        tlPasswordR = findViewById(R.id.tlPasswordR);
        tlConfirmPassword = findViewById(R.id.tlConfirmPassword);
        tlOtp = findViewById(R.id.tlOtp);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        otpEditText = findViewById(R.id.otpEditText);
        otpButton = findViewById(R.id.otpButton);
    }
}