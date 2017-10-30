package com.pujhones.bicita.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.pujhones.bicita.Manifest;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo;
import com.pujhones.bicita.R;

import java.util.List;
import java.util.jar.Manifest;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback {

    public static final double lowerLeftLatitude = 4.484987;
    public static final double lowerLeftLongitude= -74.212539;
    public static final double upperRightLatitude= 4.769700;
    public static final double upperRigthLongitude= -74.009979;

    private static final int REQUEST_CHECK_SETTINGS = 2;
    public	final	static	double	RADIUS_OF_EARTH_KM	 =	6371;

    private GoogleMap mMap;
    Button fab;
    Button friends;
    Geocoder geo;
    SearchView texto;
    Button back;
    LinearLayout modal;
    ImageButton crear;
    ImageButton imageView6;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);//------------------------------------------------PONER

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        texto = (SearchView) findViewById(R.id.search);
        modal = (LinearLayout) findViewById(R.id.modal);
        back = (Button) findViewById(R.id.back);
        geo = new Geocoder(getBaseContext());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();

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

        mLocationCallback =	new	LocationCallback()	 {
            @Override
            public	void	onLocationResult(LocationResult locationResult)	 {
                Location	location	=	locationResult.getLastLocation();
                Log.i("LOCATION",	"Location	update	in	the	callback:	"	+	location);
                if	(location	 !=	null)	{
                    //ACA SE OBTIENEN LOS DATOS----------------------------------------------------------
                }
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = (Button) findViewById(R.id.fab);
        friends = (Button) findViewById(R.id.Friends);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent in = new Intent(view.getContext(), IniciarRecorridoActivity.class);
                //startActivity(in);
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
                Intent in = new Intent(getBaseContext(), NuevoRecorridoInicioActivity.class);
                startActivity(in);
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


        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    /*
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
                    Toast.makeText(getBaseContext(), "entroooooo", Toast.LENGTH_SHORT).show();
                    return true;
                case 2: //cONFIGURACIÓN
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @SuppressWarnings("StatementWithEmptyBody")
        //@Override//-----------------------------------PONER
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
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        */
    private void buscarDireccion(String addressString) {
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
                } else {
                    Toast.makeText(MapsActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        } else {
            Toast.makeText(MapsActivity.this, "La dirección esta vacía", Toast.LENGTH_SHORT).show();
        }
    }


    protected	LocationRequest createLocationRequest()	 {
        LocationRequest mLocationRequest =	new	LocationRequest();
        mLocationRequest.setInterval(10000);	 //tasa de	refresco en	milisegundos
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
        LatLng bogota = new LatLng(4.6476286, -74.1038169);
        Marker marcador1 = mMap.addMarker(new MarkerOptions().position(bogota).title("Marcador en Gran estacion"));
        Marker marcador2 = mMap.addMarker(new MarkerOptions().position(bogota).title("GRAN ESTACIÓN")
                .snippet("Esta es una prueba")//Texto de información
                .alpha(0.5f));
        Marker bogotaBike = mMap.addMarker(new MarkerOptions().position(bogota)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)).title("Marcador actual").snippet("Ubicacion"));
        marcador1.setVisible(false);
        marcador2.setVisible(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
    }

    @Override
    protected void onStart() {
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

            //localizar();
        }
    }

    @Override
    protected	void	onActivityResult(int requestCode,	 int resultCode,	 Intent	 data)	 {
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
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //localizar();
                    Log.i("dsadjksjadlsa", "Pruebo todo");
                } else {
                    //// TODO: Hacer el else
                }
                return;
            }
        }
    }

    public void localizar() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            Log.i("	LOCATION	",
                                    "Longitud:	 " + location.getLongitude());
                            Log.i("	LOCATION	",
                                    "Latitud:	 " + location.getLatitude());
                        }
                    }
                });
        }
        private	void	startLocationUpdates()	 {
            if	(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)	 ==
                    PackageManager.PERMISSION_GRANTED)	 {				//Verificación de	permiso!!
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,	 mLocationCallback,
                        null);
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
    }
