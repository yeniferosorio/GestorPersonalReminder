package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Recordatorio_registrado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText des, titu;
    FloatingActionButton guardar, volver;
    TextView fecha, hora;

    private String descrip;
    private String titulo;
    private String dates;
    private String hours;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int year;
    int month;
    int day;
    DatePickerDialog.OnDateSetListener mDateSetListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Recordatorio_registrado lista_recordatorio = (Recordatorio_registrado) getIntent().getSerializableExtra("listElement");
        titu = findViewById(R.id.editTexttitulo);
        des = findViewById(R.id.MultiLinedesc);


        guardar = findViewById(R.id.Buttonguardar);
        volver = findViewById(R.id.botonVolver);
        hora = findViewById(R.id.txtEditHora);
        fecha = findViewById(R.id.txtEditFecha);
        inicializarFirebase();

        volver.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Opcion.class);
            startActivity(intent);
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descrip = des.getText().toString();
                titulo = titu.getText().toString();
                dates = fecha.getText().toString();
                hours = hora.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();



            }
        });


        hora.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //2:6
                    String horaDelDia = String.valueOf(hourOfDay);
                    String minutos = String.valueOf(minute);
                    if (Integer.valueOf(horaDelDia) < 10) {
                        horaDelDia = "0" + horaDelDia;
                    }
                    if (Integer.valueOf(minutos) < 10) {
                        minutos = "0" + minutos;
                    }
                    hora.setText(horaDelDia + ":" + minutos);
                }
            }, currentHour, currentMinute, false);
            timePickerDialog.show();
        });


        fecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (view, year, month, dayOfMonth) -> fecha.setText(dayOfMonth + "/" + month + "/" + year);

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

}
