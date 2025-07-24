package com.example.eat_now.utils.providers;
import com.example.eat_now.models.FoodItem;
import java.util.List;

public interface FoodItemProvider {
    List<FoodItem> getFoodItems(String restaurantId);
}