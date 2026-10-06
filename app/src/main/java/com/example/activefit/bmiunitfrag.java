package com.example.activefit;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class bmiunitfrag extends Fragment {

        Button btncal;
        TextInputEditText edwe,edhe;
        TextView tvresult,tvreal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bmiunitfrag, container, false);

        btncal= (Button) view.findViewById(R.id.btncal);
        edwe= view.findViewById(R.id.edweight);
        edhe=view.findViewById(R.id.edheight);
        tvreal= (TextView) view.findViewById(R.id.tvreal);
        tvresult= (TextView) view.findViewById(R.id.tvresult);
        btncal.setOnClickListener(v -> {
            String we=edwe.getText().toString();
            String he=edhe.getText().toString();
            float hee=Float.parseFloat(he);
            float wee=Float.parseFloat(we);
            float heightInMeters = hee / 100f;
            float bmi = wee / (heightInMeters * heightInMeters);
            String catagory;
            if(bmi<18.5) {
                catagory = "Underweight";
            }  else if (bmi<25){
                catagory="Healthy";
            } else if (bmi<30) {
                catagory = "Overweight";
            }else{
                catagory="Obese";

            }
            tvresult.setText(String.format("%.2f", bmi));
            tvreal.setText(" "+catagory);

        });






        return view;
    }
}