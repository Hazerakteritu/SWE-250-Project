package com.example.eat_now.utils;

import com.example.eat_now.models.FoodItem;
import com.example.eat_now.models.Restaurant;
import com.example.eat_now.utils.providers.FoodItemProvider;
import com.example.eat_now.utils.providers.KacchiBhaiFood;
import com.example.eat_now.utils.providers.PachBhaiFoodSupplier;
import com.example.eat_now.utils.providers.PanshiFoodSupplier;
import com.example.eat_now.utils.providers.TastyTreatFoodSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RFSampleDataFactory {

    private static final Map<String, FoodItemProvider> foodItemSuppliers = new HashMap<>();

    static {
        foodItemSuppliers.put("restaurant1", new KacchiBhaiFood());
        foodItemSuppliers.put("restaurant2", new TastyTreatFoodSupplier());
        foodItemSuppliers.put("restaurant3", new PachBhaiFoodSupplier());
        foodItemSuppliers.put("restaurant4", new PanshiFoodSupplier());
    }

    public static List<Restaurant> createSampleRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("restaurant1", "Kacchi Bhai Ambarkhana", "", "Ambarkhana, Sylhet",
                "Famous for authentic Sylheti kacchi biryani and traditional dishes", 4.8, 35, 45.0,
                24.8949, 91.8687, new ArrayList<>(Arrays.asList("Bangladeshi", "Biryani", "Traditional")), true));

        restaurants.add(new Restaurant("restaurant2", "Tasty Treat Sylhet", "", "Zindabazar, Sylhet",
                "Popular fast food chain with delicious snacks and meals", 4.6, 25, 35.0,
                24.9032, 91.8603, new ArrayList<>(Arrays.asList("Fast Food", "Snacks", "Burger")), true));

        restaurants.add(new Restaurant("restaurant3", "5 Bhai Restaurant Sylhet", "", "Bondor Bazar, Sylhet",
                "Traditional Bangladeshi cuisine with family recipes", 4.7, 40, 40.0,
                24.9176, 91.8328, new ArrayList<>(Arrays.asList("Bangladeshi", "Traditional", "Curry")), true));

        restaurants.add(new Restaurant("restaurant4", "Panshi Sylhet", "", "Uposhohor, Sylhet",
                "Authentic Sylheti cuisine with fresh ingredients", 4.9, 30, 50.0,
                24.9012, 91.8701, new ArrayList<>(Arrays.asList("Bangladeshi", "Sylheti", "Traditional")), true));

        restaurants.add(new Restaurant("restaurant5", "Pizza Hub Sylhet", "", "Dargah Gate, Sylhet",
                "Best pizza place in Sylhet with Italian and fusion flavors", 4.5, 30, 55.0,
                24.8998, 91.8712, new ArrayList<>(Arrays.asList("Italian", "Pizza", "Fast Food")), true));

        restaurants.add(new Restaurant("restaurant6", "Bonoful Sweets Sylhet", "", "Chowhatta, Sylhet",
                "Traditional Bengali sweets and desserts since 1985", 4.8, 20, 25.0,
                24.9045, 91.8675, new ArrayList<>(Arrays.asList("Dessert", "Sweets", "Traditional")), true));

        return restaurants;
    }


    public static List<FoodItem> createSampleFoodItems(String restaurantId) {
        FoodItemProvider supplier = foodItemSuppliers.get(restaurantId);
        return supplier.getFoodItems(restaurantId);
    }
}
