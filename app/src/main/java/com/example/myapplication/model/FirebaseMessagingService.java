package com.example.myapplication.model;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.e("FIREBASE",remoteMessage.getNotification().getBody());


    }
}
