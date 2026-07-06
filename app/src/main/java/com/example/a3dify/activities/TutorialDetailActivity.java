package com.example.a3dify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
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
 *
 * Features:
 *   - Watch Tutorial button opens a YouTube search for the tutorial title
 *   - Mark as Done button writes to SQLite via DatabaseHelper
 *   - Notes are saved per-tutorial in SharedPreferences
 *   - Completed status is checked on open and shown if already done
 */
public class TutorialDetailActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String uid        = "guest";
    private String tutorialId = "unknown";

    private Button   btnMarkDone;
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

        // Safety defaults so nothing crashes if an extra is missing
        if (icon == null)        icon        = "🧊";
        if (title == null)       title       = "Tutorial";
        if (category == null)    category    = "General";
        if (difficulty == null)  difficulty  = "Beginner";
        if (duration == null)    duration    = "";
        if (description == null) description = "";
        if (tutorialId == null)  tutorialId  = title;

        // Connect all views
        TextView tvBack         = findViewById(R.id.tv_back);
        TextView tvIcon         = findViewById(R.id.tv_tutorial_icon);
        TextView tvTitle        = findViewById(R.id.tv_tutorial_title);
        TextView tvDifficulty   = findViewById(R.id.tv_difficulty_badge);
        TextView tvDuration     = findViewById(R.id.tv_duration_badge);
        TextView tvCategory     = findViewById(R.id.tv_category_badge);
        TextView tvDesc         = findViewById(R.id.tv_description);
        Button   btnWatch       = findViewById(R.id.btn_watch);
        Button   btnSaveNotes   = findViewById(R.id.btn_save_notes);
        btnMarkDone             = findViewById(R.id.btn_mark_done);
        tvDoneStatus            = findViewById(R.id.tv_done_status);
        etNotes                 = findViewById(R.id.et_notes);

        // Populate views with tutorial data
        tvIcon.setText(icon);
        tvTitle.setText(title);
        tvDifficulty.setText(difficulty);
        tvDuration.setText(duration);
        tvCategory.setText(category);
        tvDesc.setText(description);

        // Back arrow
        tvBack.setOnClickListener(v -> finish());

        // Check if this tutorial was already completed
        checkCompletionStatus();

        // Load any previously saved notes for this tutorial
        loadNotes(tutorialId);

        /*
         * Watch Tutorial button.
         * Opens a YouTube search for the tutorial title.
         * In a real app this would link to a specific video URL.
         * Using ACTION_SEARCH_GLOBALLY or a YouTube search intent
         * means this always works without hardcoding any video IDs.
         */
        String finalTitle = title;
        btnWatch.setOnClickListener(v -> {
            String searchQuery = "Blender " + finalTitle + " tutorial";
            Intent watchIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query="
                            + Uri.encode(searchQuery)));
            startActivity(watchIntent);
        });

        /*
         * Mark as Done button.
         * Calls DatabaseHelper.markTutorialComplete() which writes
         * a row to the progress table in SQLite.
         * This is what makes the Progress screen completed count increment.
         */
        String finalTutorialId = tutorialId;
        btnMarkDone.setOnClickListener(v -> markAsComplete(finalTutorialId));

        /*
         * Save Notes button.
         * Stores notes in SharedPreferences keyed by tutorialId.
         * SharedPreferences is appropriate here because notes are
         * small, personal, and per-tutorial — no need for a full SQLite table.
         */
        btnSaveNotes.setOnClickListener(v -> saveNotes(finalTutorialId));
    }

    /*
     * Checks SQLite to see if this tutorial has already been marked done.
     * If it has, shows the green Completed badge and disables the Mark Done button.
     */
    private void checkCompletionStatus() {
        int completedCount = db.getCompletedCount(uid);
        // Check specifically for this tutorial
        boolean isCompleted = db.isTutorialCompleted(uid, tutorialId);

        if (isCompleted) {
            tvDoneStatus.setVisibility(View.VISIBLE);
            btnMarkDone.setText("✓   Already Completed");
            btnMarkDone.setEnabled(false);
            btnMarkDone.setAlpha(0.5f);
        }
    }

    /*
     * Writes the completion record to SQLite and updates the UI.
     * After this, the Progress screen completed count will increase by 1.
     */
    private void markAsComplete(String tutorialId) {
        boolean saved = db.markTutorialComplete(uid, tutorialId);

        if (saved) {
            tvDoneStatus.setVisibility(View.VISIBLE);
            btnMarkDone.setText("✓   Already Completed");
            btnMarkDone.setEnabled(false);
            btnMarkDone.setAlpha(0.5f);
            Toast.makeText(this,
                    "Tutorial marked as complete! Check your Progress tab.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,
                    "Could not save progress. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Loads notes from SharedPreferences for this specific tutorial
    private void loadNotes(String tutorialId) {
        SharedPreferences prefs = getSharedPreferences("tutorial_notes", MODE_PRIVATE);
        String saved = prefs.getString("notes_" + tutorialId, "");
        if (!saved.isEmpty()) {
            etNotes.setText(saved);
        }
    }

    // Saves the current notes text to SharedPreferences
    private void saveNotes(String tutorialId) {
        String notes = etNotes.getText().toString().trim();
        SharedPreferences prefs = getSharedPreferences("tutorial_notes", MODE_PRIVATE);
        prefs.edit().putString("notes_" + tutorialId, notes).apply();
        Toast.makeText(this, "Notes saved!", Toast.LENGTH_SHORT).show();
    }
}