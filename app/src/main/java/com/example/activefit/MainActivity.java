package com.example.activefit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText edmail,edpass;
    Button btnlogin,btnregister,btnforgot;
    FirebaseAuth aAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        aAuth=FirebaseAuth.getInstance();
       edmail= (EditText) findViewById(R.id.edmail);
       edpass= (EditText) findViewById(R.id.edpass);
       btnregister= (Button) findViewById(R.id.btnregister);
       btnregister.setOnClickListener(v -> {
           Intent i=new Intent(MainActivity.this,RegisterPage.class);
           startActivity(i);

       });
       btnforgot= (Button) findViewById(R.id.btnforgot);
        btnforgot.setOnClickListener(v -> {
            Intent i=new Intent(MainActivity.this, ForgotPassword.class);
            startActivity(i);

        });
       btnlogin= (Button) findViewById(R.id.btnlogin);
       btnlogin.setOnClickListener(v -> {
           String email=edmail.getText().toString().trim();
           String pass=edpass.getText().toString().trim();
           if(email.isEmpty()){
               edmail.setError("please enter email");
               edmail.requestFocus();
               return;
           }
           if(pass.isEmpty()){
               edpass.setError("please enter password");
               edpass.requestFocus();
               return;
           }
           aAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(this, "Login sucessful", Toast.LENGTH_SHORT).show();
                   Intent i=new Intent(MainActivity.this,HomePage.class);
                   startActivity(i);
                    finish();
               } else {
                   Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
       });

        }
    }
