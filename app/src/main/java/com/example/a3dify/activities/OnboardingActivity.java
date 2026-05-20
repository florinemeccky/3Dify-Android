package com.example.a3dify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.a3dify.R;
import com.example.a3dify.adapters.OnboardingAdapter;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private View dot1, dot2, dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.vp_onboarding);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        TextView btnSkip = findViewById(R.id.tv_skip);
        TextView btnGetStarted = findViewById(R.id.btn_get_started);

        // Attach the adapter (3 pages)
        viewPager.setAdapter(new OnboardingAdapter(this));

        // Update dots on page change
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDots(position);
            }
        });

        btnSkip.setOnClickListener(v -> goToLogin());
        btnGetStarted.setOnClickListener(v -> goToLogin());
    }

    private void updateDots(int position) {
        // Reset all dots to small/inactive
        setDotSize(dot1, false);
        setDotSize(dot2, false);
        setDotSize(dot3, false);

        // Make active dot wide and orange
        if (position == 0) setDotSize(dot1, true);
        else if (position == 1) setDotSize(dot2, true);
        else setDotSize(dot3, true);
    }

    private void setDotSize(View dot, boolean active) {
        int width = active
                ? (int) (18 * getResources().getDisplayMetrics().density)
                : (int) (6 * getResources().getDisplayMetrics().density);

        android.view.ViewGroup.LayoutParams params = dot.getLayoutParams();
        params.width = width;
        dot.setLayoutParams(params);

        dot.setBackgroundResource(active
                ? R.drawable.bg_btn_orange
                : R.color.border_dark);
    }

    private void goToLogin() {
        // Mark onboarding as seen so it never shows again
        SharedPreferences prefs = getSharedPreferences("3dify_prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_done", true).apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}