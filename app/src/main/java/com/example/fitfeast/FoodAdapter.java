package com.example.fitfeast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    ArrayList<FoodInfo> list;

    public FoodAdapter(ArrayList<FoodInfo> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName, tvCalories, tvBenefits, tvCaution;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvBenefits = itemView.findViewById(R.id.tvBenefits);
            tvCaution = itemView.findViewById(R.id.tvCaution);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodInfo food = list.get(position);
        holder.tvFoodName.setText(food.name);
        holder.tvCalories.setText("Calories: " + food.calories);
        holder.tvBenefits.setText("Benefits: " + food.benefits);
        holder.tvCaution.setText("Caution: " + food.caution);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
