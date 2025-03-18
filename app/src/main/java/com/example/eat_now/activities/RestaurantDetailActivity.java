package com.example.eat_now.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eat_now.R;
import com.example.eat_now.adapters.FoodItemAdapter;
import com.example.eat_now.models.FoodItem;
import com.example.eat_now.models.Restaurant;
import com.example.eat_now.utils.CartManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    private String restaurantId;
    private FirebaseFirestore db;
    private CartManager cartManager;
    private Restaurant restaurant;
    private List<FoodItem> foodItemList;
    private FoodItemAdapter foodItemAdapter;
    private RecyclerView foodRecyclerView;
    private ImageView restaurantImage;
    private TextView restaurantAddress, deliveryTime, deliveryFee, restaurantDescription;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        // Get restaurant ID from intent
        restaurantId = getIntent().getStringExtra("RESTAURANT_ID");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize cart manager
        cartManager = CartManager.getInstance();

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        restaurantImage = findViewById(R.id.restaurant_image);
        restaurantAddress = findViewById(R.id.restaurant_address);
        deliveryTime = findViewById(R.id.delivery_time);
        deliveryFee = findViewById(R.id.delivery_fee);
        restaurantDescription = findViewById(R.id.restaurant_description);
        ratingBar = findViewById(R.id.rating_bar);
        foodRecyclerView = findViewById(R.id.food_recycler_view);

        // Set up recycler view
        foodItemList = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(this, foodItemList);
        foodRecyclerView.setAdapter(foodItemAdapter);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load restaurant details
        loadRestaurantDetails();

        // Load menu items
        loadMenuItems();
    }

    private void loadRestaurantDetails() {
        // For demo purposes, we'll create a sample restaurant
        // In a real app, you would fetch this from Firebase

        // Find restaurant by ID from our list in HomeFragment
        for (Restaurant r : getRestaurantList()) {
            if (r.getId().equals(restaurantId)) {
                restaurant = r;
                break;
            }
        }

        if (restaurant != null) {
            // Set restaurant in cart manager
            cartManager.setCurrentRestaurant(restaurant);

            // Update UI
            collapsingToolbar.setTitle(restaurant.getName());
            restaurantAddress.setText(restaurant.getAddress());
            deliveryTime.setText(restaurant.getDeliveryTimeMinutes() + " min");
            deliveryFee.setText("$" + String.format("%.2f", restaurant.getDeliveryFee()));
            restaurantDescription.setText(restaurant.getDescription());
            ratingBar.setRating((float) restaurant.getRating());

            // Load image using Glide
            Glide.with(this)
                    .load(restaurant.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(restaurantImage);
        }
    }

    private void loadMenuItems() {
        // For demo purposes, we'll add sample menu items
        // In a real app, you would fetch this from Firebase

        foodItemList.clear();

        // Add sample food items based on restaurant categories
        if (restaurant != null) {
            List<String> categories = restaurant.getCategories();

            if (categories.contains("Bengali") || categories.contains("Traditional")) {
                foodItemList.add(new FoodItem("1", "Kacchi Biryani", "Aromatic rice dish with tender meat",
                        "https://example.com/kacchi.jpg", 8.99, restaurantId, "Main Course"));
                foodItemList.add(new FoodItem("2", "Beef Tehari", "Spicy rice dish with beef",
                        "https://example.com/tehari.jpg", 7.99, restaurantId, "Main Course"));
                foodItemList.add(new FoodItem("3", "Borhani", "Spiced yogurt drink",
                        "https://example.com/borhani.jpg", 2.99, restaurantId, "Drinks"));
            }

            if (categories.contains("Burgers") || categories.contains("Fast Food")) {
                foodItemList.add(new FoodItem("4", "Classic Burger", "Beef patty with lettuce, tomato, and cheese",
                        "https://example.com/burger.jpg", 5.99, restaurantId, "Burgers"));
                foodItemList.add(new FoodItem("5", "Chicken Burger", "Grilled chicken with special sauce",
                        "https://example.com/chicken_burger.jpg", 6.99, restaurantId, "Burgers"));
                foodItemList.add(new FoodItem("6", "French Fries", "Crispy golden fries",
                        "https://example.com/fries.jpg", 2.99, restaurantId, "Sides"));
            }

            if (categories.contains("Pizza")) {
                foodItemList.add(new FoodItem("7", "Margherita Pizza", "Classic cheese and tomato",
                        "https://example.com/margherita.jpg", 9.99, restaurantId, "Pizza"));
                foodItemList.add(new FoodItem("8", "Pepperoni Pizza", "Spicy pepperoni with cheese",
                        "https://example.com/pepperoni.jpg", 11.99, restaurantId, "Pizza"));
                foodItemList.add(new FoodItem("9", "Chicken BBQ Pizza", "BBQ chicken with onions",
                        "https://example.com/bbq_pizza.jpg", 12.99, restaurantId, "Pizza"));
            }

            if (categories.contains("Chicken")) {
                foodItemList.add(new FoodItem("10", "Fried Chicken", "Crispy fried chicken pieces",
                        "https://example.com/fried_chicken.jpg", 7.99, restaurantId, "Chicken"));
                foodItemList.add(new FoodItem("11", "Grilled Chicken", "Healthy grilled chicken",
                        "https://example.com/grilled_chicken.jpg", 8.99, restaurantId, "Chicken"));
                foodItemList.add(new FoodItem("12", "Chicken Wings", "Spicy chicken wings",
                        "https://example.com/wings.jpg", 6.99, restaurantId, "Appetizers"));
            }

            if (categories.contains("Desserts") || categories.contains("Bakery")) {
                foodItemList.add(new FoodItem("13", "Chocolate Cake", "Rich chocolate cake",
                        "https://example.com/chocolate_cake.jpg", 4.99, restaurantId, "Desserts"));
                foodItemList.add(new FoodItem("14", "Cheesecake", "Creamy New York style cheesecake",
                        "https://example.com/cheesecake.jpg", 5.99, restaurantId, "Desserts"));
                foodItemList.add(new FoodItem("15", "Ice Cream", "Vanilla ice cream with toppings",
                        "https://example.com/ice_cream.jpg", 3.99, restaurantId, "Desserts"));
            }

            // Add more items based on other categories
            // ...

            // If no specific category matches, add some generic items
            if (foodItemList.isEmpty()) {
                foodItemList.add(new FoodItem("16", "Special Dish 1", "Restaurant's special dish",
                        "https://example.com/special1.jpg", 9.99, restaurantId, "Specials"));
                foodItemList.add(new FoodItem("17", "Special Dish 2", "Chef's recommendation",
                        "https://example.com/special2.jpg", 10.99, restaurantId, "Specials"));
                foodItemList.add(new FoodItem("18", "Special Dish 3", "Popular among customers",
                        "https://example.com/special3.jpg", 8.99, restaurantId, "Specials"));
            }
        }

        // Notify adapter
        foodItemAdapter.notifyDataSetChanged();
    }

    // Helper method to get restaurant list (same as in HomeFragment)
    private List<Restaurant> getRestaurantList() {
        List<Restaurant> restaurantList = new ArrayList<>();

        // Add sample restaurants around Shahjalal University in Sylhet
        restaurantList.add(new Restaurant("1", "Panshi Restaurant", "https://example.com/panshi.jpg",
                "Zindabazar, Sylhet", "Famous for traditional Sylheti cuisine",
                4.5, 30, 2.50, 24.8949, 91.8687,
                new ArrayList<String>() {{
                    add("Bengali");
                    add("Traditional");
                }}, true));

        // Add more restaurants (same as in HomeFragment)
        // ...

        return restaurantList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}