package com.example.a3dify.models;

/*
 * SoftwareTool
 * Data model for one 3D software entry shown in the
 * "3D Tools & Software" section on the Home screen.
 * All content is static — no network call required.
 */
public class SoftwareTool {

    private final String name;
    private final String tagline;
    private final String pricing;       // "Free" or "Paid" or "Free + Paid"
    private final String website;
    private final String description;
    private final String minSpecs;
    private final String recommendedSpecs;
    private final String beginnerRating; // "Excellent", "Good", "Moderate", "Difficult"
    private final String bestFor;
    private final String shortcuts;
    private final String tips;
    private final int    iconColor;     // color int for the avatar circle

    public SoftwareTool(String name, String tagline, String pricing,
                        String website, String description,
                        String minSpecs, String recommendedSpecs,
                        String beginnerRating, String bestFor,
                        String shortcuts, String tips, int iconColor) {
        this.name             = name;
        this.tagline          = tagline;
        this.pricing          = pricing;
        this.website          = website;
        this.description      = description;
        this.minSpecs         = minSpecs;
        this.recommendedSpecs = recommendedSpecs;
        this.beginnerRating   = beginnerRating;
        this.bestFor          = bestFor;
        this.shortcuts        = shortcuts;
        this.tips             = tips;
        this.iconColor        = iconColor;
    }

    public String getName()             { return name; }
    public String getTagline()          { return tagline; }
    public String getPricing()          { return pricing; }
    public String getWebsite()          { return website; }
    public String getDescription()      { return description; }
    public String getMinSpecs()         { return minSpecs; }
    public String getRecommendedSpecs() { return recommendedSpecs; }
    public String getBeginnerRating()   { return beginnerRating; }
    public String getBestFor()          { return bestFor; }
    public String getShortcuts()        { return shortcuts; }
    public String getTips()             { return tips; }
    public int    getIconColor()        { return iconColor; }
}