package com.example.a3dify.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.a3dify.activities.ContactActivity;
import com.example.a3dify.activities.ComplainsActivity;

/*
 * ProfileFragment
 * Shows the user's profile information and links to all secondary screens.
 * Every row navigates to a real activity.
 * Logout signs out of Firebase and returns to LoginActivity.
 */
public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserInfo(view);
        setupRowNavigation(view);
    }

    /*
     * Loads username from SQLite and email from Firebase.
     * Shows them in the profile header.
     */
    private void loadUserInfo(View view) {
        TextView tvUsername = view.findViewById(R.id.tv_profile_username);
        TextView tvEmail    = view.findViewById(R.id.tv_user_email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Show email from Firebase
            if (tvEmail != null) {
                tvEmail.setText(user.getEmail() != null ? user.getEmail() : "");
            }

            // Load username from SQLite
            if (tvUsername != null) {
                DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
                Cursor cursor = db.getUserProfile(user.getUid());
                if (cursor != null && cursor.moveToFirst()) {
                    int col = cursor.getColumnIndex(DatabaseHelper.COL_USERNAME);
                    if (col >= 0) {
                        tvUsername.setText(cursor.getString(col));
                    }
                    cursor.close();
                }
            }
        } else {
            if (tvUsername != null) tvUsername.setText("Guest");
            if (tvEmail != null)    tvEmail.setText("Not signed in");
        }
    }

    /*
     * Each row navigates to its matching activity.
     * This is how all secondary screens become reachable from the app.
     */
    private void setupRowNavigation(View view) {

        // Account / edit profile
        LinearLayout rowAccount = view.findViewById(R.id.row_account);
        if (rowAccount != null) {
            rowAccount.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), AccountActivity.class))
            );
        }

        // Settings
        LinearLayout rowSettings = view.findViewById(R.id.row_settings);
        if (rowSettings != null) {
            rowSettings.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), SettingsActivity.class))
            );
        }

        // Feedback
        LinearLayout rowFeedback = view.findViewById(R.id.row_feedback);
        if (rowFeedback != null) {
            rowFeedback.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), UserFeedbackActivity.class))
            );
        }

        // Reports
        LinearLayout rowReports = view.findViewById(R.id.row_reports);
        if (rowReports != null) {
            rowReports.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), ReportActivity.class))
            );
        }

        // Help
        LinearLayout rowHelp = view.findViewById(R.id.row_help);
        if (rowHelp != null) {
            rowHelp.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), HelpActivity.class))
            );
        }

        // Privacy Policy
        LinearLayout rowPrivacy = view.findViewById(R.id.row_privacy);
        if (rowPrivacy != null) {
            rowPrivacy.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), PrivacyPolicyActivity.class))
            );
        }

        // Logout
        LinearLayout btnLogout = view.findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
        LinearLayout rowContact = view.findViewById(R.id.row_contact);
        if (rowContact != null) {
            rowContact.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), ContactActivity.class))
            );
        }

        LinearLayout rowComplaints = view.findViewById(R.id.row_complaints);
        if (rowComplaints != null) {
            rowComplaints.setOnClickListener(v ->
                    startActivity(new Intent(requireActivity(), ComplainsActivity.class))
            );
        }
    }
}