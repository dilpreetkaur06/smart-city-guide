package com.guide.smartcity;

/**
 * Data model for city locations used in the search logic
 */
public class Place {
    private String title;
    private String category;
    private String rating;
    private String imageUrl;

    public Place(String title, String category, String rating, String imageUrl) {
        this.title = title;
        this.category = category;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // Getters used by MainActivity search logic
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
}