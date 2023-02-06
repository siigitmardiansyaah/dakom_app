package com.example.smapre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smapre.adapter.AdapterAbsen;
import com.example.smapre.api.ApiClient;
import com.example.smapre.api.ApiInterface;
import com.example.smapre.model.AbsenData;
import com.example.smapre.model.ResponseData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Absensi extends AppCompatActivity {
    ApiInterface apiInterface;
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<AbsenData> listData = new ArrayList<>();
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    private String id_pegawai;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);
        rvData = findViewById(R.id.rv_data);
        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);
        sessionManager = new SessionManager(Absensi.this);
        if (!sessionManager.isLoggedIn()) {
            moveToLogin();
        }

        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                retrieveData();
                srlData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }
    private void moveToLogin() {
        Intent intent = new Intent(Absensi.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Absensi.this, MainActivity.class);
        startActivity(intent); }


    public void retrieveData() {
        pbData.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        id_pegawai = sessionManager.getUserDetail().get(SessionManager.ID_PEGAWAI);

        Call<ResponseData> tampilData = apiInterface.rekapData(id_pegawai);

        tampilData.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                listData = response.body().getAbsen();
                adData = new AdapterAbsen(Absensi.this, listData);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();
                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(Absensi.this, "Gagal Menghubungi Server  ", Toast.LENGTH_SHORT).show();
                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }
}