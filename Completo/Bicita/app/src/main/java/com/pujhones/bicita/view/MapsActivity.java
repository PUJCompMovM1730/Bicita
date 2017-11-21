package com.pujhones.bicita.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pujhones.bicita.R;
import com.pujhones.bicita.model.CustomDrawerButton;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback{

    public static final double lowerLeftLatitude = 4.484987;
    public static final double lowerLeftLongitude= -74.212539;
    public static final double upperRightLatitude= 4.769700;
    public static final double upperRigthLongitude= -74.009979;

    private static final int REQUEST_CHECK_SETTINGS = 2;
    public	final	static	double	RADIUS_OF_EARTH_KM = 6371;
    private static final String TAG = "This TAG";
    private static final int REQUEST_LUGAR = 24;

    public static String FACEBOOK_URL = "https://www.facebook.com/HumorInfomatico/";
    public static String FACEBOOK_PAGE_ID = "HumorInfomatico";

    Button IniciarActividad;
    Button CancelarRecorrido;
    private GoogleMap mMap;
    android.support.design.widget.FloatingActionButton fab;
    android.support.design.widget.FloatingActionButton friends;
    android.support.design.widget.FloatingActionButton floatingActionButton5;
    android.support.design.widget.FloatingActionButton floatingActionButton3;
    Geocoder geo;
    Marker bogotaBike;
    SearchView texto;

    SearchView search_recorrido_inicio_1;
    SearchView search_recorrido_fin_2;
    Marker mIni;
    Marker mFin;
    LatLng ini;
    LatLng fin;

    LinearLayout buscarPrincipal;
    Button back;
    LinearLayout modal;
    android.support.constraint.ConstraintLayout nuevoRecorrido;
    ImageButton crear;
    ImageButton imageView6;
    CustomDrawerButton customDrawerButton;
    Double lat=4.626925;
    Double lon=-74.063905;
    android.support.constraint.ConstraintLayout rutaCreada;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    boolean focused;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        texto = (SearchView) findViewById(R.id.search);
        modal = (LinearLayout) findViewById(R.id.modal);
        back = (Button) findViewById(R.id.back);
        geo = new Geocoder(getBaseContext());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();
        nuevoRecorrido = (android.support.constraint.ConstraintLayout) findViewById(R.id.nuevoRecorrido);
        IniciarActividad = (Button) findViewById(R.id.IniciarActividad);
        search_recorrido_inicio_1 = (SearchView) findViewById(R.id.search_recorrido_inicio_1);
        search_recorrido_fin_2 = (SearchView) findViewById(R.id.search_recorrido_fin_2);
        CancelarRecorrido = (Button) findViewById(R.id.CancelarRecorrido);
        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        friends = (android.support.design.widget.FloatingActionButton) findViewById(R.id.Friends);
        rutaCreada = (android.support.constraint.ConstraintLayout) findViewById(R.id.rutaCreada);
        floatingActionButton5 = (android.support.design.widget.FloatingActionButton) findViewById(R.id.floatingActionButton5);
        floatingActionButton3 = (android.support.design.widget.FloatingActionButton) findViewById(R.id.floatingActionButton3);
        buscarPrincipal = (LinearLayout) findViewById(R.id.buscarPrincipal);
        IniciarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                ruta(ini,fin);
                back.setVisibility(View.INVISIBLE);
                modal.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                friends.setVisibility(View.INVISIBLE);
                nuevoRecorrido.setVisibility(View.INVISIBLE);
                rutaCreada.setVisibility(View.VISIBLE);
                buscarPrincipal.setVisibility(View.INVISIBLE);
            }
        });
        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Yo me voy en BICITA");

                startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.INVISIBLE);
                modal.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                friends.setVisibility(View.VISIBLE);
            }
        });
        texto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                buscarDireccion(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.INVISIBLE);
                modal.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                friends.setVisibility(View.VISIBLE);
                nuevoRecorrido.setVisibility(View.INVISIBLE);
                rutaCreada.setVisibility(View.INVISIBLE);
                buscarPrincipal.setVisibility(View.VISIBLE);
                if (mMap!=null)
                    mMap.clear();
            }
        });

        search_recorrido_inicio_1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                LatLng encontrado = buscarDireccion(s);
                if (encontrado!=null)
                {
                    ini=encontrado;
                    if (mIni!=null)
                        mIni.remove();
                    mIni = mMap.addMarker(new MarkerOptions().position(encontrado)
                            .title("Inicio").snippet(s));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        search_recorrido_fin_2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                LatLng encontrado = buscarDireccion(s);
                if (encontrado!=null)
                {
                    fin=encontrado;
                    if (mFin!=null)
                        mFin.remove();
                    mIni = mMap.addMarker(new MarkerOptions().position(encontrado)
                            .title("Fin").snippet(s));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        CancelarRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.INVISIBLE);
                modal.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                friends.setVisibility(View.VISIBLE);
                nuevoRecorrido.setVisibility(View.INVISIBLE);
                buscarPrincipal.setVisibility(View.VISIBLE);
                if (mMap!=null)
                    mMap.clear();
                if (mIni!=null)
                    mIni.remove();
                if (mFin!=null)
                    mFin.remove();
            }
        });
        mLocationCallback =	new	LocationCallback()	 {
            @Override
            public	void	onLocationResult(LocationResult locationResult)	 {
                Location	location	=	locationResult.getLastLocation();
                Log.i("LOCATION",	"Location	update	in	the	callback:	"	+	location);
                if	(location	 !=	null)	{
                    //ACA SE OBTIENEN LOS DATOS----------------------------------------------------------########################################
                    lat=location.getLatitude();
                    lon=location.getLongitude();
                    if (mMap!=null)
                    {
                        focused=false;
                        //mMap.clear();
                        if (bogotaBike!=null)
                            bogotaBike.remove();
                        bogotaBike = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)).title("Marcador actual").snippet("Ubicacion"));
                        if (focused)
                        {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)));
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        }
                    }
                }
            }
        };
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.VISIBLE);
                modal.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
                friends.setVisibility(View.INVISIBLE);
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), AmigosActivity.class);
                startActivity(in);
            }
        });

        crear = (ImageButton) findViewById(R.id.crearRecorridoBtn);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent in = new Intent(getBaseContext(), NuevoRecorridoInicioActivity.class);
                //startActivity(in);
                back.setVisibility(View.INVISIBLE);
                modal.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                friends.setVisibility(View.INVISIBLE);
                nuevoRecorrido.setVisibility(View.VISIBLE);
                buscarPrincipal.setVisibility(View.INVISIBLE);
                search_recorrido_inicio_1.setQuery(""+lat+","+lon,true);
                //ruta(new LatLng(4.632594, -74.067799),new LatLng(4.624039, -74.078785));
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        customDrawerButton = (CustomDrawerButton) findViewById(R.id.btnOpenDrawer);
        customDrawerButton.setDrawerLayout( drawer );
        customDrawerButton.getDrawerLayout().addDrawerListener( customDrawerButton );
        customDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDrawerButton.changeState();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    //TENIA UN OVERRIDE---------------------------------------------------------------------------------------------#
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrar_sesion:
                mAuth.signOut();
                Intent in = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(in);
                return true;
            case 2: //cONFIGURACIÓN
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //TENIA UN OVERRIDE---------------------------------------------------------------------------------------------#
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, CrearRecorridoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intentt = new Intent(this, NuevoRecorridoInicioActivity.class);
            startActivity(intentt);

        } else if (id == R.id.nav_slideshow) {
            Intent intent= new Intent(this, VerAmigoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent= new Intent(this, VerAmigoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_verrecorridos) {
            Intent intent= new Intent(this, VerRecorridoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this,PerfilPropioActivity.class);
            startActivity(intent);
        } else if (id == R.id.promo) {
            Intent intent = new Intent(this,Promocion.class);
            startActivity(intent);
        } else if (id == R.id.cerrar) {
            mAuth.signOut();
            Intent in = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(in);
            //Toast.makeText(getBaseContext(), "entroooooo", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_clima) {
            Intent in = new Intent(getBaseContext(), ClimaActivity.class);
            startActivity(in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //---------------------------------------------------------------------------------------------------DRAWER
    private LatLng buscarDireccion(String addressString) {
        if (!addressString.isEmpty()) {
            try {
                List<Address> addresses = geo.getFromLocationName(
                        addressString, 2,
                        lowerLeftLatitude,
                        lowerLeftLongitude,
                        upperRightLatitude,
                        upperRigthLongitude);
                if (addresses != null && !addresses.isEmpty()) {
                    android.location.Address addressResult = addresses.get(0);
                    LatLng position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                    if (mMap != null) {

                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    }
                    return position;
                } else {
                    Toast.makeText(MapsActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        } else {
            Toast.makeText(MapsActivity.this, "La dirección esta vacía", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    protected	LocationRequest createLocationRequest()	 {
        LocationRequest mLocationRequest =	new	LocationRequest();
        mLocationRequest.setInterval(1000);	 //tasa de	refresco en	milisegundos
        mLocationRequest.setFastestInterval(5000);	 //máxima tasa de	refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return	mLocationRequest;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bogota = new LatLng(lat, lon);
        Marker marcador1 = mMap.addMarker(new MarkerOptions().position(bogota).title("Marcador en Gran estacion"));
        Marker marcador2 = mMap.addMarker(new MarkerOptions().position(bogota).title("GRAN ESTACIÓN")
                .snippet("Esta es una prueba")//Texto de información
                .alpha(0.5f));
        bogotaBike = mMap.addMarker(new MarkerOptions().position(bogota)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)).title("Marcador actual").snippet("Ubicacion"));
        marcador1.setVisible(false);
        marcador2.setVisible(false);
        //bogotaBike.setVisible(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
    }

    @Override
    protected void onStart() {
        focused=true;
        super.onStart();

        solicitar();
    }

    void solicitar() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                final Context c = this;
                builder.setTitle("Permisos")
                        .setMessage("Los permisos son necesarios para la funcionalidad completa de la aplicación")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) c, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            localizar();
        }
    }

    @Override
    protected	void	onActivityResult(int requestCode,	 int resultCode,	 Intent	 data)	 {
        Log.i("codifgo", ""+requestCode);
        if (requestCode ==REQUEST_LUGAR){
            CameraPosition pos= this.mMap.getCameraPosition();
            double lati= pos.target.latitude;
            double longi= pos.target.longitude;
            Log.i("LLEGOOO",""+longi);
            Intent inte = new Intent(this, CrearLugarActivity.class);
            inte.putExtra("lati", lati);
            inte.putExtra("longi",longi);
            setResult(REQUEST_LUGAR, inte);
           finish();
        }
        switch	(requestCode)	 {
            case	REQUEST_CHECK_SETTINGS:	 {
                if	(resultCode ==	RESULT_OK)	 {
                    startLocationUpdates();	 	//Se	encendió la	localización!!!
                }	else	{
                    Toast.makeText(this,
                            "Sin	acceso a	localización,	hardware	deshabilitado!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
            case REQUEST_LUGAR: {
                //// TODO: 20/11/2017
                CameraPosition pos= this.mMap.getCameraPosition();
                double lati= pos.target.latitude;
                double longi= pos.target.longitude;
                Log.i("LLEGOOO",""+longi);
                Intent inte = new Intent(this, CrearLugarActivity.class);
                inte.putExtra("lati", lati);
                inte.putExtra("longi",longi);
                startActivity(inte);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    localizar();
                    Log.i("dsadjksjadlsa", "Pruebo todo");
                } else {
                    // TODO: Hacer el else
                }
                return;
            }
        }
    }

    public void localizar() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationSettingsRequest.Builder builder	=	new
                LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client	 =	LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task	=	client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this,	 new	OnSuccessListener<LocationSettingsResponse>()
        {
            @Override
            public	void	onSuccess(LocationSettingsResponse locationSettingsResponse)	 {
                startLocationUpdates();	 //Todas las condiciones para	recibir localizaciones
            }
        });



        task.addOnFailureListener(this,	 new	OnFailureListener()	 {
            @Override
            public	void	onFailure(@NonNull Exception	 e)	{
                int statusCode =	((ApiException)	e).getStatusCode();
                switch	(statusCode)	{
                    case	CommonStatusCodes.RESOLUTION_REQUIRED:
                        //	Location	settings	are	not	satisfied,	but	this	can	be	fixed	by	showing	the	user	a	dialog.
                        break;
                    case	LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //	Location	settings	are	not	satisfied.	No	way	to	fix	the	settings	so	we	won't	show	the	dialog.
                        break;
                }
            }
        });


        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new
                OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i("LOCATION", "onSuccess location");
                        if (location != null) {
                            lat=location.getLatitude();
                            lon=location.getLongitude();
                            if (focused)
                            {
                                //focused=false;
                                if (mMap!=null)
                                {
                                    mMap.clear();
                                    bogotaBike = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)).title("Marcador actual").snippet("Ubicacion"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)));
                                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                                }
                            }
                        }
                    }
                });
    }

    private	void	startLocationUpdates()	 {
        if	(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)	 ==
                PackageManager.PERMISSION_GRANTED)	 {				//Verificación de	permiso!!
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,	 mLocationCallback, null);
        }
    }

    public	double	distance(double	 lat1,	double	long1,	double	lat2,	double	long2)	{
        double	latDistance =	Math.toRadians(lat1	 - lat2);
        double	lngDistance =	Math.toRadians(long1	 - long2);
        double	a	=	Math.sin(latDistance /	2)	*	Math.sin(latDistance /	2)
                +	Math.cos(Math.toRadians(lat1))	 *	Math.cos(Math.toRadians(lat2))
                *	Math.sin(lngDistance /	2)	*	Math.sin(lngDistance /	2);
        double	c	=	2	*	Math.atan2(Math.sqrt(a),	 Math.sqrt(1	- a));
        double	result	=	RADIUS_OF_EARTH_KM	 *	c;
        return	Math.round(result*100.0)/100.0;
    }

    public void ruta(LatLng latLngIni,LatLng latLngFin) {
        //use Google Direction API to get the route between these Locations
        String directionApiPath = Helper.getUrl(String.valueOf(latLngIni.latitude), String.valueOf(latLngIni.longitude),
                String.valueOf(latLngFin.latitude), String.valueOf(latLngFin.longitude));
        Log.d(TAG, "Path " + directionApiPath);
        getDirectionFromDirectionApiServer(directionApiPath);
    }

    private void getDirectionFromDirectionApiServer(String url){
        GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
                Request.Method.GET,
                url,
                DirectionObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());
        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(serverRequest);
    }

    ////////////////////////////////////////////////////////////////


    private Response.Listener<DirectionObject> createRequestSuccessListener() {
        return new Response.Listener<DirectionObject>() {
            @Override
            public void onResponse(DirectionObject response) {
                try {
                    Log.d("JSON Response", response.toString());
                    if(response.getStatus().equals("OK")){
                        List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                        drawRouteOnMap(mMap, mDirections);
                    }else{
                        Toast.makeText(MapsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
    }
    private List<LatLng> getDirectionPolylines(List<RouteObject> routes){
        List<LatLng> directionList = new ArrayList<LatLng>();
        for(RouteObject route : routes){
            List<LegsObject> legs = route.getLegs();
            for(LegsObject leg : legs){
                List<StepsObject> steps = leg.getSteps();
                for(StepsObject step : steps){
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline){
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }
    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(positions.get(1).latitude, positions.get(1).longitude))
                .zoom(17)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

}