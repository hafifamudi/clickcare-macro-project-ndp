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
import com.docoding.clickcare.model.Pasien;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterPasienActivity extends AppCompatActivity {
    private ActivityRegisterPasienBinding binding;

    private String name,nik,alamat,phoneNum,bpjsNum,keluhan,poli,dokter, date, valueDk, valuePoli;
    private  boolean check = false;

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

                getDataFromWidget();

                Pasien pasien = new Pasien();
                pasien.setName(name);
                pasien.setDate(date);
                pasien.setKeluhan(keluhan);
                pasien.setDokter(valueDk);
                pasien.setNo_Antrian("C2-1");
                pasien.setAlamat(alamat);
                pasien.setNik(nik);
                pasien.setPhone(phoneNum);
                pasien.setPoli(valuePoli);
                pasien.setBpjs(bpjsNum);


                Intent detailRegistrasiActivity = new Intent(RegisterPasienActivity.this, DetailRegistrasi.class);
                detailRegistrasiActivity.putExtra(DetailRegistrasi.EXTRA_PASIEN, pasien);
                startActivity(detailRegistrasiActivity);
                finish();
            }
        });



//bottom sheet
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
                        Toast.makeText(RegisterPasienActivity.this, "Poli Gigi", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valuePoli = "Poli Gigi";
                        check = true;
                    }
                });

                bottomSheetView.findViewById(R.id.poli_bedah_syaraf).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli Bedah Syaraf", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valuePoli = "Poli Bedah Syaraf";
                        check = true;
                    }
                });

                bottomSheetView.findViewById(R.id.poli_tht).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli THT", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valuePoli = "Poli THT";
                        check = true;
                    }
                });

                bottomSheetView.findViewById(R.id.poli_jantung).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli Jantung", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valuePoli = "Poli Jantung";
                        check = true;
                    }
                });

                bottomSheetView.findViewById(R.id.poli_anak).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RegisterPasienActivity.this, "Poli Anak", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valuePoli = "Poli Anak";
                        check = true;
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
                        valueDk = value.getText().toString();
                        check = true;

                    }
                });

                bottomSheetView.findViewById(R.id.dokter_dua).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valueDk = value.getText().toString();
                        check = true;
                    }
                });

                bottomSheetView.findViewById(R.id.dokter_tiga).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valueDk = value.getText().toString();
                        check = true;
                    }
                });

                bottomSheetView.findViewById(R.id.dokter_empat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valueDk = value.getText().toString();
                        check = true;
                    }
                });

                bottomSheetView.findViewById(R.id.dokter_lima).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button value = (Button) view;
                        Toast.makeText(RegisterPasienActivity.this, value.getText().toString(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        valueDk = value.getText().toString();
                        check = true;
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        if (!check) {
            binding.searchDokter.setText(valueDk);
            binding.searchPoli.setText(valuePoli);
        } else {
            binding.searchDokter.setError("This field cannot be null");
            binding.searchPoli.setError("This field cannot be null");
        }
    }

    private void getDataFromWidget() {
         name = binding.etName.getText().toString().trim();
         nik = binding.etNIK.getText().toString().trim();
         alamat = binding.etAlamat.getText().toString().trim();
         phoneNum = binding.etPhone.getText().toString().trim();
         bpjsNum = binding.etBPJS.getText().toString().trim();
         keluhan = binding.etKeluhan.getText().toString().trim();
         poli = valuePoli;
         dokter = valueDk;

//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//        date = df.format(Calendar.getInstance().getTime());

        // Start date
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
    }



}