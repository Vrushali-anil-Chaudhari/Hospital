package com.vrushali.hospital;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class signupactivity extends AppCompatActivity {
    TextView back;
    MaterialButton patientreg2,doctorreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        patientreg2=findViewById(R.id.patientreg2);
        doctorreg=findViewById(R.id.doctorreg);
        doctorreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(signupactivity.this, "Doctor Registration", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(signupactivity.this,DoctorregActivity.class);
                startActivity(intent);
            }
        });
        patientreg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(signupactivity.this, "Patient Registration", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(signupactivity.this,patientreg.class);
                startActivity(intent);
            }
        });

    }
    public void Back(View v){
        back=findViewById(R.id.Adminlogin);
        Toast.makeText(this, "Login Page", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(signupactivity.this,MainActivity.class);
        startActivity(intent);

    }

   /* public void Toast(){

    }
    public void DoctorToast(){

    }*/
}
