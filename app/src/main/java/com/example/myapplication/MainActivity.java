package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.myapplication.model.Recordatorio_registrado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView des ,titu;
    FloatingActionButton editar,volver;
    EditText fecha, hora;

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

       Recordatorio_registrado lista_recordatorio = (Recordatorio_registrado)getIntent().getSerializableExtra("listElement");
        titu=findViewById(R.id.textViewTitulo);
        des=findViewById(R.id.TextDescripcion);


        titu.setText(lista_recordatorio.getTitulo());
        des.setText(lista_recordatorio.getDescripcion());

        editar=findViewById(R.id.ButtonEditar);
        volver=findViewById(R.id.botonVolver);
        hora= findViewById(R.id.editTextTime);
        fecha=findViewById(R.id.editTextDate);
        inicializarFarebase();

        volver.setOnClickListener(v -> {
            Intent intent= new Intent(MainActivity.this,Opcion.class);
            startActivity(intent);
        });


      hora.setOnClickListener(v -> {
          calendar=Calendar.getInstance();
          currentHour=calendar.get(Calendar.HOUR_OF_DAY);
          currentMinute=calendar.get(Calendar.MINUTE);

          timePickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                  hora.setText(hourOfDay+":"+minute);
              }
          },currentHour,currentMinute,false);
          timePickerDialog.show();
      });

      fecha.setOnClickListener(v -> {
          Calendar calendar=Calendar.getInstance();
          year=calendar.get(Calendar.YEAR);
          month=calendar.get(Calendar.MONTH);
          day=calendar.get(Calendar.DAY_OF_MONTH);

          DatePickerDialog dialog=new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
          dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          dialog.show();
      });
      mDateSetListener= (view, year, month, dayOfMonth) -> fecha.setText(year+"/"+month+"/"+dayOfMonth);

    }

    private void inicializarFarebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();

    }

}
