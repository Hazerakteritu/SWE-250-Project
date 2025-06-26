package com.example.eat_now.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    private String restaurantId;
    private FirebaseFirestore db;
    private CartManager cartManager;

    private List<FoodItem> foodItemList = new ArrayList<>();
    private FoodItemAdapter foodItemAdapter;

    private ImageView restaurantImage;
    private TextView restaurantAddress, deliveryTime, deliveryFee, restaurantDescription;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton cartFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        restaurantId = getIntent().getStringExtra("RESTAURANT_ID");
        if (restaurantId == null) {
            Toast.makeText(this, "Restaurant not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        cartManager = CartManager.getInstance();

        setupViews();
        setupRecyclerView();
        loadRestaurant();
        loadMenuItems();

        cartFab.setOnClickListener(v -> {
            if (cartManager.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });
    }

    private void setupViews() {
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
        cartFab = findViewById(R.id.cart_fab);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.food_recycler_view);
        foodItemAdapter = new FoodItemAdapter(this, foodItemList);
        recyclerView.setAdapter(foodItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadRestaurant() {
        db.collection("restaurants").document(restaurantId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Restaurant restaurant = snapshot.toObject(Restaurant.class);
                    if (restaurant == null) {
                        Toast.makeText(this, "Error loading restaurant", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    cartManager.setCurrentRestaurant(restaurant);

                    collapsingToolbar.setTitle(restaurant.getName());
                    restaurantAddress.setText(restaurant.getAddress());
                    deliveryTime.setText(restaurant.getDeliveryTimeMinutes() + " min");
                    deliveryFee.setText("৳" + String.format("%.2f", restaurant.getDeliveryFee()));
                    restaurantDescription.setText(restaurant.getDescription());
                    ratingBar.setRating((float) restaurant.getRating());

                    Glide.with(this)
                            .load(restaurant.getImageUrl())
                            .placeholder(R.drawable.placeholder_image)
                            .into(restaurantImage);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load restaurant", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void loadMenuItems() {
        db.collection("food_items")
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .addOnSuccessListener(query -> {
                    foodItemList.clear();
                    for (var doc : query) {
                        FoodItem item = doc.toObject(FoodItem.class);
                        foodItemList.add(item);
                    }
                    foodItemAdapter.notifyDataSetChanged();
                });
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
