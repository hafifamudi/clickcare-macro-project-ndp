package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.docoding.clickcare.R;
import com.docoding.clickcare.databinding.ActivityDoctorConsultantBinding;
import com.docoding.clickcare.databinding.FragmentSettingUserBinding;
import com.docoding.clickcare.databinding.SettingDoctorBinding;
import com.docoding.clickcare.helper.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.pixplicity.easyprefs.library.Prefs;

import org.w3c.dom.Document;

import java.util.HashMap;

public class SettingDoctorActivity extends AppCompatActivity {
    private SettingDoctorBinding binding;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding = SettingDoctorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = firebaseFirestore.getInstance();


        binding.logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference =
                        firebaseFirestore.collection(Constants.KEY_COLLECTION_DOCTORS).document(
                                Prefs.getString(Constants.KEY_USER_ID)
                        );
                HashMap<String, Object> updates = new HashMap<>();
                documentReference.get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().getString(Constants.KEY_FCM_TOKEN) != null) {
                                    updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                                    documentReference.update(updates)
                                            .addOnSuccessListener(unused -> {
                                                Prefs.clear();
                                                startActivity(new Intent(SettingDoctorActivity.this, LoginActivity.class));
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(SettingDoctorActivity.this, "Mohon Login Terlebih Dahulu", Toast.LENGTH_SHORT).show());
                                } else {
                                    Prefs.clear();
                                    startActivity(new Intent(SettingDoctorActivity.this, LoginActivity.class));
                                }
                            }
                        });

            }
        });
    }
}