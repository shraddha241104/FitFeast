package com.example.fitfeast;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BMIResultFragment extends Fragment {

    TextView tvBmi, tvCategory;
    Button btnNext;

    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_bmi_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBmi = view.findViewById(R.id.tvBMI);
        tvCategory = view.findViewById(R.id.tvCategory);
        btnNext = view.findViewById(R.id.btnNext);

        db = FirebaseFirestore.getInstance();

        if (!(requireActivity() instanceof OnboardingActivity)) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        OnboardingActivity activity = (OnboardingActivity) requireActivity();

        // Safety check
        if (activity.onboardingData.height <= 0 || activity.onboardingData.weight <= 0) {
            Toast.makeText(requireContext(), "Invalid height or weight", Toast.LENGTH_SHORT).show();
            return;
        }

        float heightM = activity.onboardingData.height / 100f;
        float weight = activity.onboardingData.weight;

        float bmi = weight / (heightM * heightM);

        String category;
        if (bmi < 18.5f) category = "Underweight";
        else if (bmi < 25f) category = "Normal";
        else if (bmi < 30f) category = "Overweight";
        else category = "Obese";

        // Save locally
        activity.onboardingData.bmi = bmi;
        activity.onboardingData.bmiCategory = category;

        tvBmi.setText(String.format("%.1f", bmi));
        tvCategory.setText("Category: " + category);

        btnNext.setOnClickListener(v -> saveDataAndMove(activity));
    }

    // 🔹 SAVE TO FIRESTORE
    private void saveDataAndMove(OnboardingActivity activity) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", activity.onboardingData.name);
        userData.put("age", activity.onboardingData.age);
        userData.put("height", activity.onboardingData.height);
        userData.put("weight", activity.onboardingData.weight);
        userData.put("gender", activity.onboardingData.gender);
        userData.put("bmi", activity.onboardingData.bmi);
        userData.put("bmiCategory", activity.onboardingData.bmiCategory);
        userData.put("updatedAt", System.currentTimeMillis());

        db.collection("users")
                .document(uid)
                .set(userData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(),
                            "Profile saved successfully",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(requireActivity(), HomeActivity.class));
                    requireActivity().finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "DB Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }
}
