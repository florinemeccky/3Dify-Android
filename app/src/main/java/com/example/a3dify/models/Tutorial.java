package com.example.a3dify.models;

/*
 * Tutorial
 * Data model for one tutorial.
 * tutorialId is a stable unique string used for SQLite progress tracking,
 * SharedPreferences notes, and Continue Learning last-viewed tracking.
 */
public class Tutorial {

    private String icon;
    private String title;
    private String category;
    private String difficulty;
    private String duration;
    private String description;
    private String tutorialId;

    // Full constructor including tutorialId
    public Tutorial(String icon, String title, String category,
                    String difficulty, String duration,
                    String description, String tutorialId) {
        this.icon       = icon;
        this.title      = title;
        this.category   = category;
        this.difficulty = difficulty;
        this.duration   = duration;
        this.description = description;
        this.tutorialId = tutorialId;
    }

    // Getters
    public String getIcon()        { return icon; }
    public String getTitle()       { return title; }
    public String getCategory()    { return category; }
    public String getDifficulty()  { return difficulty; }
    public String getDuration()    { return duration; }
    public String getDescription() { return description; }
    public String getTutorialId()  { return tutorialId; }

    /*
     * Returns the drawable resource ID for this tutorial's
     * colored gradient thumbnail background.
     * Called by TutorialAdapter and TutorialDetailActivity.
     */
    public int getThumbnailBackground() {
        if (category == null) return com.example.a3dify.R.drawable.bg_thumbnail_basics;
        switch (category) {
            case "3D Modeling":          return com.example.a3dify.R.drawable.bg_thumbnail_modeling;
            case "Animation":            return com.example.a3dify.R.drawable.bg_thumbnail_animation;
            case "Sculpting":            return com.example.a3dify.R.drawable.bg_thumbnail_sculpting;
            case "Rendering":            return com.example.a3dify.R.drawable.bg_thumbnail_rendering;
            case "Geometry Nodes":       return com.example.a3dify.R.drawable.bg_thumbnail_nodes;
            case "Materials & Textures": return com.example.a3dify.R.drawable.bg_thumbnail_materials;
            default:                     return com.example.a3dify.R.drawable.bg_thumbnail_basics;
        }
    }

    /*
     * Returns the vector icon drawable ID for this tutorial's category.
     */
    public int getCategoryIcon() {
        if (category == null) return com.example.a3dify.R.drawable.ic_school;
        switch (category) {
            case "3D Modeling":          return com.example.a3dify.R.drawable.ic_cube;
            case "Animation":            return com.example.a3dify.R.drawable.ic_movie;
            case "Sculpting":            return com.example.a3dify.R.drawable.ic_brush;
            case "Rendering":            return com.example.a3dify.R.drawable.ic_image;
            case "Geometry Nodes":       return com.example.a3dify.R.drawable.ic_device_hub;
            case "Materials & Textures": return com.example.a3dify.R.drawable.ic_palette;
            default:                     return com.example.a3dify.R.drawable.ic_school;
        }
    }

    /*
     * Returns the difficulty badge background drawable.
     */
    public int getDifficultyBadgeBackground() {
        if (difficulty == null) return com.example.a3dify.R.drawable.bg_badge_beginner;
        switch (difficulty) {
            case "Intermediate": return com.example.a3dify.R.drawable.bg_badge_intermediate;
            case "Advanced":     return com.example.a3dify.R.drawable.bg_badge_advanced;
            default:             return com.example.a3dify.R.drawable.bg_badge_beginner;
        }
    }

    /*
     * Returns the text color for the difficulty badge.
     */
    public int getDifficultyColor() {
        if (difficulty == null) return android.graphics.Color.parseColor("#34D399");
        switch (difficulty) {
            case "Intermediate": return android.graphics.Color.parseColor("#FBBF24");
            case "Advanced":     return android.graphics.Color.parseColor("#F87171");
            default:             return android.graphics.Color.parseColor("#34D399");
        }
    }
}
