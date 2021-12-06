package com.vrushali.hospital.UserAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrushali.hospital.R;

import java.util.HashMap;

public class Use extends AppCompatActivity {
private TextView disease;
private Button book;
  private  FirebaseAuth mAuth;
   private DatabaseReference userDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userportal);
        disease=findViewById(R.id.disease);
        book=findViewById(R.id.book);
        mAuth = FirebaseAuth.getInstance();

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String diseases=disease.getText().toString().trim();
                String currentUserId = mAuth.getCurrentUser().getUid();
                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Disease").child(currentUserId);
                HashMap userInfo = new HashMap();
                userInfo.put("id",currentUserId);
                userInfo.put("Disease",diseases);
            }
        });

    }
}
