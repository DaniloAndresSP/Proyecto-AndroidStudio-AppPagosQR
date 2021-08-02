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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etContrasena, etCodigo;

    //Variables de los datos a registrar

    private String nombre = "";
    private String correo = "";
    private String contrasena = "";
    private String codigo = "";
    private int dineroInicial;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo1);
        etContrasena = findViewById(R.id.etContrasena);
        etCodigo = findViewById(R.id.etCodigo);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



    }

    public void uRegistro (View v) {
        nombre = etNombre.getText().toString();
        correo = etCorreo.getText().toString();
        contrasena = etContrasena.getText().toString();
        codigo = etCodigo.getText().toString();

        if (!nombre.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty() && !codigo.isEmpty()) {

            if (contrasena.length() >= 6)
                usuarioRegistro();
            else
                Toast.makeText(RegistroActivity.this,"Ingrese más de 6 dijitos", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(RegistroActivity.this,"Ingrese los datos comletos", Toast.LENGTH_LONG).show();
        }
    }

    private void usuarioRegistro() {
        mAuth.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    dineroInicial = 100000;

                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre",nombre);
                    map.put("correo",correo);
                    map.put("contrasena", contrasena);
                    map.put("codigo", codigo);
                    map.put("dinero", dineroInicial);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                startActivity(new Intent(RegistroActivity.this, PerfilActivity.class));
                                finish();
                            } else
                                Toast.makeText(RegistroActivity.this,"No se registraron los datos corectamente", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(RegistroActivity.this,"No se realizo el registro", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}