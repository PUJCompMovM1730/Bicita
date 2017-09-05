package com.pujhones.bicita.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.pujhones.bicita.R;

public class VerRecorridoActivity extends AppCompatActivity {
    Button menu;
    ListView listaRecorridos;

    String[] descripcion;
    int[] imagenId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_recorrido);
        iniciarArreglos();
        listaRecorridos = (ListView)findViewById(R.id.listaRecorridos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, descripcion);
        ListView listView = (ListView) findViewById(R.id.listaRecorridos);
        listView.setAdapter(adapter);

    }

    public void iniciarArreglos()
    {

        descripcion = new String[15];
        imagenId = new int[15];

        descripcion[0] = "Paseo a universidad";
        descripcion[1] = "Ciclovía";
        descripcion[2] = "A hacer tésis";
        descripcion[3] = "Travesía a Suba";
        descripcion[4] = "A la Calera";
        descripcion[5] = "Paseo a universidad";
        descripcion[6] = "Ciclovía";
        descripcion[7] = "A hacer tésis";
        descripcion[8] = "Travesía a Suba";
        descripcion[9] = "A la Calera";
        descripcion[10] = "Paseo a universidad";
        descripcion[11] = "Ciclovía";
        descripcion[12] = "A hacer tésis";
        descripcion[13] = "Travesía a Suba";
        descripcion[14] = "A la Calera";
        //Para imagenes con la lista: https://www.learn2crack.com/2013/10/android-custom-listview-images-text-example.html
        imagenId[0] = R.drawable.horus;
        imagenId[1] = R.drawable.horus;
        imagenId[2] = R.drawable.horus;
        imagenId[3] = R.drawable.mimi;
        imagenId[4] = R.drawable.mimi;
        imagenId[5] = R.drawable.horus;
        imagenId[6] = R.drawable.horus;
        imagenId[7] = R.drawable.horus;
        imagenId[8] = R.drawable.mimi;
        imagenId[9] = R.drawable.mimi;
        imagenId[10] = R.drawable.horus;
        imagenId[11] = R.drawable.horus;
        imagenId[12] = R.drawable.horus;
        imagenId[13] = R.drawable.mimi;
        imagenId[14] = R.drawable.mimi;
    }
}
