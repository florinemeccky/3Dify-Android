package com.example.a3dify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * LoginActivity
 * Handles email and password sign-in using Firebase Authentication.
 *
 * Flow:
 *   If user is already signed in → skip to DashboardActivity automatically
 *   If login succeeds → go to DashboardActivity
 *   If login fails → show the Firebase error message as a Toast
 *   Guest button → go to DashboardActivity without an account
 *   Sign up link → go to RegisterActivity
 *   Forgot password → go to ResetPasswordActivity
 */
public class LoginActivity extends AppCompatActivity {

    // Firebase Authentication instance
    private FirebaseAuth mAuth;

    // UI elements
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGuest;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialise Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // If user is already logged in, skip the login screen entirely
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToDashboard();
            return; // stop onCreate here so the rest does not run
        }

        // Connect variables to the views in activity_login.xml
        etEmail     = findViewById(R.id.et_email);
        etPassword  = findViewById(R.id.et_password);
        btnLogin    = findViewById(R.id.btn_login);
        btnGuest    = findViewById(R.id.btn_guest);
        progressBar = findViewById(R.id.progress_bar);

        // Color the logo text so D is orange — same as SplashActivity
        TextView tvLogo = findViewById(R.id.tv_logo_text);
        if (tvLogo != null) {
            SpannableString logo = new SpannableString("3Dify");
            logo.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#FF6A00")),
                    1, 2,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            tvLogo.setText(logo);
        }

        // Color "Sign up" in orange inside the register link text
        TextView tvSignUp = findViewById(R.id.tv_signup);
        if (tvSignUp != null) {
            SpannableString signUpText = new SpannableString("Don't have an account? Sign up");
            signUpText.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#FF6A00")),
                    23, 30,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            tvSignUp.setText(signUpText);

            tvSignUp.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
            );
        }

        // Forgot password link
        TextView tvForgot = findViewById(R.id.tv_forgot);
        if (tvForgot != null) {
            tvForgot.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class))
            );
        }

        // Login button — validates fields then calls Firebase
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Guest button — skips login entirely
        btnGuest.setOnClickListener(v -> goToDashboard());
    }

    /*
     * Validates the email and password fields.
     * If valid, calls Firebase signInWithEmailAndPassword.
     * Shows a spinner while waiting for Firebase to respond.
     */
    private void attemptLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // Show spinner and disable button while Firebase works
        setLoadingState(true);

        // Firebase Authentication call
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    setLoadingState(false);

                    if (task.isSuccessful()) {
                        // Login worked
                        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
                        goToDashboard();
                    } else {
                        // Login failed — show Firebase's error message
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Login failed. Please try again.";
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Shows or hides the loading spinner and enables or disables the login button
    private void setLoadingState(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        btnLogin.setEnabled(!isLoading);
        btnLogin.setAlpha(isLoading ? 0.6f : 1.0f);
    }

    /*
     * Navigates to DashboardActivity.
     * FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK means the user
     * cannot press Back and return to the login screen after logging in.
     */
    private void goToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}