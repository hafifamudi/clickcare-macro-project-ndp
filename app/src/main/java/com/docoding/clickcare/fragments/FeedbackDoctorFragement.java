package com.docoding.clickcare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.docoding.clickcare.R;
import com.docoding.clickcare.state.GlobalUserState;

import org.w3c.dom.Text;

public class FeedbackDoctorFragement extends Fragment {

    public FeedbackDoctorFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_doctor_fragement, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}