package com.example.fitfeast;

import android.app.DatePickerDialog;
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

import java.util.Calendar;

public class DOBFragment extends Fragment {

    TextView tvSelectedDate, tvAge;
    Button btnPickDate, btnNext;

    int birthYear, birthMonth, birthDay;
    boolean isDobSelected = false;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_dob, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvAge = view.findViewById(R.id.tvAge);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        btnNext = view.findViewById(R.id.btnNext);

        btnPickDate.setOnClickListener(v -> openDatePicker());

        btnNext.setOnClickListener(v -> {

            if (!isDobSelected) {
                Toast.makeText(requireContext(),
                        "Please select date of birth",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int ageYears = calculateAge(birthYear, birthMonth, birthDay);

            OnboardingActivity activity =
                    (OnboardingActivity) requireActivity();

            activity.onboardingData.age = ageYears;

            activity.goToNextFragment(new HeightWeightFragment());
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    birthYear = year;
                    birthMonth = month;
                    birthDay = dayOfMonth;
                    isDobSelected = true;

                    tvSelectedDate.setText(
                            dayOfMonth + "/" + (month + 1) + "/" + year
                    );

                    int age = calculateAge(year, month, dayOfMonth);
                    tvAge.setText("Age: " + age + " years");
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private int calculateAge(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - year;

        if (today.get(Calendar.MONTH) < month ||
                (today.get(Calendar.MONTH) == month &&
                        today.get(Calendar.DAY_OF_MONTH) < day)) {
            age--;
        }
        return age;
    }
}
