package com.docoding.clickcare.dummydata;
import com.docoding.clickcare.R;
import com.docoding.clickcare.model.DoctorModel;
import com.docoding.clickcare.model.NewsModel;

import java.util.ArrayList;

public class DoctorDummy {
    private static String[] doctorName = {
            "Dr. Bagus Budiono",
            "Dr. Maulana",
            "Dr. Sulton Hendra",
            "Dr. Sri Lestrari"
    };

    private static String[] doctorSpecialist = {
            "Spesialis jantung",
            "Spesialis jantung",
            "Dokter Umum",
            "Dokter Umum"
    };

    private static String[] doctorAverageRating = {
            "4.8",
            "4.7",
            "4.3",
            "4.8"
    };

    private static String[] doctorAllRating = {
            "(456)",
            "(239)",
            "(150)",
            "(380)"
    };

    private static String[] doctorReview = {
            "dr. Bagus Budiono, M.Sc, Sp. A " +
            "adalah Dokter Spesialis Jantung dari Fakultas Kedokteran Universitas " +
            "Asembagus, tahun 2015, beliau aktif praktik di RSUD Pasar Minggu. ",

            "Dr. Maulana" +
            "adalah Dokter Spesialis Jantung dari Fakultas Kedokteran Universitas " +
            "Asembagus, tahun 2015, beliau aktif praktik di RSUD Pasar Minggu. ",

            "Dr. Sulton Hendra" +
            "adalah Dokter Spesialis Umum dari Fakultas Kedokteran Universitas " +
            "Asembagus, tahun 2015, beliau aktif praktik di RSUD Pasar Minggu. ",

            "Dr. Sri Lestrari" +
            "adalah Dokter Spesialis Umum dari Fakultas Kedokteran Universitas " +
            "Asembagus, tahun 2015, beliau aktif praktik di RSUD Pasar Minggu. ",
    };

    private static String[] doctorNumber = {
            "2324811213502858",
            "4324811213502858",
            "7324811213502858",
            "8324811213502858"
    };

    private static String[] doctorPrice = {
            "Rp.20.000",
            "Rp.30.000",
            "Rp.10.000",
            "Rp.40.000"

    };

    private static String[] doctorPasien = {
            "600",
            "800",
            "200",
            "300"
    };

    private static  String[] doctorExperience = {
           "8 thn",
           "5 thn",
           "8 thn",
           "10 thn"
    };

    private static int[] doctorPhotos = {
            R.drawable.doctor_1,
            R.drawable.doctor_2,
            R.drawable.doctor_3,
            R.drawable.doctor_4
    };

    public static ArrayList<DoctorModel> ListData() {
        ArrayList<DoctorModel> list = new ArrayList<>();
        for (int position = 0; position < doctorName.length; position++) {
            DoctorModel doctor = new DoctorModel();
            doctor.setDoctorName(doctorName[position]);
            doctor.setDoctorSpecialist(doctorSpecialist[position]);
            doctor.setDoctorAverageRating(doctorAverageRating[position]);
            doctor.setDoctorAllRating(doctorAllRating[position]);
            doctor.setDoctorReview(doctorReview[position]);
            doctor.setDoctorNumber(doctorNumber[position]);
            doctor.setDoctorPasien(doctorPasien[position]);
            doctor.setDoctorPrice(doctorPrice[position]);
            doctor.setDoctorExperience(doctorExperience[position]);
            doctor.setDoctorPhoto(doctorPhotos[position]);

            list.add(doctor);
        }

        return list;
    }
}
