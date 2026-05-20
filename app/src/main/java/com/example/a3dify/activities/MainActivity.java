package com.example.a3dify.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.a3dify.R;
import com.example.a3dify.fragments.ExploreFragment;
import com.example.a3dify.fragments.HomeFragment;
import com.example.a3dify.fragments.ProfileFragment;
import com.example.a3dify.fragments.ProgressFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        // Load Home fragment on first launch
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selected = new HomeFragment();
            } else if (id == R.id.nav_explore) {
                selected = new ExploreFragment();
            } else if (id == R.id.nav_progress) {
                selected = new ProgressFragment();
            } else if (id == R.id.nav_profile) {
                selected = new ProfileFragment();
            } else {
                selected = new HomeFragment();
            }

            loadFragment(selected);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}