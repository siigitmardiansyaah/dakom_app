package com.example.smapre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.smapre.model.LoginData;

import java.util.HashMap;

public class SessionManager {
    private Context _context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String IS_LOGGED_IN = "isLoggedIn";

    //DATA SESSION LOGIN
    public static final String ID_PEGAWAI = "id_pegawai";
    public static final String NAMA = "nama";
    public static final String NIP = "nip";
    public static final String KD_DIV = "kd_div";

    public SessionManager(Context context){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(LoginData user){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(ID_PEGAWAI, user.getId_pegawai());
        editor.putString(NAMA, user.getNama());
        editor.putString(NIP, user.getNip());
        editor.putString(KD_DIV, user.getKd_div());
        editor.commit();
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID_PEGAWAI, sharedPreferences.getString(ID_PEGAWAI,null));
        user.put(NAMA, sharedPreferences.getString(NAMA,null));
        user.put(NIP, sharedPreferences.getString(NIP,null));
        user.put(KD_DIV, sharedPreferences.getString(KD_DIV,null));
        return user;
    }


    public void logoutSession(){
        editor.clear();
        editor.commit();
    }

    public void checkLogin(){
        if (!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        } else {
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

}
