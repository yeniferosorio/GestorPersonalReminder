
package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class LoginActivity extends AppCompatActivity {
    EditText Text_Email,Text_Password;
    Button btn_inicio_sesion;
    TextView cambio_password,crear_cuenta;
    private FirebaseAuth firebaseAuth;
    private String correo,contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Text_Email= findViewById(R.id.TextEmail);
        Text_Password=findViewById(R.id.TextPassword);
        cambio_password=findViewById(R.id.cambiopassword);
        crear_cuenta= findViewById(R.id.textcrearCuenta);
        btn_inicio_sesion=findViewById(R.id.btnIniciarSesion);
        firebaseAuth = FirebaseAuth.getInstance();


        cambio_password.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,RestPass.class));
            finish();
        });

        crear_cuenta.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,CrearCuenta.class));
            finish();
        });

        btn_inicio_sesion.setOnClickListener(v -> {
          correo=Text_Email.getText().toString();
          contraseña=Text_Password.getText().toString();
          if(!correo.isEmpty()&& !contraseña.isEmpty()){
            loginUsuario();
        }else{
              Toast.makeText(LoginActivity.this,"Complete los campos",Toast.LENGTH_SHORT).show();
          }
    });

    }

    private void loginUsuario(){
        firebaseAuth.signInWithEmailAndPassword(correo,contraseña).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(LoginActivity.this,Opcion.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Datos incorrectos ingrese nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

}