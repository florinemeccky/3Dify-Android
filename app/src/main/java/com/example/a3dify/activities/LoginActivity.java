package com.example.a3dify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail    = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin     = findViewById(R.id.btn_login);
        Button btnGuest     = findViewById(R.id.btn_guest);
        LinearLayout llGoogle = findViewById(R.id.ll_google);
        TextView tvRegister = findViewById(R.id.tv_register);
        TextView tvForgot   = findViewById(R.id.tv_forgot);

        // Color "Create one" orange — same technique as the D in 3Dify
        SpannableString registerText = new SpannableString("No account? Create one");
        registerText.setSpan(
                new ForegroundColorSpan(Color.parseColor("#FF6A00")),
                12, 22,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        tvRegister.setText(registerText);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Enter your email");
                return;
            }
            if (pass.isEmpty()) {
                etPassword.setError("Enter your password");
                return;
            }
            // TODO: Replace with real auth (Firebase etc.)
            // For now, any non-empty credentials go to MainActivity
            goToMain();
        });

        btnGuest.setOnClickListener(v -> goToMain());

        llGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Google Sign-In coming soon", Toast.LENGTH_SHORT).show()
        );

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        tvForgot.setOnClickListener(v ->
                Toast.makeText(this, "Password reset coming soon", Toast.LENGTH_SHORT).show()
        );
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}