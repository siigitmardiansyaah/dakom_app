package com.example.smapre;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smapre.api.ApiInterface;
import com.example.smapre.model.ResponseData;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.example.smapre.api.ApiClient;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    TextView nama;
    private long pressedTime;
    RelativeLayout btn_logout, btn_scan, btn_profile, btn_rekap;
    GpsTracker gpsTracker;
    double long_gps;
    double lang_gps;
    String id_pegawai, id_qr,hasil;
    String[] kata;
    ApiInterface apiInterface;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(MainActivity.this);
        if (!sessionManager.isLoggedIn()) {
            moveToLogin();
        }
        nama = (TextView) findViewById(R.id.txt_nama);
        btn_logout = (RelativeLayout) findViewById(R.id.btn_logout);
        btn_scan = (RelativeLayout) findViewById(R.id.btn_scan);
        btn_profile = (RelativeLayout) findViewById(R.id.btn_profile);
        btn_rekap = (RelativeLayout) findViewById(R.id.btn_rekap);
        nama.setText("Selamat Datang, " + sessionManager.getUserDetail().get(SessionManager.NAMA) + " (" + sessionManager.getUserDetail().get(SessionManager.KD_DIV) + ") ");

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan QRcode");
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        btn_rekap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Absensi.class));

            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Profile.class));

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLogin();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        sessionManager.logoutSession();
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah Anda Yakin Ingin Logout ?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sessionManager.logoutSession();
                            moveToLogin();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(getBaseContext(), "Ketuk lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void checkPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        IntentResult result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Keluar",
                        Toast.LENGTH_LONG).show();
            } else {
                id_pegawai = sessionManager.getUserDetail().get(SessionManager.ID_PEGAWAI);
                gpsTracker = new GpsTracker(MainActivity.this);
                if (gpsTracker.canGetLocation()) {
                    lang_gps = gpsTracker.getLatitude();
                    long_gps = gpsTracker.getLongitude();
                } else {
                    gpsTracker.showSettingsAlert();
                }
                hasil = result.getContents();


                        apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<ResponseData> sendbio = apiInterface.absenData(hasil, id_pegawai, long_gps, lang_gps);
                        sendbio.enqueue(new Callback<ResponseData>() {
                            @Override
                            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                if (response.body().isError() == false) {
                                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, Absensi.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData> call, Throwable t) {
                                Log.d("RETRO", "Falure : " + "Gagal Mengirim Request");
                            }
                        });


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}