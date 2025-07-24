package com.example.eat_now.utils;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.eat_now.models.FoodItem;
import com.example.eat_now.models.Restaurant;

import java.util.List;

public class SampleDataManager {
    public static void addSampleData(Context context) {
        FirestoreCleaner.clearExistingData(context, () -> {
            try {
                List<Restaurant> restaurants = SampleDataFactory.createSampleRestaurants();

                for (Restaurant restaurant : restaurants) {
                    FirestoreUtil.getRestaurantDocument(restaurant.getId())
                            .set(restaurant)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("SampleDataManager", "Restaurant added: " + restaurant.getName());
                                List<FoodItem> foodItems = SampleDataFactory.createSampleFoodItems(restaurant.getId());
                                for (FoodItem foodItem : foodItems) {
                                    FirestoreUtil.getFoodItemsCollection().document(foodItem.getId())
                                            .set(foodItem)
                                            .addOnSuccessListener(v ->
                                                    Log.d("SampleDataSeeder", "Food item added: " + foodItem.getName()))
                                            .addOnFailureListener(e ->
                                                    Log.e("SampleDataSeeder", "Error adding food item: " + e.getMessage()));
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("SampleDataSeeder", "Error adding restaurant: " + e.getMessage());
                                showToast(context, "Error adding restaurant: " + e.getMessage());
                            });
                }

                showToast(context, "Adding 6 Sylhet restaurants...");

            } catch (Exception e) {
                Log.e("SampleDataSeeder", "Error: " + e.getMessage(), e);
                showToast(context, "Error seeding sample data: " + e.getMessage());
            }
        });
    }

    private static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
