package com.example.a3dify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.adapters.CategoryAdapter;
import com.example.a3dify.models.Category;
import java.util.ArrayList;
import java.util.List;

/*
 * ExploreFragment
 * Shows all 7 learning categories in a vertical list.
 * Tapping a category will show its tutorials (Phase 5).
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rv_categories_explore);
        if (rv == null) return;

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        CategoryAdapter adapter = new CategoryAdapter(getContext(), getAllCategories(), true);
        adapter.setOnItemClickListener(category ->
                Toast.makeText(getContext(),
                        category.getName() + " — coming in Phase 5",
                        Toast.LENGTH_SHORT).show()
        );
        rv.setAdapter(adapter);
    }

    private List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("🔷", "Beginner Basics",      24, "#4A90E2", "Beginner"));
        list.add(new Category("🧊", "3D Modeling",          38, "#FF6A00", "All levels"));
        list.add(new Category("🎬", "Animation",            19, "#7B5EA7", "Intermediate"));
        list.add(new Category("🌊", "Sculpting",            15, "#2ECC71", "Intermediate"));
        list.add(new Category("💡", "Rendering",            22, "#F4D03F", "Advanced"));
        list.add(new Category("⚙️", "Geometry Nodes",       12, "#E74C3C", "Advanced"));
        list.add(new Category("🎨", "Materials & Textures", 29, "#9B59B6", "All levels"));
        return list;
    }
}