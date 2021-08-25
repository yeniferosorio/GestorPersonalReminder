package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class RestPass extends AppCompatActivity {

    EditText ingresar_correo;
    Button restablecer;
    TextView volver;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    private String email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_pass);
        mAuth=FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);

        ingresar_correo=(EditText)findViewById(R.id.TextEditarCorreo);
        volver=(TextView)findViewById(R.id.TxtVolveralogin);
        restablecer=(Button)findViewById(R.id.buttonRestablecer);

        restablecer.setOnClickListener(v -> {
            email=ingresar_correo.getText().toString();
            if(!email.isEmpty()){
                mDialog.setMessage("Espere un memento...");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
                RestPassword();

            }else{
                Toast.makeText(RestPass.this,"Por favor ingrese su correo",Toast.LENGTH_SHORT).show();
            }
        });

        volver.setOnClickListener(v -> {
            startActivity(new Intent(RestPass.this,LoginActivity.class));
            finish();
        });

    }
    private void RestPassword(){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(RestPass.this,"se ha enviado a su correo el restablecimiento de su contraseña",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RestPass.this,"No se pudo restablecer su contraseña",Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        });
    }
}