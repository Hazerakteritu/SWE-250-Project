package com.example.eat_now.utils.providers;

import com.example.eat_now.models.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class TastyTreatFoodSupplier implements FoodItemProvider{
    @Override
    public List<FoodItem> getFoodItems(String restaurantId) {
        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("food1_" + restaurantId, "Chicken Burger", "Crispy chicken burger with special sauce", "", 8.99, restaurantId, "Burger"));
        foodItems.add(new FoodItem("food2_" + restaurantId, "Beef Burger", "Juicy beef patty with fresh vegetables", "", 9.99, restaurantId, "Burger"));
        foodItems.add(new FoodItem("food3_" + restaurantId, "Chicken Wings", "Spicy buffalo chicken wings", "", 7.99, restaurantId, "Snacks"));
        foodItems.add(new FoodItem("food4_" + restaurantId, "French Fries", "Golden crispy potato fries", "", 3.99, restaurantId, "Sides"));
        foodItems.add(new FoodItem("food5_" + restaurantId, "Chocolate Shake", "Rich chocolate milkshake", "", 4.99, restaurantId, "Beverage"));
        return foodItems;
    }
}