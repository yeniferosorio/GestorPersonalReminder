package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationBarView;


import org.jetbrains.annotations.NotNull;


public class Opcion extends AppCompatActivity {

    Navigation_Midia navigation_Midia=new Navigation_Midia();
    Navigation_Tareas_Importantes navigation_Tareas_Importantes=new Navigation_Tareas_Importantes();
    Navigation_Mis_tareas navigation_Mis_tareas=new Navigation_Mis_tareas();
    Navigation_Misplanes navigation_Misplanes= new Navigation_Misplanes();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion);


        BottomNavigationView navigation=findViewById(R.id.bottom_nav_menu);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(navigation_Midia);


    }
    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener= item -> {
        switch (item.getItemId()){
            case R.id.navigation_Midia:
                loadFragment(navigation_Midia);
                return true;
            case R.id.navigation_Tareas_Importantes:
                loadFragment(navigation_Tareas_Importantes);
                return true;
            case R.id.navigation_Mis_tareas:
                loadFragment(navigation_Mis_tareas);
                return true;
            case R.id.navigation_Misplanes:
                loadFragment(navigation_Misplanes);
                return true;
        }
        return false;
    };
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_conteiner,fragment);
        transaction.commit();
    }
}