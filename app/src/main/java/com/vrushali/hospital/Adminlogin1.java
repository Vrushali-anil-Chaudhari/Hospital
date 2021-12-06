package com.vrushali.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Adminlogin1 extends AppCompatActivity {
TextView goback;
EditText username1,password1;
TextView Reg;
Button button;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin1);
        Reg=findViewById(R.id.Reg);
        username1=findViewById(R.id.username1);
        password1=findViewById(R.id.password1);
        button=findViewById(R.id.button);
        loader= new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();

            }

        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=username1.getText().toString().trim();
                final String pass=password1.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    username1.setError("Email is Required");
                }
                if(TextUtils.isEmpty(pass)){
                    password1.setError("Password is Required");
                }
                else{
                    loader.setMessage("login in process");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Adminlogin1.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Adminlogin1.this,Adminlite.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(Adminlogin1.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });

                }
            }
        });
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Adminlogin1.this, "Admin Registration", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Adminlogin1.this,Adminreg.class);
                startActivity(intent);
            }
        });
    }
    public void Back1(View v){
        goback=findViewById(R.id.goback);
        Toast.makeText(Adminlogin1.this, "Login Page", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Adminlogin1.this,MainActivity.class);
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