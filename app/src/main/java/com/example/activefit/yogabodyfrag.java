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


public class yogabodyfrag extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_yogabodyfrag,
                container,
                false
        );

        Button btnChildPose = view.findViewById(R.id.btnChildPose);
        Button btnTreePose = view.findViewById(R.id.btnTreePose);
        Button btnWarriorPose = view.findViewById(R.id.btnWarriorPose);
        Button btnCobraBend = view.findViewById(R.id.btnCobraBend);
        Button btnCorpsePose = view.findViewById(R.id.btnCorpsePose);

        btnChildPose.setOnClickListener(v -> showTimePicker("Child Pose"));
        btnTreePose.setOnClickListener(v -> showTimePicker("Tree Pose"));
        btnWarriorPose.setOnClickListener(v -> showTimePicker("Warrior Pose"));
        btnCobraBend.setOnClickListener(v -> showTimePicker("Cobra Bend"));
        btnCorpsePose.setOnClickListener(v -> showTimePicker("Corpse Pose"));

        return view;
    }

    private void showTimePicker(String yogaPose) {

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

        dialog.setTitle(yogaPose);
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
                                "Yoga Session Saved",
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