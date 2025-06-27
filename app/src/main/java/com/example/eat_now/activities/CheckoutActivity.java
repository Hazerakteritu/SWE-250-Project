package com.example.eat_now.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eat_now.R;
import com.example.eat_now.adapters.CartAdapter;
import com.example.eat_now.models.CartItem;
import com.example.eat_now.models.Order;
import com.example.eat_now.models.Restaurant;
import com.example.eat_now.utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity implements CartAdapter.CartItemListener {

    private RecyclerView cartRecyclerView;
    private TextView subtotalText, deliveryFeeText, totalText, restaurantNameText;
    private EditText addressEditText, phoneEditText;
    private RadioGroup paymentMethodGroup;
    private Button placeOrderButton;
    private CartManager cartManager;
    private CartAdapter cartAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Check if user is authenticated
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login to place order", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        cartRecyclerView = findViewById(R.id.checkout_cart_recycler_view);
        subtotalText = findViewById(R.id.checkout_subtotal_text);
        deliveryFeeText = findViewById(R.id.checkout_delivery_fee_text);
        totalText = findViewById(R.id.checkout_total_text);
        restaurantNameText = findViewById(R.id.checkout_restaurant_name_text);
        addressEditText = findViewById(R.id.address_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        paymentMethodGroup = findViewById(R.id.payment_method_group);
        placeOrderButton = findViewById(R.id.place_order_button);

        // Initialize cart manager
        cartManager = CartManager.getInstance();

        // Set up recycler view
        setupRecyclerView();

        // Update UI
        updateCheckoutUI();

        // Set up place order button
        placeOrderButton.setOnClickListener(v -> {
            if (validateForm()) {
                placeOrder();
            }
        });
    }

    private void setupRecyclerView() {
        List<CartItem> cartItems = cartManager.getCartItems();
        cartAdapter = new CartAdapter(this, cartItems, this);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateCheckoutUI() {
        Restaurant restaurant = cartManager.getCurrentRestaurant();

        if (restaurant != null) {
            double subtotal = cartManager.getCartTotal();
            double deliveryFee = restaurant.getDeliveryFee();
            double total = subtotal + deliveryFee;

            restaurantNameText.setText(restaurant.getName());

            subtotalText.setText(String.format("৳%.2f", subtotal));
            deliveryFeeText.setText(String.format("৳%.2f", deliveryFee));
            totalText.setText(String.format("৳%.2f", total));
        }
    }

    private boolean validateForm() {
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            return false;
        }

        if (phone.isEmpty()) {
            phoneEditText.setError("Phone number is required");
            return false;
        }

        if (paymentMethodGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void placeOrder() {
        // Get current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Get selected payment method
        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String paymentMethod = radioButton.getText().toString();

        // Get delivery address and phone
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Create order
        String orderId = UUID.randomUUID().toString();
        Restaurant restaurant = cartManager.getCurrentRestaurant();
        List<CartItem> cartItems = cartManager.getCartItems();
        double totalAmount = cartManager.getCartTotal() + restaurant.getDeliveryFee();

        // Create order map for Firestore
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", orderId);
        orderData.put("userId", userId);
        orderData.put("restaurantId", restaurant.getId());
        orderData.put("restaurantName", restaurant.getName());
        orderData.put("items", cartItems);
        orderData.put("deliveryAddress", address);
        orderData.put("phone", phone);
        orderData.put("paymentMethod", paymentMethod);
        orderData.put("totalAmount", totalAmount);
        orderData.put("status", "Order Placed");
        orderData.put("createdAt", new Date());

        // Save order to Firestore with proper error handling
        db.collection("orders")
                .document(orderId)
                .set(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CheckoutActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Clear cart
                    cartManager.clearCart();

                    // Start order confirmation activity
                    Intent intent = new Intent(CheckoutActivity.this, OrderConfirmationActivity.class);
                    intent.putExtra("ORDER_ID", orderId);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CheckoutActivity.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    // Log the error for debugging
                    e.printStackTrace();
                });
    }

    @Override
    public void onCartUpdated() {
        updateCheckoutUI();
    }
}