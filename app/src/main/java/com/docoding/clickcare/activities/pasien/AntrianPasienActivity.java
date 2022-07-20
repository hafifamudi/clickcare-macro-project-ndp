package com.docoding.clickcare.activities.pasien;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.docoding.clickcare.R;
import com.docoding.clickcare.databinding.ActivityAntrianPasienBinding;
import com.docoding.clickcare.model.Pasien;

public class AntrianPasienActivity extends AppCompatActivity {

    private ActivityAntrianPasienBinding binding;


//    final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == DetailRegistrasi.RESULT_CODE && result.getData() != null) {
//                    Pasien pasien = result.getData().getParcelableExtra(DetailRegistrasi.EXTRA_SELECTED_VALUE);
//                    binding.antrianName.setText(pasien.getName());
//                    binding.antrianDate.setText(pasien.getDate());
//                    binding.antrianKeluhan.setText(pasien.getKeluhan());
//                    binding.antrianDokter.setText(pasien.getDokter());
//                    binding.noAntrian.setText(pasien.getNo_Antrian());
//                }
//            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAntrianPasienBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);
//        resultLauncher.launch(new Intent(AntrianPasienActivity.this, HomeActivity.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}