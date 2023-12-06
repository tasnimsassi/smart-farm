package com.example.smartfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;git remote add origin https://github.com/tasnimsassi/smartFarm.git

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        auth = FirebaseAuth.getInstance();
        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);
        MaterialButton login = (MaterialButton) findViewById(R.id.login);
        TextView signup = (TextView) findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail = email.getText().toString();
                String pass = password.getText().toString();

                if(!eMail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(eMail).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(eMail, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LogIn.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LogIn.this, dashBoard.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LogIn.this, "FAILURE !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else
                        password.setError("Password cannot be empty !");
                }
                    else if(eMail.isEmpty())
                        email.setError("Email cannot be empty !");
                    else
                        email.setError("Please enter valid email !");
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}