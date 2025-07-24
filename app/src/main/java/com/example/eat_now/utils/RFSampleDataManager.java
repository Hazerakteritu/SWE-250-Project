package com.example.eat_now.utils;

import static com.example.eat_now.utils.FirestoreCleaner.clearExistingData;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.eat_now.models.Restaurant;

import java.util.List;


//Refactored SampleDataManager
public class RFSampleDataManager {
    private static final String TAG = "RestaurantDataManager";

    public static void addSampleData(Context context) {
        showToast(context, "Adding 6 Sylhet restaurants...");
        FirestoreCleaner.clearExistingData(context, () -> {
            addRF(context);
        });
    }


    //add Restaurants With Food Items
    private static void addRF(Context context) {
        List<Restaurant> restaurants = SampleDataFactory.createSampleRestaurants();

        for (Restaurant restaurant : restaurants) {
            addSR(restaurant, context);
        }

    }

    //add Single Restaurant
    private static void addSR(Restaurant restaurant, Context context) {
        FirestoreUtil.getRestaurantDocument(restaurant.getId())
                .set(restaurant)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Restaurant added: " + restaurant.getName());
                    SampleDataFactory.createSampleFoodItems(restaurant.getId());
                })
                .addOnFailureListener(e -> Error(e, context));
    }

    //handle Restaurant Error
    private static void Error(Exception e, Context context) {
        Log.e(TAG, "Error adding restaurant: " + e.getMessage());
        showToast(context, "Error: " + e.getMessage());
    }

    private static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
