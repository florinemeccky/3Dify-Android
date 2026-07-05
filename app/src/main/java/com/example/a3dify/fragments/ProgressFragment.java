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
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * ProgressFragment
 * Shows the user's learning statistics and animated skill bars.
 * Completed count comes from SQLite.
 * Skill percentages are static for now — Phase 5 will make them dynamic.
 */
public class ProgressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadStats(view);
        animateSkillBars(view);
    }

    /*
     * Loads the completed tutorial count from SQLite.
     * Uses the Firebase UID to look up the correct user's data.
     */
    private void loadStats(View view) {
        TextView tvCompleted = view.findViewById(R.id.tv_completed_count);
        if (tvCompleted == null) return;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
            int count = db.getCompletedCount(user.getUid());
            tvCompleted.setText(String.valueOf(count));
        }
    }

    /*
     * Animates each skill progress bar from 0 to its target percentage.
     * Each bar uses ValueAnimator for a smooth slide-in effect.
     * The start delay staggers the animations so they feel polished.
     */
    private void animateSkillBars(View view) {
        animateBar(view, R.id.bar_modeling,   0.72f, 900,  200);
        animateBar(view, R.id.bar_animation,  0.38f, 900,  350);
        animateBar(view, R.id.bar_sculpting,  0.55f, 900,  500);
        animateBar(view, R.id.bar_rendering,  0.20f, 900,  650);
    }

    private void animateBar(View root, int barId, float percent,
                            long duration, long startDelay) {
        View bar = root.findViewById(barId);
        if (bar == null) return;

        bar.post(() -> {
            View track      = (View) bar.getParent();
            int trackWidth  = track.getWidth();
            int targetWidth = (int) (trackWidth * percent);

            ValueAnimator animator = ValueAnimator.ofInt(0, targetWidth);
            animator.setDuration(duration);
            animator.setStartDelay(startDelay);
            animator.addUpdateListener(anim -> {
                ViewGroup.LayoutParams p = bar.getLayoutParams();
                p.width = (int) anim.getAnimatedValue();
                bar.setLayoutParams(p);
            });
            animator.start();
        });
    }
}