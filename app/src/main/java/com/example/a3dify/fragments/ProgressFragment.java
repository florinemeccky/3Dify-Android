package com.example.a3dify.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.a3dify.R;

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

        // Animate each skill bar from 0 to its target %
        animateBar(view, R.id.bar_modeling,  0.72f, 900);
        animateBar(view, R.id.bar_animation, 0.38f, 1000);
        animateBar(view, R.id.bar_sculpting, 0.55f, 1100);
        animateBar(view, R.id.bar_rendering, 0.20f, 800);
    }

    private void animateBar(View root, int barId, float percent, long duration) {
        View bar = root.findViewById(barId);
        bar.post(() -> {
            int parentWidth = ((View) bar.getParent()).getWidth();
            int targetWidth = (int) (parentWidth * percent);

            ValueAnimator animator = ValueAnimator.ofInt(0, targetWidth);
            animator.setDuration(duration);
            animator.setStartDelay(200); // slight delay so user sees animation
            animator.addUpdateListener(anim -> {
                ViewGroup.LayoutParams p = bar.getLayoutParams();
                p.width = (int) anim.getAnimatedValue();
                bar.setLayoutParams(p);
            });
            animator.start();
        });
    }
}