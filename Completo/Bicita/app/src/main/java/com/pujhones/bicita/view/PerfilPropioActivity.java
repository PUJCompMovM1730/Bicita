package com.pujhones.bicita.view;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pujhones.bicita.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilPropioActivity extends AppCompatActivity {

    public final static String TAG = "ACTUALIZAR PERFIL";
    public final static String PATH_BICIUSUARIOS = "biciusuarios/";
    public final static String PATH_STORAGE_FOTOSPRFIL = "fotosPerfil/";
    public final static String PATH_URL_BICITA = "gs://bicita-6ebab.appspot.com/";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageRef;

    CircleImageView image;

    TextView nombre;
    TextView correo;
    TextView desc;


    Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_propio);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        nombre = (TextView) findViewById(R.id.textNombre);
        correo = (TextView) findViewById(R.id.textEmail);
        desc   = (TextView) findViewById(R.id.textDesc);

        image = (CircleImageView) findViewById(R.id.profile_image);

        try {
            StorageReference sr = FirebaseStorage.getInstance().getReferenceFromUrl(PATH_URL_BICITA + PATH_STORAGE_FOTOSPRFIL + "URL::" + mAuth.getCurrentUser().getUid() + ".jpg");
            final File localFile = File.createTempFile("images", "jpg");
            sr.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {

        }


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
                correo.setText(obj.get("correo").toString());
                desc.setText(obj.get("biografia").toString());

               /* StorageReference foto = storageRef.child("").*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(),ActualizarPerfilActivity.class);
                startActivity(in);
            }
        });
    }
}
