package com.vrushali.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vrushali.hospital.Model.User;
import com.vrushali.hospital.UserAdapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class Direct extends AppCompatActivity {
 Toolbar toolbar1;
private RecyclerView recyclerview1;
    private List<User> userList;
    private UserAdapter userAdapter;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct);
        toolbar1=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Records");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerview1=findViewById(R.id.recyclerview1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerview1.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(Direct.this, userList);

        recyclerview1.setAdapter(userAdapter);

        ref = FirebaseDatabase.getInstance().getReference()
                .child("Admin's Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String title=getIntent().getStringExtra("group");
              //  String type1 = snapshot.child("type").getValue().toString();
                if (title.equals("patient")){
                    DatabaseReference reference7 = FirebaseDatabase.getInstance().getReference()
                            .child("users");
                    Query query = reference7.orderByChild("type").equalTo("patient");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                User user = dataSnapshot.getValue(User.class);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();


                            if (userList.isEmpty()){
                                Toast.makeText(Direct.this, "No recipients", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    DatabaseReference reference7 = FirebaseDatabase.getInstance().getReference()
                            .child("users");
                    Query query = reference7.orderByChild("type").equalTo("Doctor");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                User user = dataSnapshot.getValue(User.class);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();


                            if (userList.isEmpty()){
                                Toast.makeText(Direct.this, "No recipients", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("The read failed: " + error.getCode());

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}