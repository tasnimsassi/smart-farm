package com.example.smartfarm;

// MainActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Arrosage extends AppCompatActivity {

    private DatabaseReference wateringStatusRef;
    private Switch btnToggleWatering, btnOuvrirRobinet;
    private DatabaseReference robinetStatusRef;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrosage);


        btnOuvrirRobinet = findViewById(R.id.btnOuvrirRobinet);
        btnToggleWatering = findViewById(R.id.btnToggleWatering);
        btnBack = findViewById(R.id.Back);

        // Obtenez une référence à l'état de l' dans Firebase
        wateringStatusRef = FirebaseDatabase.getInstance().getReference().child("wateringStatus");
        // Obtenez une référence à l'état du robinet dans Firebase
        robinetStatusRef = FirebaseDatabase.getInstance().getReference().child("pumpStatus");

        DatabaseReference firebaseDatabase;
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        TextView textViewTemperature = findViewById(R.id.temperature);
        TextView textViewHumidite = findViewById(R.id.humidite);
        TextView textViewArrosageVrai = findViewById(R.id.arrosageVrai);
        TextView textViewArrosageFaux = findViewById(R.id.arrosageFaux);
        TextView textViewEauVrai = findViewById(R.id.eauVrai);
        TextView textViewEauFaux = findViewById(R.id.eauFaux);
        TextView textViewHappyAnimal = findViewById(R.id.happyAnimal);
        TextView textViewSadAnimal = findViewById(R.id.sadAnimal);
        TextView textViewHappyPlant = findViewById(R.id.happyPlant);
        TextView textViewSadPlant = findViewById(R.id.sadPlant);
        TextView textViewRobinetEnCours = findViewById(R.id.RobinetEnCours);
        TextView textViewArrosageEnCours = findViewById(R.id.ArrosageEnCours);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temperature = dataSnapshot.child("sensorData/humidity/temperature").getValue(String.class);
                String humidite = dataSnapshot.child("sensorData/humidity/humidity").getValue(String.class);
                Boolean WateringStatus = dataSnapshot.child("wateringStatus").getValue(Boolean.class);
                Boolean RobinetStatus = dataSnapshot.child("pumpStatus").getValue(Boolean.class);

                textViewTemperature.setText(temperature);
                textViewHumidite.setText(humidite);

                if(WateringStatus){
                    textViewArrosageVrai.setVisibility(View.GONE);
                    textViewArrosageFaux.setVisibility(View.GONE);
                    textViewHappyPlant.setVisibility(View.GONE);
                    textViewSadPlant.setVisibility(View.GONE);
                    textViewArrosageEnCours.setVisibility(View.VISIBLE);
                }
                else if (Double.parseDouble(humidite) > 50) {
                    textViewArrosageVrai.setVisibility(View.VISIBLE);
                    textViewArrosageFaux.setVisibility(View.GONE);
                    textViewHappyPlant.setVisibility(View.GONE);
                    textViewSadPlant.setVisibility(View.VISIBLE);
                    textViewArrosageEnCours.setVisibility(View.GONE);
                } else {
                    textViewArrosageVrai.setVisibility(View.GONE);
                    textViewArrosageFaux.setVisibility(View.VISIBLE);
                    textViewHappyPlant.setVisibility(View.VISIBLE);
                    textViewSadPlant.setVisibility(View.GONE);
                    textViewArrosageEnCours.setVisibility(View.GONE);
                }
                if(RobinetStatus){
                    textViewEauVrai.setVisibility(View.GONE);
                    textViewEauFaux.setVisibility(View.GONE);
                    textViewHappyAnimal.setVisibility(View.GONE);
                    textViewSadAnimal.setVisibility(View.GONE);
                    textViewRobinetEnCours.setVisibility(View.VISIBLE);
                }
                else if (Double.parseDouble(temperature) > 35) {
                    textViewEauVrai.setVisibility(View.VISIBLE);
                    textViewEauFaux.setVisibility(View.GONE);
                    textViewHappyAnimal.setVisibility(View.GONE);
                    textViewSadAnimal.setVisibility(View.VISIBLE);
                    textViewRobinetEnCours.setVisibility(View.GONE);
                } else {
                    textViewEauVrai.setVisibility(View.GONE);
                    textViewEauFaux.setVisibility(View.VISIBLE);
                    textViewHappyAnimal.setVisibility(View.VISIBLE);
                    textViewSadAnimal.setVisibility(View.GONE);
                    textViewRobinetEnCours.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });




        //display the switch toggleWatering according the its value in the data base (on for true and off for false)
        wateringStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean toggleStatus = dataSnapshot.getValue(Boolean.class);
                if (toggleStatus != null) {
                    btnToggleWatering.setChecked(toggleStatus);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });



        //display the switch toggleRobinet according the its value in the data base (on for true and off for false)
        robinetStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean toggleStatus = dataSnapshot.getValue(Boolean.class);
                if (toggleStatus != null) {
                    btnOuvrirRobinet.setChecked(toggleStatus);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });




        // Ajoutez un écouteur de clic au bouton watering plants
        btnToggleWatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inversez l'état de l' dans Firebase
                toggleWateringStatus();
            }
        });


        // Ajoutez un écouteur de clic au bouton de retour
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Démarrer l'activité du tableau de bord (dashboard)
                Intent intent = new Intent(Arrosage.this, dashBoard.class);
                startActivity(intent);
                finish(); // Terminez cette activité
            }
        });



        // Ajoutez un écouteur de clic au bouton robinet animaux
        btnOuvrirRobinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inversez l'état du robinet dans Firebase
                toggleRobinetStatus();
            }
        });

    }

    private void toggleWateringStatus() {
        // Obtenez l'état actuel de l' dans Firebase
        wateringStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Inversez l'état (true -> false, false -> true)
                Boolean currentStatus = dataSnapshot.getValue(Boolean.class);
                if (currentStatus != null) {
                    boolean newStatus = !currentStatus;
                    // Mettez à jour l'état de l' dans Firebase
                    wateringStatusRef.setValue(newStatus);
                    if(newStatus)
                        Toast.makeText(Arrosage.this, "Arrosage is On", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Arrosage.this, "Arrosage is Off", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs
            }
        });
    }

    private void toggleRobinetStatus() {
        // Obtenez l'état actuel du robinet dans Firebase
        robinetStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Inversez l'état (true -> false, false -> true)
                Boolean currentStatus = dataSnapshot.getValue(Boolean.class);
                if (currentStatus != null) {
                    boolean newStatus = !currentStatus;
                    // Mettez à jour l'état du robinet dans Firebase
                    robinetStatusRef.setValue(newStatus);
                    if(newStatus)
                        Toast.makeText(Arrosage.this, "Robinet is On", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Arrosage.this, "Robinet is Off", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gestion des erreurs
            }
        });
    }
}
