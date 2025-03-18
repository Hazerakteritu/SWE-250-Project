package com.example.eat_now.utils;

import com.example.eat_now.models.CartItem;
import com.example.eat_now.models.Order;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderUtil {

    public static Task<DocumentReference> placeOrder(String restaurantId, List<CartItem> cartItems, double totalAmount) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Convert cart items to a format suitable for Firestore
        List<Map<String, Object>> items = cartItems.stream().map(cartItem -> {
            Map<String, Object> item = new HashMap<>();
            item.put("foodItemId", cartItem.getFoodItem().getId());
            item.put("name", cartItem.getFoodItem().getName());
            item.put("price", cartItem.getFoodItem().getPrice());
            item.put("quantity", cartItem.getQuantity());
            return item;
        }).collect(Collectors.toList());

        // Create order object
        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("restaurantId", restaurantId);
        order.put("items", items);
        order.put("totalAmount", totalAmount);
        order.put("status", "pending");
        order.put("createdAt", new Date());

        // Save to Firestore
        return FirestoreUtil.getOrdersCollection().add(order);
    }
}