package com.example.henrystevensalazar.bicita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button inicio;
    Button reg;
    Button per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicio = (Button) findViewById(R.id.inicioBTN);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        reg = (Button) findViewById(R.id.registroBTN);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), RegistrarseActivity.class);
                startActivity(i);
            }
        });

        per = (Button) findViewById(R.id.perfilBTN);
        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), PerfilPropioActivity.class);
                startActivity(i);
            }
        });
    }
}
