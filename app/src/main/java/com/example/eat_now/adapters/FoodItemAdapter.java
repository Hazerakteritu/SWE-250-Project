package com.example.eat_now.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eat_now.R;
import com.example.eat_now.models.CartItem;
import com.example.eat_now.models.FoodItem;
import com.example.eat_now.utils.CartManager;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    private Context context;
    private List<FoodItem> foodItemList;
    private CartManager cartManager;

    public FoodItemAdapter(Context context, List<FoodItem> foodItemList) {
        this.context = context;
        this.foodItemList = foodItemList;
        this.cartManager = CartManager.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);

        holder.foodName.setText(foodItem.getName());
        holder.foodDescription.setText(foodItem.getDescription());
        holder.foodPrice.setText("৳" + String.format("%.0f", foodItem.getPrice()));

        // Load image using Glide
        Glide.with(context)
                .load(foodItem.getImageUrl())
                .placeholder(R.drawable.placeholder_food)
                .error(R.drawable.error_image)
                .into(holder.foodImage);

        // Set click listener for add to cart button
        holder.addToCartButton.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(
                    foodItem.getId(),
                    foodItem,
                    1,
                    ""
            );
            cartManager.addToCart(cartItem);
            Toast.makeText(context, foodItem.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodDescription, foodPrice;
        Button addToCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodPrice = itemView.findViewById(R.id.food_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}