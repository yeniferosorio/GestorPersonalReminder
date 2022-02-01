package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Recordatorio;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Navigation_Midia extends Fragment {
    private RecyclerView recycler;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Recordatorio> recordatorios;

    private static final String TAG = "EventChild";
    private DatabaseReference mDatabase; //declaracion de databasereference para la listar en fb


    public Navigation_Midia() {

    }

    public static Navigation_Midia newInstance(String param1, String param2) {
        Navigation_Midia fragment = new Navigation_Midia();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        inicializarFirebase();
        recordatorios = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_navigation__midia, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dbFirebase(mDatabase);

        recycler = view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recycler.setHasFixedSize(true);

        recycler = view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recycler.setHasFixedSize(true);

        DocumentReference userReference = firebaseFirestore.collection("usuarios").document(firebaseAuth.getCurrentUser().getUid());
        System.out.println(firebaseAuth.getCurrentUser().getUid());
        userReference.collection("recordatorio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    Recordatorio recordatorio;
                    for (DocumentSnapshot d : list) {

                        recordatorio = new Recordatorio();
                        recordatorio.setId(d.getId());
                        recordatorio.setTitulo(d.getString("titulo"));
                        recordatorio.setDescripcion(d.getString("descripcion"));
                        recordatorio.setFecha(d.getString("fecha"));
                        recordatorio.setHora(d.getString("hora"));

                        recordatorios.add(recordatorio);

                    }
                    AdaptadorRec adapter = new AdaptadorRec(recordatorios, getActivity(), new AdaptadorRec.OnNoteListener() {
                        @Override
                        public void onNoteClick(Recordatorio item) {
                            System.out.println(item.getId());
                        }
                    });
                    recycler.setAdapter(adapter);
                } else {
                    System.out.println("--------------------------------");
                    System.out.println("Lista de recordatorios vacia");
                    System.out.println("--------------------------------");
                }
            }
        });


        FloatingActionButton btnAgregarRecordatorio = view.findViewById(R.id.botonAñadir);
        btnAgregarRecordatorio.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityNewReminder.class);
            startActivity(intent);
        });


    }


    public void dbFirebase(DatabaseReference databaseReference) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    private void inicializarFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void description(Recordatorio item) {
        Intent intent = new Intent(this.getActivity(), ActivityNewReminder.class);
        intent.putExtra("listElement", item);
        startActivity(intent);
    }


}

