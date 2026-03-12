package com.example.fitfeast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OnboardingActivity extends AppCompatActivity {

    public OnboardingData onboardingData = new OnboardingData();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        db = FirebaseFirestore.getInstance();

        checkIfOnboardingCompleted();
    }

    // 🔹 CHECK FIRESTORE
    private void checkIfOnboardingCompleted() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        // ✅ PROFILE EXISTS → SKIP ONBOARDING
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    } else {
                        // ❌ NEW USER → START ONBOARDING
                        goToNextFragment(new NameFragment());
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Error checking profile",
                            Toast.LENGTH_SHORT).show();

                    // fallback → onboarding
                    goToNextFragment(new NameFragment());
                });
    }

    public void goToNextFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
