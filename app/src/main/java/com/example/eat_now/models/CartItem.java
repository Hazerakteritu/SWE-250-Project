package com.example.eat_now.models;

public class CartItem {
    private String id;
    private FoodItem foodItem;
    private int quantity;
    private String specialInstructions;
    private double totalPrice;

    public CartItem() {
    }

    public CartItem(String id, FoodItem foodItem, int quantity, String specialInstructions) {
        this.id = id;
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.totalPrice = foodItem.getPrice() * quantity;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
        calculateTotalPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    private void calculateTotalPrice() {
        if (foodItem != null) {
            this.totalPrice = foodItem.getPrice() * quantity;
        }
    }
}