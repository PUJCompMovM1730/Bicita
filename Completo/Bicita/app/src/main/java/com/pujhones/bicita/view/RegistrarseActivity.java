package com.pujhones.bicita.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pujhones.bicita.R;
import com.pujhones.bicita.model.BiciUsuario;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegistrarseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private	FirebaseAuth.AuthStateListener mAuthListener;

    Spinner gen;
    EditText nombre;
    EditText correo;
    EditText altura;
    EditText peso;
    EditText pssw;

    Button registro;

    public final static String TAG = "REGISTRO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();

        nombre = (EditText) findViewById(R.id.nombreR);
        correo = (EditText) findViewById(R.id.correoR);
        altura = (EditText) findViewById(R.id.alturaR);
        peso = (EditText) findViewById(R.id.pesoR);
        pssw = (EditText) findViewById(R.id.psswR);
        registro = (Button) findViewById(R.id.botonRegistro);

        gen = (Spinner) findViewById(R.id.generoR);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.generoSexual, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gen.setAdapter(adapter);
        gen.setOnItemSelectedListener(this);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(correo.getText().toString(), pssw.getText().toString())
                        .addOnCompleteListener(RegistrarseActivity.this, new OnCompleteListener<AuthResult>()	 {
                            @Override
                            public	void	onComplete(@NonNull Task<AuthResult> task)	{
                                if(task.isSuccessful()){
                                    Log.d(TAG,	"createUserWithEmail:onComplete:"	+	task.isSuccessful());
                                    FirebaseUser user	=	mAuth.getCurrentUser();
                                    if(user!=null){
                                        UserProfileChangeRequest.Builder upcrb =	new	UserProfileChangeRequest.Builder();
                                        upcrb.setDisplayName(nombre.getText().toString());
                                        //upcrb.setPhotoUri(Uri.parse("path/to/pic"));//fake	 uri,	real	one	coming	soon
                                        user.updateProfile(upcrb.build());
                                        startActivity(new Intent(RegistrarseActivity.this, MapsActivity.class));
                                    }
                                }
                                if	(!task.isSuccessful())	 {
                                    Toast.makeText(RegistrarseActivity.this,	R.string.auth_failed+	task.getException().toString(),Toast.LENGTH_SHORT).show();
                                    Log.e(TAG,	task.getException().getMessage());
                                }else{
                                    boolean sexo;
                                    if(gen.getSelectedItem().equals("Masculino")){
                                        sexo = true;
                                    }else{
                                        sexo = false;
                                    }
                                    BiciUsuario bu = new BiciUsuario(Double.parseDouble(altura.getText().toString()),
                                                                     Double.parseDouble(peso.getText().toString()),
                                                                     nombre.getText().toString(),
                                                                     correo.getText().toString(),
                                                                     pssw.getText().toString(), sexo);
                                    Toast.makeText(RegistrarseActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
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
}

