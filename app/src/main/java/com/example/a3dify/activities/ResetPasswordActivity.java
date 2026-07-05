package com.example.a3dify.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;

/*
 * ResetPasswordActivity
 * Sends a password reset email using Firebase Authentication.
 * The user enters their email address and Firebase sends a link
 * to that address automatically. We do not handle the reset ourselves —
 * Firebase does it entirely on their side.
 */
public class ResetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail;
    private ProgressBar progressBar;
    private TextView tvSuccess;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth       = FirebaseAuth.getInstance();
        etEmail     = findViewById(R.id.et_email);
        progressBar = findViewById(R.id.progress_bar);
        tvSuccess   = findViewById(R.id.tv_success);
        btnSend     = findViewById(R.id.btn_send);

        TextView tvBack = findViewById(R.id.tv_back);
        if (tvBack != null) {
            tvBack.setOnClickListener(v -> finish());
        }

        btnSend.setOnClickListener(v -> sendResetEmail());
    }

    private void sendResetEmail() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Please enter your email address");
            etEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        // Show spinner while Firebase sends the email
        progressBar.setVisibility(View.VISIBLE);
        btnSend.setEnabled(false);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnSend.setEnabled(true);

                    if (task.isSuccessful()) {
                        // Hide the form, show the success message
                        etEmail.setVisibility(View.GONE);
                        btnSend.setVisibility(View.GONE);
                        tvSuccess.setVisibility(View.VISIBLE);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Could not send reset email. Please try again.";
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}