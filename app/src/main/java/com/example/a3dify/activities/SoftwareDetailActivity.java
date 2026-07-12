package com.example.a3dify.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;
import com.example.a3dify.SoftwareToolRepository;
import com.example.a3dify.models.SoftwareTool;

/*
 * SoftwareDetailActivity
 * Shows full information about one 3D software tool.
 * Launched from the Home screen "3D Tools" section.
 * Receives the software name via Intent extra "software_name".
 * All data comes from SoftwareToolRepository — no network call.
 */
public class SoftwareDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_detail);

        // Back button
        LinearLayout btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Get software name from Intent
        String name = getIntent().getStringExtra("software_name");
        if (name == null) { finish(); return; }

        // Look up the data
        SoftwareTool tool = SoftwareToolRepository.getInstance().findByName(name);
        if (tool == null) { finish(); return; }

        // ── Hero ──
        View heroColor = findViewById(R.id.view_hero_color);
        if (heroColor != null) heroColor.setBackgroundColor(tool.getIconColor());

        TextView tvHeroInitial = findViewById(R.id.tv_hero_initial);
        if (tvHeroInitial != null)
            tvHeroInitial.setText(String.valueOf(tool.getName().charAt(0)));

        TextView tvHeroName = findViewById(R.id.tv_hero_name);
        if (tvHeroName != null) tvHeroName.setText(tool.getName());

        TextView tvHeroPricing = findViewById(R.id.tv_hero_pricing);
        if (tvHeroPricing != null) {
            tvHeroPricing.setText(tool.getPricing());
            if (tool.getPricing().startsWith("Free")) {
                tvHeroPricing.setTextColor(0xFF34D399);
                tvHeroPricing.setBackgroundResource(R.drawable.bg_badge_free);
            } else {
                tvHeroPricing.setTextColor(0xFFFBBF24);
                tvHeroPricing.setBackgroundResource(R.drawable.bg_badge_paid);
            }
        }

        // ── Content ──
        setField(R.id.tv_website,     tool.getWebsite());
        setField(R.id.tv_description, tool.getDescription());
        setField(R.id.tv_best_for,    tool.getBestFor());
        setField(R.id.tv_min_specs,   tool.getMinSpecs());
        setField(R.id.tv_rec_specs,   tool.getRecommendedSpecs());
        setField(R.id.tv_shortcuts,   tool.getShortcuts());
        setField(R.id.tv_tips,        tool.getTips());

        // ── Beginner rating ──
        TextView tvBeginner = findViewById(R.id.tv_beginner);
        if (tvBeginner != null) tvBeginner.setText(tool.getBeginnerRating());

        TextView tvBeginnerIcon = findViewById(R.id.tv_beginner_icon);
        if (tvBeginnerIcon != null) {
            switch (tool.getBeginnerRating()) {
                case "Excellent": tvBeginnerIcon.setText("★★★★★"); break;
                case "Good":      tvBeginnerIcon.setText("★★★★☆"); break;
                case "Moderate":  tvBeginnerIcon.setText("★★★☆☆"); break;
                default:          tvBeginnerIcon.setText("★★☆☆☆"); break;
            }
        }
    }

    private void setField(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (tv != null) tv.setText(text);
    }
}