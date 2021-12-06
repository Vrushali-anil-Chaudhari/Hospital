package com.vrushali.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vrushali.hospital.Model.User;
import com.vrushali.hospital.UserAdapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivitylite extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer_layout;
    private Toolbar toolbar;
    private NavigationView navigation_view;

   // private CircleImageView nav_profile_image;
    private TextView nav_fullname, nav_email, nav_contact, nav_type;

    private DatabaseReference userRef;
    private DatabaseReference fetch;

    private RecyclerView recyclerview;
    private ProgressBar progressbar;

    private List<User> userList;
    private UserAdapter userAdapter;
   FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Hospital");

        drawer_layout = findViewById(R.id.drawer_layout);
        navigation_view = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivitylite.this, drawer_layout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        navigation_view.setNavigationItemSelectedListener(this);

        progressbar = findViewById(R.id.progressbar);

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivitylite.this, userList);

        recyclerview.setAdapter(userAdapter);

         fetch = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAuth = FirebaseAuth.getInstance();
       // FirebaseAuth mAuth;
       String currentUserId = mAuth.getCurrentUser().getUid();
       Log.i("MainActivitylite",currentUserId);

        fetch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Datasnapshot) {
                User user=Datasnapshot.getValue(User.class);
                String type = user.getType();
                Log.i("MainActivitylite",type);
                if (type.equals("patient")){
                    readRecipients();
                }else {
                    readPatient();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



       // nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname = navigation_view.getHeaderView(0).findViewById(R.id.nav_fullname);
        nav_email = navigation_view.getHeaderView(0).findViewById(R.id.nav_email);
        nav_contact = navigation_view.getHeaderView(0).findViewById(R.id.nav_contact);
        nav_type = navigation_view.getHeaderView(0).findViewById(R.id.nav_type);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.i("Main",snapshot.toString());
                    Log.i("Main",snapshot.getValue().toString());
                    User user=snapshot.getValue(User.class);


                    String name =user.getName();
                    nav_fullname.setText(name);

                    String email = user.getEmail();
                    nav_email.setText(email);

                    String phonenumber = user.getPhonenumber();
                    nav_contact.setText(phonenumber);

                    String type = user.getType();
                    nav_type.setText(type);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readPatient() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("patient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivitylite.this, "No recipients", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readRecipients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("Doctor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivitylite.this, "No recipients", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.profile:
                Intent intent = new Intent(MainActivitylite.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.neurologists:
                Intent intent2 = new Intent(MainActivitylite.this, CategorySelected.class);
                intent2.putExtra("group","Neurologist");
                startActivity(intent2);
                break;
            case R.id.dermatologists:
                Intent intent3 = new Intent(MainActivitylite.this, CategorySelected.class);
                intent3.putExtra("group","Dermatologist");
                startActivity(intent3);
                break;
            case R.id.cardiologists:
                Intent intent4 = new Intent(MainActivitylite.this, CategorySelected.class);
                intent4.putExtra("group","Cardiologist");
                startActivity(intent4);
                break;
            case R.id.ophthalmologists:
                Intent intent5 = new Intent(MainActivitylite.this, CategorySelected.class);
                intent5.putExtra("group","Ophthalmologist");
                startActivity(intent5);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1=new Intent(MainActivitylite.this,MainActivity.class);
                startActivity(intent1);
                break;
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }
}
