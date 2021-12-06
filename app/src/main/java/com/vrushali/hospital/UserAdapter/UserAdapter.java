package com.vrushali.hospital.UserAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrushali.hospital.Email.JavaMailApi;
import com.vrushali.hospital.Model.User;
import com.vrushali.hospital.R;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {



  //  String diseases = disease.getText().toString().trim();
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_userportal,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
final User user =userList.get(position);
holder.type.setText(user.getType());
if(user.getType().equals("patient")){
    holder.book.setVisibility(View.VISIBLE);
    holder.Specialization.setVisibility(View.GONE);
}
/*else if(user.getType().equals("Doctor")){

   holder.book.setVisibility(View.GONE);
}*/
holder.email.setText(user.getEmail());
holder.phoneNumber.setText(user.getPhonenumber());
holder.name.setText(user.getName());
holder.Specialization.setText(user.getSpecialization());

        final String nameOfTheReciever= user.getName();
        final String Idofreciever= user.getIdnumber();


holder.book.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(context).setTitle("SEND EMAIL").setMessage("Send Email to"+user.getName()+"?")
                .setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nameOfSender=snapshot.child("name").getValue().toString();
                        String email=snapshot.child("email").getValue().toString();
                        String phone=snapshot.child("phonenumber").getValue().toString();

                        String mEmail=user.getEmail();
                        String subject="My Hospital";
                        String myMessage="Greetings"+nameOfTheReciever+" ,"+nameOfSender+
                                "has booked a appointment.Here's his/her details:\n"+
                                "Name:"+nameOfSender+"\n"+
                                        "Phone Number"+phone+"\n"+
                                        "Disease"+ user.getDisease()+"\n"+
                                        "Regards,"+"\n"+"My Hospital";
                        JavaMailApi javaMailApi =new JavaMailApi(context,mEmail,subject,myMessage);
                        javaMailApi.execute();

                        DatabaseReference Senderref =FirebaseDatabase.getInstance().getReference("emails").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Senderref.child(Idofreciever).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    DatabaseReference Recieverref=FirebaseDatabase.getInstance().getReference().child("emails").child(Idofreciever);
                                    Recieverref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                }
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        })
                .setNegativeButton("no",null)
                .show();



    }
});
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
       public TextView type,name,email,phoneNumber,Specialization;
       public Button book;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.type);
            name=itemView.findViewById(R.id.name);
            email=itemView.findViewById(R.id.email);
            Specialization=itemView.findViewById(R.id.Specialization);
            phoneNumber=itemView.findViewById(R.id.phoneNumber);
            //disease = itemView.findViewById(R.id.disease);

book =itemView.findViewById(R.id.book);






        }
    }
}
