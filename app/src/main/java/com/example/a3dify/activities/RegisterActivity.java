package com.example.a3dify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;

/*
 * RegisterActivity
 * Creates a new user account using Firebase Authentication.
 * After the account is created, saves the username to SQLite via DatabaseHelper.
 *
 * Flow:
 *   Fill form → tap Create Account → Firebase creates auth account
 *   → SQLite saves username and email → go to DashboardActivity
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;

    private EditText etUsername, etEmail, etPassword, etConfirm;
    private CheckBox cbTerms;
    private ProgressBar progressBar;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialise Firebase and SQLite
        mAuth    = FirebaseAuth.getInstance();
        dbHelper = DatabaseHelper.getInstance(this);

        // Connect views
        etUsername  = findViewById(R.id.et_username);
        etEmail     = findViewById(R.id.et_email);
        etPassword  = findViewById(R.id.et_password);
        etConfirm   = findViewById(R.id.et_confirm);
        cbTerms     = findViewById(R.id.cb_terms);
        progressBar = findViewById(R.id.progress_bar);
        btnCreate   = findViewById(R.id.btn_create);

        // Back arrow returns to LoginActivity
        TextView tvBack = findViewById(R.id.tv_back);
        if (tvBack != null) {
            tvBack.setOnClickListener(v -> finish());
        }

        // "Sign in" link — color it orange and navigate back to Login
        TextView tvLogin = findViewById(R.id.tv_login);
        if (tvLogin != null) {
            SpannableString loginText = new SpannableString("Already have an account? Sign in");
            loginText.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#FF6A00")),
                    25, 32,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            tvLogin.setText(loginText);
            tvLogin.setOnClickListener(v -> finish());
        }

        btnCreate.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String username = etUsername.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm  = etConfirm.getText().toString().trim();

        // Validation — check every field before calling Firebase
        if (username.isEmpty()) {
            etUsername.setError("Please enter a username");
            etUsername.requestFocus();
            return;
        }
        if (username.length() < 3) {
            etUsername.setError("Username must be at least 3 characters");
            etUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Please enter a password");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirm)) {
            etConfirm.setError("Passwords do not match");
            etConfirm.requestFocus();
            return;
        }
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the Terms of Service to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoadingState(true);

        // Step 1 — Create the Firebase Authentication account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Step 2 — Save username and email to SQLite
                        String uid = mAuth.getCurrentUser().getUid();
                        boolean saved = dbHelper.saveUserProfile(uid, username, email);

                        setLoadingState(false);

                        if (saved) {
                            Toast.makeText(this,
                                    "Welcome to 3Dify, " + username + "!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Auth worked, SQLite failed — not critical, continue anyway
                            Toast.makeText(this,
                                    "Account created! Some profile data may sync later.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Go to Dashboard — clear back stack so Back doesn't return here
                        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        setLoadingState(false);
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Registration failed. Please try again.";
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoadingState(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        btnCreate.setEnabled(!isLoading);
        btnCreate.setAlpha(isLoading ? 0.6f : 1.0f);
    }
}