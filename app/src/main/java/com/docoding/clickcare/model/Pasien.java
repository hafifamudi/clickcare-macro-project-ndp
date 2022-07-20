package com.docoding.clickcare.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pasien implements Parcelable {

    public int id;
    public int antrian;
    public String name;
    public String date;
    public String keluhan;
    public String dokter;
    public String alamat;
    public String nik;
    public String phone;
    public String bpjs;
    public String poli;
    public String no_Antrian;

    public Pasien(Parcel in) {
        id = in.readInt();
        antrian = in.readInt();
        name = in.readString();
        date = in.readString();
        keluhan = in.readString();
        dokter = in.readString();
        alamat = in.readString();
        nik = in.readString();
        phone = in.readString();
        bpjs = in.readString();
        poli = in.readString();
        no_Antrian = in.readString();
    }

    public static final Creator<Pasien> CREATOR = new Creator<Pasien>() {
        @Override
        public Pasien createFromParcel(Parcel in) {
            return new Pasien(in);
        }

        @Override
        public Pasien[] newArray(int size) {
            return new Pasien[size];
        }
    };

    public Pasien() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAntrian() {
        return antrian;
    }

    public void setAntrian(int antrian) {
        this.antrian = antrian;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getDokter() {
        return dokter;
    }

    public void setDokter(String dokter) {
        this.dokter = dokter;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBpjs() {
        return bpjs;
    }

    public void setBpjs(String bpjs) {
        this.bpjs = bpjs;
    }

    public String getPoli() {
        return poli;
    }

    public void setPoli(String poli) {
        this.poli = poli;
    }

    public String getNo_Antrian() {
        return no_Antrian;
    }

    public void setNo_Antrian(String no_Antrian) {
        this.no_Antrian = no_Antrian;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(antrian);
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeString(keluhan);
        parcel.writeString(dokter);
        parcel.writeString(alamat);
        parcel.writeString(nik);
        parcel.writeString(phone);
        parcel.writeString(bpjs);
        parcel.writeString(poli);
        parcel.writeString(no_Antrian);
    }
}
