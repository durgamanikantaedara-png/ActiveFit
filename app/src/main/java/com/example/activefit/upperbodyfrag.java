package com.example.activefit;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class upperbodyfrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upperbodyfrag, container, false);

        Button btn1 = view.findViewById(R.id.btn1);
        Button btn2 = view.findViewById(R.id.btn2);
        Button btn3 = view.findViewById(R.id.btn3);
        Button btn4 = view.findViewById(R.id.btn4);
        Button btn5 = view.findViewById(R.id.btncircle);

        btn1.setOnClickListener(v -> showTimePicker("Pull Ups"));
        btn2.setOnClickListener(v -> showTimePicker("Tricep Dips"));
        btn3.setOnClickListener(v -> showTimePicker("Pike Push Ups"));
        btn4.setOnClickListener(v -> showTimePicker("Superman Hold"));
        btn5.setOnClickListener(v -> showTimePicker("Arm Circles"));

        return view;
    }

    private void showTimePicker(String workoutName) {

        TimePickerDialog dialog =
                new TimePickerDialog(
                        getContext(),
                        (view, hour, minute) -> {

                            int totalMinutes = (hour * 60) + minute;

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
            Toast.makeText(getContext(),
                    "User not logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        FirebaseFirestore db =
                FirebaseFirestore.getInstance();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {

                    long completedWorkouts = 0;
                    long totalWorkoutTime = 0;

                    if (document.exists()) {

                        Long completed =
                                document.getLong("completedWorkouts");

                        Long time =
                                document.getLong("totalWorkoutTime");

                        if (completed != null) {
                            completedWorkouts = completed;
                        }

                        if (time != null) {
                            totalWorkoutTime = time;
                        }
                    }

                    completedWorkouts++;
                    totalWorkoutTime += minutes;

                    db.collection("users")
                            .document(uid)
                            .update(
                                    "completedWorkouts", completedWorkouts,
                                    "totalWorkoutTime", totalWorkoutTime
                            )
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(
                                            getContext(),
                                            "Workout Saved",
                                            Toast.LENGTH_SHORT
                                    ).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(
                                            getContext(),
                                            e.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show()
                            );

                });
    }
}