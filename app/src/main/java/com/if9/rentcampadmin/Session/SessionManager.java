package com.if9.rentcampadmin.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.if9.rentcampadmin.HomeActivity;
import com.if9.rentcampadmin.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    public static String NAMA = "NAMA";
    public static String ALAMAT = "ALAMAT";
    public static String EMAIL = "EMAIL";
    public static String USERNAME = "USERNAME";
    public static String PASSWORD = "PASSWORD";
    public static String JENIS_KELAMIN = "JENIS_KELAMIN";
    public static String GRUP_USER = "GRUP_USER";
    public static String NIK = "NIK";
    public static String TELP = "TELP";
    public static String FOTO = "FOTO";
    public static String ID = "ID";
    public static String ID_TOKO = "ID_TOKO";
    private final String PREF_NAME = "LOGIN";
    private final String LOGIN = "IS LOGIN";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String nama, String alamat, String email, String username,
                              String password, String jenis_kelamin, String grup_user, String nik,
                              String telp, String foto, String id) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAMA, nama);
        editor.putString(ALAMAT, alamat);
        editor.putString(EMAIL, email);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putString(NIK, nik);
        editor.putString(TELP, telp);
        editor.putString(JENIS_KELAMIN, jenis_kelamin);
        editor.putString(FOTO, foto);
        editor.putString(GRUP_USER, grup_user);
        editor.putString(ID, id);


        editor.apply();


    }

    public void createSessionIdToko(String id_toko) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ID_TOKO, id_toko);


        editor.apply();


    }


    public Boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((HomeActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAMA, sharedPreferences.getString(NAMA, null));
        user.put(ALAMAT, sharedPreferences.getString(ALAMAT, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));
        user.put(NIK, sharedPreferences.getString(NIK, null));
        user.put(TELP, sharedPreferences.getString(TELP, null));
        user.put(JENIS_KELAMIN, sharedPreferences.getString(JENIS_KELAMIN, null));
        user.put(FOTO, sharedPreferences.getString(FOTO, null));
        user.put(GRUP_USER, sharedPreferences.getString(GRUP_USER, null));
        user.put(ID, sharedPreferences.getString(ID, null));

        return user;
    }

    public HashMap<String, String> getIdTokoo() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID_TOKO, sharedPreferences.getString(ID_TOKO, null));


        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((HomeActivity) context).finish();
    }


}
