package com.pujhones.bicita.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pujhones.bicita.R;
import com.pujhones.bicita.model.BiciUsuario;
import com.pujhones.bicita.model.ElementoListaAmigo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class AmigosActivity extends AppCompatActivity {

    static class ElementoListaViewHolder {
        TextView nombre;
        CircleImageView imagenPerfil;
    }

    private class CustomArrayAdapter extends ArrayAdapter<ElementoListaAmigo> {
        private ArrayList<ElementoListaAmigo> list;

        //this custom adapter receives an ArrayList of RowData objects.
        //RowData is my class that represents the data for a single row and could be anything.
        public CustomArrayAdapter(Context context, int textViewResourceId,
                                  ArrayList<ElementoListaAmigo> rowDataList) {
            //populate the local list with data.
            super(context, textViewResourceId, rowDataList);
            this.list = new ArrayList<ElementoListaAmigo>();
            this.list.addAll(rowDataList);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            //creating the ViewHolder we defined earlier.
            final AmigosActivity.ElementoListaViewHolder holder = new AmigosActivity
                    .ElementoListaViewHolder();

            //creating LayoutInflator for inflating the row layout.
            LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);

            //inflating the row layout we defined earlier.
            convertView = inflator.inflate(R.layout.elemento_lista_amigo, null);

            //setting the views into the ViewHolder.
            holder.nombre = (TextView) convertView.findViewById(R.id.txtNombre);
            holder.imagenPerfil = (CircleImageView) convertView.findViewById(R.id.imgPerfil);

            holder.nombre.setText(list.get(position).getNombre());
            final Context context = holder.imagenPerfil.getContext();

            AsyncTask<String, Void, Drawable> task = new AsyncTask<String, Void, Drawable>() {

                protected Exception exception = null;

                protected Drawable doInBackground(String... urls) {
                    Drawable d;
                    InputStream is = null;
                    Log.e(TAG, "Descargando imagen.");
                    try {
                        is = (InputStream) new URL(list.get(position).getPhotoURL()).getContent();
                        d = Drawable.createFromStream(is, null);
                        return d;
                    } catch (IOException e) {
                        exception = e;
                        Log.e(TAG, e.getMessage());
                    }
                    return null;
                }
                protected void onPostExecute(Drawable d) {
                    if (exception == null) {
                        holder.imagenPerfil.setImageDrawable(d);
                    } else {
                        int idImagen = context.getResources().getIdentifier("horus",
                                "drawable", context.getPackageName());
                        holder.imagenPerfil.setImageResource(idImagen);
                    }
                }
            };

            task.execute();

            //return the row view.
            return convertView;
        }
    }

    protected final String TAG = "AMIGOS";

    FirebaseDatabase fireDB;

    ArrayList<ElementoListaAmigo> amigos = new ArrayList<>();
    ArrayList<BiciUsuario> amigosUsuarios = new ArrayList<>();

    ListView lstAmigos;
    FloatingActionButton btnAgregar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        //TODO volver asíncrono.

        fireDB = FirebaseDatabase.getInstance();

        Log.i(TAG, FirebaseAuth.getInstance().getCurrentUser().getUid());
        Log.i(TAG, FirebaseAuth.getInstance().getCurrentUser().getEmail());

        lstAmigos = (ListView) findViewById(R.id.lstAmigos);
        btnAgregar = (FloatingActionButton) findViewById(R.id.fab);

        lstAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(view.getContext(), VerAmigoActivity.class);
                in.putExtra("usuario", amigosUsuarios.get(i));
                startActivity(in);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), AgregarAmigosActivity.class);
                startActivity(in);
            }
        });

        setTitle("Amigos");

        Log.e(TAG, "Cargando solicitudes.");
        cargarAmigosDesdeDB();
    }

    public void cargarAmigosDesdeDB() {
        amigos = new ArrayList<>();
        Query queryAmigos1 = fireDB.getReference("amistades/").orderByChild("amigo1").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query queryAmigos2 = fireDB.getReference("amistades/").orderByChild("amigo2").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        queryAmigos1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryAmigos(dataSnapshot, "amigo2");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, ":onCancelled", databaseError.toException());
                // ...
            }
        });

        queryAmigos2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryAmigos(dataSnapshot, "amigo1");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, ":onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    protected void queryAmigos(DataSnapshot dataSnapshot, String amigoFieldName) {
        Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
        while (iter.hasNext()) {
            DataSnapshot ds = iter.next();
            if (ds.child("estado").getValue(String.class).equals("aceptado")) {
                String uid = ds.child(amigoFieldName).getValue(String.class);
                Query queryAmigo = fireDB.getReference("biciusuarios/" + uid);
                Log.e(TAG, uid);
                queryAmigo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {
                        BiciUsuario u = dataSnapshot2.getValue(BiciUsuario.class);
                        u.setUid(dataSnapshot2.getKey());
                        amigosUsuarios.add(u);
                        amigos.add(new ElementoListaAmigo(u.getNombre(), u.getPhotoURL()));
                        Log.i(TAG, amigos.toString());
                        cargarAmigosEnVista();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, ":onCancelled", databaseError.toException());
                        // ...
                    }
                });
            }

        }
    }

    protected void cargarAmigosEnVista() {
        AmigosActivity.CustomArrayAdapter dataAdapter = new
                AmigosActivity.CustomArrayAdapter(getBaseContext(), R.id.txtNombre,
                amigos);
        lstAmigos.setAdapter(dataAdapter);
        Log.e(TAG, amigos.toString());
    }
}