package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;

public class Navigation_Tareas_Importantes extends Fragment {
 Button ingHuella, buttonping;
 TextView mensaje;
 private Executor executor;
 private BiometricPrompt biometricPrompt;

 private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;


    public Navigation_Tareas_Importantes() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Navigation_Tareas_Importantes newInstance(String param1, String param2) {
        Navigation_Tareas_Importantes fragment = new Navigation_Tareas_Importantes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_navigation__tareas__importantes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ingHuella= view.findViewById(R.id.ButtonHuella);
        buttonping= view.findViewById(R.id.buttonPing);
        mensaje= view.findViewById(R.id.textViewMensaje);

        executor= ContextCompat.getMainExecutor(getActivity());

        biometricPrompt=new BiometricPrompt(getActivity(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                mensaje.setText("Error de reconocimiento de huella"+ errString);
                Toast.makeText(getActivity(),"Reconocimiento de huella fallido"+ errString,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                mensaje.setText("Reconocimiento de huella Exitoso");
                Toast.makeText(getActivity(),"Reconocimiento de huella Exitoso",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),MainImportante.class);
                startActivity(intent);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                mensaje.setText("Reconocimiento de huella fallido");
                Toast.makeText(getActivity(),"Reconocimiento de huella fallido",Toast.LENGTH_SHORT).show();

            }
        });

            promptInfo=new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometrico exitoso")
                    .setSubtitle("posiciona adecuadamente tu huella")
                    .setNegativeButtonText("Vuelve a intentarlo")
                    .build();



        ingHuella.setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));

        buttonping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),PingActivity.class);
                startActivity(intent);
            }
        });

        }



    }


