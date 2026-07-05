package com.example.a3dify.models;

/*
 * Tutorial
 * A simple data model representing one tutorial in the app.
 * Used by TutorialAdapter to populate RecyclerView cards.
 *
 * Fields:
 *   icon        — emoji shown on the thumbnail e.g. "🧊"
 *   title       — the tutorial name
 *   category    — which category it belongs to e.g. "Modeling"
 *   difficulty  — "Beginner", "Intermediate", or "Advanced"
 *   duration    — how long it takes e.g. "22 min"
 *   description — longer text shown on the detail screen
 */
public class Tutorial {

    private String icon;
    private String title;
    private String category;
    private String difficulty;
    private String duration;
    private String description;

    // Constructor — used when creating tutorial objects in fragments
    public Tutorial(String icon, String title, String category,
                    String difficulty, String duration, String description) {
        this.icon        = icon;
        this.title       = title;
        this.category    = category;
        this.difficulty  = difficulty;
        this.duration    = duration;
        this.description = description;
    }

    // Getters — used by the adapter to read values
    public String getIcon()        { return icon; }
    public String getTitle()       { return title; }
    public String getCategory()    { return category; }
    public String getDifficulty()  { return difficulty; }
    public String getDuration()    { return duration; }
    public String getDescription() { return description; }
}