package com.example.fitfeast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivitySectionActivity extends AppCompatActivity {

    RecyclerView rvActivities;
    ArrayList<ActivityInfo> activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_section);

        rvActivities = findViewById(R.id.rvActivities);
        rvActivities.setLayoutManager(new LinearLayoutManager(this));

        activityList = new ArrayList<>();

        activityList.add(new ActivityInfo(
                "Walking",
                "150 kcal / 30 min",
                "Improves heart health and aids weight loss"
        ));

        activityList.add(new ActivityInfo(
                "Gym Workout",
                "300 kcal / 45 min",
                "Builds muscle strength and stamina"
        ));

        activityList.add(new ActivityInfo(
                "Zumba",
                "250 kcal / 30 min",
                "Fun cardio and stress relief"
        ));

        activityList.add(new ActivityInfo(
                "Yoga",
                "120 kcal / 30 min",
                "Improves flexibility and mental health"
        ));

        ActivityAdapter adapter = new ActivityAdapter(activityList);
        rvActivities.setAdapter(adapter);
    }
}
