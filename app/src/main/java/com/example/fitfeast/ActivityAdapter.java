package com.example.fitfeast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    ArrayList<ActivityInfo> list;

    public ActivityAdapter(ArrayList<ActivityInfo> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvActivityName, tvCaloriesBurned, tvActivityBenefits;

        public ViewHolder(View itemView) {
            super(itemView);
            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvCaloriesBurned = itemView.findViewById(R.id.tvCaloriesBurned);
            tvActivityBenefits = itemView.findViewById(R.id.tvActivityBenefits);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityInfo activity = list.get(position);
        holder.tvActivityName.setText(activity.name);
        holder.tvCaloriesBurned.setText("Calories Burned: " + activity.caloriesBurned);
        holder.tvActivityBenefits.setText("Benefits: " + activity.benefits);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
