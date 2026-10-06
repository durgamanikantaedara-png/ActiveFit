package com.example.activefit;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class lowerbodyfrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_lowerbodyfrag,
                container,
                false
        );

        Button btnsquts = view.findViewById(R.id.btnsquts);
        Button btnlunges = view.findViewById(R.id.btnlunges);
        Button btngulte = view.findViewById(R.id.btngulte);
        Button btncalf = view.findViewById(R.id.btncalf);
        Button btnwall = view.findViewById(R.id.btnwall);

        btnsquts.setOnClickListener(v -> showTimePicker("Squats"));
        btnlunges.setOnClickListener(v -> showTimePicker("Lunges"));
        btngulte.setOnClickListener(v -> showTimePicker("Glute Bridge"));
        btncalf.setOnClickListener(v -> showTimePicker("Calf Raises"));
        btnwall.setOnClickListener(v -> showTimePicker("Wall Sit"));


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
                                "Lower Body Workout Saved",
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