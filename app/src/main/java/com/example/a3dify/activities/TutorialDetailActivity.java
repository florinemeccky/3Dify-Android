package com.example.a3dify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.CloudDatabase;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.example.a3dify.models.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * TutorialDetailActivity
 * Shows the full details of one tutorial.
 *
 * Receives tutorial data from the calling fragment via Intent extras:
 *   - "icon"        — emoji string
 *   - "title"       — tutorial name
 *   - "category"    — category name
 *   - "difficulty"  — Beginner / Intermediate / Advanced
 *   - "duration"    — e.g. "22 min"
 *   - "description" — full description text
 *   - "tutorialId"  — unique ID string for SQLite tracking
 */
public class TutorialDetailActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String uid        = "guest";
    private String tutorialId = "unknown";

    private android.widget.LinearLayout btnMarkDone;
    private TextView tvDoneStatus;
    private EditText etNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_detail);

        db = DatabaseHelper.getInstance(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) uid = user.getUid();

        // Read all tutorial data passed from the adapter via Intent
        Intent intent     = getIntent();
        String icon       = intent.getStringExtra("icon");
        String title      = intent.getStringExtra("title");
        String category   = intent.getStringExtra("category");
        String difficulty = intent.getStringExtra("difficulty");
        String duration   = intent.getStringExtra("duration");
        String description = intent.getStringExtra("description");
        tutorialId        = intent.getStringExtra("tutorialId");

        // Connect all views
        View      btnBack         = findViewById(R.id.btn_back);

        // ── Hero gradient and icon — always visible ──
        View viewHeroBg = findViewById(R.id.view_hero_bg);
        ImageView ivHeroIcon = findViewById(R.id.iv_hero_icon);

        // Build a temporary Tutorial object just to access the helper methods
        com.example.a3dify.models.Tutorial tempTut =
            new com.example.a3dify.models.Tutorial(
                icon, title, category, difficulty,
                duration, description, tutorialId);

        // Apply the colored gradient background
        if (viewHeroBg != null) {
            viewHeroBg.setBackgroundResource(tempTut.getThumbnailBackground());
        }

        // Apply the white category icon over the gradient
        if (ivHeroIcon != null) {
            ivHeroIcon.setImageResource(tempTut.getCategoryIcon());
            ivHeroIcon.setVisibility(android.view.View.VISIBLE);
            ivHeroIcon.setColorFilter(
                android.graphics.Color.WHITE,
                android.graphics.PorterDuff.Mode.SRC_IN);
        }

        TextView  tvTitle         = findViewById(R.id.tv_tutorial_title);
        TextView  tvDifficulty    = findViewById(R.id.tv_difficulty_badge);
        TextView  tvDuration      = findViewById(R.id.tv_duration_badge);
        TextView  tvCategory      = findViewById(R.id.tv_category_badge);
        TextView  tvDesc          = findViewById(R.id.tv_description);
        android.widget.LinearLayout btnWatch        = findViewById(R.id.btn_watch);
        Button    btnSaveNotes    = findViewById(R.id.btn_save_notes);
        btnMarkDone               = findViewById(R.id.btn_mark_done);
        tvDoneStatus              = findViewById(R.id.tv_done_status);
        etNotes                   = findViewById(R.id.et_notes);

        // Populate views with tutorial data
        tvTitle.setText(title);
        tvDesc.setText(description);
        tvDuration.setText(duration);
        tvCategory.setText(category);

        // Metadata badges styling
        if (tvDifficulty != null) {
            tvDifficulty.setText(difficulty);
            tvDifficulty.setTextColor(tempTut.getDifficultyColor());
            tvDifficulty.setBackgroundResource(tempTut.getDifficultyBadgeBackground());
        }

        // Back arrow
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Check if this tutorial was already completed
        checkCompletionStatus();

        // Load any previously saved notes for this tutorial
        loadNotes(tutorialId);

        /*
         * Watch Tutorial button.
         */
        if (btnWatch != null) {
            btnWatch.setOnClickListener(v -> {
                String searchQuery = "Blender " + title + " tutorial";
                Intent watchIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/results?search_query="
                                + Uri.encode(searchQuery)));
                startActivity(watchIntent);
            });
        }

        /*
         * Mark as Done button.
         */
        if (btnMarkDone != null) {
            btnMarkDone.setOnClickListener(v -> markAsComplete(tutorialId));
        }

        /*
         * Save Notes button.
         */
        if (btnSaveNotes != null) {
            btnSaveNotes.setOnClickListener(v -> saveNotes(tutorialId));
        }

        // Press animations on action buttons
        if (btnWatch    != null) com.example.a3dify.utils.AnimUtils.addPressAnimation(btnWatch);
        if (btnMarkDone != null) com.example.a3dify.utils.AnimUtils.addPressAnimation(btnMarkDone);
    }

    private void checkCompletionStatus() {
        boolean isCompleted = db.isTutorialCompleted(uid, tutorialId);
        if (isCompleted) {
            if (tvDoneStatus != null) tvDoneStatus.setVisibility(View.VISIBLE);
            if (btnMarkDone != null) {
                btnMarkDone.setEnabled(false);
                btnMarkDone.setAlpha(0.5f);
            }
        }
    }

    private void markAsComplete(String tutorialId) {
        boolean saved = db.markTutorialComplete(uid, tutorialId);
        if (saved) {
            CloudDatabase.getInstance().markTutorialComplete(uid, tutorialId);
            int newCount = db.getCompletedCount(uid);
            CloudDatabase.getInstance().updateCompletedCount(uid, newCount);

            if (tvDoneStatus != null) tvDoneStatus.setVisibility(View.VISIBLE);
            if (btnMarkDone != null) {
                btnMarkDone.setEnabled(false);
                btnMarkDone.setAlpha(0.5f);
            }

            Toast.makeText(this,
                    "Tutorial marked as complete! Check your Progress tab.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,
                    "Could not save progress. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNotes(String tutorialId) {
        SharedPreferences prefs = getSharedPreferences("tutorial_notes", MODE_PRIVATE);
        String saved = prefs.getString("notes_" + tutorialId, "");
        if (!saved.isEmpty()) {
            etNotes.setText(saved);
        }
    }

    private void saveNotes(String tutorialId) {
        String notes = etNotes.getText().toString().trim();
        SharedPreferences prefs = getSharedPreferences("tutorial_notes", MODE_PRIVATE);
        prefs.edit().putString("notes_" + tutorialId, notes).apply();
        Toast.makeText(this, "Notes saved!", Toast.LENGTH_SHORT).show();
    }
}
