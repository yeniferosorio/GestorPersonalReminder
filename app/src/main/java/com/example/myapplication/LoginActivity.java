
package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.conexion.ConexionSQLite;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.context.AttributeContext;

import org.jetbrains.annotations.NotNull;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText Text_Email,Text_Password;
    Button btn_inicio_sesion;
    LoginButton button_facebook;
    SignInButton buttonGoogle;
    TextView cambio_password,crear_cuenta;
    private FirebaseAuth firebaseAuth;
    private String correo,contrasenia;
    private CallbackManager callbackManager;
    private static final String TAG = "MyActivity";
    private GoogleApiClient googleApiClient;
    public static int SIGN_IN_CODE;
    ConexionSQLite conexion= new ConexionSQLite(this,"bd_personal_Reminder",null,1);






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager= CallbackManager.Factory.create();
        Text_Email= findViewById(R.id.TextEmail);
        Text_Password=findViewById(R.id.TextPassword);
        cambio_password=findViewById(R.id.cambiopassword);
        crear_cuenta= findViewById(R.id.textcrearCuenta);
        btn_inicio_sesion=findViewById(R.id.btnIniciarSesion);
        button_facebook=findViewById(R.id.login_button);
        buttonGoogle=findViewById(R.id.ButtonGoogle);





        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_clients_id)).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

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
          contrasenia=Text_Password.getText().toString();
          if(!correo.isEmpty()&& ! contrasenia.isEmpty()){
            loginUsuario();

        }else{
              Toast.makeText(LoginActivity.this,"Complete los campos",Toast.LENGTH_SHORT).show();
          }
    });

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent ,SIGN_IN_CODE);
                goMainScreen();
            }
        });

        button_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {
               goMainScreen();
           }

           @Override
           public void onCancel() {
               Toast.makeText(getApplicationContext(),R.string.cancel_login,Toast.LENGTH_SHORT).show();

           }

           @Override
           public void onError(FacebookException error) {
               Toast.makeText(getApplicationContext(),R.string.error_login,Toast.LENGTH_SHORT).show();

           }
       });


    }
    private void goMainScreen() {
        Intent intent = new Intent(LoginActivity.this,Opcion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void loginUsuario(){
        firebaseAuth.signInWithEmailAndPassword(correo,contrasenia).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(LoginActivity.this,Opcion.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Datos incorrectos ingrese nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if(requestCode==SIGN_IN_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            HandleSignInResult(result);
        }
    }

    private void HandleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            goMainScreen();
        }else{
            Toast.makeText(this,R.string.not_login,Toast.LENGTH_SHORT).show();
        }

    }

    AccessToken accessToken= AccessToken.getCurrentAccessToken();
    boolean isLoggedIn= accessToken!= null &&!accessToken.isExpired();

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }



}
