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

public class RegisterPage extends AppCompatActivity {
    EditText edrmail,edrpass,edrpass2;
    Button btrregister;
    FirebaseAuth aAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register_page);
       edrmail= (EditText) findViewById(R.id.edrmail);
       edrpass= (EditText) findViewById(R.id.edrpass);
       edrpass2= (EditText) findViewById(R.id.edrpass2);
       btrregister= (Button) findViewById(R.id.btrregister);
       btrregister.setOnClickListener(v -> {
           String email=edrmail.getText().toString();
           String pass=edrpass.getText().toString();
           String pass2=edrpass2.getText().toString();
           if(TextUtils.isEmpty(email)){
               edrmail.setError("please enter email");
               edrmail.requestFocus();
               return;
           }
           if(pass.isEmpty()){
               edrpass.setError("please enter password");
               edrpass.requestFocus();
               return;
           }
           if(pass2.isEmpty()){
               edrpass2.setError("please re-enter password");
               edrpass2.requestFocus();
               return;
           }
           if(!pass.equals(pass2)){
               edrpass2.setError("check the re-entered password");
               edrpass2.requestFocus();
               return;
           }

           aAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(this, "Registered sucessfully", Toast.LENGTH_SHORT).show();
                   Intent i=new Intent(RegisterPage.this,MainActivity.class);
                   startActivity(i);
                   finish();
               }
               else{
                   Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
       });
    }
    
}