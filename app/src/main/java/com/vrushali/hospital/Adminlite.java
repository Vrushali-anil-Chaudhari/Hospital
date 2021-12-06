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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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

public class Adminlite extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer_layout2;
    private Toolbar toolbar2;
    private NavigationView navigation_view2;

    // private CircleImageView nav_profile_image;
    private TextView nav_name, nav_email1, nav_contact1, nav_type1;

    private DatabaseReference userRef1;
    private DatabaseReference reference1;

    private RecyclerView recyclerview2;
    private ProgressBar progressbar;

    private List<User> userList;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlite);
        drawer_layout2=findViewById(R.id.drawer_layout2);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("My");
        drawer_layout2 = findViewById(R.id.drawer_layout2);
        navigation_view2 = findViewById(R.id.navigation_view2);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(Adminlite.this, drawer_layout2,
                toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawer_layout2.addDrawerListener(toggle);
        toggle.syncState();
        navigation_view2.setNavigationItemSelectedListener(this);
        progressbar = findViewById(R.id.progressbar);

        recyclerview2 = findViewById(R.id.recyclerview2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerview2.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(Adminlite.this, userList);

        recyclerview2.setAdapter(userAdapter);

        reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Admin's Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type1 = snapshot.child("type").getValue().toString();
                if (type1.equals("Admin")){
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference()
                                .child("Admin's Data");
                        Query query = reference2.orderByChild("type").equalTo("Admin");
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
                                    Toast.makeText(Adminlite.this, "No recipients", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                }


        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
            });




        // nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_name = navigation_view2.getHeaderView(0).findViewById(R.id.nav_name);
        nav_email1 = navigation_view2.getHeaderView(0).findViewById(R.id.nav_email1);
        nav_contact1 = navigation_view2.getHeaderView(0).findViewById(R.id.nav_contact1);
        nav_type1 = navigation_view2.getHeaderView(0).findViewById(R.id.nav_type1);

        userRef1 = FirebaseDatabase.getInstance().getReference().child("Admin's Data").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    String name1 = user.getName();
                    nav_name.setText(name1);

                    String email1 = user.getEmail();
                    nav_email1.setText(email1);

                    String phonenumber1 = user.getPhonenumber();
                    nav_contact1.setText(phonenumber1);

                    String type4 = user.getType();
                    nav_type1.setText(type4);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }








    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.profile:
                Intent intent = new Intent(Adminlite.this, Profile.class);
                startActivity(intent);
                break;

            case R.id.Patient:
                Intent intent4 = new Intent(Adminlite.this,Direct.class);
                intent4.putExtra("group","patient");
                startActivity(intent4);
                break;
            case R.id.Doctor:
                Intent intent5 = new Intent(Adminlite.this, Direct.class);
                intent5.putExtra("group","doctor");
                startActivity(intent5);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1=new Intent(Adminlite.this,MainActivity.class);
                startActivity(intent1);
                break;
        }
        drawer_layout2.closeDrawer(GravityCompat.START);
        return true;
    }
}