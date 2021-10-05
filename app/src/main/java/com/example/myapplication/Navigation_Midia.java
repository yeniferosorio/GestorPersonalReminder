package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.model.Recordatorio_registrado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Navigation_Midia extends Fragment implements AdaptadorRec.OnNoteListener {

    ArrayList<Recordatorio_registrado>lista_recordatorio;
    RecyclerView recycler;


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
        return inflater.inflate(R.layout.fragment_navigation__midia, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lista_recordatorio=new ArrayList<>();
        recycler=view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recycler.setHasFixedSize(true);



       AdaptadorRec adapter=new AdaptadorRec(lista_recordatorio, this.getActivity(), (AdaptadorRec.OnNoteListener) this::description);
        recycler.setAdapter(adapter);

        llenarLista();

        FloatingActionButton floatingActionButton=view.findViewById(R.id.botonAñadir);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent= new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        });


        }

    private void llenarLista() {

    lista_recordatorio.add(new Recordatorio_registrado("Comprar pan","comprar pan a las 7 de la tarde",R.drawable.ic_baseline_wb_sunny_24));
    lista_recordatorio.add(new Recordatorio_registrado("Tomar pastillas","tomar pastillas 1)paracetamol 2)anticonceptivos 3)ibuprofeno.",R.drawable.ic_baseline_wb_sunny_24));
    lista_recordatorio.add(new Recordatorio_registrado(" Veterinario","hora veterinario a las 3 de la tarde el dia 04/08",R.drawable.ic_baseline_wb_sunny_24));
    lista_recordatorio.add(new Recordatorio_registrado("Cortar uñas","cortar uñas al chopper",R.drawable.ic_baseline_wb_sunny_24));

    }

    public void description(Recordatorio_registrado item){
        Intent intent=new Intent(this.getActivity(),MainActivity.class);
        intent.putExtra("listElement",item);
        startActivity(intent);
    }



    @Override
    public void onNoteClick(Recordatorio_registrado item) {

    }
}

