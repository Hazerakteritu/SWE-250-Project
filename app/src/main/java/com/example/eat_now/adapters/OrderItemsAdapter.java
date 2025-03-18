package com.example.eat_now.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eat_now.R;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {
    private List<Map<String, Object>> items;

    public OrderItemsAdapter(List<Map<String, Object>> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> item = items.get(position);
        String name = (String) item.get("name");
        Long quantity = (Long) item.get("quantity");
        Double price = item.get("price") instanceof Double ?
                (Double) item.get("price") :
                Double.valueOf(String.valueOf(item.get("price")));

        holder.itemName.setText(String.format(Locale.getDefault(), "%dx %s", quantity, name));
        holder.itemPrice.setText("৳" + String.format(Locale.getDefault(), "%.2f", price * quantity));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.order_item_name);
            itemPrice = itemView.findViewById(R.id.order_item_price);
        }
    }
}