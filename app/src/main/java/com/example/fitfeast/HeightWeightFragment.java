package com.example.fitfeast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HeightWeightFragment extends Fragment {

    EditText etHeight, etWeight;
    Button btnNext;

    public HeightWeightFragment() {
        super(R.layout.fragment_height_weight);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {

            String h = etHeight.getText().toString().trim();
            String w = etWeight.getText().toString().trim();

            if (h.isEmpty() || w.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Please enter height and weight",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            float height, weight;

            try {
                height = Float.parseFloat(h);
                weight = Float.parseFloat(w);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(),
                        "Invalid number format",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            OnboardingActivity activity =
                    (OnboardingActivity) requireActivity();

            activity.onboardingData.height = height;
            activity.onboardingData.weight = weight;

            activity.goToNextFragment(new GenderFragment());
        });
    }
}
