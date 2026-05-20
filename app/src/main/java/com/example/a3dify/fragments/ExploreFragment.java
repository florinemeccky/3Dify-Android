package com.example.a3dify.fragments;

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
import com.example.a3dify.adapters.CategoryAdapter;
import com.example.a3dify.models.CategoryModel;
import java.util.ArrayList;
import java.util.List;

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
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        // true = vertical row mode (uses item_category_row.xml)
        rv.setAdapter(new CategoryAdapter(getAllCategories(), true));
    }

    private List<CategoryModel> getAllCategories() {
        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("🔷", "Beginner Basics",     "24", "#4A90E2", "Beginner"));
        list.add(new CategoryModel("🧊", "3D Modeling",         "38", "#FF6A00", "All levels"));
        list.add(new CategoryModel("🎬", "Animation",           "19", "#7B5EA7", "Intermediate"));
        list.add(new CategoryModel("🌊", "Sculpting",           "15", "#2ECC71", "Intermediate"));
        list.add(new CategoryModel("💡", "Rendering",           "22", "#F4D03F", "Advanced"));
        list.add(new CategoryModel("⚙️", "Geometry Nodes",      "12", "#E74C3C", "Advanced"));
        list.add(new CategoryModel("🎨", "Materials & Textures","29", "#9B59B6", "All levels"));
        return list;
    }
}