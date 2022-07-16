package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.activities.dokter.ListChatPasienActivityTwo;
import com.docoding.clickcare.databinding.ActivityDetailDoctorBinding;
import com.docoding.clickcare.fragments.FragmentDetailDoctorPagerAdapter;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.model.DoctorModel;
import com.docoding.clickcare.model.User;
import com.docoding.clickcare.state.GlobalUserState;
import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;

public class DetailDoctorActivity extends AppCompatActivity {
    private ActivityDetailDoctorBinding binding;
    private User receiverUser;
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


        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);

        if (receiverUser != null) {
            Prefs.putString(Constants.KEY_NAME_DOCTOR, receiverUser.name);
            Prefs.putString(Constants.KEY_DOCTOR_SPESIALIST, receiverUser.spesialis);

            Glide.with(this)
                    .load(receiverUser.image)
                    .into(binding.doctorPhotoDetail);

            binding.usernameDetailDoctor.setText(receiverUser.name);
            binding.spesialisDoctorDetail.setText("Spesialis " + receiverUser.spesialis);
        } else {
            Glide.with(this)
                    .load(Prefs.getString(Constants.KEY_IMAGE))
                    .into(binding.doctorPhotoDetail);

            binding.usernameDetailDoctor.setText(Prefs.getString(Constants.KEY_NAME_DOCTOR));
            binding.spesialisDoctorDetail.setText("Spesialis " + Prefs.getString(Constants.KEY_DOCTOR_SPESIALIST));
        }


        if (Prefs.getString(Constants.KEY_LOGIN_INFO).equalsIgnoreCase("doctor"))
            binding.chatDoctor.setVisibility(View.GONE);

        binding.chatDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentActivity = new Intent(DetailDoctorActivity.this, PaymentGatewayActivity.class);
                paymentActivity.putExtra(Constants.KEY_USER, receiverUser);
                startActivity(paymentActivity);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity;
                if (Prefs.getString(Constants.KEY_LOGIN_INFO).equalsIgnoreCase("doctor")) {
                    activity = new Intent(DetailDoctorActivity.this, ListChatPasienActivityTwo.class);
                } else {
                    activity = new Intent(DetailDoctorActivity.this, PaymentGatewayActivity.class);
                }
                startActivity(activity);
            }
        });
    }
}