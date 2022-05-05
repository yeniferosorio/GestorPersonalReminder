package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.goodiebag.pinview.Pinview;

public class PingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);


        Pinview pinview=(Pinview) findViewById(R.id.pinView);
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                Toast.makeText(PingActivity.this, ""+pinview.getValue(), Toast.LENGTH_SHORT).show();
               // Intent intent = new Intent(PingActivity.this, Importante.class);
               // startActivity(intent);
            }
        });
    }
}