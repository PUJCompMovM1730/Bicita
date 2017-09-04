package com.pujhones.bicita.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pujhones.bicita.model.ElementoListaAmigo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pujhones.bicita.R;

import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarAmigoSolicitudesActivity extends AppCompatActivity {

    static class ElementoListaViewHolder {
        TextView nombre;
        CircleImageView imagenPerfil;
    }

    private class CustomArrayAdapter extends ArrayAdapter<ElementoListaAmigo> {
        private ArrayList<ElementoListaAmigo> list;

        //this custom adapter receives an ArrayList of RowData objects.
        //RowData is my class that represents the data for a single row and could be anything.
        public CustomArrayAdapter(Context context, int textViewResourceId, ArrayList<ElementoListaAmigo> rowDataList) {
            //populate the local list with data.
            super(context, textViewResourceId, rowDataList);
            this.list = new ArrayList<ElementoListaAmigo>();
            this.list.addAll(rowDataList);
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            //creating the ViewHolder we defined earlier.
            AmigosActivity.ElementoListaViewHolder holder = new AmigosActivity.ElementoListaViewHolder();

            //creating LayoutInflator for inflating the row layout.
            LayoutInflater inflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflating the row layout we defined earlier.
            convertView = inflator.inflate(R.layout.elemento_lista_amigo, null);

            //setting the views into the ViewHolder.
            holder.nombre = (TextView) convertView.findViewById(R.id.txtNombre);
            holder.imagenPerfil = (CircleImageView) convertView.findViewById(R.id.imgPerfil);

            holder.nombre.setText(list.get(position).getNombre());
            Context context = holder.imagenPerfil.getContext();
            int idImagen = context.getResources().getIdentifier(list.get(position).getImagenPerfilR(), "drawable", context.getPackageName());
            holder.imagenPerfil.setImageResource(idImagen);

            //return the row view.
            return convertView;
        }
    }

    ArrayList<ElementoListaAmigo> amigos = new ArrayList<>();

    ListView lstAmigos;
    Button buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigo_solicitudes);

        buscar = (Button) findViewById(R.id.buscarBtn);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), AgregarAmigoBuscarActivity.class);
                startActivity(in);
            }
        });

        lstAmigos = (ListView) findViewById(R.id.lstAmigos);

        JSONObject json = null;
        try {
            json = new JSONObject(loadJSONFromAsset());
            JSONArray amigosJsonArray = json.getJSONArray("amigos");
            amigos = new ArrayList<>();
            for(int i = 0; i < amigosJsonArray.length(); i++) {
                JSONObject jsonObject = amigosJsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                String imagenPerfilR = jsonObject.getString("imagen_perfil_r");
                if (nombre == null) {
                    nombre = "";
                }
                if (imagenPerfilR == null) {
                    imagenPerfilR = "horus";
                }
                amigos.add(new ElementoListaAmigo(nombre, imagenPerfilR));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AgregarAmigoSolicitudesActivity.CustomArrayAdapter dataAdapter = new AgregarAmigoSolicitudesActivity.CustomArrayAdapter(this, R.id.txtNombre, amigos);
        lstAmigos.setAdapter(dataAdapter);

        setTitle("Amigos");



    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("amigos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
