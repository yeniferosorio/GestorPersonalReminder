package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("accountPersistence",
                Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.contains("keepSessionKey")){
                    if (Boolean.valueOf(sharedPreferences.getString("keepSessionKey",""))) {

                        firebaseAuth = FirebaseAuth.getInstance();
                        String email = sharedPreferences.getString("emailKey","");
                        String pass = sharedPreferences.getString("passKey","");
                        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Toast.makeText(SplashActivity.this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(new Intent(SplashActivity.this, FragmentActivity.class));
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(SplashActivity.this, "Datos incorrectos ingrese nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}