package com.example.eat_now.utils.providers;

import com.example.eat_now.models.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class PanshiFoodSupplier implements FoodItemProvider {
    @Override
    public List<FoodItem> getFoodItems(String restaurantId) {
        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("food1_" + restaurantId, "Shatkora Beef", "Beef curry with citrus shatkora", "", 16.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food2_" + restaurantId, "Shutki Bhorta", "Dried fish mash Sylheti style", "", 8.99, restaurantId, "Traditional"));
        foodItems.add(new FoodItem("food3_" + restaurantId, "Duck Curry", "Traditional duck curry", "", 15.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food4_" + restaurantId, "Panta Bhat", "Fermented rice with accompaniments", "", 7.99, restaurantId, "Traditional"));
        foodItems.add(new FoodItem("food5_" + restaurantId, "Seven Color Tea", "Famous Sylheti layered tea", "", 2.99, restaurantId, "Beverage"));
        return foodItems;
    }

}
