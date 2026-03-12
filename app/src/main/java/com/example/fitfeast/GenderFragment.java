package com.example.fitfeast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GenderFragment extends Fragment {

    Button maleBtn, femaleBtn;

    Button btnCalculate;

    String selectedGender = "";

    public GenderFragment() {
        super(R.layout.fragment_gender);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        maleBtn = view.findViewById(R.id.btnMale);
        femaleBtn = view.findViewById(R.id.btnFemale);
        btnCalculate = view.findViewById(R.id.btnCalculateBMI);


        maleBtn.setOnClickListener(v -> {
            selectedGender = "Male";
            maleBtn.setAlpha(1f);
            femaleBtn.setAlpha(0.5f);
        });

        femaleBtn.setOnClickListener(v -> {
            selectedGender = "Female";
            femaleBtn.setAlpha(1f);
            maleBtn.setAlpha(0.5f);
        });

        btnCalculate.setOnClickListener(v -> {
            if (selectedGender.isEmpty()) {
                Toast.makeText(requireContext(), "Select gender", Toast.LENGTH_SHORT).show();
                return;
            }

            OnboardingActivity activity =
                    (OnboardingActivity) requireActivity();

            activity.onboardingData.gender = selectedGender;


            ((OnboardingActivity) requireActivity())
                    .goToNextFragment(new BMIResultFragment());
        });
    }
}
