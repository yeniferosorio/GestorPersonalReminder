package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainImportante extends AppCompatActivity {
  FloatingActionButton volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_importante);

        volver=findViewById(R.id.volveratras);

        volver.setOnClickListener(v -> {
            Intent i = new Intent(MainImportante.this, FragmentActivity.class);
            startActivity(i);
            finish();
        });
    }

}