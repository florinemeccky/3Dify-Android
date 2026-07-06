package com.example.a3dify.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.example.a3dify.adapters.CategoryAdapter;
import com.example.a3dify.adapters.TutorialAdapter;
import com.example.a3dify.models.Category;
import com.example.a3dify.models.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * HomeFragment
 * The main dashboard screen shown after login.
 *
 * Shows:
 *   - Time-based greeting with the user's username from SQLite
 *   - Continue Learning card with animated progress bar
 *   - Horizontal category pill row
 *   - Horizontal featured tutorial card row
 */
public class HomeFragment extends Fragment {

    private RecyclerView rvCategories, rvFeatured;

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

        setupGreeting(view);
        setupMotivationalQuote(view);
        setupContinueLearningBar(view);
        setupCategoryRow(view);
        setupFeaturedTutorials(view);
    }

    /*
     * Sets the greeting text based on current hour.
     * Then loads the username from SQLite using the Firebase UID.
     * Falls back to the email prefix if SQLite has no record yet.
     */
    private void setupGreeting(View view) {
        TextView tvGreeting  = view.findViewById(R.id.tv_greeting_label);
        TextView tvUsername  = view.findViewById(R.id.tv_username);

        // Time-based greeting
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (tvGreeting != null) {
            if (hour < 12)      tvGreeting.setText("Good morning,");
            else if (hour < 17) tvGreeting.setText("Good afternoon,");
            else                tvGreeting.setText("Good evening,");
        }

        // Load username from SQLite
        if (tvUsername != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
                android.database.Cursor cursor = db.getUserProfile(uid);

                if (cursor != null && cursor.moveToFirst()) {
                    int col = cursor.getColumnIndex(DatabaseHelper.COL_USERNAME);
                    if (col >= 0) {
                        String username = cursor.getString(col);
                        tvUsername.setText(username + " ✦");
                    }
                    cursor.close();
                } else {
                    // No SQLite record — use email prefix as fallback
                    String email = user.getEmail() != null ? user.getEmail() : "";
                    String name  = email.contains("@") ? email.split("@")[0] : "Learner";
                    tvUsername.setText(name + " ✦");
                }
            } else {
                tvUsername.setText("Guest ✦");
            }
        }
    }

    /*
     * Rotates motivational quotes based on the day of the year.
     */
    private void setupMotivationalQuote(View view) {
        String[] quotes = {
                "Every expert was once a beginner. Start your lesson today.",
                "3D mastery is built one tutorial at a time.",
                "The best render is the one you finish.",
                "Blender is a tool. Creativity is yours.",
                "Model something today that did not exist yesterday."
        };
        TextView tvQuote = view.findViewById(R.id.tv_quote);
        if (tvQuote != null) {
            int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            tvQuote.setText(quotes[dayOfYear % quotes.length]);
        }
    }

    /*
     * Animates the orange progress bar from 0 to 67% width.
     * Uses ValueAnimator so the bar slides in smoothly when
     * the Home tab is opened rather than jumping to the final value.
     */
    private void setupContinueLearningBar(View view) {
        View progressFill = view.findViewById(R.id.view_progress_fill);
        if (progressFill == null) return;

        progressFill.post(() -> {
            // Get the width of the parent track
            View track = (View) progressFill.getParent();
            int trackWidth = track.getWidth();
            int targetWidth = (int) (trackWidth * 0.67f);

            ValueAnimator animator = ValueAnimator.ofInt(0, targetWidth);
            animator.setDuration(900);
            animator.setStartDelay(300);
            animator.addUpdateListener(anim -> {
                ViewGroup.LayoutParams params = progressFill.getLayoutParams();
                params.width = (int) anim.getAnimatedValue();
                progressFill.setLayoutParams(params);
            });
            animator.start();
        });
    }

    /*
     * Sets up the horizontal category pill RecyclerView.
     * Uses CategoryAdapter in pill mode (useRowLayout = false).
     */
    private void setupCategoryRow(View view) {
        rvCategories = view.findViewById(R.id.rv_categories);
        if (rvCategories == null) return;

        rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        rvCategories.setAdapter(new CategoryAdapter(getContext(), getCategories(), false));
    }

    /*
     * Sets up the horizontal featured tutorial RecyclerView.
     * Uses TutorialAdapter.
     */
    private void setupFeaturedTutorials(View view) {
        rvFeatured = view.findViewById(R.id.rv_featured);
        if (rvFeatured == null) return;

        rvFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        TutorialAdapter adapter = new TutorialAdapter(getContext(), getTutorials());
        adapter.setOnItemClickListener(tutorial -> {
            // TODO Phase 5: open TutorialDetailActivity with this tutorial's data
        });
        rvFeatured.setAdapter(adapter);
    }

    // Sample categories — in a real app these would come from a database
    private List<Category> getCategories() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("🔷", "Basics",   24, "#4A90E2", "Beginner"));
        list.add(new Category("🧊", "Modeling", 38, "#FF6A00", "All levels"));
        list.add(new Category("🎬", "Animate",  19, "#7B5EA7", "Intermediate"));
        list.add(new Category("🌊", "Sculpt",   15, "#2ECC71", "Intermediate"));
        list.add(new Category("💡", "Render",   22, "#F4D03F", "Advanced"));
        list.add(new Category("⚙️", "Nodes",    12, "#E74C3C", "Advanced"));
        return list;
    }

    // Sample tutorials — in a real app these would come from SQLite or an API
    private List<Tutorial> getTutorials() {
        List<Tutorial> list = new ArrayList<>();
        list.add(new Tutorial("🧊", "Intro to 3D Modeling",
                "Modeling", "Beginner", "28 min",
                "Learn the fundamental tools and viewport navigation in Blender."));
        list.add(new Tutorial("🌊", "Sculpting Basics",
                "Sculpting", "Beginner", "22 min",
                "Explore the sculpt mode brushes and dynamic topology."));
        list.add(new Tutorial("⚙️", "Geometry Nodes",
                "Nodes", "Intermediate", "45 min",
                "Build procedural models using Blender's node-based system."));
        list.add(new Tutorial("🎨", "PBR Materials",
                "Materials", "Intermediate", "35 min",
                "Create physically based render materials with the node editor."));
        list.add(new Tutorial("💡", "Lighting Setups",
                "Rendering", "Beginner", "18 min",
                "Three-point lighting, HDRI environments, and light linking."));
        return list;
    }
}