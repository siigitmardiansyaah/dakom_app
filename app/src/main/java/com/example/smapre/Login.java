package com.example.smapre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smapre.api.ApiClient;
import com.example.smapre.api.ApiInterface;
import com.example.smapre.model.LoginData;
import com.example.smapre.model.ResponseData;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    TextView btn_regis;
    Button btn_login;
    TextInputEditText editTextEmail,editTextPassword;
    String nip,password,device_id;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_regis = (TextView) findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.cirLoginButton);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nip = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseData> loginCall = apiInterface.loginResponse(nip,password,device_id);
                if(nip.length() < 1 && password.length() < 1) {
                    Toast.makeText(Login.this, "Kedua Kolom Harus di Isi", Toast.LENGTH_LONG).show();
                }else if(nip.length() < 1 || password.length() < 1) {
                    Toast.makeText(Login.this, "Field Tidak Bole Kosong", Toast.LENGTH_LONG).show();
                }else{
                    loginCall.enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if(response.isSuccessful() && response.body().isError() == false){

                                // Ini untuk menyimpan sesi
                                sessionManager = new SessionManager(Login.this);
                                LoginData loginData = response.body().getLogin();
                                sessionManager.createLoginSession(loginData);

                                //Ini untuk pindah
                                Toast.makeText(Login.this,"Selamat Datang " + response.body().getLogin().getNama(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
//                        Toast.makeText(Login.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Ketuk lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void onRegisterClick(View V){
        startActivity(new Intent(Login.this, Register.class));
    }
}