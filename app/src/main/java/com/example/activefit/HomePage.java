package com.example.activefit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class HomePage extends AppCompatActivity {
    BottomNavigationView bottomNav;
    homefrag homefrag=new homefrag();
    workoutfrag workoutfrag=new workoutfrag();
    settingfrag settingfrag=new settingfrag();
    profilefrag profilefrag=new profilefrag();
    activityfrag activityfrag=new activityfrag();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
        bottomNav= (BottomNavigationView) findViewById(R.id.bottomNav);
       getSupportFragmentManager().beginTransaction().replace(R.id.container,profilefrag).commit();
       bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homefrag).commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_workout) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,workoutfrag).commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_activity) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,activityfrag).commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profilefrag).commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_settings) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,settingfrag).commit();
                return true;
            }

            return false;
        });
    }


}