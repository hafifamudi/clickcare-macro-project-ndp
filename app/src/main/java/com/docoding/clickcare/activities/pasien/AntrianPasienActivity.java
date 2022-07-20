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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAntrianPasienBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}