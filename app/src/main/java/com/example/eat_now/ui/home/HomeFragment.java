package com.example.eat_now.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eat_now.R;
import com.example.eat_now.adapters.RestaurantAdapter;
import com.example.eat_now.models.Restaurant;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView greetingText;
    private RecyclerView popularRestaurantsRecycler;
    private RecyclerView allRestaurantsRecycler;
    private ChipGroup categoryChipGroup;

    private RestaurantAdapter popularRestaurantsAdapter;
    private RestaurantAdapter allRestaurantsAdapter;

    private List<Restaurant> allRestaurants;
    private List<Restaurant> popularRestaurants;
    private List<Restaurant> filteredRestaurants;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentCategory = "All";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        greetingText = root.findViewById(R.id.greeting_text);
        popularRestaurantsRecycler = root.findViewById(R.id.popular_restaurants_recycler);
        allRestaurantsRecycler = root.findViewById(R.id.all_restaurants_recycler);

        // Set up greeting text
        setGreetingText();

        // Initialize restaurant lists
        allRestaurants = new ArrayList<>();
        popularRestaurants = new ArrayList<>();
        filteredRestaurants = new ArrayList<>();

        // Set up recycler views
        popularRestaurantsAdapter = new RestaurantAdapter(getContext(), popularRestaurants);
        popularRestaurantsRecycler.setAdapter(popularRestaurantsAdapter);
        popularRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        allRestaurantsAdapter = new RestaurantAdapter(getContext(), filteredRestaurants);
        allRestaurantsRecycler.setAdapter(allRestaurantsAdapter);
        allRestaurantsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Set up category chips
        setupCategoryChips(root);

        // Load restaurants from Firestore
        loadRestaurantsFromFirestore();

        return root;
    }

    private void setGreetingText() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hourOfDay < 12) {
            greeting = "Good Morning";
        } else if (hourOfDay < 18) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String name = "Guest";

        if (user != null) {
            String email = user.getEmail();
            if (email != null && !email.isEmpty()) {
                name = email.substring(0, email.indexOf('@'));
                // Capitalize first letter
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }

        greetingText.setText(greeting + ", " + name + "!");
    }

    private void setupCategoryChips(View root) {
        // Get the chip group
        categoryChipGroup = root.findViewById(R.id.category_chip_group);

        // Define categories
        String[] categories = {"All", "Fast Food", "Bangladeshi", "Italian", "Indian", "Japanese",
                "Pizza", "Burger", "Dessert", "Biryani", "Seafood"};

        // Add chips programmatically
        for (String category : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setClickable(true);

            // Set the first chip (All) as checked by default
            if (category.equals("All")) {
                chip.setChecked(true);
            }

            chip.setOnClickListener(v -> {
                currentCategory = category;
                filterRestaurants();
            });

            categoryChipGroup.addView(chip);
        }
    }

    private void loadRestaurantsFromFirestore() {
        db.collection("restaurants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allRestaurants.clear();
                    popularRestaurants.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurant.setId(document.getId());
                        allRestaurants.add(restaurant);

                        // Add to popular restaurants if rating >= 4.5
                        if (restaurant.getRating() >= 4.5) {
                            popularRestaurants.add(restaurant);
                        }
                    }

                    // Update adapters
                    popularRestaurantsAdapter.notifyDataSetChanged();

                    // Initial filtering (show all)
                    filterRestaurants();

                    if (allRestaurants.isEmpty()) {
                        Toast.makeText(getContext(), "No restaurants found. Please add sample data.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading restaurants: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void filterRestaurants() {
        filteredRestaurants.clear();

        if (currentCategory.equals("All")) {
            filteredRestaurants.addAll(allRestaurants);
        } else {
            for (Restaurant restaurant : allRestaurants) {
                List<String> categories = restaurant.getCategories();
                boolean matchesCategory = false;

                // Check if restaurant categories match the selected category
                for (String category : categories) {
                    if (category.equalsIgnoreCase(currentCategory) ||
                            (currentCategory.equals("Fast Food") && category.equalsIgnoreCase("Burger"))) {
                        matchesCategory = true;
                        break;
                    }
                }

                if (matchesCategory) {
                    filteredRestaurants.add(restaurant);
                }
            }
        }

        allRestaurantsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        loadRestaurantsFromFirestore();
    }
}