package com.example.activefit;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class settingfrag extends Fragment {

    Button btlogout;
    FirebaseAuth aAuth;
    CardView btnunits,about,cardhelp;
    bmiunitfrag bmiunitfrag=new bmiunitfrag();
    helpfrag helpfrag=new helpfrag();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settingfrag, container, false);
        btnunits= (CardView) view.findViewById(R.id.btnunits);
        cardhelp= (CardView) view.findViewById(R.id.cardhelp);
        btnunits.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().beginTransaction().replace(R.id.container,bmiunitfrag).addToBackStack(null).commit();
        });
        aAuth=FirebaseAuth.getInstance();
        btlogout= (Button) view.findViewById(R.id.btlogout);
        btlogout.setOnClickListener(v -> {
            aAuth.signOut();
            Intent i=new Intent(getActivity(),MainActivity.class);
            startActivity(i);
            requireActivity().finish();
        });
        about= (CardView) view.findViewById(R.id.about);
        about.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Version:2.O", Toast.LENGTH_SHORT).show();
        });
        cardhelp.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.container,helpfrag).addToBackStack(null).commit();
        });


        return view;
    }
}