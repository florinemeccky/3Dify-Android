package com.example.a3dify.activities;

import com.example.a3dify.CloudDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.CloudDatabase;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * AccountActivity
 * Lets the user view and update their username.
 * Reads current username from SQLite, writes the updated value back.
 */
public class AccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private TextView tvSuccess, tvCurrentEmail;
    private DatabaseHelper db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        db = DatabaseHelper.getInstance(this);

        etUsername     = findViewById(R.id.et_username);
        tvSuccess      = findViewById(R.id.tv_save_success);
        tvCurrentEmail = findViewById(R.id.tv_current_email);

        TextView tvBack = findViewById(R.id.tv_back);
        Button   btnSave = findViewById(R.id.btn_save);

        tvBack.setOnClickListener(v -> finish());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();

            // Show email
            if (tvCurrentEmail != null && user.getEmail() != null) {
                tvCurrentEmail.setText(user.getEmail());
            }

            // Pre-fill username from SQLite
            Cursor cursor = db.getUserProfile(uid);
            if (cursor != null && cursor.moveToFirst()) {
                int col = cursor.getColumnIndex(DatabaseHelper.COL_USERNAME);
                if (col >= 0) etUsername.setText(cursor.getString(col));
                cursor.close();
            }
        }

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String newUsername = etUsername.getText().toString().trim();

        if (newUsername.isEmpty()) {
            etUsername.setError("Username cannot be empty");
            return;
        }
        if (newUsername.length() < 3) {
            etUsername.setError("Username must be at least 3 characters");
            return;
        }

        // Save to SQLite locally
        boolean saved = db.updateUsername(uid, newUsername);

        // Sync to cloud
        CloudDatabase.getInstance().updateUsername(uid, newUsername);

        if (saved) {
            tvSuccess.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Username updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Could not save locally. Cloud sync attempted.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}