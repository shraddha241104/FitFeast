package com.example.fitfeast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KnowledgeActivity extends AppCompatActivity {

    RecyclerView rvKnowledge;
    ArrayList<FoodInfo> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        rvKnowledge = findViewById(R.id.rvKnowledge);
        rvKnowledge.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();

        // Sample Knowledge Data
        foodList.add(new FoodInfo(
                "Apple",
                "52 kcal / 100g",
                "Improves digestion and heart health",
                "Avoid excess if diabetic"
        ));

        foodList.add(new FoodInfo(
                "Spinach",
                "23 kcal / 100g",
                "Rich in iron and boosts immunity",
                "Avoid in kidney stone conditions"
        ));

        foodList.add(new FoodInfo(
                "Milk",
                "42 kcal / 100ml",
                "Strong bones and teeth",
                "Avoid if lactose intolerant"
        ));

        FoodAdapter adapter = new FoodAdapter(foodList);
        rvKnowledge.setAdapter(adapter);
    }
}
