package com.pujhones.bicita.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pujhones.bicita.R;
import com.pujhones.bicita.model.ElementoListaAmigo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarAmigoSolicitudesFragment extends Fragment {

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
            LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_amigo_solicitudes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        lstAmigos = (ListView) v.findViewById(R.id.lstAmigos);

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
        AgregarAmigoSolicitudesFragment.CustomArrayAdapter dataAdapter = new AgregarAmigoSolicitudesFragment.CustomArrayAdapter(getActivity(), R.id.txtNombre, amigos);
        lstAmigos.setAdapter(dataAdapter);
        lstAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(view.getContext(),VerAmigoActivity.class);
                startActivity(in);
            }
        });

        getActivity().setTitle("Amigos");
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("amigos.json");
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
