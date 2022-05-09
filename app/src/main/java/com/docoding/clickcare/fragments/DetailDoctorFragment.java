package com.docoding.clickcare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.docoding.clickcare.R;

public class DetailDoctorFragment extends Fragment {
    private static final String DOCTOR_REVIEW= "DOCTOR_REVIEW";
    private static final String DOCTOR_NUMBER = "DOCTOR_NUMBER";
    private static final String DOCTOR_PRICE = "DOCTOR_PRICE";
    private static final String DOCTOR_PASIEN = "DOCTOR_PASIEN";
    private static final String DOCTOR_EXPERIENCE = "DOCTOR_EXPERIENCE";

    private TextView detailReview;
    private TextView detailNumber;
    private TextView detailPrice;
    private TextView detailPasien;
    private TextView detailExperience;

    public DetailDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_doctor, container, false);

        detailReview = view.findViewById(R.id.doctor_detail_review);
        detailNumber = view.findViewById(R.id.doctor_detail_number);
        detailPrice = view.findViewById(R.id.doctor_detail_price);
        detailPasien = view.findViewById(R.id.doctor_detail_pasien);
        detailExperience = view.findViewById(R.id.doctor_detail_experience);

        String review = getArguments().getString(DOCTOR_REVIEW);
        String number = getArguments().getString(DOCTOR_NUMBER);
        String price = getArguments().getString(DOCTOR_PRICE);
        String pasien = getArguments().getString(DOCTOR_PASIEN);
        String experience = getArguments().getString(DOCTOR_EXPERIENCE);

        detailReview.setText(review);
        detailNumber.setText(number);
        detailPrice.setText(price);
        detailPasien.setText(pasien);
        detailExperience.setText(experience);

        return view;
    }
}