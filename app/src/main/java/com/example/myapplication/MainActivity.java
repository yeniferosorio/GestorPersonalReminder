package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView des ,titu;
    FloatingActionButton editar, eliminar,volver;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Recordatorio_registrado lista_recordatorio = (Recordatorio_registrado)getIntent().getSerializableExtra("listElement");
        titu=findViewById(R.id.textViewTitulo);
        des=findViewById(R.id.TextDescripcion);


        titu.setText(lista_recordatorio.getTitulo());
        des.setText(lista_recordatorio.getDescripcion());

        editar=findViewById(R.id.ButtonEditar);
        eliminar=findViewById(R.id.buttonEliminar);
        volver=findViewById(R.id.botonVolver);
        inicializarFarebase();




    }

    private void inicializarFarebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();

    }




}
