package com.example.fitfeast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Spinner spActivity;
    RadioGroup rgDiet;
    EditText etAllergy;
    Button btnGenerate, btnLogout;
    TextView tvResult;

    FirebaseAuth auth;
    FirebaseFirestore db;
    Button btnActivity, btnProgress, btnKnowledge;

    float height = 0;
    float weight = 0;
    int age = 0;
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI
        spActivity = findViewById(R.id.spActivity);
        rgDiet = findViewById(R.id.rgDiet);
        etAllergy = findViewById(R.id.etAllergy);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnLogout = findViewById(R.id.btnLogout);
        tvResult = findViewById(R.id.tvResult);
        btnActivity = findViewById(R.id.btnActivity);
        btnProgress = findViewById(R.id.btnProgress);
        btnKnowledge = findViewById(R.id.btnKnowledge);

        // Activity level spinner
        Spinner spActivity = findViewById(R.id.spActivity);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.activity_levels,
                R.layout.spinner_selected_item
        );

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spActivity.setAdapter(adapter);

        loadUserProfile();

        btnGenerate.setOnClickListener(v -> generatePlan());

        btnLogout.setOnClickListener(v -> logoutUser());

        btnActivity.setOnClickListener(v ->
                startActivity(new Intent(this, PhysicalActivityActivity.class)));

        btnProgress.setOnClickListener(v ->
                startActivity(new Intent(this, ProgressTrackingActivity.class)));

        btnKnowledge.setOnClickListener(v ->
                startActivity(new Intent(this, KnowledgeWallActivity.class)));
    }

    // 🔹 Load user profile from Firestore
    private void loadUserProfile() {

        String uid = auth.getUid();
        if (uid == null) return;

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {

                    if (!doc.exists()) return;

                    if (doc.getDouble("height") != null)
                        height = doc.getDouble("height").floatValue();

                    if (doc.getDouble("weight") != null)
                        weight = doc.getDouble("weight").floatValue();

                    if (doc.getLong("age") != null)
                        age = doc.getLong("age").intValue();

                    if (doc.getString("gender") != null)
                        gender = doc.getString("gender");

                });
    }

    // 🔹 Generate diet plan
    private void generatePlan() {

        int dietId = rgDiet.getCheckedRadioButtonId();

        if (dietId == -1) {
            Toast.makeText(this, "Please select diet preference", Toast.LENGTH_SHORT).show();
            return;
        }

        String dietType;

        if (dietId == R.id.rbVeg) dietType = "Vegetarian";
        else if (dietId == R.id.rbNonVeg) dietType = "Non-Vegetarian";
        else if (dietId == R.id.rbEgg) dietType = "Eggetarian";
        else dietType = "Vegan";

        if (height == 0 || weight == 0 || age == 0 || gender.isEmpty()) {

            Toast.makeText(this,
                    "User profile not loaded yet. Please wait.",
                    Toast.LENGTH_LONG).show();

            return;
        }

        // 🔹 BMR Formula
        float bmr;

        if (gender.equalsIgnoreCase("Male")) {

            bmr = (10 * weight) + (6.25f * height) - (5 * age) + 5;

        } else {

            bmr = (10 * weight) + (6.25f * height) - (5 * age) - 161;

        }

        // 🔹 Activity multiplier
        float factor;

        switch (spActivity.getSelectedItemPosition()) {

            case 1:
                factor = 1.375f;
                break;

            case 2:
                factor = 1.55f;
                break;

            case 3:
                factor = 1.725f;
                break;

            default:
                factor = 1.2f;
        }

        int dailyCalories = Math.round(bmr * factor);

        // Show calories
        tvResult.setText("Daily Calories Needed: " + dailyCalories + " kcal");

        // Save plan
        savePlanToDB(dailyCalories, dietType);

        // Go to AI diet screen
        Intent intent = new Intent(HomeActivity.this, DietPlanActivity.class);

        intent.putExtra("dailyCalories", dailyCalories);
        intent.putExtra("dietType", dietType);
        intent.putExtra("allergies", etAllergy.getText().toString().trim());

        startActivity(intent);
    }

    // 🔹 Save plan in Firestore
    private void savePlanToDB(int calories, String diet) {

        String uid = auth.getUid();
        if (uid == null) return;

        Map<String, Object> plan = new HashMap<>();

        plan.put("activityLevel", spActivity.getSelectedItem().toString());
        plan.put("dietPreference", diet);
        plan.put("allergies", etAllergy.getText().toString().trim());
        plan.put("dailyCalories", calories);
        plan.put("timestamp", FieldValue.serverTimestamp());

        db.collection("user_plans")
                .document(uid)
                .set(plan);

        Toast.makeText(this, "Plan generated 🎉", Toast.LENGTH_SHORT).show();
    }

    // 🔹 Logout
    private void logoutUser() {

        FirebaseAuth.getInstance().signOut();

        GoogleSignIn.getClient(
                this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut();

        Intent i = new Intent(this, LoginActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);

        finish();
    }
}