package com.example.smartfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class SignUp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up);

         FirebaseAuth auth;
//    private EditText username = (EditText) findViewById(R.id.username);
         EditText email = (EditText) findViewById(R.id.email);
         EditText password = (EditText) findViewById(R.id.password);
         MaterialButton submit = (MaterialButton) findViewById(R.id.submit);
         TextView login = (TextView) findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                String eMail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(eMail.isEmpty())
                    email.setError("Email cannot be empty !");
                if(pass.isEmpty())
                    password.setError("Password cannot be empty !");
                else{
                    auth.createUserWithEmailAndPassword(eMail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "SignUp successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, LogIn.class));
                            }
                            else {
                                Toast.makeText(SignUp.this, "Failed to signup !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });

    }
}