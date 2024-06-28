package com.example.onlineelectronicgadget.authentication;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.example.onlineelectronicgadget.activities.LoginActivity;
import com.example.onlineelectronicgadget.activities.MainActivity;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    private DatabaseHelper db;
    private FirebaseAuth mAuth;
    private Context context;
    private LoginActivity loginActivity;

    public Auth(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseHelper();
        loginActivity = new LoginActivity();
    }

    public void authenticate() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) Log.d("myTag", "user successfully login");
        else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }

    public void registerUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "Registration successful");
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show();

                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener((Activity) context, t -> {
                            if (t.isSuccessful()) {
                                Log.d("myTag", "Email Verification sent");
                                Toast.makeText(context, "Verify your email", Toast.LENGTH_SHORT).show();

                                db.saveUser(username, email, password);

                                changeActivity(context, LoginActivity.class);
                            }
                            else {
                                Log.d("myTag", "Email verification failed");
                                Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.d("myTag", "Registration failed");
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void login(String email, String password) {
        Log.d("myTag", "in Auth class => login()");
        if (loginActivity.validateData()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, task -> {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            if (task.isSuccessful()) {
                                Log.d("myTag", "Authentication successful");
                                Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show();
                                changeActivity(context, MainActivity.class);
                            } else {
                                Log.d("myTag", "Authentication failed");
                                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("myTag", "Email is not verified");
                            Toast.makeText(context, "Please verify your email first", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void changeActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

}
