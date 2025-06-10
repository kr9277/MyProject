package com.example.myproject;

import static com.example.myproject.FBref.refFamily;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "Alarm received");
        Log.i("AlarmReceiver", "intent: " + intent);
        Log.i("AlarmReceiver", "Extras: " + intent.getExtras());

        String tId = intent.getStringExtra("tId");
        String fId = intent.getStringExtra("fId");
        Log.i("AlarmReceiver", "Extracted tId: " + tId);
        Log.i("AlarmReceiver", "Extracted fId: " + fId);
        for (String key : intent.getExtras().keySet()) {
            Log.i("AlarmReceiver", "Intent extra key: " + key + " = " + intent.getExtras().get(key));
        }

        Toast.makeText(context, "tId = " + tId, Toast.LENGTH_SHORT).show();
        if (fId != null && tId != null){
            Toast.makeText(context, "not null", Toast.LENGTH_SHORT).show();
            DatabaseReference ref = refFamily.child(fId).child("currentFamilyTasks").child(tId);
            Toast.makeText(context, "ref = " + ref, Toast.LENGTH_SHORT).show();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.i("AlarmReceiver", "Task exists");
                        Toast.makeText(context, "Task exists", Toast.LENGTH_SHORT).show();
                        Task task = snapshot.getValue(Task.class);
                        if (task != null) {
                            Log.i("AlarmReceiver", "Task description: " + task.getDisc());
                            Toast.makeText(context, "call Showing notification", Toast.LENGTH_SHORT).show();
                            showNotification(context, task.getDisc());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("AlarmReceiver", "Database error: " + error.getMessage());
                    Toast.makeText(context, "Eror", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void showNotification(Context context, String description) {
        Toast.makeText(context, "Showing notification", Toast.LENGTH_SHORT).show();
        try {
            if (context != null) {
                Log.i("showNotification try", "showNotification try");
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    String channelId = "task_over_channel";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(
                                channelId, "משימות שפג תוקפן", NotificationManager.IMPORTANCE_HIGH
                        );
                        channel.setDescription("התראות על משימות שלא בוצעו בזמן");
                        notificationManager.createNotificationChannel(channel);

                        Log.i("Notification", "Notification channel created");
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(android.R.drawable.ic_dialog_alert)
                            .setContentTitle("משימה לא הושלמה בזמן")
                            .setContentText(description)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                    notificationManager.notify((int) System.currentTimeMillis(), builder.build());
                }

            }

        } catch (Exception e) {
            Toast.makeText(context, "Eror catch", Toast.LENGTH_SHORT).show();
            Log.e("Notification", "Failed to show notification", e);
        }
    }
}