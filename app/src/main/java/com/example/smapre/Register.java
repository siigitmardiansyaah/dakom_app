package com.example.smapre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smapre.api.ApiClient;
import com.example.smapre.api.ApiInterface;
import com.example.smapre.model.ResponseData;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    TextInputEditText editTextNIS,editTextPassword,editTextPassword1;
    TextView btn_login;
    ImageView btn_login1;
    String nip,password,device_id,konfirmasi;
    ApiInterface apiInterface;
    Button cirRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_login = (TextView) findViewById(R.id.btn_login);
        btn_login1 = (ImageView) findViewById(R.id.btn_login1);
        editTextNIS = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword1 = findViewById(R.id.editTextPassword1);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);

        cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nip = editTextNIS.getText().toString();
                password = editTextPassword.getText().toString();
                konfirmasi= editTextPassword1.getText().toString();
                device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseData> loginCall = apiInterface.regisResponse(nip,password,device_id);
                if(nip.length() < 1 && password.length() < 1) {
                    Toast.makeText(Register.this, "Kedua Kolom Harus di Isi", Toast.LENGTH_LONG).show();
                }else if(nip.length() < 1 || password.length() < 1) {
                    Toast.makeText(Register.this, "Field Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }else if(!password.equals(konfirmasi)){
                    Toast.makeText(Register.this, "Password Tidak Sama", Toast.LENGTH_LONG).show();
                }else{
                    loginCall.enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if(response.isSuccessful() && !response.body().isError()){
                                //Ini untuk pindah
                                Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
//                        Toast.makeText(Register.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }

    public void onLoginClick(View V){
        startActivity(new Intent(Register.this, Login.class));
    }
}