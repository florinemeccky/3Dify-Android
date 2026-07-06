package com.example.a3dify;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

/*
 * ThreeDifyApp
 * Custom Application class — runs before any Activity starts.
 * Applies the saved dark/light mode preference immediately on launch
 * so the correct theme is applied from the very first frame.
 *
 * Without this class, the theme would only apply after the user
 * visits SettingsActivity and the activity recreates itself.
 *
 * Registered in AndroidManifest.xml via android:name=".ThreeDifyApp"
 */
public class ThreeDifyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        applyThemeFromPreferences();
    }

    /*
     * Reads the dark_mode setting.
     * Uses SharedPreferences here (not SQLite) because the Application
     * class runs before we know the user's UID for SQLite lookups.
     * SettingsActivity mirrors the value to both SQLite and SharedPreferences.
     */
    private void applyThemeFromPreferences() {
        SharedPreferences prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", true); // default: dark

        AppCompatDelegate.setDefaultNightMode(
                isDark
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}