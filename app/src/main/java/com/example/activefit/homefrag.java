package com.example.activefit;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class homefrag extends Fragment {
    private long pauseTime = 0;
    private long totalPausedTime = 0;
    private SharedPreferences sp;
    private int baseSteps = -1;
    private StepCounterManager stepCounterManager;

    private int currentSteps = 0;
    private int goalSteps = 0;
    private long startTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homefrag, container, false);


        sp = requireActivity().getSharedPreferences("StepData", 0);



        startTime = sp.getLong("startTime", -1);

        if (startTime == -1) {
            startTime = System.currentTimeMillis();
            sp.edit().putLong("startTime", startTime).apply();
        }

        baseSteps = sp.getInt("baseSteps", -1);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        String savedDate = sp.getString("date", "");

        if (!today.equals(savedDate)) {

            startTime = System.currentTimeMillis();

            sp.edit()
                    .putString("date", today)
                    .remove("baseSteps")
                    .putLong("startTime", startTime)
                    .apply();

            baseSteps = -1;
            totalPausedTime = 0;
        }


        TextView tv = view.findViewById(R.id.percent);
        TextView step1= (TextView) view.findViewById(R.id.step1);
        TextView step2= (TextView) view.findViewById(R.id.step2);
        TextView cal1= (TextView) view.findViewById(R.id.cal1);
        TextView cal2= (TextView) view.findViewById(R.id.cal2);
        TextView time1= (TextView) view.findViewById(R.id.time1);
        TextView time2= (TextView) view.findViewById(R.id.time2);
        TextView step1final= (TextView) view.findViewById(R.id.step1final);
        TextView cal1final= (TextView) view.findViewById(R.id.cal1final);
        CircularProgressIndicator circularProgress= (CircularProgressIndicator) view.findViewById(R.id.circularProgress);
        circularProgress.setProgress(0);
        tv.setText("0%");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        101
                );
            }
        }



        stepCounterManager = new StepCounterManager(requireContext(), totalSteps -> {

            Log.d("STEP_SENSOR", "Total Steps = " + totalSteps);
            Log.d("STEP_BASE", "Base Steps = " + baseSteps);

            if (baseSteps == -1 || totalSteps < baseSteps) {
                baseSteps = totalSteps;

                sp.edit()
                        .putInt("baseSteps", baseSteps)
                        .apply();
            }

            currentSteps = totalSteps - baseSteps;

            Log.d("STEP_CURRENT", "Current Steps = " + currentSteps);


            double calories = currentSteps * 0.04;

            int walkingMinutes = currentSteps / 100;




            step1.setText(String.valueOf(currentSteps));
            step2.setText(String.valueOf(currentSteps));

            cal1.setText(String.valueOf((int) calories));
            cal2.setText(String.valueOf((int) calories));

            time1.setText(String.valueOf(walkingMinutes+"min"));
            time2.setText(String.valueOf(walkingMinutes));

            String todayDate = new SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
            ).format(new Date());
            long currentTime = System.currentTimeMillis();

            int workoutTimeMinutes =
                    (int) ((currentTime - startTime - totalPausedTime) / 60000);
            
            Map<String, Object> dailyData = new HashMap<>();

            dailyData.put("steps", currentSteps);
            dailyData.put("calories", (int) calories);
            dailyData.put("workoutTime", workoutTimeMinutes);

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("weeklyHistory")
                    .document(todayDate)
                    .set(dailyData)
                    .addOnSuccessListener(unused -> {

                        deleteOldHistory(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    });

            if (goalSteps > 0) {

                int percent = (currentSteps * 100)/goalSteps ;

                if (percent > 100) {
                    percent = 100;
                }

                circularProgress.setProgress(percent);
                tv.setText(percent + "%");

            } else {

                circularProgress.setProgress(0);
                tv.setText("0%");
            }

        });

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(uid).get().addOnSuccessListener(document -> {
           if(document.exists()) {
               String Step1final = document.getString("Steps");
               if (Step1final != null) {
                   goalSteps = Integer.parseInt(Step1final);
               }
               String Cal1final = document.getString("Calorie");

               step1final.setText("/"+Step1final);
               cal1final.setText("/"+Cal1final);


           }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });





        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (stepCounterManager != null) {
            stepCounterManager.start();
        }

        // subtract paused time from startTime logic
        if (pauseTime != 0) {
            long resumedTime = System.currentTimeMillis();
            totalPausedTime += (resumedTime - pauseTime);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (stepCounterManager != null) {
            stepCounterManager.stop();
        }

        // record when user left app
        pauseTime = System.currentTimeMillis();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void deleteOldHistory(String uid) {

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("weeklyHistory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    Date today = new Date();
                    Calendar cal = Calendar.getInstance();

                    cal.setTime(today);
                    cal.add(Calendar.DAY_OF_YEAR, -7);
                    Date limitDate = cal.getTime();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        try {
                            Date docDate = sdf.parse(doc.getId());

                            if (docDate != null && docDate.before(limitDate)) {
                                doc.getReference().delete();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

    }
    }
