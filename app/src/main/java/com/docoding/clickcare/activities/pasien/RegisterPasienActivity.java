package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.docoding.clickcare.R;
import com.docoding.clickcare.databinding.ActivityRegisterPasienBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

public class RegisterPasienActivity extends AppCompatActivity {
    private ActivityRegisterPasienBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPasienBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        binding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeActivity = new Intent(RegisterPasienActivity.this, HomeActivity.class);
                startActivity(homeActivity);
            }
        });

        binding.registrasiPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailRegistrasiActivity = new Intent(RegisterPasienActivity.this, DetailRegistrasi.class);
                startActivity(detailRegistrasiActivity);
            }
        });

        binding.searchPoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        RegisterPasienActivity.this, R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.activity_poli_pasien,
                                (LinearLayout) findViewById(R.id.bottomSheetContainer)
                        );

                bottomSheetView.findViewById(R.id.poli_gigi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli gigi", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.poli_bedah_syaraf).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli gigi", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.poli_tht).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli gigi", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.poli_jantung).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli gigi", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.poli_anak).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli gigi", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        binding.searchDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        RegisterPasienActivity.this, R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.activity_doctor_pasien,
                                (LinearLayout) findViewById(R.id.bottomSheetContainer)
                        );

                bottomSheetView.findViewById(R.id.dokter_satu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.dokter_dua).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.dokter_tiga).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.dokter_empat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.dokter_lima).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }
}