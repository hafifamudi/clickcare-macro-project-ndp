package com.docoding.clickcare.model;

import java.io.Serializable;

public class DoctorModel implements Serializable {
    private String doctorName;
    private String doctorSpecialist;
    private String doctorAverageRating;
    private String doctorAllRating;
    private String doctorReview;
    private String doctorNumber;
    private String doctorPrice;
    private String doctorPasien;
    private String doctorExperience;
    private int doctorPhoto;

    public String getDoctorNumber() {
        return doctorNumber;
    }

    public void setDoctorNumber(String doctorNumber) {
        this.doctorNumber = doctorNumber;
    }

    public String getDoctorPrice() {
        return doctorPrice;
    }

    public void setDoctorPrice(String doctorPrice) {
        this.doctorPrice = doctorPrice;
    }

    public String getDoctorPasien() {
        return doctorPasien;
    }

    public void setDoctorPasien(String doctorPasien) {
        this.doctorPasien = doctorPasien;
    }

    public String getDoctorExperience() {
        return doctorExperience;
    }

    public void setDoctorExperience(String doctorExperience) {
        this.doctorExperience = doctorExperience;
    }


    public DoctorModel() {
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSpecialist() {
        return doctorSpecialist;
    }

    public void setDoctorSpecialist(String doctorSpecialist) {
        this.doctorSpecialist = doctorSpecialist;
    }

    public String getDoctorAverageRating() {
        return doctorAverageRating;
    }

    public void setDoctorAverageRating(String doctorAverageRating) {
        this.doctorAverageRating = doctorAverageRating;
    }

    public String getDoctorAllRating() {
        return doctorAllRating;
    }

    public void setDoctorAllRating(String doctorAllRating) {
        this.doctorAllRating = doctorAllRating;
    }

    public int getDoctorPhoto() {
        return doctorPhoto;
    }

    public void setDoctorPhoto(int doctorPhoto) {
        this.doctorPhoto = doctorPhoto;
    }


    public String getDoctorReview() {
        return doctorReview;
    }

    public void setDoctorReview(String doctorReview) {
        this.doctorReview = doctorReview;
    }
}
