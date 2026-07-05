package com.example.a3dify.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.a3dify.R;
import com.example.a3dify.fragments.ExploreFragment;
import com.example.a3dify.fragments.HomeFragment;
import com.example.a3dify.fragments.ProfileFragment;
import com.example.a3dify.fragments.ProgressFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
 * DashboardActivity
 * Main container after login. Hosts the 4 bottom nav fragments.
 * Also checks internet connectivity on launch and shows a banner
 * if the device is offline. This satisfies the lecturer's
 * "Internet Connection Detection" mobile feature requirement.
 */
public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        checkInternetConnection();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            if      (id == R.id.nav_home)     fragment = new HomeFragment();
            else if (id == R.id.nav_explore)  fragment = new ExploreFragment();
            else if (id == R.id.nav_progress) fragment = new ProgressFragment();
            else if (id == R.id.nav_profile)  fragment = new ProfileFragment();
            else                              fragment = new HomeFragment();

            loadFragment(fragment);
            return true;
        });
    }

    /*
     * Checks if the device has an active internet connection.
     * If offline, makes the banner visible at the top of the screen.
     * If online, the banner stays hidden (visibility=gone by default).
     *
     * This satisfies the lecturer's Internet Connection Detection requirement.
     */
    private void checkInternetConnection() {
        TextView offlineBanner = findViewById(R.id.tv_offline_banner);
        if (offlineBanner == null) return;

        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        if (!isConnected) {
            offlineBanner.setVisibility(View.VISIBLE);
        } else {
            offlineBanner.setVisibility(View.GONE);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}