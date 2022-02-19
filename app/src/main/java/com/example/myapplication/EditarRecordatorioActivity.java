package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Recordatorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditarRecordatorioActivity extends AppCompatActivity {

    EditText editdescripcion, edittitulo;
    FloatingActionButton buttonguardar, buttonvolver, buttonEliminar;
    TextView fecha, hora;
    private Calendar calendar;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ArrayList<Recordatorio> lista_recordatorio;
    private ArrayAdapter<Recordatorio> arrayAdapterRecordatorio;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    int currentHour;
    int currentMinute;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_recordatorio);
        inicializarFirebase();
        String userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference userReference = db.collection("usuarios").document(userId);
        String[] r = getIntent().getStringArrayExtra("item");
        edittitulo = findViewById(R.id.edittitulo);
        editdescripcion = findViewById(R.id.EditDescripcion);
        fecha = findViewById(R.id.txtEditFecha);
        hora = findViewById(R.id.txtEditHora);
        buttonguardar = findViewById(R.id.Buttonguardar);
        buttonvolver = findViewById(R.id.buttonVolver);
        buttonEliminar = findViewById(R.id.ButtonEliminar);

        buttonvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditarRecordatorioActivity.this, FragmentActivity.class);
                startActivity(i);
                finish();
            }
        });
        buttonguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> recordatorio = new HashMap<>();
                recordatorio.put("titulo", edittitulo.getText().toString());
                recordatorio.put("descripcion", editdescripcion.getText().toString());
                recordatorio.put("fecha", fecha.getText().toString());
                recordatorio.put("hora", hora.getText().toString());
                userReference.collection("recordatorio").document(r[0]).update(recordatorio);
                Intent i = new Intent(EditarRecordatorioActivity.this, FragmentActivity.class);
                startActivity(i);
                finish();
            }
        });
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditarRecordatorioActivity.this);
                builder.setMessage("¿Seguro que desea eliminar el recordatorio?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userReference.collection("recordatorio").document(r[0]).delete();
                        Toast.makeText(EditarRecordatorioActivity.this, "Recordatorio eliminado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarRecordatorioActivity.this, FragmentActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();


            }
        });

        hora.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(EditarRecordatorioActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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

            DatePickerDialog dialog = new DatePickerDialog(EditarRecordatorioActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (view, year, month, dayOfMonth) -> fecha.setText(dayOfMonth + "/" + month + "/" + year);


        // AQUÍ SE SETEAN LOS DATOS DEL ITEM


        edittitulo.setText(r[1]);
        editdescripcion.setText(r[2]);
        fecha.setText(r[3]);
        hora.setText(r[4]);


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }


}