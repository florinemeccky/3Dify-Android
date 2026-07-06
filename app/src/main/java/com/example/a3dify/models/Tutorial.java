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
}
