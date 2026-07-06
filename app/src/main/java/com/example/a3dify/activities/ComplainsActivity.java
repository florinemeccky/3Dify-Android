package com.example.a3dify.activities;

import com.example.a3dify.CloudDatabase;
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
 * ComplainsActivity
 * Lets users submit a written complaint.
 * Saved to SQLite with status "pending".
 */
public class ComplainsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complains);

        TextView tvBack      = findViewById(R.id.tv_back);
        EditText etComplaint = findViewById(R.id.et_complaint);
        Button   btnSubmit   = findViewById(R.id.btn_submit);
        TextView tvSuccess   = findViewById(R.id.tv_success);

        tvBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            String complaintText = etComplaint.getText().toString().trim();

            if (complaintText.isEmpty()) {
                etComplaint.setError("Please describe your complaint");
                return;
            }
            if (complaintText.length() < 20) {
                etComplaint.setError("Please provide more detail (at least 20 characters)");
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user != null ? user.getUid() : "guest";

            DatabaseHelper db = DatabaseHelper.getInstance(this);

            // Save to SQLite locally
            boolean saved = db.saveComplaint(uid, complaintText);

            // Sync to cloud
            CloudDatabase.getInstance().saveComplaint(uid, complaintText);

            if (saved) {
                etComplaint.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                tvSuccess.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this,
                        "Could not save locally. Cloud sync attempted.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}