package com.docoding.clickcare.model;

public class PasienModelKonsultasi {
    private String nama;
    private String dokter;
    private String createdAt;

    public PasienModelKonsultasi() {}

    public PasienModelKonsultasi(String nama, String dokter, String createdAt) {
        this.nama = nama;
        this.dokter = dokter;
        this.createdAt = createdAt;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDokter() {
        return dokter;
    }

    public void setDokter(String dokter) {
        this.dokter = dokter;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
