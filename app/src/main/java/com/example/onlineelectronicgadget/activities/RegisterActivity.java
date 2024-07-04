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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private Auth auth;
    private TextInputLayout tlName, tlEmailR, tlPasswordR, tlConfirmPassword;
    private TextInputEditText nameEditText, emailEditTextR, passwordEditTextR, confirmPasswordEditText;
    private TextView loginLink;
    private Button registerButton;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponent();
    }

    private void onRegisterButton() {
        String name = String.valueOf(nameEditText.getText()).trim();
        String email = String.valueOf(emailEditTextR.getText()).trim();
        String password = String.valueOf(passwordEditTextR.getText()).trim();
        Chip chip = findViewById(chipGroup.getCheckedChipId());
        if (chip == null) {
            Toast.makeText(this, "Please select account type", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("myTag", "" + chipGroup.getCheckedChipId());
            String accType = String.valueOf(chip.getText());
            if (validateData()) registerUser(name, email, password, accType);
            else Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show();
        }
    }

    private void onLoginLink() {
        changeActivity(RegisterActivity.this, LoginActivity.class);
        Log.d("myTag", "to login activity");
    }

    private void registerUser(String username, String email, String password, String accType) {
        auth.registerUser(username, email, password, accType);
    }

    private void changeActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
        finish();
    }

    private boolean validateData() {
        List<Boolean> check = new ArrayList<>();
        String name = String.valueOf(nameEditText.getText()).trim();
        String email = String.valueOf(emailEditTextR.getText()).trim();
        String password = String.valueOf(passwordEditTextR.getText()).trim();
        String confirmPassword = String.valueOf(confirmPasswordEditText.getText()).trim();

        if (name.isEmpty()) {
            check.add(false);
            tlName.setError("Invalid name");
        } else check.add(true);

        if (email.isEmpty()) {
            check.add(false);
            tlEmailR.setError("Invalid Email");
        } else check.add(true);

        if (password.trim().isEmpty() && password.length() < 6) {
            check.add(false);
            tlPasswordR.setError("Invalid password. Password should be of 6 character");
        } else check.add(true);

        if (confirmPassword.isEmpty()) {
            check.add(false);
            tlConfirmPassword.setError("Confirm password");
        } else {
            if (confirmPassword.equals(password)) check.add(true);
            else {
                check.add(false);
                tlConfirmPassword.setError("password mismatch");
            }
        }

        return !check.contains(false);
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
        chipGroup = findViewById(R.id.chipGroup);

        registerButton.setOnClickListener(v -> onRegisterButton());
        loginLink.setOnClickListener(v -> onLoginLink());
    }
}