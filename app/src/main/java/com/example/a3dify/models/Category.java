package com.example.a3dify.models;

/*
 * Category
 * Represents one learning category shown in the Explore tab
 * and the horizontal category pills on the Home screen.
 *
 * Fields:
 *   icon       — emoji for the category e.g. "🧊"
 *   name       — display name e.g. "3D Modeling"
 *   count      — number of tutorials in this category
 *   colorHex   — accent color for this category e.g. "#FF6A00"
 *   difficulty — overall level label e.g. "All levels"
 */
public class Category {

    private String icon;
    private String name;
    private int    count;
    private String colorHex;
    private String difficulty;

    public Category(String icon, String name, int count,
                    String colorHex, String difficulty) {
        this.icon       = icon;
        this.name       = name;
        this.count      = count;
        this.colorHex   = colorHex;
        this.difficulty = difficulty;
    }

    public String getIcon()       { return icon; }
    public String getName()       { return name; }
    public int    getCount()      { return count; }
    public String getColorHex()   { return colorHex; }
    public String getDifficulty() { return difficulty; }
}