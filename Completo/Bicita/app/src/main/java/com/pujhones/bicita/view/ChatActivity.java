package com.pujhones.bicita.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.pujhones.bicita.model.ElementoListaMensaje;
import com.pujhones.bicita.model.Mensaje;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    static class ElementoListaViewHolder {
        TextView contenido;
        CircleImageView autor;
    }

    private class CustomArrayAdapter extends ArrayAdapter<ElementoListaMensaje> {
        private ArrayList<ElementoListaMensaje> list;

        //this custom adapter receives an ArrayList of RowData objects.
        //RowData is my class that represents the data for a single row and could be anything.
        public CustomArrayAdapter(Context context, int textViewResourceId,
                                  ArrayList<ElementoListaMensaje> rowDataList) {
            //populate the local list with data.
            super(context, textViewResourceId, rowDataList);
            this.list = new ArrayList<ElementoListaMensaje>();
            this.list.addAll(rowDataList);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            //creating the ViewHolder we defined earlier.
            ChatActivity.ElementoListaViewHolder holder = new ChatActivity
                    .ElementoListaViewHolder();

            //creating LayoutInflator for inflating the row layout.
            LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);

            //inflating the row layout we defined earlier.
            convertView = inflator.inflate(R.layout.mensaje_chat, null);

            //setting the views into the ViewHolder.
            holder.contenido = (TextView) convertView.findViewById(R.id.txtNombre);
            holder.autor = (CircleImageView) convertView.findViewById(R.id.imgPerfil);

            holder.contenido.setText(list.get(position).getNombre());
            Context context = holder.autor.getContext();

            Drawable d;
            InputStream is = null;
            Log.e(TAG, "Descargando imagen.");
            try {
                is = (InputStream) new URL(list.get(position).getPhotoURL()).getContent();
                d = Drawable.createFromStream(is, null);
                holder.autor.setImageDrawable(d);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                int idImagen = context.getResources().getIdentifier("horus",
                        "drawable", context.getPackageName());
                holder.autor.setImageResource(idImagen);
            }

            //return the row view.
            return convertView;
        }
    }

    protected final String TAG = "CHAT";

    FirebaseDatabase fireDB;

    ArrayList<ElementoListaMensaje> chat = new ArrayList<>();

    ListView lstMensajes;
    FloatingActionButton btnAgregar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        fireDB = FirebaseDatabase.getInstance();

        Log.i(TAG, FirebaseAuth.getInstance().getCurrentUser().getUid());
        Log.i(TAG, FirebaseAuth.getInstance().getCurrentUser().getEmail());

        lstMensajes = (ListView) findViewById(R.id.lstMensajes);
        btnAgregar = (FloatingActionButton) findViewById(R.id.fab);


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), AgregarAmigosActivity.class);
                startActivity(in);
            }
        });

        setTitle("Chat");

        Log.e(TAG, "Cargando mensajes.");

        loadMensajesFromDB();
    }

    public void loadMensajesFromDB() {
        chat = new ArrayList<>();
        Query queryMensajes1 = fireDB.getReference("amistades/").orderByChild("amigo1").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query queryMensajes2 = fireDB.getReference("amistades/").orderByChild("amigo2").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        queryMensajes1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryBiciUsuariosSnapshot(dataSnapshot, "amigo2");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, ":onCancelled", databaseError.toException());
                // ...
            }
        });

        queryMensajes2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryBiciUsuariosSnapshot(dataSnapshot, "amigo1");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, ":onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    protected void queryBiciUsuariosSnapshot(DataSnapshot dataSnapshot, String amigoFieldName) {
        Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
        while (iter.hasNext()) {
            DataSnapshot ds = iter.next();
            if (ds.child("estado").getValue(String.class).equals("aceptado")) {
                String uid = ds.child(amigoFieldName).getValue(String.class);
                Query queryAmigo = fireDB.getReference("biciusuarios/" + uid);
                Log.e(TAG, uid);
                queryAmigo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {
                        Mensaje u = dataSnapshot2.getValue(Mensaje.class);
                        u.setAutor(dataSnapshot2.getKey());
                        chat.add(new ElementoListaMensaje(u.getContenido(), u.getAutor()));
                        Log.i(TAG, chat.toString());
                        cargarChatEnVista();
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

    protected void cargarChatEnVista() {
        ChatActivity.CustomArrayAdapter dataAdapter = new
                ChatActivity.CustomArrayAdapter(getBaseContext(), R.id.txtMensaje,
                chat);
        lstMensajes.setAdapter(dataAdapter);
        Log.e(TAG, chat.toString());
    }
}
