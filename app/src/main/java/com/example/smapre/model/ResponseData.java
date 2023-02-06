package com.example.smapre.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private boolean error;
    @SerializedName("login")
    private LoginData login;
    private List<AbsenData> absen;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public LoginData getLogin() {
        return login;
    }

    public void setLogin(LoginData login) {
        this.login = login;
    }

    public List<AbsenData> getAbsen() {
        return absen;
    }

    public void setAbsen(List<AbsenData> absen) {
        this.absen = absen;
    }
}