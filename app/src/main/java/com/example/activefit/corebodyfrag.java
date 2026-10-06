package com.example.activefit;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class corebodyfrag extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_corebodyfrag,
                container,
                false
        );

        Button btnplank = view.findViewById(R.id.btnplank);
        Button btncrunches = view.findViewById(R.id.btncrunches);
        Button btnrussian = view.findViewById(R.id.btnrussian);
        Button btnlegraises = view.findViewById(R.id.btnlegraises);

        btnplank.setOnClickListener(v -> showTimePicker("Plank"));
        btncrunches.setOnClickListener(v -> showTimePicker("Crunches"));
        btnrussian.setOnClickListener(v -> showTimePicker("Russian Twists"));
        btnlegraises.setOnClickListener(v -> showTimePicker("Leg Raises"));

        return view;
    }

    private void showTimePicker(String workoutName) {

        TimePickerDialog dialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {

                    int totalMinutes = (hourOfDay * 60) + minute;

                    saveWorkout(totalMinutes);
                },
                0,
                0,
                true
        );

        dialog.setTitle(workoutName);
        dialog.show();
    }

    private void saveWorkout(int minutes) {

        FirebaseUser user =
                FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(
                    getContext(),
                    "User not logged in",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String uid = user.getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .update(
                        "completedWorkouts", FieldValue.increment(1),
                        "totalWorkoutTime", FieldValue.increment(minutes)
                )
                .addOnSuccessListener(unused ->
                        Toast.makeText(
                                getContext(),
                                "Core Workout Saved",
                                Toast.LENGTH_SHORT
                        ).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(
                                getContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }
}
