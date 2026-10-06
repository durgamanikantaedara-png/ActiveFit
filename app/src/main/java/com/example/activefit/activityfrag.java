package com.example.activefit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;


public class activityfrag extends Fragment {

    private BarChart barChart;
    private TextView tvSteps;
    private TextView tvCalories;

    private TextView tvSelectedDay;
    private ArrayList<DocumentSnapshot> historyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_activityfrag, container, false);

        barChart = view.findViewById(R.id.barChart);



        tvSelectedDay = view.findViewById(R.id.tvSelectedDay);
        tvSteps = view.findViewById(R.id.tvSummarySteps);
        tvCalories = view.findViewById(R.id.tvSummaryCalories);


        loadWeeklyChart();





        return view;
    }

    private void loadWeeklyChart() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("weeklyHistory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    ArrayList<BarEntry> entries = new ArrayList<>();

                    ArrayList<String> days = new ArrayList<>();

                    int index = 0;

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        historyList.add(doc);

                        Long steps = doc.getLong("steps");

                        if (steps != null) {
                            entries.add(new BarEntry(index, steps.floatValue()));
                        }

                        String date = doc.getId();

                        try {

                            SimpleDateFormat inputFormat =
                                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                            SimpleDateFormat outputFormat =
                                    new SimpleDateFormat("EEE", Locale.getDefault());

                            Date d = inputFormat.parse(date);

                            if (d != null) {
                                days.add(outputFormat.format(d));
                            } else {
                                days.add(date);
                            }

                        } catch (Exception e) {
                            days.add(date);
                        }

                        index++;
                    }

                    BarDataSet dataSet = new BarDataSet(entries, "");

                    dataSet.setColor(Color.parseColor("#7B61FF"));
                    dataSet.setValueTextSize(12f);
                    dataSet.setDrawValues(false);

                    BarData data = new BarData(dataSet);
                    data.setBarWidth(0.5f);

                    barChart.setData(data);
                    XAxis xAxis = barChart.getXAxis();

                    xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setGranularityEnabled(true);

                    barChart.getDescription().setEnabled(false);
                    barChart.getLegend().setEnabled(false);
                    barChart.getAxisRight().setEnabled(false);

                    barChart.getAxisLeft().setDrawGridLines(false);
                    barChart.getXAxis().setDrawGridLines(false);

                    barChart.animateY(1000);
                    barChart.invalidate();
                    if (!historyList.isEmpty()) {

                        DocumentSnapshot latestDoc =
                                historyList.get(historyList.size() - 1);

                        updateSummary(latestDoc);

                    }

                    barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                        @Override
                        public void onValueSelected(Entry e, Highlight h) {

                            int position = Math.round(e.getX());

                            if (position < 0 || position >= historyList.size()) {
                                return;
                            }

                            DocumentSnapshot doc = historyList.get(position);
                            updateSummary(doc);
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

                });


    }

    private void updateSummary(DocumentSnapshot doc) {

        Long steps = doc.getLong("steps");
        Long calories = doc.getLong("calories");


        tvSelectedDay.setText(doc.getId());

        tvSteps.setText("👟 Steps : " + steps);
        tvCalories.setText("🔥 Calories : " + calories + " kcal");

    }
}