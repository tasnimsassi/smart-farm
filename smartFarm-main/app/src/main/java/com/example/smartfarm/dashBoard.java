package com.example.smartfarm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.*;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.*;

import java.util.Map;


public class dashBoard extends AppCompatActivity {

    private Button btnLedControl, btnArrosage;
//    FirebaseMessaging firebaseMessaging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.dashboard);
        //start the service
//        Intent serviceIntent = new Intent(this, myService.class);
//        startService(serviceIntent);


        // Références des éléments de l'interface utilisateur
        btnLedControl = findViewById(R.id.btnLedControl);
        btnArrosage = findViewById(R.id.btnArrosage);
        // Ajoutez un écouteur de clic au bouton pour LedControlActivity
        btnLedControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashBoard.this, LedControlActivity.class));
            }
        });

        // Ajoutez un écouteur de clic au bouton pour ArrosageActivity
        btnArrosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashBoard.this, Arrosage.class));
            }
        });


        TextView textViewTemperature = findViewById(R.id.Temperature);
        TextView textViewPluie = findViewById(R.id.Pluie);
        TextView textViewHumidite = findViewById(R.id.Humidite);
        TextView textViewLumiere = findViewById(R.id.Lumiere);
        TextView textViewGaz = findViewById(R.id.Gaz);
        TextView textViewMotion = findViewById(R.id.Motion);

        DatabaseReference firebaseDatabase;
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String temperature = dataSnapshot.child("sensorData/humidity/temperature").getValue(String.class);
                    String humidite = dataSnapshot.child("sensorData/humidity/humidity").getValue(String.class);
                    String pluie = dataSnapshot.child("moistureData/pluie/pluie").getValue(String.class);
                    String lumiere = dataSnapshot.child("lightData/lightValue/lightValue").getValue(String.class);
                    String gaz = dataSnapshot.child("gasData/gasData/gasValue").getValue(String.class);
                    Boolean motion = dataSnapshot.child("motionData/motionDetected").getValue(Boolean.class);


                    if (temperature != null && humidite != null && pluie != null && lumiere != null && gaz != null) {
                        textViewTemperature.setText(temperature);
                        textViewPluie.setText(pluie);
                        textViewHumidite.setText(humidite);
                        textViewLumiere.setText(lumiere);
                        textViewGaz.setText(gaz);
                        if (motion) {
                            textViewMotion.setText("Mouvement detecté !");
                            showNotification("attention", "on a detecter un mouvement dans votre ferme", dashBoard.this);
                        } else {
                            textViewMotion.setText("Pas de mouvement !");
                        }
                    } else {
                        // Handle null values if needed
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log any exceptions for debugging
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel-03",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Your channel description");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String message, Activity activity) {
        Intent resultIntent = new Intent(activity, dashBoard.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(activity, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, "channel-03")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText("Tap to open the app")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());    }


}