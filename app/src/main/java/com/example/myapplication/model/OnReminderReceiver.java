package com.example.myapplication.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;

public class OnReminderReceiver extends BroadcastReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra("title");
        String contenido = intent.getStringExtra("content");


        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, String.valueOf(NotificationID.getActualID()))
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(NotificationID.getActualID(), nBuilder.build());

    }



}
