package com.example.fitfeast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DietActivity extends AppCompatActivity {

    RecyclerView rvDiet;
    ArrayList<DietAlternative> dietList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        rvDiet = findViewById(R.id.rvDiet);
        rvDiet.setLayoutManager(new LinearLayoutManager(this));

        dietList = new ArrayList<>();

        dietList.add(new DietAlternative(
                "Rice (1 cup)",
                "200 kcal",
                "Chapati (2 medium)"
        ));

        dietList.add(new DietAlternative(
                "Paneer (100g)",
                "265 kcal",
                "Tofu (120g)"
        ));

        dietList.add(new DietAlternative(
                "Fried Eggs (2)",
                "180 kcal",
                "Boiled Eggs (2)"
        ));

        dietList.add(new DietAlternative(
                "Chocolate",
                "210 kcal",
                "Fruit Salad"
        ));

        DietAlternativeAdapter adapter = new DietAlternativeAdapter(dietList);
        rvDiet.setAdapter(adapter);
    }
}
