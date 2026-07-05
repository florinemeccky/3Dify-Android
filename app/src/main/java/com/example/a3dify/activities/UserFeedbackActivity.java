package com.example.a3dify.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * UserFeedbackActivity
 * Collects a star rating (1-5) and written feedback from the user.
 * Saves both to SQLite via DatabaseHelper.saveFeedback().
 */
public class UserFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);

        TextView  tvBack    = findViewById(R.id.tv_back);
        RatingBar ratingBar = findViewById(R.id.rating_bar);
        EditText  etFeedback = findViewById(R.id.et_feedback);
        Button    btnSubmit  = findViewById(R.id.btn_submit);
        TextView  tvSuccess  = findViewById(R.id.tv_success);

        tvBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            String feedbackText = etFeedback.getText().toString().trim();
            int    rating       = (int) ratingBar.getRating();

            if (feedbackText.isEmpty()) {
                etFeedback.setError("Please write your feedback before submitting");
                return;
            }

            // Get user UID — use "guest" for non-logged-in users
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user != null ? user.getUid() : "guest";

            DatabaseHelper db = DatabaseHelper.getInstance(this);
            boolean saved = db.saveFeedback(uid, feedbackText, rating);

            if (saved) {
                // Hide form, show success message
                ratingBar.setVisibility(View.GONE);
                etFeedback.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                tvSuccess.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Could not save feedback. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}