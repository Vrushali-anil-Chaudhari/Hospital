package com.vrushali.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Adminreg extends AppCompatActivity {
TextView GoBack;
EditText entername,email,phonenumber,password1;
MaterialButton regbutton;
private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminreg);
        entername = findViewById(R.id.entername);
        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.phonenumber);
        password1 = findViewById(R.id.password1);
        regbutton=findViewById(R.id.regbutton);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = entername.getText().toString().trim();
                final String phone = phonenumber.getText().toString().trim();
                final String emails = email.getText().toString().trim();

                final String password = password1.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    entername.setError("Name is Required");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    phonenumber.setError("Phone number is Required");
                    return;
                }
                if (TextUtils.isEmpty(emails)) {
                    email.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    password1.setError("Password is Required");
                    return;
                }

                 else {
                    loader.setMessage("Registration in progress"); //for loading symbol
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(emails, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                String error = task.getException().toString();
                                Toast.makeText(Adminreg.this, "Error occured" + error, Toast.LENGTH_SHORT).show();
                            } else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Admin's Data").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id",currentUserId);
                                userInfo.put("name", name);
                                userInfo.put("email", emails);

                                userInfo.put("phonenumber", phone);

                                userInfo.put("password", password);


                                userInfo.put("type", "Admin");
                                userDatabaseRef.updateChildren(userInfo)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Adminreg.this,
                                                            "Details set successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Adminreg.this,
                                                            task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                                finish();
                                                loader.dismiss();

                                            }

                                        });
                                Intent intent = new Intent(Adminreg.this,
                                        Adminlogin1.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }


                        }

                    });

                }
            }
        });


    }

    public void Back (View v){
        GoBack = findViewById(R.id.GoBack);
        Toast.makeText(this, "Login Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Adminreg.this, Adminlogin1.class);
        startActivity(intent);

    }
}