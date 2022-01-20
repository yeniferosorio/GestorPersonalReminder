package com.example.myapplication.model;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UsuarioActualUtil extends Application {

    public static String actualUserMail = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("usuarios");
    private Query query;
    private int contador;

    public UsuarioActualUtil() {
    }

    public boolean isValidEmailForSingIn(String correoUsado) {
        contador = 0;
        boolean isValidEmail = false;
        query = userRef.whereEqualTo("correo",correoUsado);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        contador++;
                        System.out.println("------------------");
                        System.out.println(document.get("correo"));
                        System.out.println("------------------");
                        System.out.println("contador: "+ contador);

                    }

                } else {
                    System.out.println(task.getException());
                }

            }
        });


        if (contador == 0) {
            isValidEmail = true;
        }
        return isValidEmail;
    }


}
