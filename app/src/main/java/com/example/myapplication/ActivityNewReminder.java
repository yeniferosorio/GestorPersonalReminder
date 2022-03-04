package com.example.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.model.NotificationID;
import com.example.myapplication.model.OnReminderReceiver;
import com.example.myapplication.model.Recordatorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityNewReminder extends AppCompatActivity {

    EditText descripcion, titulo;
    FloatingActionButton guardar, volver;
    TextView fecha, hora;
    ImageView notify;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TimePickerDialog timePickerDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Calendar calendar;
    int currentHour;
    int currentMinute;
    int year;
    int month;
    int day;




    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ArrayList<Recordatorio> lista_recordatorio;
    private ArrayAdapter<Recordatorio> arrayAdapterRecordatorio;
    private static final String TAG = "Recordatorio";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
        inicializarFirebase();
        //esta es la referencia del documento(id) del usuario
        String userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference userReference = db.collection("usuarios").document(userId);
        titulo = findViewById(R.id.editTexttitulo);
        descripcion = findViewById(R.id.MultiLinedesc);
        fecha = findViewById(R.id.txtEditFecha);
        hora = findViewById(R.id.txtEditHora);
        guardar = findViewById(R.id.buttonguardar);
        volver = findViewById(R.id.buttonVolver);
        notify = findViewById(R.id.notificacion);


        volver.setOnClickListener(v -> {
            Intent i = new Intent(ActivityNewReminder.this, Navigation_Midia.class);
            startActivity(i);
            finish();
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {


                Map<String, Object> recordatorio = new HashMap<>();
                recordatorio.put("titulo", titulo.getText().toString());
                recordatorio.put("descripcion", descripcion.getText().toString());
                recordatorio.put("fecha", fecha.getText().toString());
                recordatorio.put("hora", hora.getText().toString());
                userReference.collection("recordatorio").document().set(recordatorio);

                String time = fecha.getText().toString().replace("/"," ") + " " + hora.getText().toString().replace(":"," "); // Results in "2-5-2012 20:43"
                String[] splittedTime = time.split(" ");

                Calendar calendar = Calendar.getInstance();

                calendar.set(Integer.valueOf(splittedTime[2]),(Integer.valueOf(splittedTime[1])-1),Integer.valueOf(splittedTime[0]),Integer.valueOf(splittedTime[3]),Integer.valueOf(splittedTime[4]),00);

                createNotificationChannel();
                crearNotificacion(calendar, titulo.getText().toString(), descripcion.getText().toString());


                Intent i = new Intent(ActivityNewReminder.this, FragmentActivity.class);
                startActivity(i);
                finish();

            }

            private void crearNotificacion(Calendar calendar, String textTitle, String textContent) {

                Intent intent = new Intent(getApplicationContext(), OnReminderReceiver.class);
                intent.putExtra("title",textTitle);
                intent.putExtra("content",textContent);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),NotificationID.getID(),intent,0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent);



            }
            private void createNotificationChannel() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String name = String.valueOf(NotificationID.getActualID());
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(String.valueOf(NotificationID.getActualID()), name, importance);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
            }

        });


        hora.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(ActivityNewReminder.this, new TimePickerDialog.OnTimeSetListener() {
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

            DatePickerDialog dialog = new DatePickerDialog(ActivityNewReminder.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (view, year, month, dayOfMonth) -> fecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);

    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }


}
