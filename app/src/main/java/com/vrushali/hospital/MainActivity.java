package com.vrushali.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText loginEmail, password;
    MaterialButton login;
    TextView newuser;
    TextView Adminlogin;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        loginEmail = findViewById(R.id.loginEmail);
        password = findViewById(R.id.password);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent intent;

                    if (!user.getUid().equals("xGERbidk1fWEWcs2Qdh4upIu2xv2")) {
                        intent = new Intent(MainActivity.this, MainActivitylite.class);
                    } else {
                        intent = new Intent(MainActivity.this, Adminlite.class);
                    }
                    startActivity(intent);
                    finish();

                    // Define what your app should do if no activity can handle the intent.


                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = loginEmail.getText().toString().trim();
                final String pass = password.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is Required");
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is Required");
                } else {
                    loader.setMessage("login in process");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                                //startActivity(intent);
                                //finish();
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });

                }
            }
        });
    }

    public void OpenActivity(View v) {
        Toast.makeText(MainActivity.this, "Sign Up Page", Toast.LENGTH_SHORT).show();
        newuser = findViewById(R.id.newuser);
        Intent intent = new Intent(MainActivity.this, signupactivity.class);
        startActivity(intent);

    }

    public void Admingo(View v) {
        Adminlogin = findViewById(R.id.Adminlogin);
        Intent intent = new Intent(MainActivity.this, Adminlogin1.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}