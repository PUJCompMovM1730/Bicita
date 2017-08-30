package com.example.usuario.bicita;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VerRecorridos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listaRecorridos;

    String[] descripcion;
    int[] imagenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_recorridos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.agregarAmigo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ver_recorridos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
