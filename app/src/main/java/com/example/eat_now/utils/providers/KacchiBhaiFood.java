package com.example.eat_now.utils.providers;

import com.example.eat_now.models.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class KacchiBhaiFood implements FoodItemProvider {
    @Override
    public List<FoodItem> getFoodItems(String restaurantId) {
        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("food1_" + restaurantId, "Kacchi Biryani", "Authentic Sylheti kacchi biryani with tender mutton", "", 15.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food2_" + restaurantId, "Beef Rezala", "Traditional beef curry with rich gravy", "", 12.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food3_" + restaurantId, "Chicken Roast", "Spicy roasted chicken Sylheti style", "", 10.99, restaurantId, "Main Course"));
        foodItems.add(new FoodItem("food4_" + restaurantId, "Borhani", "Traditional yogurt drink with spices", "", 3.99, restaurantId, "Beverage"));
        foodItems.add(new FoodItem("food5_" + restaurantId, "Firni", "Creamy rice pudding dessert", "", 4.99, restaurantId, "Dessert"));
        return foodItems;
    }
}