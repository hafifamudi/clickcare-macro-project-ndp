package com.docoding.clickcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.adapter.NewsAdapter;
import com.docoding.clickcare.databinding.ActivityHomeBinding;
import com.docoding.clickcare.dummydata.NewsDummy;
import com.docoding.clickcare.model.NewsModel;
import com.docoding.clickcare.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private ArrayList<NewsModel> listNews = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;

    private String imageURIacessToken;

    public static final String SUCCESS_ORDER = "accepted";
    public static final String END_CHAT = "endchat";
    static int gpsDialog = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);


        if (gpsDialog == 0) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(HomeActivity.this);
            LayoutInflater factory = LayoutInflater.from(HomeActivity.this);
            final View view = factory.inflate(R.layout.permission_alert, null);
            alertadd.setView(view);

            alertadd.show();

            gpsDialog += 1;
        }

        String statusOrder = getIntent().getStringExtra(SUCCESS_ORDER);
        if (statusOrder != null) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(HomeActivity.this);
            LayoutInflater factory = LayoutInflater.from(HomeActivity.this);
            final View view = factory.inflate(R.layout.success_alert, null);
            alertadd.setView(view);
            alertadd.show();
        }

        String statusChat = getIntent().getStringExtra(END_CHAT);
        if (statusChat != null) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(HomeActivity.this);
            LayoutInflater factory = LayoutInflater.from(HomeActivity.this);
            final View view = factory.inflate(R.layout.feedback_doctor_alert, null);
            alertadd.setView(view);
            alertadd.show();
        }



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        binding.newsList.setHasFixedSize(true);

        binding.konsultasiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DoctorConsultantActivity.class);
                startActivity(intent);
            }
        });

        binding.registrasiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RegisterPasienActivity.class);
                startActivity(intent);
            }
        });

        binding.infoAppActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, InfoAppActivity.class);
                startActivity(intent);
            }
        });

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        listNews.addAll(NewsDummy.ListData());
        showRecycleListNews();

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
                Toast.makeText(getApplicationContext(), "Failed To Fetch", Toast.LENGTH_SHORT).show();
            }
        });

//               get user photo
        storageReference = firebaseStorage.getReference();
        storageReference.child("Images").child(
                firebaseAuth.getCurrentUser().getUid())
                .child("Profile Pic").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURIacessToken = uri.toString();
                        Glide.with(HomeActivity.this)
                                .load(uri)
                                .into(binding.imgItemPhotoUser);
                    }
                });
    }

    public void showRecycleListNews() {
        binding.newsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        NewsAdapter listNewsAdapter = new NewsAdapter(listNews);
        binding.newsList.setAdapter(listNewsAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_page:
                     // do nothing
                    System.out.println("testing");

                    break;
                case R.id.calender_page:
                    // do nothing
                    Intent bookinDoctor = new Intent(HomeActivity.this, BookAntrianPasienActivity.class);
                    startActivity(bookinDoctor);
                    System.out.println("testing");
                    break;
                case R.id.settings_page:
                    // do nothing
                    Intent settingProfile = new Intent(HomeActivity.this, SettingUserActivity.class);
                    startActivity(settingProfile);
                    System.out.println("testing");
                    break;
            }
            return true;
        }
    };
}
