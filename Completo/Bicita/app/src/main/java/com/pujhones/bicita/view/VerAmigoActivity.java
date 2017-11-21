package com.pujhones.bicita.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pujhones.bicita.R;
import com.pujhones.bicita.model.Amistad;
import com.pujhones.bicita.model.BiciUsuario;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerAmigoActivity extends AppCompatActivity {

    protected final String TAG = "VER AMIGO";

    protected CircleImageView fotoPerfil;
    protected TextView tvNombre, tvBiografia, tvPuntaje, tvPeso, tvSexo, tvAltura;
    protected FloatingActionButton fab;

    protected Amistad amistad = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_amigo);

        Intent in = getIntent();
        final BiciUsuario u = (BiciUsuario) in.getSerializableExtra("usuario");
        final String tipoVista = in.getStringExtra("tipo");

        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvBiografia = (TextView) findViewById(R.id.tvBiografia);
        tvPuntaje = (TextView) findViewById(R.id.tvPuntaje);
        tvSexo = (TextView) findViewById(R.id.tvSexo);
        tvAltura = (TextView) findViewById(R.id.tvAltura);
        tvPeso = (TextView) findViewById(R.id.tvPeso);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        tvNombre.setText(u.getNombre());
        tvBiografia.setText(u.getBiografia());
        tvPuntaje.setText(u.getPuntaje() + " puntos");
        tvSexo.setText(u.getSexo() + "");
        tvAltura.setText(u.getAltura() + " m");
        tvPeso.setText(u.getPeso() + " kg");

        final Context context = fotoPerfil.getContext();

        AsyncTask<String, Void, Drawable> task = new AsyncTask<String, Void, Drawable>() {

            protected Exception exception = null;

            protected Drawable doInBackground(String... urls) {
                Drawable d;
                InputStream is = null;
                Log.e(TAG, "Descargando imagen.");
                try {
                    is = (InputStream) new URL(u.getPhotoURL()).getContent();
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
                    fotoPerfil.setImageDrawable(d);
                } else {
                    int idImagen = context.getResources().getIdentifier("horus",
                            "drawable", context.getPackageName());
                    fotoPerfil.setImageResource(idImagen);
                }
            }
        };

        task.execute();
    }

    void cargarBoton(final BiciUsuario u, final String tipoVista) {
        final FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        final FirebaseAuth fireAuth = FirebaseAuth.getInstance();
        Query queryAmigos1 = fireDB.getReference("amistades/").orderByChild("amigo1").equalTo
                (fireAuth.getCurrentUser().getUid());
        Query queryAmigos2 = fireDB.getReference("amistades/").orderByChild("amigo2").equalTo
                (fireAuth.getCurrentUser().getUid());
        queryAmigos1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                while (iter.hasNext()) {
                    DataSnapshot ds = iter.next();
                    if (ds.child("amigo2").getValue(String.class).equals(u.getUid()) && ds.child("amigo1").getValue(String.class).equals(fireAuth.getCurrentUser().getUid())) {
                        if (amistad == null) {
                            amistad = ds.getValue(Amistad.class);
                            decidirBoton(u, tipoVista);
                        }
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        queryAmigos2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                while (iter.hasNext()) {
                    DataSnapshot ds = iter.next();
                    if (ds.child("amigo1").getValue(String.class).equals(u.getUid()) && ds.child("amigo2").getValue(String.class).equals(fireAuth.getCurrentUser().getUid())) {
                        if (amistad == null) {
                            amistad = ds.getValue(Amistad.class);
                            decidirBoton(u, tipoVista);
                        }
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void decidirBoton(final BiciUsuario u, String tipoVista) {
        if (tipoVista.equals("amigo")) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
                    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
                    Query q = fireDB.getReference("biciusuarios").orderByKey().equalTo(fireAuth.getCurrentUser().getUid());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Intent in = new Intent(getApplicationContext(), ChatActivity.class);
                            BiciUsuario uPropio = dataSnapshot.getValue(BiciUsuario.class);
                            in.putExtra("usuarioPropio", uPropio);
                            in.putExtra("usuarioOtro", u);
                            Log.i(TAG, "INICIANDO OTRA ACTIVIDAD.");
                            startActivity(in);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        } else if (tipoVista.equals("solicitud")) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
                    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
                    Query q = fireDB.getReference("amistades").orderByKey().equalTo(fireAuth.getCurrentUser().getUid());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Intent in = new Intent(getApplicationContext(), ChatActivity.class);
                            BiciUsuario uPropio = dataSnapshot.getValue(BiciUsuario.class);
                            in.putExtra("usuarioPropio", uPropio);
                            in.putExtra("usuarioOtro", u);
                            startActivity(in);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
                    final FirebaseAuth fireAuth = FirebaseAuth.getInstance();
                    Query queryAmigos1 = fireDB.getReference("amistades/").orderByChild("amigo2").equalTo
                            (fireAuth.getCurrentUser().getUid());
                    queryAmigos1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                            while (iter.hasNext()) {
                                DataSnapshot ds = iter.next();
                                if (ds.child("estado").getValue(String.class).equals("solicitado") && ds.child("amigo1").getValue(String.class).equals(u.getUid())) {
                                    Amistad a = new Amistad(u.getUid(), fireAuth.getCurrentUser().getUid(), "aceptado");
                                    ds.getRef().setValue(a);
                                    fab.setEnabled(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        } else if (tipoVista.equals("amigo_encontrado")) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
                    final FirebaseAuth fireAuth = FirebaseAuth.getInstance();
                    Query queryAmigos1 = fireDB.getReference("amistades/").orderByChild("amigo2").equalTo
                            (fireAuth.getCurrentUser().getUid());
                    queryAmigos1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                            while (iter.hasNext()) {
                                DataSnapshot ds = iter.next();
                                if (ds.child("estado").getValue(String.class).equals("solicitado") && ds.child("amigo1").getValue(String.class).equals(u.getUid())) {
                                    Amistad a = new Amistad(u.getUid(), fireAuth.getCurrentUser().getUid(), "aceptado");
                                    ds.getRef().setValue(a);
                                    fab.setEnabled(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
