package com.example.a3dify.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.TutorialRepository;
import com.example.a3dify.activities.AllTutorialsActivity;
import com.example.a3dify.adapters.CategoryAdapter;
import com.example.a3dify.models.Category;
import java.util.ArrayList;
import java.util.List;

/*
 * ExploreFragment
 * Shows all 7 learning categories as full-width rows.
 * Tapping any row opens AllTutorialsActivity filtered to that category.
 * The tutorial count shown in each row is the real count from TutorialRepository.
 */
public class ExploreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rv_categories_explore);
        if (rv == null) return;

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        TutorialRepository repo = TutorialRepository.getInstance();

        // Build category list with real tutorial counts
        List<Category> categories = new ArrayList<>();
        String[][] catData = {
            {"🔷", "Beginner Basics",      "#4A90E2", "Beginner"},
            {"🧊", "3D Modeling",          "#FF6A00", "All levels"},
            {"🎬", "Animation",            "#7B5EA7", "Intermediate"},
            {"🌊", "Sculpting",            "#2ECC71", "Intermediate"},
            {"💡", "Rendering",            "#F4D03F", "Advanced"},
            {"⚙️", "Geometry Nodes",       "#E74C3C", "Advanced"},
            {"🎨", "Materials & Textures", "#9B59B6", "All levels"},
        };

        for (String[] cat : catData) {
            int count = repo.getByCategory(cat[1]).size();
            categories.add(new Category(cat[0], cat[1], count, cat[2], cat[3]));
        }

        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories, true);
        adapter.setOnItemClickListener(cat -> {
            // Open AllTutorialsActivity filtered to this category
            Intent intent = new Intent(requireActivity(), AllTutorialsActivity.class);
            intent.putExtra("category", cat.getName());
            startActivity(intent);
        });
        rv.setAdapter(adapter);
    }
}