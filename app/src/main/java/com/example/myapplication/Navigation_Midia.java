package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Recordatorio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Navigation_Midia extends Fragment{
    private RecyclerView recycler;
    FloatingActionButton cerrarSesion;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Recordatorio> recordatorios;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private static final String TAG = "Search";


    private ArrayList<Recordatorio> lista_recordatorio;




    private DatabaseReference mDatabase; //declaracion de databasereference para la listar en fb


    public Navigation_Midia() {

    }

    public static Navigation_Midia newInstance(String param1, String param2) {
        Navigation_Midia fragment = new Navigation_Midia();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("accountPersistence",
                Context.MODE_PRIVATE);

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



        recycler = view.findViewById(R.id.recyclerView);
        cerrarSesion=view.findViewById(R.id.buttonCerrarSesion);
        dbFirebase(mDatabase);


        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recycler.setHasFixedSize(true);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Desea cerrar Sesión?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("emailKey", "");
                        editor.putString("passKey", "");
                        editor.putString("keepSessionKey", "false");
                        editor.commit();
                        Intent intent= new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });





        DocumentReference userReference = firebaseFirestore.collection("usuarios").document(firebaseAuth.getCurrentUser().getUid());
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

                            //ABRIR EL ACTIVITY Y PASARLE CON PUT EXTRA EL item

                            Intent i = new Intent(getActivity() , EditarRecordatorioActivity.class);
                            String[] data = new String[5];
                            data[0] = item.getId();
                            data[1] = item.getTitulo();
                            data[2] = item.getDescripcion();
                            data[3] = item.getFecha();
                            data[4] = item.getHora();

                            i.putExtra("item",data);
                            startActivity(i);
                            getActivity().finish();

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.action_search,menu);
        MenuItem item=menu.findItem(R.id.action_search_menu);
        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               buscarr(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscarr(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void buscarr(String s) {
        DocumentReference userReference = firebaseFirestore.collection("usuarios").document(firebaseAuth.getCurrentUser().getUid());

    }
}

