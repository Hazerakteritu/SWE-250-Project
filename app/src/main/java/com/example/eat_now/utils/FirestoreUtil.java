package com.example.eat_now.utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FirestoreUtil {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // User collection
    public static CollectionReference getUsersCollection() {
        return db.collection("users");
    }

    public static DocumentReference getUserDocument(String userId) {
        return getUsersCollection().document(userId);
    }

    // Restaurant collection
    public static CollectionReference getRestaurantsCollection() {
        return db.collection("restaurants");
    }

    public static DocumentReference getRestaurantDocument(String restaurantId) {
        return getRestaurantsCollection().document(restaurantId);
    }

    public static Query getRestaurantsByCategory(String category) {
        return getRestaurantsCollection().whereArrayContains("categories", category);
    }

    public static Query getFeaturedRestaurants() {
        return getRestaurantsCollection().whereEqualTo("featured", true);
    }

    // Food items collection
    public static CollectionReference getFoodItemsCollection() {
        return db.collection("foodItems");
    }

    public static Query getFoodItemsByRestaurant(String restaurantId) {
        return getFoodItemsCollection().whereEqualTo("restaurantId", restaurantId);
    }

    public static Query getFoodItemsByCategory(String restaurantId, String category) {
        return getFoodItemsCollection()
                .whereEqualTo("restaurantId", restaurantId)
                .whereEqualTo("category", category);
    }

    // Orders collection
    public static CollectionReference getOrdersCollection() {
        return db.collection("orders");
    }

    public static Query getOrdersByUser(String userId) {
        return getOrdersCollection().whereEqualTo("userId", userId);
    }

    public static DocumentReference getOrderDocument(String orderId) {
        return getOrdersCollection().document(orderId);
    }
}