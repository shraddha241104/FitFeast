package com.example.fitfeast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DietAlternativeAdapter extends RecyclerView.Adapter<DietAlternativeAdapter.ViewHolder> {

    ArrayList<DietAlternative> list;

    public DietAlternativeAdapter(ArrayList<DietAlternative> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFood, tvCalories, tvAlternative;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFood = itemView.findViewById(R.id.tvFood);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvAlternative = itemView.findViewById(R.id.tvAlternative);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diet_alternative, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DietAlternative item = list.get(position);
        holder.tvFood.setText("Food: " + item.food);
        holder.tvCalories.setText("Calories: " + item.calories);
        holder.tvAlternative.setText("Alternative: " + item.alternative);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
