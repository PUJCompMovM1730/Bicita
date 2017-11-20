package com.pujhones.bicita.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.pujhones.bicita.R;
import com.pujhones.bicita.model.BiciUsuario;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerAmigoActivity extends AppCompatActivity {

    protected final String TAG = "VER AMIGO";

    protected CircleImageView fotoPerfil;
    protected TextView tvNombre, tvBiografia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_amigo);

        Intent in = getIntent();
        final BiciUsuario u = (BiciUsuario) in.getSerializableExtra("usuario");

        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvBiografia = (TextView) findViewById(R.id.tvBiografia);

        tvNombre.setText(u.getNombre());
        tvBiografia.setText(u.getBiografia());

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
}
