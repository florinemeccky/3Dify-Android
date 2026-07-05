package com.example.a3dify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.a3dify.R;

/*
 * ProgressFragment
 * Shows the user's learning progress:
 * completed tutorial count, day streak, skill bars, achievements.
 * Full content and animations will be added in Phase 4.
 */
public class ProgressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }
}