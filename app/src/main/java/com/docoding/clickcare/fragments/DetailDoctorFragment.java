package com.docoding.clickcare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.docoding.clickcare.R;
import com.docoding.clickcare.helper.Constants;
import com.pixplicity.easyprefs.library.Prefs;

public class DetailDoctorFragment extends Fragment {
    private TextView detailReview;
    private TextView detailNumber;
    private TextView detailPrice;
    private TextView detailPasien;
    private TextView detailExperience;

    DetailDoctorFragment() {
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


        detailReview.setText(Prefs.getString(Constants.KEY_NAME_DOCTOR) +" adalah Dokter Spesialis " +
                Prefs.getString(Constants.KEY_DOCTOR_SPESIALIST) + " dari Fakultas Kedokteran Universitas Asembagus, " +
                "tahun 2015, beliau aktif praktik di RSUD Pasar Minggu. ");
        detailNumber.setText("2324811213502858");
        detailPrice.setText("Rp.20.000");
        detailPasien.setText("600 +");
        detailExperience.setText("5 Thn");

        return view;
    }
}