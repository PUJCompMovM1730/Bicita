package com.example.usuario.bicita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class ActividadPruebaVistas extends AppCompatActivity {

    Button botonRecorridos;
    Button botonAmigo;
    Button botonUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_prueba_vistas);

        botonRecorridos = (Button) findViewById(R.id.botonRecorridos);
        botonAmigo = (Button) findViewById(R.id.botonAmigo);
        botonUsuario = (Button) findViewById(R.id.botonUsuario);

        botonRecorridos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerRecorridos.class);
                startActivity(intent);
            }
        });

        botonAmigo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerAmigo.class);
                startActivity(intent);
            }
        });

        botonUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerUsuario.class);
                startActivity(intent);
            }
        });
    }
}
