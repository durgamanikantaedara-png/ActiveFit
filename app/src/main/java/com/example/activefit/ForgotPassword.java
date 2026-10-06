package com.example.activefit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText edfmail;
    Button btfforgot,btncancel;
    FirebaseAuth aAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_forgot_password);
       edfmail= (EditText) findViewById(R.id.edfemail);
       btfforgot= (Button) findViewById(R.id.btfforgot);
       btncancel= (Button) findViewById(R.id.btncancel);
       btfforgot.setOnClickListener(v -> {
           String email=edfmail.getText().toString();
           if(TextUtils.isEmpty(email)){
               edfmail.setError("Enter your email");
               edfmail.requestFocus();
               return;
           }
           aAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(this, "Reset message sent to your email", Toast.LENGTH_LONG).show();
                   Intent i=new Intent(ForgotPassword.this, MainActivity.class);
                   startActivity(i);
                   finish();
               }
               else {
                   Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
       });
       btncancel.setOnClickListener(v -> {
           Intent i=new Intent(ForgotPassword.this,MainActivity.class);
           startActivity(i);
           finish();
       });
    }
}