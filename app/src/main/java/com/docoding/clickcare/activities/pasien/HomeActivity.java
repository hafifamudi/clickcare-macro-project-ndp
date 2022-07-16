package com.docoding.clickcare.activities.pasien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.adapter.NewsAdapter;
import com.docoding.clickcare.databinding.ActivityHomeBinding;
import com.docoding.clickcare.dummydata.NewsDummy;
import com.docoding.clickcare.model.NewsModel;
import com.docoding.clickcare.model.UserModel;
import com.docoding.clickcare.state.GlobalUserState;
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
    public static final String SUCCESS_ORDER = "accepted";
    public static final String END_CHAT = "endchat";
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        if (GlobalUserState.userAuthStatus.equalsIgnoreCase("login_false")) {
            replaceFragment(new HomeUserLogin());
        } else if (GlobalUserState.userAuthStatus.equalsIgnoreCase("login_true")) {
            replaceFragment(new HomeNoLoginUser());
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_page:
                    if (GlobalUserState.userAuthStatus.equalsIgnoreCase("login_false")) {
                        replaceFragment(new HomeUserLogin());
                    } else if (GlobalUserState.userAuthStatus.equalsIgnoreCase("login_true")) {
                        replaceFragment(new HomeNoLoginUser());
                    }
                    break;
                case R.id.calender_page:
                    replaceFragment(new BookAntrianPasien());
                    break;
                case R.id.settings_page:
                    replaceFragment(new SettingUser());
                    break;
            }
            return true;
        }
    };

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }
}
