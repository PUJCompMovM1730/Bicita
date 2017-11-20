package com.pujhones.bicita.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pujhones.bicita.R;
import com.pujhones.bicita.model.BiciUsuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActualizarPerfilActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageRef;

    EditText nombre;
    EditText altura;
    EditText peso;
    EditText correo;
    EditText desc;

    CircleImageView cam;

    Button galeria;
    Button actualizar;

    public final static String TAG = "ACTUALIZAR PERFIL";
    public final static String PATH_BICIUSUARIOS = "biciusuarios/";
    public final static String PATH_STORAGE_FOTOSPRFIL = "fotosPerfil/";
    public final static String PATH_URL_BICITA = "gs://bicita-6ebab.appspot.com/";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();


        nombre = (EditText) findViewById(R.id.nombreA);
        altura = (EditText) findViewById(R.id.alturaA);
        peso = (EditText) findViewById(R.id.pesoA);
        correo = (EditText) findViewById(R.id.correoA);
        desc = (EditText) findViewById(R.id.descA);

        correo.setFocusable(false);

        cam = (CircleImageView) findViewById(R.id.btnCamara);
        galeria = (Button) findViewById(R.id.btnGaleria);
        actualizar = (Button) findViewById(R.id.botonActualizar);

        try {
            StorageReference sr = FirebaseStorage.getInstance().getReferenceFromUrl(PATH_URL_BICITA + PATH_STORAGE_FOTOSPRFIL + "URL::" + mAuth.getCurrentUser().getUid() + ".jpg");
            final File localFile = File.createTempFile("images", "jpg");
            sr.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    cam.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

        // Get a reference to our posts
        myRef = database.getReference(PATH_BICIUSUARIOS + mAuth.getCurrentUser().getUid());

        // Attach a listener to read the data at our posts reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, dataSnapshot.getValue().toString());
                String str = dataSnapshot.getValue().toString();
                Map<String, String> obj = (Map<String, String>) dataSnapshot.getValue();
                nombre.setText(obj.get("nombre"));
                peso.setText(String.valueOf(obj.get("peso")));
                altura.setText(String.valueOf(obj.get("altura")));
                correo.setText(obj.get("correo").toString());
                desc.setText(obj.get("biografia").toString());

               /* StorageReference foto = storageRef.child("").*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarGaleriaConPermiso();
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamaraConPermiso();
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){

                    //Elimina foto anterior
                    StorageReference url = storageRef.child(PATH_STORAGE_FOTOSPRFIL + "URL::" + user.getUid() + ".jpg");
                    url.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                    //Agrega foto nueva
                    String urlPath = PATH_STORAGE_FOTOSPRFIL + "URL::" + user.getUid() + ".jpg";
                    url = storageRef.child(urlPath);


                    // Get the data from an ImageView as bytes
                    cam.setDrawingCacheEnabled(true);
                    cam.buildDrawingCache();
                    Bitmap bitmap = cam.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = url.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });

                    database.getReference(PATH_BICIUSUARIOS + user.getUid()).child("nombre").setValue(nombre.getText().toString());
                    database.getReference(PATH_BICIUSUARIOS + user.getUid()).child("altura").setValue(altura.getText().toString());
                    database.getReference(PATH_BICIUSUARIOS + user.getUid()).child("peso").setValue(peso.getText().toString());
                    database.getReference(PATH_BICIUSUARIOS + user.getUid()).child("photoURL").setValue(urlPath);
                    database.getReference(PATH_BICIUSUARIOS + user.getUid()).child("biografia").setValue(desc.getText().toString());

                    Intent in = new Intent(v.getContext(), MapsActivity.class);
                    Toast.makeText(v.getContext(), "Actualización Satisfactoria", Toast.LENGTH_SHORT).show();
                    startActivity(in);

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

}
