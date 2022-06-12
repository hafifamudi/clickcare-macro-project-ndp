package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.databinding.ActivityDetailDoctorBinding;
import com.docoding.clickcare.fragments.FragmentDetailDoctorPagerAdapter;
import com.docoding.clickcare.model.DoctorModel;
import com.docoding.clickcare.state.GlobalUserState;
import com.google.android.material.tabs.TabLayout;

public class DetailDoctorActivity extends AppCompatActivity {
    private ActivityDetailDoctorBinding binding;
    public static final String ITEM_EXTRA = "detail_doctor";

    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailDoctorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Tentang"));
        tabLayout.addTab(tabLayout.newTab().setText("Ulasan"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final FragmentDetailDoctorPagerAdapter adapter =
                new FragmentDetailDoctorPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        DoctorModel doctorModel = (DoctorModel) getIntent().getSerializableExtra(ITEM_EXTRA);

        if (doctorModel != null) {
            Glide.with(this)
                    .load(doctorModel.getDoctorPhoto())
                    .into(binding.doctorPhotoDetail);

            binding.usernameDetailDoctor.setText(doctorModel.getDoctorName());
            binding.spesialisDoctorDetail.setText(doctorModel.getDoctorSpecialist());

//          save state
            GlobalUserState.doctorReview = doctorModel.getDoctorReview();
            GlobalUserState.doctorNumber = doctorModel.getDoctorNumber();
            GlobalUserState.doctorPrice = doctorModel.getDoctorPrice();
            GlobalUserState.doctorPasien = doctorModel.getDoctorPasien();
            GlobalUserState.doctorExperience = doctorModel.getDoctorExperience();
        }


        binding.chatDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentActivity = new Intent(DetailDoctorActivity.this, PaymentGatewayActivity.class);
                startActivity(paymentActivity);
            }
        });
    }
}