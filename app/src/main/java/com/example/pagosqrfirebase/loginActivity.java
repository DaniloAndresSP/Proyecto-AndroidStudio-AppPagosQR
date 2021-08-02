package com.example.pagosqrfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etCorreo,etContrasena;

    private String correo = "";
    private String contracena = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreo1);
        etContrasena = findViewById(R.id.etContrasena);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser usuarioActual = mAuth.getCurrentUser();

    }

    public void iniciarSesion (View v) {
        correo = etCorreo.getText().toString();
        contracena = etContrasena.getText().toString();

        if (!correo.isEmpty() && !contracena.isEmpty()) {
            loginUser();
        } else {
            Toast.makeText(this,"Complete los campos",Toast.LENGTH_LONG).show();
        }

    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(correo,contracena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(loginActivity.this,PerfilActivity.class));
                    finish();
                } else {
                    Toast.makeText(loginActivity.this,"Error en el inicio de sesión, compruebe los datos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(loginActivity.this, PerfilActivity.class));
        }
    }

    public void registro (View v) {
        startActivity(new Intent(loginActivity.this, RegistroActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        //finish();
    }
}