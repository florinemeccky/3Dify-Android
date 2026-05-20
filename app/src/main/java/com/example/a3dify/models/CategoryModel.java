package com.example.a3dify.models;

public class CategoryModel {
    public String icon, name, count, colorHex, level;

    public CategoryModel(String icon, String name,
                         String count, String colorHex, String level) {
        this.icon     = icon;
        this.name     = name;
        this.count    = count;
        this.colorHex = colorHex;
        this.level    = level;
    }
}