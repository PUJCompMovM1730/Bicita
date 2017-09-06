package com.pujhones.bicita.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pujhones.bicita.R;

public class CrearRecorridoActivity extends AppCompatActivity {

    Button IniciarActividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recorrido);
        IniciarActividad = (Button) findViewById(R.id.IniciarActividad);
        IniciarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(),RecorridoActivity.class);
                startActivity(in);
            }
        });
    }
}
