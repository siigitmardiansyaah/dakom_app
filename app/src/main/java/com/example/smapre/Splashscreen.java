package com.example.smapre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.smapre.SessionManager;

public class Splashscreen extends AppCompatActivity {
    ImageView GambarGif;
    SessionManager sessionManager;
    private int waktu_loading = 5000; // 2000 = 2 detikx
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        sessionManager = new SessionManager(Splashscreen.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sessionManager.checkLogin();
            }
        }, waktu_loading);

    }
}