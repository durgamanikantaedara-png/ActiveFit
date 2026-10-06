package com.example.activefit;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class editprofilefrag extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_editprofilefrag,container,false);
        EditText etName = view.findViewById(R.id.etName);
        EditText etWeight = view.findViewById(R.id.etWeight);
        EditText etHeight = view.findViewById(R.id.etHeight);
        EditText etStepGoal = view.findViewById(R.id.etStepGoal);
        EditText etCalorieGoal = view.findViewById(R.id.etCalorieGoal);
        Button btnSave = view.findViewById(R.id.btnSave);


       btnSave.setOnClickListener(v -> {

           String error1=etStepGoal.getText().toString().trim();
           String error2=etCalorieGoal.getText().toString().trim();

           if(error1.isEmpty()){
               etStepGoal.setError("please enter steps goal");
               etStepGoal.requestFocus();
               return;
           }

           if(error2.isEmpty()){
               etCalorieGoal.setError("please enter calories goal");
               etCalorieGoal.requestFocus();
               return;
           }


            FirebaseFirestore db=FirebaseFirestore.getInstance();

            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String,Object> userMap=new HashMap<>();

            userMap.put("Name",etName.getText().toString());
           userMap.put("Height",etHeight.getText().toString());
           userMap.put("Weight",etWeight.getText().toString());
           userMap.put("Steps",etStepGoal.getText().toString());
           userMap.put("Calorie",etCalorieGoal.getText().toString());
           userMap.put("completedWorkouts", 0);
           userMap.put("totalWorkoutTime", 0);



            db.collection("users").document(uid).set(userMap).addOnSuccessListener(unused -> {
                Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });


            getParentFragmentManager().beginTransaction().replace(R.id.container,new profilefrag()).commit();


        });



        return view;

    }
}