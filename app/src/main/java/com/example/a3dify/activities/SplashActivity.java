package com.example.a3dify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;
import android.os.Looper;
import com.example.a3dify.activities.OnboardingActivity;
import com.example.a3dify.activities.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Make the "D" in 3Dify orange
        TextView appName = findViewById(R.id.appName);
        SpannableString text = new SpannableString("3Dify");
        ForegroundColorSpan orange = new ForegroundColorSpan(Color.parseColor("#FF6A00"));
        text.setSpan(orange, 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        appName.setText(text);

        // Wait 2.5 seconds then navigate based on onboarding status
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if user has seen onboarding before
            SharedPreferences prefs = getSharedPreferences("3dify_prefs", MODE_PRIVATE);
            boolean onboardingDone = prefs.getBoolean("onboarding_done", false);

            Intent intent;
            if (!onboardingDone) {
                // First time user - show onboarding
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else {
                // Returning user - go to login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2500);
    }
}