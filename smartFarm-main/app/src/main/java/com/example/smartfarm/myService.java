package com.example.smartfarm;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class myService extends Service {
    int notificationId = 3;
    String channelId = "channel-03";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;

    ValueEventListener listener;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("sensorData/humidity/temperature");
    public void sendmessage(String s) {
        Intent intent = new Intent("service_message");
        intent.putExtra("message_key",s);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        myRef.removeEventListener(listener);
        Toast.makeText(getApplicationContext(),"Stop service", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onCreate() {
        final Intent intent=new Intent(getApplicationContext(), com.example.smartfarm.myService.class);

        //   editTextValue =prefs.getString("Key", "1000");
        //showNotification(getApplicationContext(), "Alert", "hacking begins" , intent);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming temperature is stored as a numeric value in the database
                    double temperature = dataSnapshot.getValue(Double.class);
                    if (temperature > 15.0) {
                        showNotification(getApplicationContext(),"Temperature Alert", "Temperature exceeds 15°C!",intent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                /* .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle(title)
                 .setContentText(body);*/
                .setSmallIcon(R.drawable.notification)     // drawable for API 26
                .setAutoCancel(true)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentText(body)
                .setVibrate(new long[] { 0, 500, 110, 500, 110, 450, 110, 200, 110,
                        170, 40, 450, 110, 200, 110, 170, 40, 500 } )
                .setLights(Color.RED, 3000, 3000);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(resultPendingIntent);
        startForeground(notificationId, mBuilder.build());
        //   stopForeground(false);
        // notificationManager.notify(notificationId, mBuilder.build());
    }
}