package com.docoding.clickcare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.helper.OnItemClickCallback;
import com.docoding.clickcare.model.DoctorModel;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorFilterViewHolder>{
    private ArrayList<DoctorModel> listDoctors;
    private OnItemClickCallback onItemClickCallback;

    public DoctorAdapter(ArrayList<DoctorModel> list) {
        this.listDoctors = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public DoctorAdapter.DoctorFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_list, parent, false);
        return new DoctorAdapter.DoctorFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorFilterViewHolder holder, int position) {
        DoctorModel doctor = listDoctors.get(position);
        Glide.with(holder.itemView.getContext())
                .load(doctor.getDoctorPhoto())
                .into(holder.doctorPhoto);
        holder.doctorName.setText(doctor.getDoctorName());
        holder.doctorSpecialist.setText(doctor.getDoctorSpecialist());
        holder.doctorAverageRating.setText(doctor.getDoctorAverageRating());
        holder.doctorAllRating.setText(doctor.getDoctorAllRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDoctors.size();
    }

    public class DoctorFilterViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName;
        TextView doctorSpecialist;
        TextView doctorAverageRating;
        TextView doctorAllRating;
        ImageView doctorPhoto;

        public DoctorFilterViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.doctor_name);
            doctorSpecialist = itemView.findViewById(R.id.doctor_spesialis);
            doctorAverageRating = itemView.findViewById(R.id.avg_rating);
            doctorAllRating = itemView.findViewById(R.id.all_rating);
            doctorPhoto = itemView.findViewById(R.id.doctor_photo);
        }
    }
}
