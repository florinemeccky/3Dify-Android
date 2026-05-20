package com.example.a3dify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etUsername = findViewById(R.id.et_username);
        EditText etEmail    = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        EditText etConfirm  = findViewById(R.id.et_confirm);
        CheckBox cbTerms    = findViewById(R.id.cb_terms);
        Button   btnCreate  = findViewById(R.id.btn_create);
        TextView tvSignin   = findViewById(R.id.tv_signin);
        TextView tvBack     = findViewById(R.id.tv_back);

        // Color "Sign in" orange
        SpannableString signinText = new SpannableString("Already have one? Sign in");
        signinText.setSpan(
                new ForegroundColorSpan(Color.parseColor("#FF6A00")),
                18, 25,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        tvSignin.setText(signinText);

        tvBack.setOnClickListener(v -> finish());
        tvSignin.setOnClickListener(v -> finish());

        btnCreate.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm  = etConfirm.getText().toString().trim();

            if (username.isEmpty()) {
                etUsername.setError("Enter a username"); return;
            }
            if (username.length() < 3) {
                etUsername.setError("Username must be at least 3 characters"); return;
            }
            if (email.isEmpty()) {
                etEmail.setError("Enter your email"); return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Enter a password"); return;
            }
            if (password.length() < 8) {
                etPassword.setError("Password must be at least 8 characters"); return;
            }
            if (!confirm.equals(password)) {
                etConfirm.setError("Passwords do not match"); return;
            }
            if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Please accept the Terms of Service", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Replace with real registration (Firebase etc.)
            Toast.makeText(this, "Account created! Please sign in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}