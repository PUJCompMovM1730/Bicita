package com.pujhones.bicita.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pujhones.bicita.R;

public class NuevoRecorridoInicioActivity extends AppCompatActivity {
    Button IniciarActividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_recorrido_inicio);
        IniciarActividad = (Button) findViewById(R.id.IniciarActividad);
        IniciarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(),CrearRecorridoActivity.class);
                startActivity(in);
            }
        });
    }
}
