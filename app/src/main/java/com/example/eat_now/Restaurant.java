package com.example.eat_now;

public class Restaurant {
    private String name;
    private String imageUrl;
    private String address;
    private String description;

    // Default constructor (required for Firebase)
    public Restaurant() {
    }

    // Constructor to create a restaurant object
    public Restaurant(String name, String imageUrl, String address, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.description = description;
    }

    // Getters (needed for Firebase and RecyclerView Adapter)
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }
}
