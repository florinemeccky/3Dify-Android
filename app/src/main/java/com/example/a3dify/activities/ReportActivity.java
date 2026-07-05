package com.example.a3dify.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * ReportActivity
 * Reads data from SQLite and generates a formatted learning report.
 * Shows:
 *   - Total tutorials completed
 *   - Number of feedback submissions
 *   - Number of complaint submissions
 * Tapping Generate Full Report builds a detailed text report and saves it to SQLite.
 */
public class ReportActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String uid = "guest";
    private TextView tvReportOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        db = DatabaseHelper.getInstance(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) uid = user.getUid();

        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(v -> finish());

        tvReportOutput = findViewById(R.id.tv_report_output);

        loadSummaryStats();

        Button btnGenerate = findViewById(R.id.btn_generate);
        if (btnGenerate != null) {
            btnGenerate.setOnClickListener(v -> generateFullReport());
        }
    }

    /*
     * Loads the three summary numbers from SQLite and displays them
     * in the summary card at the top of the screen.
     */
    private void loadSummaryStats() {
        // Completed tutorials
        TextView tvCompleted = findViewById(R.id.tv_report_completed);
        if (tvCompleted != null) {
            int count = db.getCompletedCount(uid);
            tvCompleted.setText(String.valueOf(count));
        }

        // Feedback count
        TextView tvFeedback = findViewById(R.id.tv_report_feedback);
        if (tvFeedback != null) {
            Cursor c = db.getAllFeedback();
            int count = c != null ? c.getCount() : 0;
            if (c != null) c.close();
            tvFeedback.setText(String.valueOf(count));
        }

        // Complaints count
        TextView tvComplaints = findViewById(R.id.tv_report_complaints);
        if (tvComplaints != null) {
            Cursor c = db.getUserComplaints(uid);
            int count = c != null ? c.getCount() : 0;
            if (c != null) c.close();
            tvComplaints.setText(String.valueOf(count));
        }
    }

    /*
     * Builds a formatted text report, displays it on screen,
     * and saves it to the SQLite reports table.
     */
    private void generateFullReport() {
        String date = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(new Date());

        int completed  = db.getCompletedCount(uid);
        Cursor fb      = db.getAllFeedback();
        int feedbackN  = fb != null ? fb.getCount() : 0;
        if (fb != null) fb.close();
        Cursor cp      = db.getUserComplaints(uid);
        int complainN  = cp != null ? cp.getCount() : 0;
        if (cp != null) cp.close();

        // Build the report text
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════\n");
        report.append("  3DIFY LEARNING REPORT\n");
        report.append("═══════════════════════════\n\n");
        report.append("Generated: ").append(date).append("\n\n");
        report.append("LEARNING ACTIVITY\n");
        report.append("───────────────────\n");
        report.append("Tutorials Completed : ").append(completed).append("\n");
        report.append("Feedback Submitted  : ").append(feedbackN).append("\n");
        report.append("Complaints Filed    : ").append(complainN).append("\n\n");
        report.append("SKILL PROGRESS\n");
        report.append("───────────────────\n");
        report.append("Modeling    : ████████░░ 72%\n");
        report.append("Animation   : ████░░░░░░ 38%\n");
        report.append("Sculpting   : █████░░░░░ 55%\n");
        report.append("Rendering   : ██░░░░░░░░ 20%\n\n");
        report.append("═══════════════════════════\n");
        report.append("Keep learning every day!\n");

        // Show on screen
        if (tvReportOutput != null) {
            tvReportOutput.setText(report.toString());
            tvReportOutput.setVisibility(View.VISIBLE);
        }

        // Save to SQLite
        db.saveReport(uid, "Learning Report", report.toString());
    }
}