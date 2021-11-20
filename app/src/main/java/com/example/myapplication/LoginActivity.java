
package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.config.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    Button btn_inicio_sesion;
    LoginButton button_facebook;
    SignInButton buttonGoogle;
    TextView cambio_password, crear_cuenta;
    EditText Text_Email, Text_Password;
    private String correo, contrasenia;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private static final String TAG = "GoogleActivity";
    private static final String TAGS = "FacebookActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Text_Email = findViewById(R.id.TextEmail);
        Text_Password = findViewById(R.id.TextPassword);
        cambio_password = findViewById(R.id.cambiopassword);
        crear_cuenta = findViewById(R.id.textcrearCuenta);
        btn_inicio_sesion = findViewById(R.id.btnIniciarSesion);
        button_facebook = findViewById(R.id.login_button);
        buttonGoogle = findViewById(R.id.ButtonGoogle);

        firebaseAuth = FirebaseAuth.getInstance();

        //Inicio de sesion con google//
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_clients_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //Inicio de sesion con facebook
        callbackManager = CallbackManager.Factory.create();
        button_facebook.setPermissions("email", "public_profile");

        button_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAGS, "Facebook:onSuccess" + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent i = new Intent(LoginActivity.this, Opcion.class);
                i.putExtra("correoEnUso", "");
                startActivity(i);
                finish();

            }

            @Override
            public void onCancel() {
                Log.d(TAGS, "Facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAGS, "Facebook:onError", error);

            }
        });


        cambio_password.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RestPass.class));
            finish();
        });

        crear_cuenta.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CrearCuenta.class));
            finish();
        });

        btn_inicio_sesion.setOnClickListener(v -> {
            correo = Text_Email.getText().toString();
            contrasenia = Text_Password.getText().toString();
            if (!correo.isEmpty() && !contrasenia.isEmpty()) {
                loginUsuario();

            } else {
                Toast.makeText(LoginActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
            }
        });

        buttonGoogle.setOnClickListener(v -> signIn());


    }


    //login con firebase con correo y contraseña
    private void loginUsuario() {
        firebaseAuth.signInWithEmailAndPassword(correo, contrasenia).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(new Intent(LoginActivity.this, Opcion.class));
                i.putExtra("correoEnUso", user.getEmail());

                startActivity(i);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Datos incorrectos ingrese nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //google
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
    }

    private void goMainScreen() {
        Intent intent = new Intent(LoginActivity.this, Opcion.class);

        startActivity(intent);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK--
        // resolver esta linea:  callbackManager.onActivityResult(requestCode,resultCode,data);
        //--


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithgoogle" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }

        }
    }

    //autentificacion con google en firebase
    private void firebaseAuthWithGoogle(String idtoken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idtoken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    try {
                        System.out.println(firebaseAuth.getCurrentUser().getEmail());
                        Util.setProperty("user.mail", firebaseAuth.getCurrentUser().getEmail(),getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent i = new Intent(new Intent(LoginActivity.this, Opcion.class));
                    startActivity(i);
                    finish();
                    updateUI(user);

                } else {
                    Log.d(TAG, "signInWithCredential:failure", task.getException());
                    updateUI(null);
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    //facebook start auth with facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAGS, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAGS, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAGS, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Aunthentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }


}
