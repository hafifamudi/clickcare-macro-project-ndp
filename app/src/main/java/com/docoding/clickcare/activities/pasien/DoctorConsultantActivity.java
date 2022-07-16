package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.docoding.clickcare.activities.dokter.BaseActivity;
import com.docoding.clickcare.activities.dokter.ChatPasienActivity;
import com.docoding.clickcare.adapter.DoctorAdapter;
import com.docoding.clickcare.adapter.UsersAdapter;
import com.docoding.clickcare.databinding.ActivityDoctorConsultantBinding;
import com.docoding.clickcare.dummydata.DoctorDummy;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.helper.OnItemClickCallback;
import com.docoding.clickcare.listeners.UserListener;
import com.docoding.clickcare.model.DoctorModel;
import com.docoding.clickcare.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

public class DoctorConsultantActivity extends BaseActivity implements UserListener {
    private ActivityDoctorConsultantBinding binding;
    private ArrayList<DoctorModel> listDoctors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorConsultantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.filterDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.open();
            }
        });

        getUsers();
    }


    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_DOCTORS)
                .get().addOnCompleteListener(
                task -> {
                    loading(false);
                    String currentUserId = Prefs.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME_DOCTOR);
                            user.spesialis = queryDocumentSnapshot.getString(Constants.KEY_DOCTOR_SPESIALIST);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.availability = queryDocumentSnapshot.getString(Constants.KEY_AVAILABILITY);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            binding.doctorList.setLayoutManager(new LinearLayoutManager(this));
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.doctorList.setAdapter(usersAdapter);
                            binding.doctorList.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                }
        );
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s,", "No user Available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        if (user.availability.equals("0")) {
            Toast.makeText(this,
                    String.format("Dokter %s tidak tersedia, silahkan pilih dokter yang lain", user.name), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), DetailDoctorActivity.class);
            intent.putExtra(Constants.KEY_USER, user);
            startActivity(intent);
            finish();
        }
    }
}