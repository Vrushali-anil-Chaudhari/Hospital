package com.vrushali.hospital;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class patientreg extends AppCompatActivity{
EditText entername,idnumber,phonenumber,email,gender,age,password1;
Button regbutton;
TextView GoBack;
CircleImageView ProfileImage;
private Uri imageUri;
private FirebaseAuth mAuth;
private DatabaseReference userDatabaseRef;
private ProgressDialog loader;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_patientregfinal);
            entername=findViewById(R.id.entername);
            idnumber=findViewById(R.id.idnumber);
            phonenumber=findViewById(R.id.phonenumber);
            email=findViewById(R.id.email);
            gender=findViewById(R.id.gender);
            age=findViewById(R.id.age);
            password1=findViewById(R.id.password1);
            regbutton=findViewById(R.id.regbutton);
            ProfileImage=findViewById(R.id.ProfileImage);
            loader =new ProgressDialog(this);
            mAuth= FirebaseAuth.getInstance();
            ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,1);
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
                    if (imageUri == null) {
                        Toast.makeText(patientreg.this, "Upload an image", Toast.LENGTH_SHORT).show();
                    } else {
                        loader.setMessage("Registration in progress");
                        loader.setCanceledOnTouchOutside(false);
                        loader.show();

                        mAuth.createUserWithEmailAndPassword(emails, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    String error = task.getException().toString();
                                    Toast.makeText(patientreg.this, "Error occured" + error, Toast.LENGTH_SHORT).show();
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
                                    userInfo.put("type", "patient");
                                    userDatabaseRef.updateChildren(userInfo)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(patientreg.this,
                                                                "Details set successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(patientreg.this,
                                                                task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    finish();
                                                    loader.dismiss();

                                                }

                                            });
                                    if(imageUri != null){
                                        final StorageReference filepath =
                                                FirebaseStorage.getInstance().getReference().child("profile pictures")
                                                        .child(currentUserId);
                                        Bitmap bitmap = null;
                                        try{
                                            bitmap = MediaStore.Images.Media.getBitmap(getApplication().
                                                    getContentResolver(),imageUri);
                                        }catch(IOException e){
                                            e.printStackTrace();
                                        }
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                                        byte[] data =byteArrayOutputStream.toByteArray();

                                        UploadTask uploadTask = filepath.putBytes(data);

                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                finish();
                                                return;
                                            }
                                        });
                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                if(taskSnapshot.getMetadata() != null){
                                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String imageUrl = uri.toString();
                                                            Map newImageMap = new HashMap();
                                                            newImageMap.put("profilepicture1",imageUrl);

                                                            userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(patientreg.this,
                                                                                "Registered successfully",Toast.LENGTH_SHORT).show();
                                                                    } else{
                                                                        Toast.makeText(patientreg.this,
                                                                                task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        Intent intent = new Intent(patientreg.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        loader.dismiss();
                                    }
                                }

                            }
                        });

                    }
                }
            });
        }
                    public void Back (View v){
                        GoBack = findViewById(R.id.Adminlogin);
                        Toast.makeText(this, "Login Page", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(patientreg.this, MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    protected void onActivityResult ( int requestCode, int resultCode,
                    @Nullable Intent data){
                        super.onActivityResult(requestCode, resultCode, data);
                        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
                            imageUri = data.getData();
                            ProfileImage.setImageURI(imageUri);
                        }
                    }

                }
