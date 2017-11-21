package com.pujhones.bicita.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.database.Cursor;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pujhones.bicita.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private final String TAG = "LOGIN";
    private final int SIGNUP_REQUEST = 100;

    protected EditText etEmail, etPassword;
    protected Button btnLogin, btnSignup, btnEmpresa;

    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.contrasenaLogin);

        btnLogin = (Button) findViewById(R.id.botonLogin);
        btnSignup = (Button) findViewById(R.id.botonRegister);
        btnEmpresa = (Button) findViewById(R.id.botonRegisterEmpresa);


        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    launchMaps();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        etEmail.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                    etEmail.setError("El texto introducido no es una dirección de correo electrónico.");
                } else {
                    etEmail.setError(null);
                }
            }
        });
        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),RegistrarEmpresaActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Exception e = task.getException();
                                        String msg = "";
                                        if (e != null) {
                                            msg = e.getMessage();
                                        } else {
                                            msg = getString(R.string.unknown_error_message);
                                        }
                                        Log.w(TAG, "signInWithEmail:failed", e);
                                        Toast.makeText(LoginActivity.this, msg,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        launchMaps();
                                    }
                                }
                            }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String msg = "";
                            if (e != null) {
                                msg = e.getMessage();
                            } else {
                                msg = getString(R.string.unknown_error_message);
                            }
                            Log.w(TAG, "signInWithEmail:failed", e);
                            Toast.makeText(LoginActivity.this, msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.w(TAG, "signInWithEmail:failed", e);
                    Toast.makeText(LoginActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), RegistrarseActivity.class);
                startActivityForResult(in, SIGNUP_REQUEST);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SIGNUP_REQUEST:
                if (resultCode == RESULT_OK) {
                    launchMaps();
                }

        }
    }

    private void launchMaps() {
        Intent in = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(in);
    }
}

