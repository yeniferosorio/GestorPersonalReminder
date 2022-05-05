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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


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


        crear_cuenta.setOnClickListener(v -> {
            nombre = edit_nombre.getText().toString();
            telefono = edit_telefono.getText().toString();
            correo = edit_correo_Electronico.getText().toString();
            password = edit_contrasenia.getText().toString();
            if (!nombre.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() && !password.isEmpty() && password.length() >= 6) {


                firebaseAuth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dr.child("usuarios").push().setValue(registrobdfirebase());
                            Toast.makeText(CrearCuenta.this, "Usuario Creado Correctamente", Toast.LENGTH_SHORT).show();
                            Clean();
                            finish();
                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CrearCuenta.this, "Ya existe una cuenta para el correo electrónico ingresado, pruebe con otro correo", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        iniciar_sesion.setOnClickListener(v -> startActivity(new Intent(CrearCuenta.this, LoginActivity.class)));


    }


    //registros en bd firebase
    private Map registrobdfirebase() {
        String nombre = edit_nombre.getText().toString();
        String telefono = edit_telefono.getText().toString();
        String correo = edit_correo_Electronico.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("telefono", telefono);
        user.put("correo", correo);

        db.collection("usuarios").document(firebaseAuth.getCurrentUser().getUid()).set(user);

        return user;

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