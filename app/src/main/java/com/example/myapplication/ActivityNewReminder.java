package com.example.myapplication;

import android.app.DatePickerDialog;
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

    String channelID = "channelID";
    String channelName = "channelName";

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


                String toParse = fecha.getText().toString().replace('/', '-') + " " + hora.getText().toString(); // Results in "2-5-2012 20:43"
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                Date date = null;
                try {
                    date = formatter.parse(toParse);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long horaNotificacion = date.getTime();

                createNotificationChannel();

                crearNotificacion(horaNotificacion, titulo.getText().toString(), descripcion.getText().toString(), getApplicationContext());
                Intent i = new Intent(ActivityNewReminder.this, FragmentActivity.class);
                startActivity(i);
                finish();


            }

            private void crearNotificacion(long notificationTime, String textTitle, String textContent, Context applicationContext) {

                NotificationCompat.Builder notification = new NotificationCompat.Builder(applicationContext,channelID);
                notification.setSmallIcon(R.drawable.ic_notifications_black_24dp);
                notification.setTicker("New Personal Reminder notification");
                notification.setWhen(notificationTime);
                notification.setContentTitle(textTitle);
                notification.setContentText(textContent);

                Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                notification.setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(0, notification.build());


            }
            private void createNotificationChannel() {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String name = channelName;
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(channelID, name, importance);
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
        mDateSetListener = (view, year, month, dayOfMonth) -> fecha.setText(dayOfMonth + "/" + month + "/" + year);

    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }


}
