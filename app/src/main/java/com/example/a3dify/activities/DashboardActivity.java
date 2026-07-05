package com.example.a3dify.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.a3dify.R;
import com.example.a3dify.fragments.HomeFragment;
import com.example.a3dify.fragments.ExploreFragment;
import com.example.a3dify.fragments.ProgressFragment;
import com.example.a3dify.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
 * DashboardActivity
 * The main container screen after login.
 * Contains a BottomNavigationView with 4 tabs.
 * Each tab swaps a different Fragment into the container.
 *
 * Tabs:
 *   Home    → HomeFragment    (tutorials, continue learning, categories)
 *   Explore → ExploreFragment (browse all categories)
 *   Progress→ ProgressFragment(skill bars, achievements, streak)
 *   Profile → ProfileFragment (account info, settings, logout)
 */
public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        // Load HomeFragment when the Dashboard first opens
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        // Switch fragments when the user taps a bottom nav tab
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_explore) {
                fragment = new ExploreFragment();
            } else if (id == R.id.nav_progress) {
                fragment = new ProgressFragment();
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else {
                fragment = new HomeFragment();
            }

            loadFragment(fragment);
            return true;
        });
    }

    // Replaces the current fragment inside the container
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}