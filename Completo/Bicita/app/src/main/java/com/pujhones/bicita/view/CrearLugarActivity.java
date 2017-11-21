package com.pujhones.bicita.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pujhones.bicita.R;
import com.pujhones.bicita.model.BiciEmpresa;
import com.pujhones.bicita.model.Lugar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrearLugarActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText nombre;
    EditText descripcion;
    RadioButton promocionar;
    EditText lug;

    double longitud;
    double latitud;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageRef;

    Button registro;
    Button elegir;
    CircleImageView cam;
    Button galeria;

    Geocoder geo;

    public final static String TAG = "REGISTRO_EMPRESAS";
    public final static String PATH_BICIUSUARIOS = "lugares/";
    public final static String PATH_STORAGE_FOTOSPRFIL = "fotosPerfil/";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    public static final double lowerLeftLatitude = 4.484987;
    public static final double lowerLeftLongitude= -74.212539;
    public static final double upperRightLatitude= 4.769700;
    public static final double upperRigthLongitude= -74.009979;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_lugar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(PATH_BICIUSUARIOS);
        storageRef = FirebaseStorage.getInstance().getReference(PATH_BICIUSUARIOS);

        nombre = (EditText) findViewById(R.id.nombreLugar);
        lug = (EditText) findViewById(R.id.stringLugar);
        geo = new Geocoder(getBaseContext());

        descripcion = (EditText) findViewById(R.id.descripcionLugar);
        promocionar = (RadioButton) findViewById(R.id.promocionar);

        registro = (Button) findViewById(R.id.botonRegistro_lugar);
        elegir = (Button) findViewById(R.id.botonElegir_lugar);
        galeria = (Button) findViewById(R.id.btnGaleria_lugar);
        cam = (CircleImageView) findViewById(R.id.btnCamara_lugar);

        elegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng aux=  buscarDireccion(lug.getText().toString());
                longitud = aux.longitude;
                latitud = aux.latitude;

            }
        });
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarGaleriaConPermiso();
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamaraConPermiso();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!nombre.getText().toString().equals("") &&
                        !descripcion.getText().toString().equals("")
                       ) {
                    String urlPath = PATH_STORAGE_FOTOSPRFIL + "URL::" + nombre.getText().toString() + ".jpg";
                    StorageReference url = storageRef.child(urlPath);

                    Lugar bu = new Lugar(
                            nombre.getText().toString(),
                            descripcion.getText().toString(),
                            urlPath,
                            longitud,
                            latitud,
                            0,
                            promocionar.isEnabled());
                    database.getReference().push().setValue(bu);
                    Log.i("algo", "dkalshdaskjd");
                    if (descripcion.getText().toString().equals("")) {
                        Toast.makeText(CrearLugarActivity.this, "La descripción no puede estar vacia", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    protected void cargarGaleriaConPermiso() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously*
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            cargarGaleria();
        }
    }

    protected void abrirCamaraConPermiso() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show an expanation to the user *asynchronously*
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            abrirCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, continue with task related to permission
                    cargarGaleria();
                } else {
                    // permission denied, disable functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, continue with task related to permission
                    abrirCamara();
                } else {
                    // permission denied, disable functionality that depends on this permission.
                }
                return;
            }

        }
    }

    protected void cargarGaleria() {
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        startActivityForResult(pickImage, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    protected void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, MY_PERMISSIONS_REQUEST_CAMERA);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        cam.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    cam.setImageBitmap(imageBitmap);
                }
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos);
        Toast.makeText(view.getContext(), parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
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

                    return position;
                } else {
                    Toast.makeText(CrearLugarActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        } else {
            Toast.makeText(CrearLugarActivity.this, "La dirección esta vacía", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


}
