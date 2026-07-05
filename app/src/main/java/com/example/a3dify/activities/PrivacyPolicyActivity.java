package com.example.a3dify.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;

/*
 * PrivacyPolicyActivity
 * Displays the privacy policy — static screen.
 */
public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(v -> finish());
    }
}