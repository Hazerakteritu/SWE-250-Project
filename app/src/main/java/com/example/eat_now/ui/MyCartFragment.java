package com.example.eat_now.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eat_now.R;
import com.example.eat_now.activities.CheckoutActivity;
import com.example.eat_now.adapters.CartAdapter;
import com.example.eat_now.models.CartItem;
import com.example.eat_now.models.Restaurant;
import com.example.eat_now.utils.CartManager;

import java.util.List;

public class MyCartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView cartRecyclerView;
    private TextView emptyCartText, subtotalText, deliveryFeeText, totalText, restaurantNameText;
    private Button checkoutButton;
    private CartManager cartManager;
    private CartAdapter cartAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_cart, container, false);

        // Initialize views
        cartRecyclerView = root.findViewById(R.id.cart_recycler_view);
        emptyCartText = root.findViewById(R.id.empty_cart_text);
        subtotalText = root.findViewById(R.id.subtotal_text);
        deliveryFeeText = root.findViewById(R.id.delivery_fee_text);
        totalText = root.findViewById(R.id.total_text);
        restaurantNameText = root.findViewById(R.id.restaurant_name_text);
        checkoutButton = root.findViewById(R.id.checkout_button);

        // Initialize cart manager
        cartManager = CartManager.getInstance();

        // Set up recycler view
        setupRecyclerView();

        // Update UI
        updateCartUI();

        // Set up checkout button
        checkoutButton.setOnClickListener(v -> {
            if (!cartManager.getCartItems().isEmpty()) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void setupRecyclerView() {
        List<CartItem> cartItems = cartManager.getCartItems();
        cartAdapter = new CartAdapter(getContext(), cartItems, this);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void updateCartUI() {
        List<CartItem> cartItems = cartManager.getCartItems();
        Restaurant restaurant = cartManager.getCurrentRestaurant();

        if (cartItems.isEmpty()) {
            emptyCartText.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            subtotalText.setText("৳0.00");
            deliveryFeeText.setText("৳0.00");
            totalText.setText("৳0.00");
            restaurantNameText.setText("");
            checkoutButton.setEnabled(false);
        } else {
            emptyCartText.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(true);

            double subtotal = cartManager.getCartTotal();
            double deliveryFee = restaurant != null ? restaurant.getDeliveryFee() : 0;
            double total = subtotal + deliveryFee;

            subtotalText.setText(String.format("৳%.2f", subtotal));
            deliveryFeeText.setText(String.format("৳%.2f", deliveryFee));
            totalText.setText(String.format("৳%.2f", total));

            if (restaurant != null) {
                restaurantNameText.setText(restaurant.getName());
            }
        }
    }

    @Override
    public void onCartUpdated() {
        updateCartUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh cart when fragment becomes visible
        if (cartAdapter != null) {
            cartAdapter.notifyDataSetChanged();
        }
        updateCartUI();
    }
}