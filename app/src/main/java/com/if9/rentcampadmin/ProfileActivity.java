package com.if9.rentcampadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.if9.rentcampadmin.Session.SessionManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbarProfile;
    CircleImageView fotoP;
    TextView tnama, tusername, talamat, temail, ttelp;
    SessionManager sessionManager;
    String getId;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sessionManager = new SessionManager(this);

        toolbarProfile = findViewById(R.id.toolbarprofile);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        fotoP = findViewById(R.id.fotoprofile);
        tnama = findViewById(R.id.namauser);
        tusername = findViewById(R.id.username);
        talamat = findViewById(R.id.t_alamat);
        temail = findViewById(R.id.t_email);
        ttelp = findViewById(R.id.t_telp);

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

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorW), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new GetImageFromURL(fotoP).execute(mFoto);

        tnama.setText(mNama);
        tusername.setText(mUsername);
        talamat.setText(mAlamat);
        temail.setText(mEmail);
        ttelp.setText(mTelp);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuprofile, menu);
        return true;
    }

    public void editAkun(MenuItem mi) {
        startActivity(new Intent(ProfileActivity.this, PengaturanActivity.class));


    }

    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgV;

        public GetImageFromURL(ImageView imgV) {
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            fotoP = null;
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
