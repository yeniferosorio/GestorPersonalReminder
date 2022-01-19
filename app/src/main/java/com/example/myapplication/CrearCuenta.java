package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CrearCuenta extends AppCompatActivity {
    EditText edit_nombre, edit_telefono, edit_correo_Electronico, edit_contrasenia;
    TextView iniciar_sesion;
    Button crear_cuenta;

    FirebaseAuth firebaseAuth;
    DatabaseReference dr;
    FirebaseFirestore db;


    private String nombre;
    private String telefono;
    private String correo;
    private String password;
    private static final String TAG = "Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);


        edit_nombre = findViewById(R.id.EditTextNombre);
        edit_telefono = findViewById(R.id.editTextPhone);
        iniciar_sesion = findViewById(R.id.TextIniciarSesion);
        edit_correo_Electronico = findViewById(R.id.editTextTextEmailAddress);
        edit_contrasenia = findViewById(R.id.editTextTextPassword);
        crear_cuenta = findViewById(R.id.buttonCrearCuenta);
        firebaseAuth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        registrobdfirebase();
        crear_cuenta.setOnClickListener(v -> {
            nombre = edit_nombre.getText().toString();
            telefono = edit_telefono.getText().toString();
            correo = edit_correo_Electronico.getText().toString();
            password = edit_contrasenia.getText().toString();
            if (!nombre.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() && !password.isEmpty()) {
                if (password.length() >= 6) {

                    Clean();
                }
                firebaseAuth.createUserWithEmailAndPassword(correo,  password ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("nombre", nombre);
                            map.put("telefono", telefono);
                            map.put("correo Electronico", correo);
                            map.put("contraseña", encriptarPassword(password));

                            String id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            dr.child("Usuario").child(id).setValue(map).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(CrearCuenta.this, "Usuario Creado Correctamente", Toast.LENGTH_SHORT).show();
                                    Clean();
                                } else {
                                    Toast.makeText(CrearCuenta.this, "Error en la creación de tus datos intenta nuevamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(CrearCuenta.this, "Datos ya ocupados o no estan registrados por su correo Electronico correspondiente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        iniciar_sesion.setOnClickListener(v -> startActivity(new Intent(CrearCuenta.this, LoginActivity.class)));


    }


    //registros en bd firebase
    private void registrobdfirebase() {
        String nombre = edit_nombre.getText().toString();
        String telefono = edit_telefono.getText().toString();
        String correo = edit_correo_Electronico.getText().toString();
        String password = edit_contrasenia.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("telefono", telefono);
        user.put("correo ", correo);
        user.put("password", encriptarPassword(password));
        db.collection("usuarios").document().set(user);





    }

    //encriptacion de password en firebase
    private String encriptarPassword(String passwordToEncrypt) {

        MessageDigest md;
        byte[] messageDigest;
        BigInteger number;
        String encryptedPass = "";

        try {
            md = MessageDigest.getInstance("MD5");
            messageDigest = md.digest(passwordToEncrypt.getBytes());
            number = new BigInteger(1, messageDigest);
            encryptedPass = number.toString(16);
            while (encryptedPass.length() < 32) {
                encryptedPass = "0" + encryptedPass;
            }

        } catch (Exception exception) {
            System.out.println("Error encriptando clave");
        }
        return encryptedPass;
    }

    private void Clean() {
        edit_nombre.setText("");
        edit_telefono.setText("");
        edit_correo_Electronico.setText("");
        edit_contrasenia.setText("");

    }

}