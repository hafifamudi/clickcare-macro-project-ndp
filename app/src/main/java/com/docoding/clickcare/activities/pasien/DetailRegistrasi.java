package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.docoding.clickcare.activities.pasien.register.RegisterPasienViewModel;
import com.docoding.clickcare.databinding.ActivityDetailRegistrasiBinding;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.model.Pasien;
import com.docoding.clickcare.state.GlobalUserState;
import com.pixplicity.easyprefs.library.Prefs;

public class DetailRegistrasi extends AppCompatActivity {

    private RegisterPasienViewModel viewModel;
    private ActivityDetailRegistrasiBinding binding;
    public static final String EXTRA_PASIEN = "extra_pasien";
    private String name,nik,alamat,phoneNum,bpjsNum,keluhan,poli,dokter, date, noAntrian;

    public static final String EXTRA_SELECTED_VALUE = "extra_selected_value";
    public static final int RESULT_CODE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRegistrasiBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        viewModel = new ViewModelProvider(this).get(RegisterPasienViewModel.class);

        getData();
        setWidget();

        binding.registrasiAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
//              viewModel.addRegisterToCloud(name, nik, alamat, phoneNum, bpjsNum, keluhan, poli, dokter, date, noAntrian);

                Pasien pasien = new Pasien();
                pasien.setName(name);
                pasien.setDate(date);
                pasien.setKeluhan(keluhan);
                pasien.setDokter(dokter);
                pasien.setNo_Antrian(noAntrian);

                Prefs.putString(Constants.KEY_NAMA_PASIEN, name);
                Prefs.putString(Constants.KEY_KELUHAN_PASIEN, keluhan);
                Prefs.putString(Constants.KEY_WAKTU_PASIEN, date);
                Prefs.putString(Constants.KEY_DOKTER_PASIEN, dokter);
                Prefs.putString(Constants.KEY_ANTRIAN_PASIEN, noAntrian);
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra(EXTRA_SELECTED_VALUE, pasien);
//                setResult(RESULT_CODE, resultIntent);

                Intent homeActivity = new Intent(DetailRegistrasi.this, HomeActivity.class);
                GlobalUserState.userSuccessOrder = "ACCEPT";
                startActivity(homeActivity);
            }
        });

    }

    private void getData() {
        Pasien pasien = getIntent().getParcelableExtra(EXTRA_PASIEN);
        name = pasien.getName();
        nik = pasien.getNik();
        alamat = pasien.getAlamat();
        phoneNum = pasien.getPhone();
        bpjsNum = pasien.getBpjs();
        keluhan = pasien.getKeluhan();
        noAntrian = pasien.getNo_Antrian();
        poli = pasien.getPoli();
        dokter = pasien.getDokter();
        date = pasien.getDate();
    }

    private void setWidget() {
        Pasien pasien = getIntent().getParcelableExtra(EXTRA_PASIEN);
        binding.nameRegister.setText(pasien.getName());
        binding.dateRegister.setText(pasien.getDate());
        binding.keluhanRegister.setText(pasien.getKeluhan());
        binding.dokterRegister.setText(pasien.getDokter());
        binding.noAntrianRegister.setText(pasien.getNo_Antrian());
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}