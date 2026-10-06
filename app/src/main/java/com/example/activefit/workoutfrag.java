package com.example.activefit;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class workoutfrag extends Fragment {


    upperbodyfrag upperbodyfrag =new upperbodyfrag();

    lowerbodyfrag lowerbodyfrag = new lowerbodyfrag();
    corebodyfrag corebodyfrag=new corebodyfrag();
    yogabodyfrag yogabodyfrag=new yogabodyfrag();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_workoutfrag,container,false);

        CardView cardupperbody= (CardView) view.findViewById(R.id.cardupperbody);
        cardupperbody.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.container, upperbodyfrag).addToBackStack(null).commit();
        });
        CardView cardlowerbody= (CardView) view.findViewById(R.id.cardlowerbody);
        cardlowerbody.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.container,lowerbodyfrag).addToBackStack(null).commit();
    });
        CardView cardcorebody= (CardView) view.findViewById(R.id.cardcorebody);
        cardcorebody.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.container,corebodyfrag).addToBackStack(null).commit();
        });
        CardView cardyoga= (CardView) view.findViewById(R.id.cardyoga);
        cardyoga.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.container,yogabodyfrag).addToBackStack(null).commit();
        });


        return view;
    }
    }
