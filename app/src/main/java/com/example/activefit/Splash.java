package com.example.activefit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
        Button btn;
        FirebaseAuth aAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash);
        btn= (Button) findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            Intent i=new Intent(Splash.this,MainActivity.class);
            startActivity(i);
            finish();
        });

        if(aAuth.getCurrentUser()!=null){
            Intent i=new Intent(Splash.this,HomePage.class);
            startActivity(i);
            finish();
        }




}}