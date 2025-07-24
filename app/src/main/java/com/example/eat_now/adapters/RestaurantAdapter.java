package com.example.eat_now.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eat_now.R;
import com.example.eat_now.activities.RestaurantDetailActivity;
import com.example.eat_now.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurantList;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        Glide.with(context)
                .load(restaurant.getImageUrl())
                .placeholder(R.drawable.placeholder_restaurant)
                .into(holder.restaurantImage);

        holder.restaurantName.setText(restaurant.getName());

        // Set categories
        List<String> categories = restaurant.getCategories();
        if (categories != null && !categories.isEmpty()) {
            holder.restaurantCategory.setText(String.join(", ", categories));
        } else {
            holder.restaurantCategory.setText("Restaurant");
        }

        holder.deliveryTime.setText(restaurant.getDeliveryTimeMinutes() + " min");

        holder.deliveryFee.setText("৳" + String.format("%.0f", restaurant.getDeliveryFee()));

        holder.ratingText.setText(String.format("%.1f", restaurant.getRating()));

        // Featured tag for high ratings
        if (restaurant.getRating() >= 4.7) {
            holder.featuredTag.setVisibility(View.VISIBLE);
        } else {
            holder.featuredTag.setVisibility(View.GONE);
        }

        holder.restaurantCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra("RESTAURANT_ID", restaurant.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName, restaurantCategory, deliveryTime, deliveryFee, ratingText, featuredTag;
        CardView restaurantCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_image);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantCategory = itemView.findViewById(R.id.restaurant_category);
            deliveryTime = itemView.findViewById(R.id.delivery_time);
            deliveryFee = itemView.findViewById(R.id.delivery_fee);
            ratingText = itemView.findViewById(R.id.rating_text);
            featuredTag = itemView.findViewById(R.id.featured_tag);
            restaurantCard = itemView.findViewById(R.id.restaurant_card);
        }
    }
}