package com.example.eat_now.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantDetailActivity";

    private String restaurantId;
    private FirebaseFirestore db;
    private CartManager cartManager;
    private Restaurant restaurant;
    private List<FoodItem> foodItemList;
    private FoodItemAdapter foodItemAdapter;

    // Views
    private RecyclerView foodRecyclerView;
    private ImageView restaurantImage;
    private TextView restaurantAddress, deliveryTime, deliveryFee, restaurantDescription;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton cartFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        // Get restaurant ID from intent
        restaurantId = getIntent().getStringExtra("RESTAURANT_ID");
        if (restaurantId == null) {
            Toast.makeText(this, "Restaurant not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Restaurant ID: " + restaurantId);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        cartManager = CartManager.getInstance();

        // Initialize views
        initializeViews();

        // Set up recycler view
        setupRecyclerView();

        // Load restaurant details from Firestore
        loadRestaurantFromFirestore();

        // Load menu items from Firestore
        loadMenuItemsFromFirestore();

        // Set up cart FAB
        setupCartFab();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        restaurantImage = findViewById(R.id.restaurant_image);
        restaurantAddress = findViewById(R.id.restaurant_address);
        deliveryTime = findViewById(R.id.delivery_time);
        deliveryFee = findViewById(R.id.delivery_fee);
        restaurantDescription = findViewById(R.id.restaurant_description);
        ratingBar = findViewById(R.id.rating_bar);
        foodRecyclerView = findViewById(R.id.food_recycler_view);
        cartFab = findViewById(R.id.cart_fab);
    }

    private void setupRecyclerView() {
        foodItemList = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(this, foodItemList);
        foodRecyclerView.setAdapter(foodItemAdapter);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadRestaurantFromFirestore() {
        Log.d(TAG, "Loading restaurant from Firestore: " + restaurantId);

        db.collection("restaurants").document(restaurantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "Restaurant document found");

                        // Create restaurant object from Firestore data
                        restaurant = documentSnapshot.toObject(Restaurant.class);
                        if (restaurant != null) {
                            restaurant.setId(documentSnapshot.getId());

                            // Set restaurant in cart manager
                            cartManager.setCurrentRestaurant(restaurant);

                            // Update UI with restaurant details
                            updateRestaurantUI();
                        } else {
                            Log.e(TAG, "Failed to convert document to Restaurant object");
                            Toast.makeText(this, "Error loading restaurant details", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Restaurant document does not exist");
                        Toast.makeText(this, "Restaurant not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading restaurant", e);
                    Toast.makeText(this, "Error loading restaurant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void updateRestaurantUI() {
        if (restaurant == null) return;

        Log.d(TAG, "Updating UI for restaurant: " + restaurant.getName());

        // Set title
        collapsingToolbar.setTitle(restaurant.getName());

        // Set restaurant details
        restaurantAddress.setText(restaurant.getAddress());
        deliveryTime.setText(restaurant.getDeliveryTimeMinutes() + " min");
        deliveryFee.setText("৳" + String.format("%.2f", restaurant.getDeliveryFee()));
        restaurantDescription.setText(restaurant.getDescription());
        ratingBar.setRating((float) restaurant.getRating());

        // Load restaurant image
        String imageUrl = restaurant.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Loading image: " + imageUrl);
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(restaurantImage);
        } else {
            Log.d(TAG, "No image URL found, using placeholder");
            restaurantImage.setImageResource(R.drawable.placeholder_image);
        }
    }

    private void loadMenuItemsFromFirestore() {
        Log.d(TAG, "Loading menu items for restaurant: " + restaurantId);

        db.collection("food_items")
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Found " + queryDocumentSnapshots.size() + " food items");

                    foodItemList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        FoodItem foodItem = document.toObject(FoodItem.class);
                        foodItem.setId(document.getId());
                        foodItemList.add(foodItem);

                        Log.d(TAG, "Added food item: " + foodItem.getName());
                    }

                    // Notify adapter of data change
                    foodItemAdapter.notifyDataSetChanged();

                    if (foodItemList.isEmpty()) {
                        Toast.makeText(this, "No menu items available", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading menu items", e);
                    Toast.makeText(this, "Error loading menu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupCartFab() {
        cartFab.setOnClickListener(v -> {
            if (!cartManager.isEmpty()) {
                Intent intent = new Intent(this, CheckoutActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Update cart FAB visibility based on cart contents
        updateCartFab();
    }

    private void updateCartFab() {
        if (cartManager.isEmpty()) {
            cartFab.setVisibility(View.GONE);
        } else {
            cartFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartFab();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}