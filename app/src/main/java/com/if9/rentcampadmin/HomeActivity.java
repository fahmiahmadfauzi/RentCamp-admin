package com.if9.rentcampadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.if9.rentcampadmin.Session.SessionManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    String nama, alamat, email, username, password, nik, telp, jk, foto, grup, getId;
    TextView tvnama, tvalamat, tvemail, tvusername, tvpass, tvnik, tvtelp, tvjk, tvfoto, tvgrup, namaHeader, emailHeader;
    Button btnLogout;
    Intent cek;
    SessionManager sessionManager;
    String cekUser;
    Toolbar toolbar;
    LinearLayout fiturToko, fiturTransaksi, fiturRiwayat, fiturAlat;
    CircleImageView imgProfile;
    Bitmap bitmap;
    private int waktu = 5000;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        toolbar = findViewById(R.id.tolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);


        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        tvnama = findViewById(R.id.edtnama);
        tvalamat = findViewById(R.id.edtalamat);
        tvemail = findViewById(R.id.edtemail);
        tvusername = findViewById(R.id.edtusername);
        tvpass = findViewById(R.id.edtpassword);
        tvnik = findViewById(R.id.edtnik);
        tvtelp = findViewById(R.id.edttelp);
        tvjk = findViewById(R.id.edtjk);
        tvfoto = findViewById(R.id.edtfoto);
        tvgrup = findViewById(R.id.edtgrup);
        btnLogout = findViewById(R.id.logout);

        fiturToko = findViewById(R.id.fiturtoko);
        fiturRiwayat = findViewById(R.id.fiturriwayat);
        fiturTransaksi = findViewById(R.id.fiturtransaksi);
        fiturAlat = findViewById(R.id.fituralat);

        namaHeader = headerView.findViewById(R.id.namaheader);
        emailHeader = headerView.findViewById(R.id.emailheader);
        imgProfile = headerView.findViewById(R.id.fotop);

        HashMap<String, String> userr = sessionManager.getUserDetail();
        String mNama = userr.get(SessionManager.NAMA);
        String mEmail = userr.get(SessionManager.EMAIL);
        String mAlamat = userr.get(SessionManager.ALAMAT);
        String mUsername = userr.get(SessionManager.USERNAME);
        String mPassword = userr.get(SessionManager.PASSWORD);
        String mNIK = userr.get(SessionManager.NIK);
        String mTelp = userr.get(SessionManager.TELP);
        String mjk = userr.get(SessionManager.JENIS_KELAMIN);
        String mFoto = userr.get(SessionManager.FOTO);
        String mGrupUser = userr.get(SessionManager.GRUP_USER);
        getId = userr.get(SessionManager.ID);

        tvnama.setText(getId);
        tvalamat.setText(mAlamat);
        tvemail.setText(mEmail);
        tvusername.setText(mUsername);
        tvpass.setText(mPassword);
        tvnik.setText(mNIK);
        tvtelp.setText(mTelp);
        tvjk.setText(mjk);
        tvfoto.setText(mFoto);
        tvgrup.setText(mGrupUser);

        namaHeader.setText(mNama);
        emailHeader.setText(mEmail);
        new GetImageFromURL(imgProfile).execute(mFoto);

        fiturToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent gotoCari = new Intent(HomeActivity.this, TokoActivity.class);
                startActivity(gotoCari);
            }
        });
        fiturRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent gotoRiwayat = new Intent(HomeActivity.this, RiwayatActivity.class);
                startActivity(gotoRiwayat);
            }
        });
        fiturTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent gotoTransaksi = new Intent(HomeActivity.this, TransaksiActivity.class);
                startActivity(gotoTransaksi);
            }
        });
        fiturAlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent gotoTrend = new Intent(HomeActivity.this, AlatTokoActivity.class);
                startActivity(gotoTrend);
            }
        });
        //saat item navigasi di klik
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else
                    menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        Intent gotoProf = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(gotoProf);
                        return true;
                    case R.id.seting:
                        Intent gotoSetting = new Intent(HomeActivity.this, PengaturanActivity.class);
                        startActivity(gotoSetting);
                        return true;
                    case R.id.favorite:
                        //Intent gotoFav=new Intent(HomeActivity.this,FavoriteActivity.class);
                        //startActivity(gotoFav);
                        return true;
                    case R.id.logout:
                        sessionManager.logout();
                        return true;
                }

                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgV;

        public GetImageFromURL(ImageView imgV) {
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            imgProfile = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);

            } catch (EOFException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }
    }
}
