package com.example.lab_6;

public class Artwork {
    private String title;
    private String imageBase64;

    private String description;

    public Artwork(String title, String imageBase64,String description) {
        this.title = title;
        this.imageBase64 = imageBase64;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }

    public String getImageBase64() {
        return imageBase64;
    }
}