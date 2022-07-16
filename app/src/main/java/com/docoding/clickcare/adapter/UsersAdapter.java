package com.docoding.clickcare.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.databinding.DoctorListBinding;
import com.docoding.clickcare.databinding.ItemContainerUserBinding;
import com.docoding.clickcare.listeners.UserListener;
import com.docoding.clickcare.model.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
    private final List<User> users;
    private final UserListener userListener;

    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DoctorListBinding doctorListBinding = DoctorListBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
        );
        return new UserViewHolder(doctorListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setuserData(users.get(position), holder);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        DoctorListBinding binding;

        UserViewHolder(DoctorListBinding doctorListBinding){
            super(doctorListBinding.getRoot());
            binding = doctorListBinding;
        }

        void setuserData(User user, UserViewHolder userViewHolder){
            if (user.availability.equals("0"))  binding.status.setVisibility(userViewHolder.itemView.GONE);
            else binding.status.setVisibility(userViewHolder.itemView.VISIBLE);

            binding.doctorName.setText(user.name);
            binding.doctorSpesialis.setText("Spesialis " + user.spesialis);
            binding.avgRating.setText("4.6");
            binding.allRating.setText("(300)");
            Glide.with(userViewHolder.itemView.getContext())
                    .load(user.image)
                    .into(binding.doctorPhoto);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }

    }
}
