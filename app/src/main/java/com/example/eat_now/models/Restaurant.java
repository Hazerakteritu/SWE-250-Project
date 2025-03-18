package com.example.eat_now.models;

import java.util.List;

public class Restaurant {
    private String id;
    private String name;
    private String imageUrl;
    private String address;
    private String description;
    private double rating;
    private int deliveryTimeMinutes;
    private double deliveryFee;
    private double latitude;
    private double longitude;
    private List<String> categories;
    private boolean isOpen;

    // Default constructor (required for Firebase)
    public Restaurant() {
    }

    // Constructor to create a restaurant object
    public Restaurant(String id, String name, String imageUrl, String address, String description,
                      double rating, int deliveryTimeMinutes, double deliveryFee,
                      double latitude, double longitude, List<String> categories, boolean isOpen) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.description = description;
        this.rating = rating;
        this.deliveryTimeMinutes = deliveryTimeMinutes;
        this.deliveryFee = deliveryFee;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categories = categories;
        this.isOpen = isOpen;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getDeliveryTimeMinutes() {
        return deliveryTimeMinutes;
    }

    public void setDeliveryTimeMinutes(int deliveryTimeMinutes) {
        this.deliveryTimeMinutes = deliveryTimeMinutes;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}