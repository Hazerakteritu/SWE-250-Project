package com.example.eat_now.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eat_now.R;
import com.example.eat_now.models.CartItem;
import com.example.eat_now.utils.CartManager;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartManager cartManager;
    private CartItemListener listener;

    public interface CartItemListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartManager = CartManager.getInstance();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.itemName.setText(cartItem.getFoodItem().getName());
        // Changed from $ to ৳ (Taka symbol)
        holder.itemPrice.setText("৳" + String.format("%.2f", cartItem.getTotalPrice()));
        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // Load image using Glide
        Glide.with(context)
                .load(cartItem.getFoodItem().getImageUrl())
                .placeholder(R.drawable.placeholder_food)
                .error(R.drawable.error_image)
                .into(holder.itemImage);

        // Set click listeners for quantity buttons
        holder.decreaseButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartManager.updateCartItem(cartItem);
                notifyItemChanged(position);
                listener.onCartUpdated();
            } else {
                cartManager.removeFromCart(cartItem.getId());
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
                listener.onCartUpdated();
            }
        });

        holder.increaseButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartManager.updateCartItem(cartItem);
            notifyItemChanged(position);
            listener.onCartUpdated();
        });

        holder.removeButton.setOnClickListener(v -> {
            cartManager.removeFromCart(cartItem.getId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice, itemQuantity;
        ImageButton decreaseButton, increaseButton, removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cart_item_image);
            itemName = itemView.findViewById(R.id.cart_item_name);
            itemPrice = itemView.findViewById(R.id.cart_item_price);
            itemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            decreaseButton = itemView.findViewById(R.id.decrease_quantity_button);
            increaseButton = itemView.findViewById(R.id.increase_quantity_button);
            removeButton = itemView.findViewById(R.id.remove_item_button);
        }
    }
}