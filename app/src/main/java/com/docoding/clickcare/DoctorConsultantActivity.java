package com.docoding.clickcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.docoding.clickcare.adapter.DoctorAdapter;
import com.docoding.clickcare.databinding.ActivityDoctorConsultantBinding;
import com.docoding.clickcare.dummydata.DoctorDummy;
import com.docoding.clickcare.helper.OnItemClickCallback;
import com.docoding.clickcare.model.DoctorModel;

import java.util.ArrayList;

public class DoctorConsultantActivity extends AppCompatActivity {
    private ActivityDoctorConsultantBinding binding;
    private ArrayList<DoctorModel> listDoctors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorConsultantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.doctorList.setHasFixedSize(true);

        binding.filterNavbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.open();
            }
        });

        listDoctors.addAll(DoctorDummy.ListData());
        showRecycleListDoctor();
    }

    public void showRecycleListDoctor() {
        binding.doctorList.setLayoutManager(new LinearLayoutManager(this));
        DoctorAdapter listNewsAdapter = new DoctorAdapter(listDoctors);
        binding.doctorList.setAdapter(listNewsAdapter);

        listNewsAdapter.setOnItemClickCallback(new OnItemClickCallback() {
            @Override
            public void onItemClicked(DoctorModel doctorModel) {
                Intent detailFood = new Intent(DoctorConsultantActivity.this, DetailDoctorActivity.class);
                detailFood.putExtra(DetailDoctorActivity.ITEM_EXTRA, doctorModel);
                startActivity(detailFood);
            }
        });
    }
}