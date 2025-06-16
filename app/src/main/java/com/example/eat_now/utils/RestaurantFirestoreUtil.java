package com.example.eat_now.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.eat_now.models.FoodItem;
import com.example.eat_now.models.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantFirestoreUtil {
    private static final String TAG = "RestaurantFirestoreUtil";

    // Get all restaurants - LIMIT TO 6
    public static Task<QuerySnapshot> getAllRestaurants() {
        return FirestoreUtil.getRestaurantsCollection()
                .limit(6)  // Limit to 6 restaurants
                .get();
    }

    // Get popular restaurants - LIMIT TO 6
    public static Task<QuerySnapshot> getPopularRestaurants() {
        return FirestoreUtil.getRestaurantsCollection()
                .whereGreaterThanOrEqualTo("rating", 4.5)
                .limit(6)  // Limit to 6 restaurants
                .get();
    }

    // Get restaurant by ID
    public static Task<DocumentSnapshot> getRestaurant(String restaurantId) {
        if (restaurantId == null || restaurantId.isEmpty()) {
            Log.e(TAG, "getRestaurant: restaurantId is null or empty");
            return null;
        }
        return FirestoreUtil.getRestaurantDocument(restaurantId).get();
    }

    // Get food items by restaurant - LIMIT TO 5
    public static Task<QuerySnapshot> getFoodItemsByRestaurant(String restaurantId) {
        if (restaurantId == null || restaurantId.isEmpty()) {
            Log.e(TAG, "getFoodItemsByRestaurant: restaurantId is null or empty");
            return null;
        }
        return FirestoreUtil.getFoodItemsCollection()
                .whereEqualTo("restaurantId", restaurantId)
                .limit(5)  // Limit to 5 food items per restaurant
                .get();
    }

    // Add sample data - 6 UNIQUE restaurants with 5 food items each
    public static void addSampleData(Context context) {
        // First clear existing data
        clearExistingData(context, () -> {
            try {
                // Add sample restaurants - EXACTLY 6 UNIQUE RESTAURANTS
                List<Restaurant> restaurants = createSampleRestaurants();

                for (Restaurant restaurant : restaurants) {
                    String id = restaurant.getId();

                    // Add restaurant to Firestore
                    FirestoreUtil.getRestaurantDocument(id)
                            .set(restaurant)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "Restaurant added: " + restaurant.getName());

                                // Add food items for this restaurant - 5 PER RESTAURANT
                                List<FoodItem> foodItems = createSampleFoodItems(id);
                                for (FoodItem foodItem : foodItems) {
                                    String foodId = foodItem.getId();
                                    FirestoreUtil.getFoodItemsCollection().document(foodId)
                                            .set(foodItem)
                                            .addOnSuccessListener(aVoid2 ->
                                                    Log.d(TAG, "Food item added: " + foodItem.getName()))
                                            .addOnFailureListener(e ->
                                                    Log.e(TAG, "Error adding food item: " + e.getMessage()));
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error adding restaurant: " + e.getMessage());
                                if (context != null) {
                                    Toast.makeText(context, "Error adding restaurant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (context != null) {
                    Toast.makeText(context, "Adding 6 unique restaurants...", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in addSampleData: " + e.getMessage(), e);
                if (context != null) {
                    Toast.makeText(context, "Error adding sample data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Clear existing data
    private static void clearExistingData(Context context, Runnable onComplete) {
        try {
            FirestoreUtil.getRestaurantsCollection().get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            doc.getReference().delete();
                        }

                        FirestoreUtil.getFoodItemsCollection().get()
                                .addOnSuccessListener(foodSnapshot -> {
                                    for (DocumentSnapshot doc : foodSnapshot.getDocuments()) {
                                        doc.getReference().delete();
                                    }

                                    if (onComplete != null) {
                                        onComplete.run();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error clearing food items: " + e.getMessage());
                                    if (context != null) {
                                        Toast.makeText(context, "Error clearing food items: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    // Still run the completion callback
                                    if (onComplete != null) {
                                        onComplete.run();
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error clearing restaurants: " + e.getMessage());
                        if (context != null) {
                            Toast.makeText(context, "Error clearing restaurants: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Still run the completion callback
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error in clearExistingData: " + e.getMessage(), e);
            if (context != null) {
                Toast.makeText(context, "Error clearing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Still run the completion callback
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }

    // Create sample restaurants - EXACTLY 6 UNIQUE RESTAURANTS
    private static List<Restaurant> createSampleRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        // Restaurant 1: Biryani House
        Restaurant biryaniHouse = new Restaurant(
                "restaurant1",
                "Sylhet Biryani House",
                "", // No image URL
                "Zindabazar, Sylhet",
                "Famous for traditional Sylheti biryani",
                4.8,
                30,
                40.0,
                24.8949,
                91.8687,
                new ArrayList<>(Arrays.asList("Bangladeshi", "Biryani")),
                true
        );
        restaurants.add(biryaniHouse);

        // Restaurant 2: Pizza Planet
        Restaurant pizzaPlanet = new Restaurant(
                "restaurant2",
                "Pizza Planet",
                "", // No image URL
                "Uposhohor, Sylhet",
                "Best pizza in town",
                4.7,
                35,
                50.0,
                24.9032,
                91.8603,
                new ArrayList<>(Arrays.asList("Italian", "Pizza")),
                true
        );
        restaurants.add(pizzaPlanet);

        // Restaurant 3: Campus Burger
        Restaurant campusBurger = new Restaurant(
                "restaurant3",
                "Campus Burger",
                "", // No image URL
                "University Road, Sylhet",
                "Student favorite burger joint",
                4.5,
                25,
                30.0,
                24.9176,
                91.8328,
                new ArrayList<>(Arrays.asList("Fast Food", "Burger")),
                true
        );
        restaurants.add(campusBurger);

        // Restaurant 4: Spice Garden
        Restaurant spiceGarden = new Restaurant(
                "restaurant4",
                "Spice Garden",
                "", // No image URL
                "Amberkhana, Sylhet",
                "Authentic Indian cuisine",
                4.6,
                40,
                45.0,
                24.9012,
                91.8701,
                new ArrayList<>(Arrays.asList("Indian", "Vegetarian")),
                true
        );
        restaurants.add(spiceGarden);

        // Restaurant 5: Sushi Express
        Restaurant sushiExpress = new Restaurant(
                "restaurant5",
                "Sushi Express",
                "", // No image URL
                "Dargah Gate, Sylhet",
                "Fresh Japanese cuisine",
                4.9,
                45,
                60.0,
                24.8998,
                91.8712,
                new ArrayList<>(Arrays.asList("Japanese", "Seafood")),
                true
        );
        restaurants.add(sushiExpress);

        // Restaurant 6: Dessert Paradise (ADDED 6TH RESTAURANT)
        Restaurant dessertParadise = new Restaurant(
                "restaurant6",
                "Dessert Paradise",
                "", // No image URL
                "Bondor Bazar, Sylhet",
                "Sweet treats and desserts",
                4.7,
                20,
                25.0,
                24.9045,
                91.8675,
                new ArrayList<>(Arrays.asList("Dessert", "Bakery")),
                true
        );
        restaurants.add(dessertParadise);

        return restaurants;
    }

    // Create sample food items - 5 FOOD ITEMS PER RESTAURANT
    private static List<FoodItem> createSampleFoodItems(String restaurantId) {
        List<FoodItem> foodItems = new ArrayList<>();

        // Different food items based on restaurant type
        if (restaurantId.equals("restaurant1")) {
            // Biryani House - Bangladeshi food
            foodItems.add(new FoodItem("food1_" + restaurantId, "Kacchi Biryani", "Aromatic rice with tender meat", "", 12.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Chicken Curry", "Spicy chicken curry", "", 9.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Beef Tehari", "Flavorful beef rice dish", "", 11.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Borhani", "Yogurt-based spicy drink", "", 3.99, restaurantId, "Beverage"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Jorda", "Sweet rice dessert", "", 4.99, restaurantId, "Dessert"));
        }
        else if (restaurantId.equals("restaurant2")) {
            // Pizza Planet - Italian food
            foodItems.add(new FoodItem("food1_" + restaurantId, "Margherita Pizza", "Classic cheese and tomato", "", 10.99, restaurantId, "Pizza"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Pepperoni Pizza", "Spicy pepperoni topping", "", 12.99, restaurantId, "Pizza"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Garlic Bread", "Crispy bread with garlic butter", "", 4.99, restaurantId, "Sides"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Pasta Carbonara", "Creamy pasta with bacon", "", 9.99, restaurantId, "Pasta"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Tiramisu", "Coffee-flavored dessert", "", 5.99, restaurantId, "Dessert"));
        }
        else if (restaurantId.equals("restaurant3")) {
            // Campus Burger - Fast food
            foodItems.add(new FoodItem("food1_" + restaurantId, "Classic Burger", "Beef patty with lettuce and tomato", "", 6.99, restaurantId, "Burger"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Cheese Burger", "Classic burger with cheese", "", 7.99, restaurantId, "Burger"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "French Fries", "Crispy golden fries", "", 3.99, restaurantId, "Sides"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Chicken Wings", "Spicy buffalo wings", "", 8.99, restaurantId, "Appetizer"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Chocolate Shake", "Rich chocolate milkshake", "", 4.99, restaurantId, "Beverage"));
        }
        else if (restaurantId.equals("restaurant4")) {
            // Spice Garden - Indian food
            foodItems.add(new FoodItem("food1_" + restaurantId, "Butter Chicken", "Creamy tomato chicken curry", "", 11.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Vegetable Biryani", "Spiced rice with vegetables", "", 9.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Garlic Naan", "Flatbread with garlic", "", 2.99, restaurantId, "Bread"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Paneer Tikka", "Grilled cottage cheese", "", 8.99, restaurantId, "Appetizer"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Gulab Jamun", "Sweet milk dumplings", "", 4.99, restaurantId, "Dessert"));
        }
        else if (restaurantId.equals("restaurant5")) {
            // Sushi Express - Japanese food
            foodItems.add(new FoodItem("food1_" + restaurantId, "California Roll", "Crab, avocado and cucumber roll", "", 8.99, restaurantId, "Sushi"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Salmon Nigiri", "Fresh salmon over rice", "", 9.99, restaurantId, "Sushi"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Miso Soup", "Traditional Japanese soup", "", 3.99, restaurantId, "Soup"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Chicken Teriyaki", "Grilled chicken with teriyaki sauce", "", 12.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Green Tea Ice Cream", "Refreshing green tea dessert", "", 4.99, restaurantId, "Dessert"));
        }
        else if (restaurantId.equals("restaurant6")) {
            // Dessert Paradise - Desserts
            foodItems.add(new FoodItem("food1_" + restaurantId, "Chocolate Cake", "Rich chocolate layer cake", "", 5.99, restaurantId, "Cake"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Cheesecake", "Creamy New York style cheesecake", "", 6.99, restaurantId, "Cake"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Ice Cream Sundae", "Vanilla ice cream with toppings", "", 4.99, restaurantId, "Ice Cream"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Apple Pie", "Traditional apple pie with cinnamon", "", 5.99, restaurantId, "Pie"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Coffee", "Freshly brewed coffee", "", 2.99, restaurantId, "Beverage"));
        }
        else {
            // Default food items if restaurant ID doesn't match
            foodItems.add(new FoodItem("food1_" + restaurantId, "Special Dish 1", "Delicious special dish", "", 9.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Special Dish 2", "Another delicious dish", "", 8.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Side Dish", "Perfect side dish", "", 4.99, restaurantId, "Sides"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Dessert", "Sweet dessert", "", 5.99, restaurantId, "Dessert"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Beverage", "Refreshing drink", "", 2.99, restaurantId, "Beverage"));
        }

        return foodItems;
    }
}