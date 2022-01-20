package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Recordatorio_registrado;
import com.example.myapplication.model.UsuarioActualUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Navigation_Midia extends Fragment implements AdaptadorRec.OnNoteListener {
    private String emailActual;
    private List<Recordatorio_registrado> lista_recordatorio= new ArrayList<Recordatorio_registrado>();
    ArrayAdapter<Recordatorio_registrado>recArrayAdapter;

    RecyclerView recycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth firebaseAuth;

    private static final String TAG = "EventChild";
    private DatabaseReference  mDatabase; //declaracion de databasereference para la listar en fb

    public Navigation_Midia() {

    }

    public static Navigation_Midia newInstance(String param1, String param2) {
        Navigation_Midia fragment = new Navigation_Midia();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("=============================================================");
        System.out.println(UsuarioActualUtil.actualUserMail);
        System.out.println("=============================================================");
        return inflater.inflate(R.layout.fragment_navigation__midia, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarFirebase();
        LeerSnippets(mDatabase);



        recycler = view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recycler.setHasFixedSize(true);



        FloatingActionButton floatingActionButton = view.findViewById(R.id.botonAñadir);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        //nuevo intento con listas en firebase

        //***********************************



    }
    public void LeerSnippets(DatabaseReference databaseReference){
        mDatabase= FirebaseDatabase.getInstance().getReference();
    }






    private void inicializarFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getActivity());
    }


    @Override
    public void onNoteClick(Recordatorio_registrado item) {

    }


}

