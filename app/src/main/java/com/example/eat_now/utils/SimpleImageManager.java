package com.example.eat_now.utils;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * SUPER SIMPLE - Fixed images for restaurants and food
 * No complex upload needed!
 */
public class SimpleImageManager {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * STEP 1: Set fixed images for all restaurants
     * Just call this once and all restaurants get images!
     */
    public static void setAllRestaurantImages(Context context) {
        Toast.makeText(context, "Setting restaurant images...", Toast.LENGTH_SHORT).show();

        // Fixed image URLs for each restaurant
        Map<String, String> restaurantImages = new HashMap<>();
        restaurantImages.put("restaurant1", "https://images.unsplash.com/photo-1563379091339-03246963d51a?w=400"); // Kacchi Bhai - Biryani
        restaurantImages.put("restaurant2", "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=400"); // Tasty Treat - Burger
        restaurantImages.put("restaurant3", "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=400"); // 5 Bhai - Fish Curry
        restaurantImages.put("restaurant4", "https://images.unsplash.com/photo-1574484284002-952d92456975?w=400"); // Panshi - Traditional
        restaurantImages.put("restaurant5", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400"); // Pizza Hub - Pizza
        restaurantImages.put("restaurant6", "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400"); // Bonoful - Sweets

        // Update each restaurant with its image
        for (Map.Entry<String, String> entry : restaurantImages.entrySet()) {
            String restaurantId = entry.getKey();
            String imageUrl = entry.getValue();

            db.collection("restaurants").document(restaurantId)
                    .update("imageUrl", imageUrl)
                    .addOnSuccessListener(aVoid -> {
                        // Success - no need to show message for each
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "❌ Failed to update " + restaurantId, Toast.LENGTH_SHORT).show();
                    });
        }

        Toast.makeText(context, "✅ All restaurant images set!", Toast.LENGTH_LONG).show();
    }

    /**
     * STEP 2: Set fixed images for all food items
     * Just call this once and all food gets images!
     */
    public static void setAllFoodImages(Context context) {
        Toast.makeText(context, "Setting food images...", Toast.LENGTH_SHORT).show();

        // Fixed image URLs for each food item
        Map<String, String> foodImages = new HashMap<>();

        // Restaurant 1 - Kacchi Bhai foods
        foodImages.put("food1_restaurant1", "https://images.unsplash.com/photo-1563379091339-03246963d51a?w=300"); // Kacchi Biryani
        foodImages.put("food2_restaurant1", "https://images.unsplash.com/photo-1574484284002-952d92456975?w=300"); // Beef Rezala
        foodImages.put("food3_restaurant1", "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=300"); // Chicken Roast
        foodImages.put("food4_restaurant1", "https://images.unsplash.com/photo-1544145945-f90425340c7e?w=300"); // Borhani
        foodImages.put("food5_restaurant1", "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300"); // Firni

        // Restaurant 2 - Tasty Treat foods
        foodImages.put("food1_restaurant2", "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=300"); // Chicken Burger
        foodImages.put("food2_restaurant2", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300"); // Beef Burger
        foodImages.put("food3_restaurant2", "https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=300"); // Chicken Wings
        foodImages.put("food4_restaurant2", "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=300"); // French Fries
        foodImages.put("food5_restaurant2", "https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=300"); // Chocolate Shake

        // Restaurant 3 - 5 Bhai foods
        foodImages.put("food1_restaurant3", "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=300"); // Hilsa Fish Curry
        foodImages.put("food2_restaurant3", "https://images.unsplash.com/photo-1574484284002-952d92456975?w=300"); // Mutton Curry
        foodImages.put("food3_restaurant3", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=300"); // Dal Gosht
        foodImages.put("food4_restaurant3", "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=300"); // Plain Rice
        foodImages.put("food5_restaurant3", "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=300"); // Mixed Vegetables

        // Restaurant 4 - Panshi foods
        foodImages.put("food1_restaurant4", "https://images.unsplash.com/photo-1574484284002-952d92456975?w=300"); // Shatkora Beef
        foodImages.put("food2_restaurant4", "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=300"); // Shutki Bhorta
        foodImages.put("food3_restaurant4", "https://images.unsplash.com/photo-1574484284002-952d92456975?w=300"); // Duck Curry
        foodImages.put("food4_restaurant4", "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=300"); // Panta Bhat
        foodImages.put("food5_restaurant4", "https://images.unsplash.com/photo-1544145945-f90425340c7e?w=300"); // Seven Color Tea

        // Restaurant 5 - Pizza Hub foods
        foodImages.put("food1_restaurant5", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=300"); // Margherita Pizza
        foodImages.put("food2_restaurant5", "https://images.unsplash.com/photo-1565299507177-b0ac66763828?w=300"); // Chicken Supreme
        foodImages.put("food3_restaurant5", "https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f?w=300"); // Beef Pepperoni
        foodImages.put("food4_restaurant5", "https://images.unsplash.com/photo-1541745537411-b8046dc6d66c?w=300"); // Garlic Bread
        foodImages.put("food5_restaurant5", "https://images.unsplash.com/photo-1621996346565-e3dbc353d2e5?w=300"); // Pasta Alfredo

        // Restaurant 6 - Bonoful Sweets foods
        foodImages.put("food1_restaurant6", "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300"); // Rasgulla
        foodImages.put("food2_restaurant6", "https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=300"); // Sandesh
        foodImages.put("food3_restaurant6", "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=300"); // Mishti Doi
        foodImages.put("food4_restaurant6", "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300"); // Chomchom
        foodImages.put("food5_restaurant6", "https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=300"); // Kalojam

        // Update each food item with its image
        for (Map.Entry<String, String> entry : foodImages.entrySet()) {
            String foodId = entry.getKey();
            String imageUrl = entry.getValue();

            db.collection("food_items").document(foodId)
                    .update("imageUrl", imageUrl)
                    .addOnSuccessListener(aVoid -> {
                        // Success - no need to show message for each
                    })
                    .addOnFailureListener(e -> {
                        // Ignore individual failures
                    });
        }

        Toast.makeText(context, "✅ All food images set!", Toast.LENGTH_LONG).show();
    }

    /**
     * STEP 3: Set ALL images at once (restaurants + food)
     * This is the easiest method - just call this!
     */
    public static void setAllImages(Context context) {
        Toast.makeText(context, "Setting all images...", Toast.LENGTH_SHORT).show();

        // Set restaurant images first
        setAllRestaurantImages(context);

        // Wait 2 seconds, then set food images
        new android.os.Handler().postDelayed(() -> {
            setAllFoodImages(context);
        }, 2000);
    }
}
