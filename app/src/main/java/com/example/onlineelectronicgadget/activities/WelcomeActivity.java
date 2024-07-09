package com.example.onlineelectronicgadget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineelectronicgadget.R;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    private static final int WELCOME_SCREEN_DISPLAY_LENGTH = 3000;
    private ImageView logo;
    private TextView company_name;
    private TextView tagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        logo = findViewById(R.id.logo);
        company_name = findViewById(R.id.company_name);
        tagline = findViewById(R.id.tagline);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(anim);
        company_name.startAnimation(anim);
        tagline.startAnimation(anim);

        new Handler().postDelayed(() -> checkUserAuthentication(), WELCOME_SCREEN_DISPLAY_LENGTH);
    }

    private void checkUserAuthentication() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }
        finish();
    }
}