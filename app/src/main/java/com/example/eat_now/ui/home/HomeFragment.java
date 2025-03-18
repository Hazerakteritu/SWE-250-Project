package com.example.eat_now.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eat_now.R;
import com.example.eat_now.adapters.CategoryAdapter;
import com.example.eat_now.adapters.RestaurantAdapter;
import com.example.eat_now.models.Category;
import com.example.eat_now.models.Restaurant;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private String currentCategory = "All";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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

        // Load restaurants
        loadRestaurants();

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
        String[] categories = {"All", "Fast Food", "Chicken", "Beef", "Seafood", "Vegetarian",
                "Breakfast", "Sandwich", "Dessert", "Soup", "Bakery"};

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

    private void loadRestaurants() {
        // Clear existing lists
        allRestaurants.clear();
        popularRestaurants.clear();

        // Add sample restaurants
        allRestaurants.add(new Restaurant("1", "Sylhet Biryani House",
                "https://example.com/biryani_house.jpg", "Zindabazar, Sylhet",
                "Famous for traditional Sylheti biryani", 4.8, 30, 40.0,
                24.8949, 91.8687, new ArrayList<String>() {{
            add("Bangladeshi");
            add("Biryani");
        }}, true));

        allRestaurants.add(new Restaurant("2", "Pizza Planet",
                "https://example.com/pizza_planet.jpg", "Uposhohor, Sylhet",
                "Best pizza in town", 4.7, 35, 50.0,
                24.9032, 91.8603, new ArrayList<String>() {{
            add("Italian");
            add("Pizza");
        }}, true));

        allRestaurants.add(new Restaurant("3", "Campus Burger",
                "https://example.com/campus_burger.jpg", "University Road, Sylhet",
                "Student favorite burger joint", 4.5, 25, 30.0,
                24.9176, 91.8328, new ArrayList<String>() {{
            add("Fast Food");
            add("Burger");
        }}, true));

        allRestaurants.add(new Restaurant("4", "Tandoori Express",
                "https://example.com/tandoori_express.jpg", "Amberkhana, Sylhet",
                "Authentic tandoori dishes", 4.6, 40, 45.0,
                24.9011, 91.8712, new ArrayList<String>() {{
            add("Indian");
            add("Tandoori");
        }}, true));

        allRestaurants.add(new Restaurant("5", "Sylhet Chinese Corner",
                "https://example.com/chinese_corner.jpg", "Zindabazar, Sylhet",
                "Best Chinese food in Sylhet", 4.3, 25, 35.0,
                24.8949, 91.8687, new ArrayList<String>() {{
            add("Chinese");
        }}, true));

        allRestaurants.add(new Restaurant("6", "Cafe Campus",
                "https://example.com/cafe_campus.jpg", "University Road, Sylhet",
                "Cozy cafe near university", 4.4, 20, 25.0,
                24.9176, 91.8328, new ArrayList<String>() {{
            add("Cafe");
            add("Dessert");
        }}, true));

        // Add more restaurants as needed

        // Select popular restaurants (those with rating >= 4.5)
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.getRating() >= 4.5) {
                popularRestaurants.add(restaurant);
            }
        }

        // Update adapters
        popularRestaurantsAdapter.notifyDataSetChanged();

        // Initial filtering (show all)
        filterRestaurants();
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
        loadRestaurants();
    }
}