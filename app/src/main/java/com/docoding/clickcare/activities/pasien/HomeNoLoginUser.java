package com.docoding.clickcare.activities.pasien;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.adapter.NewsAdapter;
import com.docoding.clickcare.databinding.FragmentHomeNoLoginUserBinding;
import com.docoding.clickcare.databinding.FragmentHomeUserLoginBinding;
import com.docoding.clickcare.dummydata.NewsDummy;
import com.docoding.clickcare.model.NewsModel;
import com.docoding.clickcare.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class HomeNoLoginUser extends Fragment {
    private FragmentHomeNoLoginUserBinding binding;
    private Dialog dialog;
    private ArrayList<NewsModel> listNews = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;

    private String imageURIacessToken;

    public static final String SUCCESS_ORDER = "accepted";
    public static final String END_CHAT = "endchat";
    static int gpsDialog = 0;

    public HomeNoLoginUser() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeNoLoginUserBinding.inflate(inflater, container, false);
        View viewBinding = binding.getRoot();
        dialog = new Dialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        listNews.addAll(NewsDummy.ListData());
        showRecycleListNews();

//     cek if user already login or not

        if (gpsDialog == 0) {
//            AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
//            LayoutInflater factory = LayoutInflater.from(getActivity());
//            final View view = factory.inflate(R.layout.permission_alert, null);
//            alertadd.setView(view);
            dialog.setContentView(R.layout.permission_alert);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
            dialog.show();

            gpsDialog += 1;
        }
//
//        String statusOrder = getArguments().getString(SUCCESS_ORDER);
//        if (statusOrder != null) {
//            AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
//            LayoutInflater factory = LayoutInflater.from(getActivity());
//            final View view = factory.inflate(R.layout.success_alert, null);
//            alertadd.setView(view);
//            alertadd.show();
//        }
//
//        String statusChat = getArguments().getString(END_CHAT);
//        if (statusChat != null) {
//            AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
//            LayoutInflater factory = LayoutInflater.from(getActivity());
//            final View view = factory.inflate(R.layout.feedback_doctor_alert, null);
//            alertadd.setView(view);
//            alertadd.show();
//        }


        binding.newsList.setHasFixedSize(true);

        binding.konsultasiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DoctorConsultantActivity.class);
                startActivity(intent);
            }
        });

        binding.registrasiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterPasienActivity.class);
                startActivity(intent);
            }
        });

        binding.infoAppActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InfoAppActivity.class);
                startActivity(intent);
            }
        });


//      update user profile in home activity
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userProfile = snapshot.getValue(UserModel.class);
                binding.usernameHome.setText(userProfile.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed To Fetch", Toast.LENGTH_SHORT).show();
            }
        });

//      get user photo
        storageReference = firebaseStorage.getReference();
        storageReference.child("Images").child(
                firebaseAuth.getCurrentUser().getUid())
                .child("Profile Pic").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURIacessToken = uri.toString();
                        Glide.with(getActivity())
                                .load(uri)
                                .into(binding.imgItemPhotoUser);
                    }
                });


        return viewBinding;
    }

    public void showRecycleListNews() {
        binding.newsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        NewsAdapter listNewsAdapter = new NewsAdapter(listNews);
        binding.newsList.setAdapter(listNewsAdapter);
        System.out.println("execute recycle....");
    }
}