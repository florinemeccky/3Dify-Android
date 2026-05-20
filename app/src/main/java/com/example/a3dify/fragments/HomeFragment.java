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
import com.example.a3dify.R;
import com.example.a3dify.adapters.CategoryAdapter;
import com.example.a3dify.adapters.TutorialAdapter;
import com.example.a3dify.models.CategoryModel;
import com.example.a3dify.models.TutorialModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

        // ── Greeting based on time of day ──
        TextView tvGreeting = view.findViewById(R.id.tv_greeting_label);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12)       tvGreeting.setText("Good morning,");
        else if (hour < 17)  tvGreeting.setText("Good afternoon,");
        else                 tvGreeting.setText("Good evening,");

        // ── Animate progress bar to 67% ──
        View progressFill = view.findViewById(R.id.view_progress_fill);
        progressFill.post(() -> {
            int parentWidth = ((View) progressFill.getParent()).getWidth();
            int targetWidth = (int) (parentWidth * 0.67f);

            ValueAnimator animator = ValueAnimator.ofInt(0, targetWidth);
            animator.setDuration(900);
            animator.addUpdateListener(anim -> {
                ViewGroup.LayoutParams p = progressFill.getLayoutParams();
                p.width = (int) anim.getAnimatedValue();
                progressFill.setLayoutParams(p);
            });
            animator.start();
        });

        // ── Categories RecyclerView (horizontal) ──
        RecyclerView rvCategories = view.findViewById(R.id.rv_categories);
        rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(new CategoryAdapter(getSampleCategories(), false));

        // ── Featured Tutorials RecyclerView (horizontal cards) ──
        RecyclerView rvFeatured = view.findViewById(R.id.rv_featured);
        rvFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFeatured.setAdapter(new TutorialAdapter(getSampleTutorials(), true, requireActivity()));

        // ── Recommended RecyclerView (vertical rows, no internal scroll) ──
        RecyclerView rvRecommended = view.findViewById(R.id.rv_recommended);
        rvRecommended.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecommended.setAdapter(new TutorialAdapter(getSampleTutorials(), false, requireActivity()));
        rvRecommended.setNestedScrollingEnabled(false);
    }

    private List<CategoryModel> getSampleCategories() {
        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("🔷", "Basics",  "24", "#4A90E2", "Beginner"));
        list.add(new CategoryModel("🧊", "Model",   "38", "#FF6A00", "All levels"));
        list.add(new CategoryModel("🎬", "Animate", "19", "#7B5EA7", "Intermediate"));
        list.add(new CategoryModel("🌊", "Sculpt",  "15", "#2ECC71", "Intermediate"));
        list.add(new CategoryModel("💡", "Render",  "22", "#F4D03F", "Advanced"));
        list.add(new CategoryModel("⚙️", "Nodes",   "12", "#E74C3C", "Advanced"));
        return list;
    }

    private List<TutorialModel> getSampleTutorials() {
        List<TutorialModel> list = new ArrayList<>();
        list.add(new TutorialModel("🎯", "Sculpting Basics",
                "Beginner", "22 min",
                "Learn the fundamentals of digital sculpting in Blender."));
        list.add(new TutorialModel("⚙️", "Geometry Nodes",
                "Intermediate", "45 min",
                "Procedural modeling using Blender's node system."));
        list.add(new TutorialModel("🎨", "PBR Materials",
                "Advanced", "1 hr",
                "Create realistic physically based render materials."));
        list.add(new TutorialModel("💡", "Lighting Setups",
                "Beginner", "18 min",
                "Three-point lighting and HDRI environment setups."));
        return list;
    }
}