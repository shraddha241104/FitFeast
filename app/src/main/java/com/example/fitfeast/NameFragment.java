package com.example.fitfeast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NameFragment extends Fragment {

    EditText etName;
    Button btnNext;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_name, container, false);

        etName = view.findViewById(R.id.etName);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Enter your name",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            OnboardingActivity activity =
                    (OnboardingActivity) requireActivity();

            activity.onboardingData.name = name;

            activity.goToNextFragment(new DOBFragment());
        });

        return view;
    }
}
