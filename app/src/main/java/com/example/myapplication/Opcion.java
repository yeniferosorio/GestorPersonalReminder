package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class Opcion extends AppCompatActivity {

    Navigation_Midia navigation_Midia=new Navigation_Midia();
    Navigation_Tareas_Importantes navigation_Tareas_Importantes=new Navigation_Tareas_Importantes();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion);


        BottomNavigationView navigation=findViewById(R.id.bottom_nav_menu);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(navigation_Midia);

        if(AccessToken.getCurrentAccessToken()!=null){
            goLoginScreen();
        }


    }
    public void logout(View view){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    private void goLoginScreen() {
        Intent intent=new Intent(Opcion.this,LoginActivity.class);
        System.out.println("se cambio de ventana");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener= item -> {

        switch (item.getItemId()){
            case R.id.navigation_Midia:
                loadFragment(navigation_Midia);
                return true;
            case R.id.navigation_Tareas_Importantes:
                loadFragment(navigation_Tareas_Importantes);
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