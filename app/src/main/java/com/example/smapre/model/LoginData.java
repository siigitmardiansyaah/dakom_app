package com.example.smapre.model;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("id_pegawai")
    private String id_pegawai;

    @SerializedName("nip")
    private String nip;

    @SerializedName("nama")
    private String nama;

    @SerializedName("kd_div")
    private String kd_div;

    public String getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(String id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKd_div() {
        return kd_div;
    }

    public void setKd_div(String kd_div) {
        this.kd_div = kd_div;
    }
}
