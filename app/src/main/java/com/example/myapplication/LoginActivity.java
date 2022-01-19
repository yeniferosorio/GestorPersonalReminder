
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
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    Button btn_inicio_sesion;
    SignInButton buttonGoogle;
    TextView cambio_password, crear_cuenta;
    EditText Text_Email, Text_Password;
    private String correo, contrasenia;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private static final String TAG = "GoogleActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Text_Email = findViewById(R.id.TextEmail);
        Text_Password = findViewById(R.id.TextPassword);
        cambio_password = findViewById(R.id.cambiopassword);
        crear_cuenta = findViewById(R.id.textcrearCuenta);
        btn_inicio_sesion = findViewById(R.id.btnIniciarSesion);

        buttonGoogle = findViewById(R.id.ButtonGoogle);

        firebaseAuth = FirebaseAuth.getInstance();

        //Inicio de sesion con google//
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_clients_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        //goMainScreen();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    updateUI(null);
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    firebaseAuth.signOut();
                }
            }
        };

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
                System.out.println("=========================================================================");
                data();
                System.out.println("=========================================================================");
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

    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void updateUI(FirebaseUser user) {

    }

    private void goMainScreen() {
        Intent intent = new Intent(LoginActivity.this, Opcion.class);
        startActivity(intent);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                        Util.setProperty("user.mail", firebaseAuth.getCurrentUser().getEmail(), getApplicationContext());
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


    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    public void data() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("usuarios");
        Query query = userRef.whereEqualTo("nombre", "Marcelo");

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("----------------------------------------------------------------------------------");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                System.out.println("----------------------------------------------------------------------------------");
                            }
                        } else {
                            System.out.println("----------------------------------------------------------------------------------");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.println("----------------------------------------------------------------------------------");
                        }
                    }
                });

    }
    private void usuarioActual(){

    }

}
