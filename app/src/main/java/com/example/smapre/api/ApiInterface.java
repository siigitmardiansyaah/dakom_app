package com.example.smapre.api;


import com.example.smapre.model.ResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("Auth")
    Call<ResponseData> loginResponse(
            @Field("nip") String nip,
            @Field("password") String password,
            @Field("device_id") String device_id
    );

    @FormUrlEncoded
    @POST("Auth/register")
    Call<ResponseData> regisResponse(
            @Field("nip") String nip,
            @Field("password") String password,
            @Field("device_id") String device_id
    );

    @FormUrlEncoded
    @POST("Absen/add")
    Call<ResponseData> absenData(
            @Field("id_qr") String id_qr,
            @Field("id_pegawai") String id_pegawai,
            @Field("long_gps") double long_gps,
            @Field("lang_gps") double lang_gps
    );

    @FormUrlEncoded
    @POST("Absen")
    Call<ResponseData> rekapData(
            @Field("id_pegawai") String id_pegawai
    );

    @FormUrlEncoded
    @PUT("Profile")
    Call<ResponseData> profileResponse(
            @Field("id_pegawai") String id_pegawai,
            @Field("nama") String nama,
            @Field("password") String password
    );

}
