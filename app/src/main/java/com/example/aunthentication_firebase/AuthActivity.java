package com.example.aunthentication_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth; // Declaracción de firebaseAuth para poder autenticarme
    FirebaseAuth.AuthStateListener fiAuthStateListener; // Su principal objectivo es leer los inicios de sessión
    public static final int REQUEST_CODE = 54654; // Es un num aletorio en total son 5 num

    List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.GitHubBuilder().build()
    ); // Permite definir con que tipo de plataforma se va autenticar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firebaseAuth = FirebaseAuth.getInstance();
        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    Toast.makeText(AuthActivity.this, "Se inició sesión", Toast.LENGTH_LONG).show();
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(provider)
                                    .build(),
                            REQUEST_CODE);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(fiAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(fiAuthStateListener);
    }

    public void cerrar_session(View view) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AuthActivity.this, "La sesión ha sido cerrada", Toast.LENGTH_LONG).show();
            }
        });
    }
}