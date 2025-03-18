package com.example.eat_now.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eat_now.MainActivity;
import com.example.eat_now.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView orderIdText, statusText, totalAmountText, orderDateText;
    private Button trackOrderButton, backToHomeButton;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Get order ID from intent
        orderId = getIntent().getStringExtra("ORDER_ID");

        // Initialize views
        orderIdText = findViewById(R.id.order_id_text);
        statusText = findViewById(R.id.status_text);
        totalAmountText = findViewById(R.id.total_amount_text);
        orderDateText = findViewById(R.id.order_date_text);
        trackOrderButton = findViewById(R.id.track_order_button);
        backToHomeButton = findViewById(R.id.back_to_home_button);

        // Load order details
        loadOrderDetails();

        // Set up button click listeners
        trackOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, OrderTrackingActivity.class);
            intent.putExtra("ORDER_ID", orderId);
            startActivity(intent);
        });

        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadOrderDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference orderRef = db.collection("orders").document(orderId);

        orderRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Set order ID
                orderIdText.setText("Order #" + orderId);

                // Set status
                String status = documentSnapshot.getString("status");
                statusText.setText(status != null ? status : "Order Placed");

                // Set total amount
                Double totalAmount = documentSnapshot.getDouble("totalAmount");
                totalAmountText.setText("৳" + String.format(Locale.getDefault(), "%.2f", totalAmount != null ? totalAmount : 0.0));

                // Set order date
                Date orderDate = documentSnapshot.getTimestamp("createdAt") != null ?
                        documentSnapshot.getTimestamp("createdAt").toDate() : new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault());
                orderDateText.setText(sdf.format(orderDate));
            }
        });
    }
}