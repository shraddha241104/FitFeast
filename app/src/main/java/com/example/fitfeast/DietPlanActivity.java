package com.example.fitfeast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

public class DietPlanActivity extends AppCompatActivity {

    TextView tvGreeting, tvCalories;

    TextView tvBreakfast1, tvBreakfastCal1;
    TextView tvBreakfast2, tvBreakfastCal2;

    TextView tvLunch1, tvLunchCal1;
    TextView tvLunch2, tvLunchCal2;

    TextView tvDinner1, tvDinnerCal1;
    TextView tvDinner2, tvDinnerCal2;

    ImageView imgBreakfast1, imgBreakfast2;
    ImageView imgLunch1, imgLunch2;
    ImageView imgDinner1, imgDinner2;

    Button btnRegenerate, btnConsult;

    FirebaseAuth auth;
    FirebaseFirestore db;

    float dailyCalories = 0;
    String dietType = "Vegetarian";
    String userName = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        tvGreeting = findViewById(R.id.tvGreeting);
        tvCalories = findViewById(R.id.tvCalories);

        tvBreakfast1 = findViewById(R.id.tvBreakfast1);
        tvBreakfastCal1 = findViewById(R.id.tvBreakfastCal1);
        tvBreakfast2 = findViewById(R.id.tvBreakfast2);
        tvBreakfastCal2 = findViewById(R.id.tvBreakfastCal2);

        tvLunch1 = findViewById(R.id.tvLunch1);
        tvLunchCal1 = findViewById(R.id.tvLunchCal1);
        tvLunch2 = findViewById(R.id.tvLunch2);
        tvLunchCal2 = findViewById(R.id.tvLunchCal2);

        tvDinner1 = findViewById(R.id.tvDinner1);
        tvDinnerCal1 = findViewById(R.id.tvDinnerCal1);
        tvDinner2 = findViewById(R.id.tvDinner2);
        tvDinnerCal2 = findViewById(R.id.tvDinnerCal2);

        imgBreakfast1 = findViewById(R.id.imgBreakfast1);
        imgBreakfast2 = findViewById(R.id.imgBreakfast2);

        imgLunch1 = findViewById(R.id.imgLunch1);
        imgLunch2 = findViewById(R.id.imgLunch2);

        imgDinner1 = findViewById(R.id.imgDinner1);
        imgDinner2 = findViewById(R.id.imgDinner2);

        btnRegenerate = findViewById(R.id.btnRegenerate);
        btnConsult = findViewById(R.id.btnConsult);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dailyCalories = getIntent().getIntExtra("dailyCalories", 0);
        dietType = getIntent().getStringExtra("dietType");

        if(dietType == null) dietType = "Vegetarian";

        tvCalories.setText("Total Daily Calories: " + (int) dailyCalories + " kcal");

        loadUserName();

        btnRegenerate.setOnClickListener(v -> {

            Toast.makeText(this,"Generating new AI diet...",Toast.LENGTH_SHORT).show();
            generateDietPlan();

        });

        btnConsult.setOnClickListener(v -> {

            Intent intent = new Intent(DietPlanActivity.this, DieticianActivity.class);
            startActivity(intent);

        });
    }

    private void loadUserName() {

        String uid = auth.getUid();

        if(uid == null){

            tvGreeting.setText("Hello User");
            generateDietPlan();
            return;

        }

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {

                    String name = doc.getString("name");

                    if(name != null) userName = name;

                    tvGreeting.setText("Hello " + userName);

                    generateDietPlan();

                })
                .addOnFailureListener(e -> {

                    tvGreeting.setText("Hello " + userName);
                    generateDietPlan();

                });
    }

    private void generateDietPlan() {

        String allergies = getIntent().getStringExtra("allergies");

        GeminiHelper.generateDietPlan(
                (int) dailyCalories,
                dietType,
                allergies,
                new GeminiHelper.GeminiCallback() {

                    @Override
                    public void onSuccess(String result) {

                        runOnUiThread(() -> {

                            try {

                                String cleaned = result
                                        .replace("```json","")
                                        .replace("```","")
                                        .trim();

                                JSONObject json = new JSONObject(cleaned);

                                JSONObject b1 = json.getJSONObject("breakfast1");
                                JSONObject b2 = json.getJSONObject("breakfast2");

                                JSONObject l1 = json.getJSONObject("lunch1");
                                JSONObject l2 = json.getJSONObject("lunch2");

                                JSONObject d1 = json.getJSONObject("dinner1");
                                JSONObject d2 = json.getJSONObject("dinner2");

                                String mealB1 = b1.getString("meal");
                                String mealB2 = b2.getString("meal");

                                String mealL1 = l1.getString("meal");
                                String mealL2 = l2.getString("meal");

                                String mealD1 = d1.getString("meal");
                                String mealD2 = d2.getString("meal");

                                // 🔥 CALORIE DISTRIBUTION
                                int breakfastCalories = (int)(dailyCalories * 0.30);
                                int lunchCalories = (int)(dailyCalories * 0.40);
                                int dinnerCalories = (int)(dailyCalories * 0.30);

                                int breakfastEach = breakfastCalories / 2;
                                int lunchEach = lunchCalories / 2;
                                int dinnerEach = dinnerCalories / 2;

                                tvBreakfast1.setText(mealB1);
                                tvBreakfastCal1.setText("Calories: " + breakfastEach);

                                tvBreakfast2.setText(mealB2);
                                tvBreakfastCal2.setText("Calories: " + breakfastEach);

                                tvLunch1.setText(mealL1);
                                tvLunchCal1.setText("Calories: " + lunchEach);

                                tvLunch2.setText(mealL2);
                                tvLunchCal2.setText("Calories: " + lunchEach);

                                tvDinner1.setText(mealD1);
                                tvDinnerCal1.setText("Calories: " + dinnerEach);

                                tvDinner2.setText(mealD2);
                                tvDinnerCal2.setText("Calories: " + dinnerEach);

                                // Images
                                PexelsImageLoader.loadFoodImage(mealB1, imgBreakfast1);
                                PexelsImageLoader.loadFoodImage(mealB2, imgBreakfast2);

                                PexelsImageLoader.loadFoodImage(mealL1, imgLunch1);
                                PexelsImageLoader.loadFoodImage(mealL2, imgLunch2);

                                PexelsImageLoader.loadFoodImage(mealD1, imgDinner1);
                                PexelsImageLoader.loadFoodImage(mealD2, imgDinner2);

                            }
                            catch (Exception e){

                                Toast.makeText(
                                        DietPlanActivity.this,
                                        "AI format error",
                                        Toast.LENGTH_LONG
                                ).show();

                            }

                        });

                    }

                    @Override
                    public void onError(String error) {

                        runOnUiThread(() ->
                                Toast.makeText(
                                        DietPlanActivity.this,
                                        "AI Error: " + error,
                                        Toast.LENGTH_LONG
                                ).show()
                        );

                    }
                }
        );
    }
}