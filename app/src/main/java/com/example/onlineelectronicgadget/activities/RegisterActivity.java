package com.example.onlineelectronicgadget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText nameEditText;
    private EditText emailEditTextR;
    private EditText passwordEditTextR;
    private Button registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponent();

        registerButton.setOnClickListener(v -> {
            registerUser(nameEditText.getText().toString(), emailEditTextR.getText().toString(), passwordEditTextR.getText().toString());
        });

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
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
    }
}