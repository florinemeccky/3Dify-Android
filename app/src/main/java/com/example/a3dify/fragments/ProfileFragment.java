package com.example.a3dify.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.example.a3dify.activities.AccountActivity;
import com.example.a3dify.activities.LoginActivity;
import com.example.a3dify.activities.SettingsActivity;
import com.example.a3dify.activities.HelpActivity;
import com.example.a3dify.activities.PrivacyPolicyActivity;
import com.example.a3dify.activities.UserFeedbackActivity;
import com.example.a3dify.activities.ReportActivity;
import com.example.a3dify.activities.ContactActivity;
import com.example.a3dify.activities.ComplainsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * ProfileFragment
 * Shows the user's profile information and links to all secondary screens.
 * Every row navigates to a real activity.
 * Logout signs out of Firebase and returns to LoginActivity.
 */
public class ProfileFragment extends Fragment {

    private long lastClickTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserInfo(view);
        setupRows(view);
    }

    private void loadUserInfo(View view) {
        TextView tvUsername  = view.findViewById(R.id.tv_profile_username);
        TextView tvEmail     = view.findViewById(R.id.tv_user_email);
        TextView tvInitials  = view.findViewById(R.id.tv_avatar_initials);
        TextView tvCompleted = view.findViewById(R.id.tv_profile_completed);
        TextView tvStreak    = view.findViewById(R.id.tv_profile_streak);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (tvEmail != null && user.getEmail() != null) {
                tvEmail.setText(user.getEmail());
            }

            DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
            android.database.Cursor cursor = db.getUserProfile(user.getUid());
            if (cursor != null && cursor.moveToFirst()) {
                int col = cursor.getColumnIndex(DatabaseHelper.COL_USERNAME);
                if (col >= 0) {
                    String username = cursor.getString(col);
                    if (tvUsername != null) tvUsername.setText(username);
                    // Set initials — first letter of username, uppercase
                    if (tvInitials != null && !username.isEmpty()) {
                        tvInitials.setText(String.valueOf(username.charAt(0)).toUpperCase());
                    }
                }
                cursor.close();
            }

            // Load stats
            int completed = db.getCompletedCount(user.getUid());
            int streak    = completed > 0 ? Math.max(1, completed / 2) : 0;
            if (tvCompleted != null) tvCompleted.setText(String.valueOf(completed));
            if (tvStreak    != null) tvStreak.setText(String.valueOf(streak));
        } else {
            if (tvUsername != null) tvUsername.setText("Guest");
            if (tvInitials != null) tvInitials.setText("G");
            if (tvEmail    != null) tvEmail.setText("Not signed in");
        }
    }

    private void configRow(View row, int iconRes, String label) {
        if (row == null) return;
        ImageView iv = row.findViewById(R.id.iv_row_icon);
        TextView  tv = row.findViewById(R.id.tv_row_label);
        if (iv != null) iv.setImageResource(iconRes);
        if (tv != null) tv.setText(label);
    }

    private void setupRows(View view) {
        View rowAccount    = view.findViewById(R.id.row_account);
        View rowSettings   = view.findViewById(R.id.row_settings);
        View rowFeedback   = view.findViewById(R.id.row_feedback);
        View rowReports    = view.findViewById(R.id.row_reports);
        View rowContact    = view.findViewById(R.id.row_contact);
        View rowComplaints = view.findViewById(R.id.row_complaints);
        View rowHelp       = view.findViewById(R.id.row_help);
        View rowPrivacy    = view.findViewById(R.id.row_privacy);

        configRow(rowAccount,    R.drawable.ic_nav_profile, "My Account");
        configRow(rowSettings,   R.drawable.ic_nav_progress, "Settings");
        configRow(rowFeedback,   R.drawable.ic_palette, "Give Feedback");
        configRow(rowReports,    R.drawable.ic_image, "My Reports");
        configRow(rowContact,    R.drawable.ic_nav_explore, "Contact Us");
        configRow(rowComplaints, R.drawable.ic_brush, "Submit Complaint");
        configRow(rowHelp,       R.drawable.ic_school, "Help & Support");
        configRow(rowPrivacy,    R.drawable.ic_cube, "Privacy Policy");

        View.OnClickListener debouncedNav = v -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < 600) return;
            lastClickTime = currentTime;

            Intent intent = null;
            int id = v.getId();
            if (id == R.id.row_account) intent = new Intent(requireActivity(), AccountActivity.class);
            else if (id == R.id.row_settings) intent = new Intent(requireActivity(), SettingsActivity.class);
            else if (id == R.id.row_feedback) intent = new Intent(requireActivity(), UserFeedbackActivity.class);
            else if (id == R.id.row_reports) intent = new Intent(requireActivity(), ReportActivity.class);
            else if (id == R.id.row_contact) intent = new Intent(requireActivity(), ContactActivity.class);
            else if (id == R.id.row_complaints) intent = new Intent(requireActivity(), ComplainsActivity.class);
            else if (id == R.id.row_help) intent = new Intent(requireActivity(), HelpActivity.class);
            else if (id == R.id.row_privacy) intent = new Intent(requireActivity(), PrivacyPolicyActivity.class);

            if (intent != null) startActivity(intent);
        };

        if (rowAccount != null) rowAccount.setOnClickListener(debouncedNav);
        if (rowSettings != null) rowSettings.setOnClickListener(debouncedNav);
        if (rowFeedback != null) rowFeedback.setOnClickListener(debouncedNav);
        if (rowReports != null) rowReports.setOnClickListener(debouncedNav);
        if (rowContact != null) rowContact.setOnClickListener(debouncedNav);
        if (rowComplaints != null) rowComplaints.setOnClickListener(debouncedNav);
        if (rowHelp != null) rowHelp.setOnClickListener(debouncedNav);
        if (rowPrivacy != null) rowPrivacy.setOnClickListener(debouncedNav);

        // Logout
        View btnLogout = view.findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < 600) return;
                lastClickTime = currentTime;

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
    }
}
