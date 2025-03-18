package com.example.eat_now.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eat_now.R;
import com.example.eat_now.adapters.OrderItemsAdapter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderTrackingActivity extends AppCompatActivity {
    private TextView orderIdText, orderDateText;
    private TextView statusPlaced, statusPreparing, statusOnWay, statusDelivered;
    private ImageView statusPlacedIcon, statusPreparingIcon, statusOnWayIcon, statusDeliveredIcon;
    private ProgressBar progressBar;
    private RecyclerView orderItemsRecycler;
    private TextView subtotalText, deliveryFeeText, totalText;
    private TextView deliveryAddressText, deliveryPhoneText, orderedTimeText;

    private FirebaseFirestore db;
    private String orderId;
    private Handler handler = new Handler();
    private int currentStatusIndex = 0;
    private final List<String> statusList = Arrays.asList(
            "Order Placed",
            "Preparing",
            "On the Way",
            "Delivered"
    );
    private final int UPDATE_INTERVAL = 10000; // 10 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Get order ID from intent
        orderId = getIntent().getStringExtra("ORDER_ID");
        if (orderId == null) {
            orderId = "sample_order_id"; // Fallback for testing
        }

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize views
        initializeViews();

        // Load order details
        loadOrderDetails();

        // Start status updates
        startStatusUpdates();
    }

    private void initializeViews() {
        orderIdText = findViewById(R.id.order_id);
        orderDateText = findViewById(R.id.order_date);
        statusPlaced = findViewById(R.id.status_placed);
        statusPreparing = findViewById(R.id.status_preparing);
        statusOnWay = findViewById(R.id.status_on_way);
        statusDelivered = findViewById(R.id.status_delivered);
        statusPlacedIcon = findViewById(R.id.status_placed_icon);
        statusPreparingIcon = findViewById(R.id.status_preparing_icon);
        statusOnWayIcon = findViewById(R.id.status_on_way_icon);
        statusDeliveredIcon = findViewById(R.id.status_delivered_icon);
        progressBar = findViewById(R.id.order_progress_bar);
        orderItemsRecycler = findViewById(R.id.order_items_recycler);
        subtotalText = findViewById(R.id.subtotal);
        deliveryFeeText = findViewById(R.id.delivery_fee);
        totalText = findViewById(R.id.total);
        deliveryAddressText = findViewById(R.id.delivery_address);
        deliveryPhoneText = findViewById(R.id.delivery_phone);
        orderedTimeText = findViewById(R.id.ordered_time);

        // Set up RecyclerView
        orderItemsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrderDetails() {
        DocumentReference orderRef = db.collection("orders").document(orderId);
        orderRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Set order ID
                orderIdText.setText("Order #" + orderId);

                // Set order date
                Date orderDate = documentSnapshot.getTimestamp("createdAt") != null
                        ? documentSnapshot.getTimestamp("createdAt").toDate()
                        : new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
                String orderDateStr = sdf.format(orderDate);
                orderDateText.setText(orderDateStr);

                // Set order items
                List<Map<String, Object>> items = (List<Map<String, Object>>) documentSnapshot.get("items");
                if (items != null) {
                    OrderItemsAdapter adapter = new OrderItemsAdapter(items);
                    orderItemsRecycler.setAdapter(adapter);
                } else {
                    // Create sample items for testing
                    List<Map<String, Object>> sampleItems = new ArrayList<>();
                    Map<String, Object> item1 = new HashMap<>();
                    item1.put("name", "Kacchi Biryani");
                    item1.put("quantity", 1L);
                    item1.put("price", 280.0);
                    sampleItems.add(item1);

                    Map<String, Object> item2 = new HashMap<>();
                    item2.put("name", "Borhani");
                    item2.put("quantity", 1L);
                    item2.put("price", 60.0);
                    sampleItems.add(item2);

                    OrderItemsAdapter adapter = new OrderItemsAdapter(sampleItems);
                    orderItemsRecycler.setAdapter(adapter);
                }

                // Set amounts
                double subtotal = 340.0; // Default value
                double deliveryFee = 50.0; // Default value
                double total = 390.0; // Default value

                if (documentSnapshot.getDouble("totalAmount") != null) {
                    total = documentSnapshot.getDouble("totalAmount");
                    subtotal = total - deliveryFee;
                }

                subtotalText.setText("৳" + String.format(Locale.getDefault(), "%.2f", subtotal));
                deliveryFeeText.setText("৳" + String.format(Locale.getDefault(), "%.2f", deliveryFee));
                totalText.setText("৳" + String.format(Locale.getDefault(), "%.2f", total));

                // Set delivery info
                String address = documentSnapshot.getString("deliveryAddress");
                if (address == null) address = "varsity gate, Akhalia, sylhet";

                String phone = documentSnapshot.getString("phone");
                if (phone == null) phone = "+8801620129229";

                deliveryAddressText.setText(address);
                deliveryPhoneText.setText(phone);
                orderedTimeText.setText("Ordered: " + orderDateStr);

                // Check current status
                String currentStatus = documentSnapshot.getString("status");
                if (currentStatus != null) {
                    int statusIndex = statusList.indexOf(currentStatus);
                    if (statusIndex >= 0) {
                        currentStatusIndex = statusIndex;
                        for (int i = 0; i <= statusIndex; i++) {
                            updateOrderStatus(i);
                        }
                    }
                }
            }
        });
    }

    private void startStatusUpdates() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentStatusIndex < statusList.size() - 1) {
                    currentStatusIndex++;
                    updateOrderStatus(currentStatusIndex);
                    handler.postDelayed(this, UPDATE_INTERVAL);
                }
            }
        }, UPDATE_INTERVAL);
    }

    private void updateOrderStatus(int index) {
        int activeColor = Color.BLACK;
        int inactiveColor = getResources().getColor(R.color.grey_600);

        switch (index) {
            case 0:
                statusPlaced.setTextColor(activeColor);
                statusPlacedIcon.setColorFilter(activeColor);
                progressBar.setProgress(25);
                break;
            case 1:
                statusPreparing.setTextColor(activeColor);
                statusPreparingIcon.setColorFilter(activeColor);
                progressBar.setProgress(50);
                break;
            case 2:
                statusOnWay.setTextColor(activeColor);
                statusOnWayIcon.setColorFilter(activeColor);
                progressBar.setProgress(75);
                break;
            case 3:
                statusDelivered.setTextColor(activeColor);
                statusDeliveredIcon.setColorFilter(activeColor);
                progressBar.setProgress(100);
                break;
        }

        // Update status in Firestore
        db.collection("orders").document(orderId)
                .update("status", statusList.get(index));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}