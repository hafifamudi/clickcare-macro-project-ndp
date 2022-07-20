package com.docoding.clickcare.activities.pasien.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.docoding.clickcare.helper.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterPasienViewModel extends ViewModel {
    private static final String TAG = RegisterPasienViewModel.class.getName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addRegisterToCloud(String name, String nik, String alamat, String phone, String bpjs,
                                   String keluhan, String poli, String dokter, String date, String noAntrian) {


        CollectionReference cr = db.collection("pasien-register");
        Map<String, Object> pasienRegister = new HashMap<>();
        pasienRegister.put("alamat", alamat);
        pasienRegister.put("antrian", Prefs.getString("antrian"));
        pasienRegister.put("createdAt", FieldValue.serverTimestamp());
        pasienRegister.put("dokter", dokter);
        pasienRegister.put("keluhan", keluhan);
        pasienRegister.put("nama", name);
        pasienRegister.put("nik", nik);
        pasienRegister.put("no_antrian", noAntrian+Prefs.getString("antrian"));
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

    private void getLastAntrianPasien() {
        String date;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        date = sdf.format(c.getTime());
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        date = sdf.format(c.getTime());  // dt is now the new date

        db.collection("pasien-register")
                .orderBy("createdAt")
                .whereEqualTo("status", "Konfirmasi")
                .whereEqualTo("waktu", date)
                .limitToLast(1)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                Prefs.putString("antrian", documentSnapshot.getString("antrian"));
            }
        });
    }
}

