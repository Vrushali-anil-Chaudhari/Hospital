package com.vrushali.hospital.UserAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import com.vrushali.hospital.Model.Data;


import com.vrushali.hospital.R;

import java.util.Calendar;
import java.util.List;



public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {

    private Context context;
    private List<Data> setList;

    public SetAdapter(Context context, List<Data> setList) {
        this.context = context;
        this.setList = setList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_set, parent, false);
        return new SetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data data = setList.get(position);

        holder.Enteryourname.setText(data.getEnteryourname());
        holder.disease.setText(data.getDisease());
        holder.SelectDate.setText(data.getSelectDate());
        holder.time.setText(data.getTime());
    }


    @Override
    public int getItemCount() {
        return setList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // public CircleImageView notification_profile_image;
        TextInputEditText disease, time;
        EditText SelectDate, Enteryourname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Enteryourname = itemView.findViewById(R.id.Enteryourname);
            SelectDate = itemView.findViewById(R.id.SelectDate);
            time = itemView.findViewById(R.id.time);
            disease = itemView.findViewById(R.id.disease);
        }
    }
}