package com.docoding.clickcare.activities.pasien.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPasienViewModel extends ViewModel {
    private static final String TAG = RegisterPasienViewModel.class.getName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addRegisterToCloud(String name, String nik, String alamat, String phone, String bpjs,
                                   String keluhan, String poli, String dokter, String date, String noAntrian) {

        CollectionReference cr = db.collection("pasien-register");
        Map<String, Object> pasienRegister = new HashMap<>();
        pasienRegister.put("alamat", alamat );
        pasienRegister.put("antrian", 8);
        pasienRegister.put("createdAt", FieldValue.serverTimestamp());
        pasienRegister.put("dokter", dokter);
        pasienRegister.put("keluhan", keluhan);
        pasienRegister.put("nama", name);
        pasienRegister.put("nik", nik);
        pasienRegister.put("no_antrian", noAntrian);
        pasienRegister.put("no_bpjs", bpjs);
        pasienRegister.put("no_telepon", phone);
        pasienRegister.put("poli", poli);
        pasienRegister.put("status", "Konfirmasi");
        pasienRegister.put("waktu", date);



        cr.add(pasienRegister).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
                String message = "Data on Cloud Firestore send success";
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = "Failed to send data to firestore" + e.getMessage();
                Log.w(TAG, "Error adding document", e);
            }
        });

    }



}

