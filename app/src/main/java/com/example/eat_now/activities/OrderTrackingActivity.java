package com.example.eat_now.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.eat_now.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;


/**
 * FIXED Order Tracking - Matches your XML file!
 */
public class OrderTrackingActivity extends AppCompatActivity {

    private TextView orderIdText, orderDateText;
    private TextView statusPlaced, statusPreparing, statusOnWay, statusDelivered;
    private ProgressBar progressBar;
    private TextView totalText, subtotalText, deliveryFeeText; // ← Added missing TextViews
    private TextView deliveryAddressText, deliveryPhoneText, orderedTimeText;
    private TextView paymentMethodText, restaurantNameText;

    private String orderId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // FIXED: Use your actual XML file name
            setContentView(R.layout.activity_order_tracking);

            // Initialize Firebase
            db = FirebaseFirestore.getInstance();

            // Get order ID
            orderId = getIntent().getStringExtra("ORDER_ID");
            if (orderId == null) {
                orderId = "ORD" + System.currentTimeMillis();
            }

            // Setup
            setupToolbar();
            initializeViews();
            loadRealOrderDetails();
            simulateOrderProgress();

        } catch (Exception e) {
            Toast.makeText(this, "Error loading order", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    private void initializeViews() {
        orderIdText = findViewById(R.id.order_id);
        orderDateText = findViewById(R.id.order_date);
        statusPlaced = findViewById(R.id.status_placed);
        statusPreparing = findViewById(R.id.status_preparing);
        statusOnWay = findViewById(R.id.status_on_way);
        statusDelivered = findViewById(R.id.status_delivered);
        progressBar = findViewById(R.id.order_progress_bar);

        // FIXED: Added the missing TextViews from your XML
        totalText = findViewById(R.id.total);
        subtotalText = findViewById(R.id.subtotal);
        deliveryFeeText = findViewById(R.id.delivery_fee);

        deliveryAddressText = findViewById(R.id.delivery_address);
        deliveryPhoneText = findViewById(R.id.delivery_phone);
        orderedTimeText = findViewById(R.id.ordered_time);
        paymentMethodText = findViewById(R.id.payment_method);
        restaurantNameText = findViewById(R.id.restaurant_name);
    }

    /**
     * Load REAL order details from Firebase - FIXED!
     */
    private void loadRealOrderDetails() {
        try {
            // Set order ID first
            if (orderIdText != null) {
                orderIdText.setText("Order #" + orderId);
            }

            // Get REAL order data from Firebase
            db.collection("orders").document(orderId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Get REAL data from your CheckoutActivity
                            String realAddress = documentSnapshot.getString("deliveryAddress");
                            String realPhone = documentSnapshot.getString("phone");
                            String paymentMethod = documentSnapshot.getString("paymentMethod");
                            String restaurantName = documentSnapshot.getString("restaurantName");
                            Double totalAmount = documentSnapshot.getDouble("totalAmount");
                            Date createdAt = documentSnapshot.getDate("createdAt");

                            // Show REAL information
                            if (realAddress != null && deliveryAddressText != null) {
                                deliveryAddressText.setText("📍 " + realAddress);
                            }
                            if (realPhone != null && deliveryPhoneText != null) {
                                deliveryPhoneText.setText("📞 " + realPhone);
                            }
                            if (paymentMethod != null && paymentMethodText != null) {
                                paymentMethodText.setText("💳 " + paymentMethod);
                            }
                            if (restaurantName != null && restaurantNameText != null) {
                                restaurantNameText.setText("🏪 " + restaurantName);
                            }

                            // FIXED: Handle all three amounts properly
                            if (totalAmount != null) {
                                // Smart breakdown calculation
                                double deliveryFee = 50.0; // You can get this from restaurant data
                                double subtotal = totalAmount - deliveryFee;

                                // Update all three amounts
                                if (totalText != null) {
                                    totalText.setText("৳" + String.format(Locale.getDefault(), "%.2f", totalAmount));
                                }
                                if (subtotalText != null) {
                                    subtotalText.setText("৳" + String.format(Locale.getDefault(), "%.2f", subtotal));
                                }
                                if (deliveryFeeText != null) {
                                    deliveryFeeText.setText("৳" + String.format(Locale.getDefault(), "%.2f", deliveryFee));
                                }
                            }

                            if (createdAt != null && orderedTimeText != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.getDefault());
                                orderedTimeText.setText("🕐 Ordered: " + sdf.format(createdAt));
                            }
                            if (orderDateText != null && createdAt != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.getDefault());
                                orderDateText.setText(sdf.format(createdAt));
                            }

                        } else {
                            // Fallback to default values
                            loadDefaultValues();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error loading order details", Toast.LENGTH_SHORT).show();
                        loadDefaultValues();
                    });

        } catch (Exception e) {
            loadDefaultValues();
        }
    }

    /**
     * FIXED: Fallback values for all TextViews
     */
    private void loadDefaultValues() {
        try {
            if (orderDateText != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                orderDateText.setText(currentDate);
            }
            if (totalText != null) {
                totalText.setText("৳390.00");
            }
            if (subtotalText != null) {
                subtotalText.setText("৳340.00");
            }
            if (deliveryFeeText != null) {
                deliveryFeeText.setText("৳50.00");
            }
            if (deliveryAddressText != null) {
                deliveryAddressText.setText("📍 Varsity Gate, Akhalia, Sylhet");
            }
            if (deliveryPhoneText != null) {
                deliveryPhoneText.setText("📞 +8801620129229");
            }
            if (orderedTimeText != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                orderedTimeText.setText("🕐 Ordered: " + currentTime);
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    private void simulateOrderProgress() {
        try {
            // Update every 5 seconds
            new android.os.Handler().postDelayed(() -> updateStatus(1), 5000);  // Preparing
            new android.os.Handler().postDelayed(() -> updateStatus(2), 10000); // On Way
            new android.os.Handler().postDelayed(() -> updateStatus(3), 15000); // Delivered
        } catch (Exception e) {
            // Ignore
        }
    }

    private void updateStatus(int status) {
        try {
            if (isFinishing()) return;

            // Reset colors
            if (statusPlaced != null) statusPlaced.setTextColor(0xFF666666);
            if (statusPreparing != null) statusPreparing.setTextColor(0xFF666666);
            if (statusOnWay != null) statusOnWay.setTextColor(0xFF666666);
            if (statusDelivered != null) statusDelivered.setTextColor(0xFF666666);

            // Update status
            switch (status) {
                case 1: // Preparing
                    if (statusPlaced != null) statusPlaced.setTextColor(0xFF4CAF50);
                    if (statusPreparing != null) statusPreparing.setTextColor(0xFF4CAF50);
                    if (progressBar != null) progressBar.setProgress(50);
                    Toast.makeText(this, "🍳 Your order is being prepared!", Toast.LENGTH_SHORT).show();
                    break;
                case 2: // On Way
                    if (statusPlaced != null) statusPlaced.setTextColor(0xFF4CAF50);
                    if (statusPreparing != null) statusPreparing.setTextColor(0xFF4CAF50);
                    if (statusOnWay != null) statusOnWay.setTextColor(0xFF4CAF50);
                    if (progressBar != null) progressBar.setProgress(75);
                    Toast.makeText(this, "🚗 Your order is on the way!", Toast.LENGTH_SHORT).show();
                    break;
                case 3: // Delivered - NEW: Navigate to delivery complete page
                    if (statusPlaced != null) statusPlaced.setTextColor(0xFF4CAF50);
                    if (statusPreparing != null) statusPreparing.setTextColor(0xFF4CAF50);
                    if (statusOnWay != null) statusOnWay.setTextColor(0xFF4CAF50);
                    if (statusDelivered != null) statusDelivered.setTextColor(0xFF4CAF50);
                    if (progressBar != null) progressBar.setProgress(100);

                    // Show delivery complete page
                    showDeliveryCompletePage();
                    break;
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeliveryCompletePage() {
        // Wait 2 seconds then show delivery complete page
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(this, CompleteActivity.class);
            intent.putExtra("ORDER_ID", orderId);
            startActivity(intent);
            finish(); // Close tracking activity
        }, 2000);
    }
}
