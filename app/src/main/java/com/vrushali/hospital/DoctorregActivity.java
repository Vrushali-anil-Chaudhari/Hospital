package com.vrushali.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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

public class DoctorregActivity extends AppCompatActivity {
TextView Piche;
EditText entername,idnumber,phonenumber,email,gender,age,password1;
EditText Timings,Specialization,Department;
MaterialButton regbutton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorreg);
        Piche = findViewById(R.id.Piche);
        regbutton = findViewById(R.id.regbutton);
        entername = findViewById(R.id.entername);
        idnumber = findViewById(R.id.idnumber);
        phonenumber = findViewById(R.id.phonenumber);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        password1 = findViewById(R.id.password1);
        Timings = findViewById(R.id.Timings);
        Specialization = findViewById(R.id.Specialization);
        Department = findViewById(R.id.Department);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        Piche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorregActivity.this, "Login Page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DoctorregActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = entername.getText().toString().trim();
                final String id = idnumber.getText().toString().trim();
                final String phone = phonenumber.getText().toString().trim();
                final String emails = email.getText().toString().trim();
                final String genders = gender.getText().toString().trim();
                final String Age = age.getText().toString().trim();
                final String password = password1.getText().toString().trim();
                final String time = Timings.getText().toString().trim();
                final String special = Specialization.getText().toString().trim();
                final String dept = Department.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    entername.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(id)) {
                    idnumber.setError("Id Number is Required");
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
                if (TextUtils.isEmpty(genders)) {
                    gender.setError("Gender is Required");
                    return;
                }
                if (TextUtils.isEmpty(Age)) {
                    age.setError("Age is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    password1.setError("Password is Required");
                    return;
                }
                if (TextUtils.isEmpty(time)) {
                    password1.setError("Timings required to check Availability");
                    return;
                }
                if (TextUtils.isEmpty(special)) {
                    password1.setError("Specialization is Required");
                    return;
                }
                if (TextUtils.isEmpty(dept)) {
                    password1.setError("Department is Required");
                    return;
                } else {
                    loader.setMessage("Registration in progress"); //for loading symbol
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(emails, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                String error = task.getException().toString();
                                Toast.makeText(DoctorregActivity.this, "Error occured" + error, Toast.LENGTH_SHORT).show();
                            } else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", name);
                                userInfo.put("email", emails);
                                userInfo.put("idnumber", id);
                                userInfo.put("phonenumber", phone);
                                userInfo.put("age", Age);
                                userInfo.put("gender", genders);
                                userInfo.put("password", password);
                                userInfo.put("Timings", time);
                                userInfo.put("Specialization", special);
                                userInfo.put("Department", dept);
                                userInfo.put("search","Specialization"+special);
                                userInfo.put("type", "Doctor");
                                userDatabaseRef.updateChildren(userInfo)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DoctorregActivity.this,
                                                            "Details set successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    String errors=task.getException().toString();
                                                    Toast.makeText(DoctorregActivity.this,"Error"+errors
                                                            , Toast.LENGTH_SHORT).show();
                                                }
                                                finish();
                                                loader.dismiss();

                                            }

                                        });
                                Intent intent = new Intent(DoctorregActivity.this,
                                        MainActivity.class);
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

    }
