package com.example.pagosqrfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PerfilActivity extends AppCompatActivity {

    private TextView etSaldo;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etSaldo = findViewById(R.id.etSaldo);

        cargarSaldo();

    }

    public void cerrarSesion (View v) {
        mAuth.signOut();
        startActivity(new Intent(PerfilActivity.this, loginActivity.class));
        finish();
    }

    public void cargarSaldo() {
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String saldo =snapshot.child("dinero").getValue().toString();
                    etSaldo.setText(saldo +" Pesos");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void scanQr (View v) {
        IntentIntegrator integrator = new IntentIntegrator(PerfilActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escaneé el codigo QR");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null) {
                Toast.makeText(this, "Error en el escaneo del codigo QR",Toast.LENGTH_LONG).show();
            } else {
                int pagoQr = Integer.parseInt(result.getContents());
                String id = mAuth.getCurrentUser().getUid();

                mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String saldo = snapshot.child("dinero").getValue().toString();
                            int saldoCuenta = Integer.parseInt(saldo);
                            int saldoNuevo = saldoCuenta + pagoQr;
                            etSaldo.setText(saldoNuevo +" Pesos");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void enviarDinero (View v) {
        startActivity(new Intent(PerfilActivity.this, CodigoTranActivity.class));
    }
}