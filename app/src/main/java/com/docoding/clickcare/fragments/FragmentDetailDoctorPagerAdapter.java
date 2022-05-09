package com.docoding.clickcare.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.docoding.clickcare.model.DoctorModel;
import com.docoding.clickcare.state.GlobalUserState;

public class FragmentDetailDoctorPagerAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    public static final String ITEM_EXTRA = "detail_doctor";

    public FragmentDetailDoctorPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DetailDoctorFragment detailDoctor = new DetailDoctorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("DOCTOR_REVIEW", GlobalUserState.doctorReview);
                bundle.putString("DOCTOR_NUMBER", GlobalUserState.doctorNumber);
                bundle.putString("DOCTOR_PRICE", GlobalUserState.doctorPrice);
                bundle.putString("DOCTOR_PASIEN", GlobalUserState.doctorPasien);
                bundle.putString("DOCTOR_EXPERIENCE", GlobalUserState.doctorExperience);
                detailDoctor.setArguments(bundle);
                return detailDoctor;
            case 1:
                FeedbackDoctorFragement feedbackDoctor = new FeedbackDoctorFragement();
                return feedbackDoctor;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
