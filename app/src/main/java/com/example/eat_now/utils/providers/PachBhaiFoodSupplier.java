package com.example.eat_now.utils.providers;

import com.example.eat_now.models.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class PachBhaiFoodSupplier implements FoodItemProvider{
    @Override
    public List<FoodItem> getFoodItems(String restaurantId) {
        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("food1_" + restaurantId, "Hilsa Fish Curry", "Fresh hilsa fish in traditional curry", "", 14.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food2_" + restaurantId, "Mutton Curry", "Tender mutton in spicy gravy", "", 13.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food3_" + restaurantId, "Dal Gosht", "Lentils cooked with meat", "", 11.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food4_" + restaurantId, "Plain Rice", "Steamed basmati rice", "", 2.99, restaurantId, "Sides"));
        foodItems.add(new FoodItem("food5_" + restaurantId, "Mixed Vegetables", "Seasonal vegetables curry", "", 6.99, restaurantId, "Vegetarian"));
        return foodItems;
    }
}