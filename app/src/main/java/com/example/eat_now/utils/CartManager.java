package com.example.eat_now.utils;

import com.example.eat_now.models.CartItem;
import com.example.eat_now.models.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private Map<String, CartItem> cartItems;
    private Restaurant currentRestaurant;

    private CartManager() {
        cartItems = new HashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void setCurrentRestaurant(Restaurant restaurant) {
        this.currentRestaurant = restaurant;
    }

    public Restaurant getCurrentRestaurant() {
        return currentRestaurant;
    }

    public void addToCart(CartItem item) {
        String itemId = item.getId();
        if (cartItems.containsKey(itemId)) {
            CartItem existingItem = cartItems.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            cartItems.put(itemId, item);
        }
    }

    public void updateCartItem(CartItem item) {
        if (cartItems.containsKey(item.getId())) {
            cartItems.put(item.getId(), item);
        }
    }

    public void removeFromCart(String itemId) {
        cartItems.remove(itemId);
    }

    public void clearCart() {
        cartItems.clear();
        currentRestaurant = null;
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());
    }

    public int getItemCount() {
        return cartItems.size();
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
