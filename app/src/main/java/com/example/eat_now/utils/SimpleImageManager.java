package com.example.eat_now.utils;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SimpleImageManager {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static void setAllRestaurantImages(Context context) {
        Toast.makeText(context, "Setting restaurant images...", Toast.LENGTH_SHORT).show();

        // Fixed image URLs for each restaurant
        Map<String, String> restaurantImages = new HashMap<>();
        restaurantImages.put("restaurant1", "https://scontent.fdac138-1.fna.fbcdn.net/v/t39.30808-6/464619126_4248163125410501_2020016519552104049_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=8iR7XzWcRJ8Q7kNvwHNcclE&_nc_oc=AdnDvTxEIckt0qwMT-FS_pWMeMC1ESYA-ph0E0hBRHKm5dAeNLxpNk3k3RKQ9l-SK9Y&_nc_zt=23&_nc_ht=scontent.fdac138-1.fna&_nc_gid=8VwebIZcsLDDUi1-JR2_9A&oh=00_AfMqt3Jfb66IkR-HKEYsOw0YnkucjoOoOpV_8VgeRjWKVg&oe=685A136B"); // Kacchi Bhai - Biryani
        restaurantImages.put("restaurant2", "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=400"); // Tasty Treat
        restaurantImages.put("restaurant3", "https://images.deliveryhero.io/image/fd-bd/Products/879397.jpg??width=600"); // 5 Bhai
        restaurantImages.put("restaurant4", "https://images.deliveryhero.io/image/fd-bd/Products/4450186.jpg??width=600"); // Panshi
        restaurantImages.put("restaurant5", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400"); // Pizza Hub - Pizza
        restaurantImages.put("restaurant6", "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400"); // Bonoful - Sweets

        for (Map.Entry<String, String> entry : restaurantImages.entrySet()) {
            String restaurantId = entry.getKey();
            String imageUrl = entry.getValue();

            db.collection("restaurants").document(restaurantId)
                .update("imageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "❌ Failed to update " + restaurantId, Toast.LENGTH_SHORT).show();
                });
        }

        Toast.makeText(context, "✅ All restaurant images set!", Toast.LENGTH_LONG).show();
    }


    public static void setAllFoodImages(Context context) {
        Toast.makeText(context, "Setting food images...", Toast.LENGTH_SHORT).show();

        // Fixed image URLs for each food item
        Map<String, String> foodImages = new HashMap<>();

        // Restaurant 1 - Kacchi Bhai foods
        foodImages.put("food1_restaurant1", "https://images.deliveryhero.io/image/fd-bd/Products/2046151.jpg??width=600"); // Kacchi Biryani
        foodImages.put("food2_restaurant1", "https://images.deliveryhero.io/image/fd-bd/Products/8091169.jpg??width=600");
        foodImages.put("food3_restaurant1", "https://images.deliveryhero.io/image/fd-bd/Products/2046170.jpg??width=600"); // Chicken Roast
        foodImages.put("food4_restaurant1", "https://images.deliveryhero.io/image/fd-bd/Products/2046177.jpg??width=600"); // Borhani
        foodImages.put("food5_restaurant1", "https://images.deliveryhero.io/image/fd-bd/Products/2046175.jpg??width=600"); // Firni

        // Restaurant 2 - Tasty Treat foods
        foodImages.put("food1_restaurant2", "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=300"); // Chicken Burger
        foodImages.put("food2_restaurant2", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300"); // Beef Burger
        foodImages.put("food3_restaurant2", "https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=300"); // Chicken Wings
        foodImages.put("food4_restaurant2", "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=300"); // French Fries
        foodImages.put("food5_restaurant2", "https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=300"); // Chocolate Shake

        // Restaurant 3 - 5 Bhai foods
        foodImages.put("food1_restaurant3", "https://as1.ftcdn.net/v2/jpg/14/08/69/36/1000_F_1408693633_Vd1IGCM58jqp1UKdM2oh1D15sFN5P33z.jpg");
        foodImages.put("food2_restaurant3", "https://images.deliveryhero.io/image/fd-bd/Products/879397.jpg??width=600");
        foodImages.put("food3_restaurant3", "https://images.deliveryhero.io/image/fd-bd/Products/4049907.jpg??width=600");
        foodImages.put("food4_restaurant3", "https://images.deliveryhero.io/image/fd-bd/products/4049887.jpg??width=600");
        foodImages.put("food5_restaurant3", "https://images.deliveryhero.io/image/fd-bd/products/4049910.jpg??width=600");

        // Restaurant 4 - Panshi foods
        foodImages.put("food1_restaurant4", "https://images.deliveryhero.io/image/fd-bd/products/4450222.jpg??width=600");
        foodImages.put("food2_restaurant4", "https://images.deliveryhero.io/image/fd-bd/Products/4450186.jpg??width=600"); // Shutki Bhorta
        foodImages.put("food3_restaurant4", "https://images.deliveryhero.io/image/fd-bd/products/4450193.jpg??width=600");
        foodImages.put("food4_restaurant4", "https://images.deliveryhero.io/image/fd-bd/Products/4569067.jpg??width=600");
        foodImages.put("food5_restaurant4", "https://images.deliveryhero.io/image/fd-bd/Products/4450285.jpg??width=600");

        // Restaurant 5 - Pizza Hub foods
        foodImages.put("food1_restaurant5", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=300"); // Margherita Pizza
        foodImages.put("food2_restaurant5", "https://images.deliveryhero.io/image/fd-bd/products/%20pizza%20express%20/907130.jpg?width=%s"); // Chicken Supreme
        foodImages.put("food3_restaurant5", "https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f?w=300"); // Beef Pepperoni
        foodImages.put("food4_restaurant5", "https://images.deliveryhero.io/image/fd-bd/products/5834313.jpg??width=600"); // Garlic Bread
        foodImages.put("food5_restaurant5", "https://images.deliveryhero.io/image/fd-bd/products/%20pizza%20express%20/907146.jpg?width=%s"); // Pasta Alfredo

        // Restaurant 6 - Fuchka tong
        foodImages.put("food1_restaurant6", "https://images.deliveryhero.io/image/fd-bd/products/5655700.jpg??width=600"); // Rasgulla
        foodImages.put("food2_restaurant6", "https://images.deliveryhero.io/image/fd-bd/products/5655705.jpg??width=600"); // Sandesh
        foodImages.put("food3_restaurant6", "https://images.deliveryhero.io/image/fd-bd/Products/6031862.jpg??width=600"); // Mishti Doi
        foodImages.put("food4_restaurant6", "https://images.deliveryhero.io/image/fd-bd/Products/6031867.jpg??width=600"); // Chomchom
        foodImages.put("food5_restaurant6", "https://images.deliveryhero.io/image/fd-bd/products/7206786.jpg??width=600"); // Kalojam

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
