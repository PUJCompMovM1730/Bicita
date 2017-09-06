package com.pujhones.bicita.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.pujhones.bicita.R;

public class IniciarRecorridoActivity extends AppCompatActivity {

    ImageButton crear;
    ImageButton imageView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_recorrido);
        crear = (ImageButton) findViewById(R.id.crearRecorridoBtn);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), NuevoRecorridoInicioActivity.class);
                startActivity(in);
            }
        });
        imageView6 = (ImageButton) findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), VerRecorridoActivity.class);
                startActivity(in);
            }
        });
    }
}
