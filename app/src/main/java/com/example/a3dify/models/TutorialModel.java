package com.example.a3dify.models;

public class TutorialModel {
    public String icon, title, level, duration, description;

    public TutorialModel(String icon, String title,
                         String level, String duration, String description) {
        this.icon        = icon;
        this.title       = title;
        this.level       = level;
        this.duration    = duration;
        this.description = description;
    }
}