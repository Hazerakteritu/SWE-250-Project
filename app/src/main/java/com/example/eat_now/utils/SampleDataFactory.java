package com.example.eat_now.utils;

import com.example.eat_now.models.FoodItem;
import com.example.eat_now.models.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleDataFactory {

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


    // Create sample food items
    public static List<FoodItem> createSampleFoodItems(String restaurantId) {

        List<FoodItem> foodItems = new ArrayList<>();

        if (restaurantId.equals("restaurant1")) {
            // Kacchi Bhai Ambarkhana
            foodItems.add(new FoodItem("food1_" + restaurantId, "Kacchi Biryani", "Authentic Sylheti kacchi biryani with tender mutton", "", 15.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Beef Rezala", "Traditional beef curry with rich gravy", "", 12.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Chicken Roast", "Spicy roasted chicken Sylheti style", "", 10.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Borhani", "Traditional yogurt drink with spices", "", 3.99, restaurantId, "Beverage"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Firni", "Creamy rice pudding dessert", "", 4.99, restaurantId, "Dessert"));
        }
        else if (restaurantId.equals("restaurant2")) {
            // Tasty Treat Sylhet
            foodItems.add(new FoodItem("food1_" + restaurantId, "Chicken Burger", "Crispy chicken burger with special sauce", "", 8.99, restaurantId, "Burger"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Beef Burger", "Juicy beef patty with fresh vegetables", "", 9.99, restaurantId, "Burger"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Chicken Wings", "Spicy buffalo chicken wings", "", 7.99, restaurantId, "Snacks"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "French Fries", "Golden crispy potato fries", "", 3.99, restaurantId, "Sides"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Chocolate Shake", "Rich chocolate milkshake", "", 4.99, restaurantId, "Beverage"));
        }
        else if (restaurantId.equals("restaurant3")) {
            // 5 Bhai Restaurant
            foodItems.add(new FoodItem("food1_" + restaurantId, "Hilsa Fish Curry", "Fresh hilsa fish in traditional curry", "", 14.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Mutton Curry", "Tender mutton in spicy gravy", "", 13.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Dal Gosht", "Lentils cooked with meat", "", 11.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Plain Rice", "Steamed basmati rice", "", 2.99, restaurantId, "Sides"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Mixed Vegetables", "Seasonal vegetables curry", "", 6.99, restaurantId, "Vegetarian"));
        }
        else if (restaurantId.equals("restaurant4")) {
            // Panshi Sylhet - Authentic Sylheti
            foodItems.add(new FoodItem("food1_" + restaurantId, "Shatkora Beef", "Beef curry with citrus shatkora", "", 16.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Shutki Bhorta", "Dried fish mash Sylheti style", "", 8.99, restaurantId, "Traditional"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Duck Curry", "Traditional duck curry", "", 15.99, restaurantId, "Main Course"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Panta Bhat", "Fermented rice with accompaniments", "", 7.99, restaurantId, "Traditional"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Seven Color Tea", "Famous Sylheti layered tea", "", 2.99, restaurantId, "Beverage"));
        }
        else if (restaurantId.equals("restaurant5")) {
            // Pizza Hub Sylhet - Italian/Pizza
            foodItems.add(new FoodItem("food1_" + restaurantId, "Margherita Pizza", "Classic tomato and mozzarella pizza", "", 12.99, restaurantId, "Pizza"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Chicken Supreme", "Chicken pizza with vegetables", "", 15.99, restaurantId, "Pizza"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Beef Pepperoni", "Spicy pepperoni pizza", "", 14.99, restaurantId, "Pizza"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Garlic Bread", "Crispy bread with garlic butter", "", 4.99, restaurantId, "Sides"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Pasta Alfredo", "Creamy white sauce pasta", "", 10.99, restaurantId, "Pasta"));
        }
        else if (restaurantId.equals("restaurant6")) {
            // Bonoful Sweets - Traditional Sweets
            foodItems.add(new FoodItem("food1_" + restaurantId, "Rasgulla", "Soft cottage cheese balls in syrup", "", 5.99, restaurantId, "Sweets"));
            foodItems.add(new FoodItem("food2_" + restaurantId, "Sandesh", "Traditional Bengali sweet", "", 6.99, restaurantId, "Sweets"));
            foodItems.add(new FoodItem("food3_" + restaurantId, "Mishti Doi", "Sweet yogurt dessert", "", 4.99, restaurantId, "Dessert"));
            foodItems.add(new FoodItem("food4_" + restaurantId, "Chomchom", "Cylindrical sweet in syrup", "", 5.99, restaurantId, "Sweets"));
            foodItems.add(new FoodItem("food5_" + restaurantId, "Kalojam", "Dark sweet balls in syrup", "", 5.99, restaurantId, "Sweets"));
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
