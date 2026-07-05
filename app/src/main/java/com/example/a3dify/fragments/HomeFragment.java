package com.example.a3dify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Calendar;

/*
 * HomeFragment
 * The first screen users see after logging in.
 * Shows a personalised greeting, continue learning card,
 * category pills, and featured tutorials.
 * Full content will be added in Phase 4.
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set greeting based on time of day
        TextView tvGreeting = view.findViewById(R.id.tv_greeting_label);
        if (tvGreeting != null) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour < 12) {
                tvGreeting.setText("Good morning,");
            } else if (hour < 17) {
                tvGreeting.setText("Good afternoon,");
            } else {
                tvGreeting.setText("Good evening,");
            }
        }

        // Show the logged-in user's email as a temporary name placeholder
        // In Phase 4 we will replace this with the username from SQLite
        TextView tvUsername = view.findViewById(R.id.tv_username);
        if (tvUsername != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getEmail() != null) {
                // Show the part of the email before the @ symbol as a name
                String email = user.getEmail();
                String name  = email.contains("@") ? email.split("@")[0] : email;
                tvUsername.setText(name + " ✦");
            } else {
                tvUsername.setText("Guest ✦");
            }
        }
    }
}