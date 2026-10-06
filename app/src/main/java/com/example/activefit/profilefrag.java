package com.example.activefit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class profilefrag extends Fragment {

    // ✅ Badge Logic
    public String getBadgeFromSteps(long totalSteps) {

        if (totalSteps <= 40000) {
            return "🥉 Beginner";
        } else if (totalSteps <= 100000) {
            return "🥈 Intermediate";
        } else {
            return "🥇 Pro";
        }
    }
    ImageView imagg;
    Uri imageUri;

    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagg.setImageURI(uri);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profilefrag, container, false);

        // UI Components
        Button edit = view.findViewById(R.id.edit);
        imagg= (ImageView) view.findViewById(R.id.imagg);
        imagg.setOnClickListener(v -> {
            pickImage.launch("image/*");
        });

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvHeight = view.findViewById(R.id.tvHeight);
        TextView tvWeight = view.findViewById(R.id.tvWeight);
        TextView badgeText = view.findViewById(R.id.badgeText);

        TextView tvCompleted = view.findViewById(R.id.tvCompleted);
        TextView tvTime = view.findViewById(R.id.tvTime);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 🔥 EDIT PROFILE BUTTON
        edit.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new editprofilefrag())
                    .addToBackStack(null)
                    .commit();
        });

        // 🔥 SINGLE FIRESTORE CALL (FIXED)
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(document -> {

                    if (document.exists()) {

                        // PROFILE INFO
                        String name = document.getString("Name");
                        String height = document.getString("Height");
                        String weight = document.getString("Weight");

                        tvName.setText(name != null ? name : "");
                        tvHeight.setText(height != null ? height + "cm" : "0cm");
                        tvWeight.setText(weight != null ? weight + " kg" : " 0kg");

                        // WORKOUT DATA
                        Long completed = document.getLong("completedWorkouts");
                        Long time = document.getLong("totalWorkoutTime");
                        Long stepsObj = document.getLong("totalSteps");

                        long totalSteps = (stepsObj != null) ? stepsObj : 0;

                        // BADGE CALCULATION
                        String badge = getBadgeFromSteps(totalSteps);
                        badgeText.setText(badge);

                        // DISPLAY STATS
                        tvCompleted.setText(
                                completed != null ? String.valueOf(completed) : "0"
                        );

                        tvTime.setText(
                                time != null ? time + " min" : "0 min"
                        );
                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );

        return view;
    }
}