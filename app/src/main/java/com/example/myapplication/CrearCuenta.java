package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.conexion.ConexionSQLite;
import com.example.myapplication.conexion.Utilidades;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CrearCuenta extends AppCompatActivity {
    EditText edit_nombre,edit_telefono,edit_correo_Electronico,edit_contrasenia;
    TextView iniciar_sesion;
    Button crear_cuenta;

    FirebaseAuth firebaseAuth;
    DatabaseReference dr;


    private String nombre;
    private String telefono;
    private String correo;
    private String contrasenia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);


        edit_nombre=findViewById(R.id.EditTextNombre);
        edit_telefono=findViewById(R.id.editTextPhone);
        iniciar_sesion=findViewById(R.id.TextIniciarSesion);
        edit_correo_Electronico=findViewById(R.id.editTextTextEmailAddress);
        edit_contrasenia=findViewById(R.id.editTextTextPassword);
        crear_cuenta=findViewById(R.id.buttonCrearCuenta);
        firebaseAuth = FirebaseAuth.getInstance();
        dr= FirebaseDatabase.getInstance().getReference();


        crear_cuenta.setOnClickListener(v -> {
            nombre=edit_nombre.getText().toString();
            telefono=edit_telefono.getText().toString();
            correo=edit_correo_Electronico.getText().toString();
            contrasenia=edit_contrasenia.getText().toString();
            if(!nombre.isEmpty()&&!telefono.isEmpty()&&!correo.isEmpty() &&!contrasenia.isEmpty()){
                if(contrasenia.length()>=6){
                    RegistrarUsuario();

                }
            }
        });
        iniciar_sesion.setOnClickListener(v -> startActivity(new Intent(CrearCuenta.this,LoginActivity.class)));

    }

    private void RegistrarUsuariosSql(){
        ConexionSQLite conexion= new ConexionSQLite(this,"bd_reminder",null,1);
        SQLiteDatabase db=conexion.getWritableDatabase();

        String insert="INSERT INTO "+Utilidades.TABLE_USUARIO+"("+Utilidades.CAMPO_ID+","+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+","+Utilidades.CAMPO_CORREO+")"
                +"VALUES("+edit_nombre.getText().toString()+","+edit_telefono.getText().toString()+","+edit_correo_Electronico.getText().toString()+"')";





        db.close();
    }
    private void RegistrarUsuario(){
        ConexionSQLite conexion= new ConexionSQLite(this,"bd_reminder",null,1);
        SQLiteDatabase db=conexion.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE,nombre);
        values.put(Utilidades.CAMPO_TELEFONO,telefono);
        values.put(Utilidades.CAMPO_CORREO,correo);

        Long resultado= db.insert(Utilidades.TABLE_USUARIO,Utilidades.CAMPO_ID,values);
        Toast.makeText(getApplicationContext(),"id registro:"+resultado,Toast.LENGTH_SHORT).show();
        db.close();

        
        firebaseAuth.createUserWithEmailAndPassword(correo,contrasenia).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Map<String,Object>map=new HashMap<>();
                map.put("nombre",nombre);
                map.put("telefono",telefono);
                map.put("correo Electronico",correo);
                map.put("contraseña",contrasenia);

                String id= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                dr.child("Usuario").child(id).setValue(map).addOnCompleteListener(task2 -> {
                    if(task2.isSuccessful()){
                        Toast.makeText(CrearCuenta.this,"Usuario Creado Correctamente",Toast.LENGTH_SHORT).show();
                        Clean();
                    }else{
                        Toast.makeText(CrearCuenta.this,"Error en la creación de tus datos intenta nuevamente",Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(CrearCuenta.this,"Datos ya ocupados o no estan registrados por su correo Electronico correspondiente",Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void Clean(){
        edit_nombre.setText("");
        edit_telefono.setText("");
        edit_correo_Electronico.setText("");
        edit_contrasenia.setText("");




    }
}