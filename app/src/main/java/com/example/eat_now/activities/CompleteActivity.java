package com.example.eat_now.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eat_now.MainActivity;
import com.example.eat_now.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompleteActivity extends AppCompatActivity {

    private TextView orderIdText, restaurantNameText, deliveryAddressText, totalAmountText;
    private Button btnReceiveOrder, btnGoHome;

    private String orderId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery_complete);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Get order ID from intent
        orderId = getIntent().getStringExtra("ORDER_ID");
        if (orderId == null) {
            orderId = "ORD" + System.currentTimeMillis();
        }

        initializeViews();
        loadOrderDetails();
        setupButtons();
        setupBackPressHandler();
    }

    private void initializeViews() {
        orderIdText = findViewById(R.id.order_id_text);
        restaurantNameText = findViewById(R.id.restaurant_name_text);
        deliveryAddressText = findViewById(R.id.delivery_address_text);
        totalAmountText = findViewById(R.id.total_amount_text);
        btnReceiveOrder = findViewById(R.id.btn_receive_order);
        btnGoHome = findViewById(R.id.btn_go_home);
    }

    private void loadOrderDetails() {
        orderIdText.setText("Order #" + orderId);

        // Load order details from Firebase
        db.collection("orders").document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String restaurantName = documentSnapshot.getString("restaurantName");
                        String deliveryAddress = documentSnapshot.getString("deliveryAddress");
                        Double totalAmount = documentSnapshot.getDouble("totalAmount");

                        if (restaurantName != null) {
                            restaurantNameText.setText("🏪 " + restaurantName);
                        }
                        if (deliveryAddress != null) {
                            deliveryAddressText.setText("📍 " + deliveryAddress);
                        }
                        if (totalAmount != null) {
                            totalAmountText.setText("💰 Total: ৳" + String.format("%.2f", totalAmount));
                        }
                    } else {
                        // Set default values
                        restaurantNameText.setText("🏪 Restaurant");
                        deliveryAddressText.setText("📍 Your Address");
                        totalAmountText.setText("💰 Total: ৳390.00");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading order details", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupButtons() {
        btnReceiveOrder.setOnClickListener(v -> {
            // Mark order as received in Firebase
            markOrderAsReceived();
        });

        btnGoHome.setOnClickListener(v -> {
            // Go to home screen
            goToHome();
        });
    }

    // Modern way to handle back press
    private void setupBackPressHandler() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Prevent going back, show message
                Toast.makeText(CompleteActivity.this,
                        "Please use the buttons to continue", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void markOrderAsReceived() {
        // Update order status to "received" in Firebase
        db.collection("orders").document(orderId)
                .update("status", "received", "receivedAt", new java.util.Date())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "✅ Order received successfully!", Toast.LENGTH_SHORT).show();

                    // Enable go home button and change text
                    btnReceiveOrder.setText("✅ Order Received");
                    btnReceiveOrder.setEnabled(false);
                    btnReceiveOrder.setAlpha(0.6f);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating order status", Toast.LENGTH_SHORT).show();
                });
    }

    private void goToHome() {
        // Clear all activities and go to main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Remove the old onBackPressed method completely
}