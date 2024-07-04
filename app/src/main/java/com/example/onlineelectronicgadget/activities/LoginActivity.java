package com.example.onlineelectronicgadget.activities;

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

public class LoginActivity extends AppCompatActivity {
    private Auth auth;
    private TextInputLayout tlEmail, tlPassword;
    private TextInputEditText etEmail, etPassword;
    private Button loginButton;
    private TextView registerLink;
    private ChipGroup chipGroupL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
        Log.d("myTag", "in login activity");
    }

    private void onLoginButton() {
        String email = String.valueOf(etEmail.getText()).trim();
        String password = String.valueOf(etPassword.getText()).trim();
        Chip chip = findViewById(chipGroupL.getCheckedChipId());
        if (chip == null) {
            Toast.makeText(this, "Please select account type", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("myTag", "" + chipGroupL.getCheckedChipId());
            String accType = String.valueOf(chip.getText());
            if (validateData()) login(email, password, accType);
            else Log.d("myTag", "invalid data");
        }
    }

    private void onRegisterLink() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(String email, String password, String accType) {auth.login(email, password, accType);
    }

    public boolean validateData() {
        List<Boolean> check = new ArrayList<>();
        String email = String.valueOf(etEmail.getText()).trim();
        String password = String.valueOf(etPassword.getText()).trim();

        if (email.isEmpty()) {
            check.add(false);
            tlEmail.setError("Invalid Email");
        } else check.add(true);

        if (password.isEmpty()) {
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
        chipGroupL = findViewById(R.id.chipGroupL);

        loginButton.setOnClickListener(v -> onLoginButton());
        registerLink.setOnClickListener(v -> onRegisterLink());
    }
}