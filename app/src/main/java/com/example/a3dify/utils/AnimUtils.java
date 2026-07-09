package com.example.a3dify.utils;

import android.view.MotionEvent;
import android.view.View;

/*
 * AnimUtils
 * ══════════
 * Adds subtle press/release scale animations to any view.
 * Call AnimUtils.addPressAnimation(view) on any button or card
 * to give it physical press feedback.
 *
 * The card scales down to 96% on press and springs back on release.
 * This is the single biggest differentiator between apps that feel
 * premium and apps that feel like student projects.
 */
public class AnimUtils {

    private static long lastClickTime = 0;
    private static final long CLICK_THRESHOLD = 600; // ms

    public static void addPressAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Debounce check
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime < CLICK_THRESHOLD) {
                        return true; // Consume event
                    }
                    lastClickTime = currentTime;

                    v.animate()
                        .scaleX(0.96f)
                        .scaleY(0.96f)
                        .setDuration(120)
                        .start();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(150)
                        .setInterpolator(
                            new android.view.animation.OvershootInterpolator(2.0f))
                        .start();
                    break;
            }
            // Return false so click listeners still fire
            return false;
        });
    }

    /*
     * Adds a focus glow to an input field.
     * Background switches to bg_input_focused on focus,
     * back to bg_input_default on blur.
     */
    public static void addSearchFocusGlow(android.widget.EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            View parent = (View) v.getParent();
            if (parent != null) {
                parent.setBackgroundResource(hasFocus
                    ? com.example.a3dify.R.drawable.bg_input_focused
                    : com.example.a3dify.R.drawable.bg_input_default);
            }
        });
    }
}
