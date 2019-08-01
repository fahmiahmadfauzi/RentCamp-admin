package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.if9.rentcampadmin.Session.SessionManager;
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PengaturanActivity extends AppCompatActivity {
    private static final String TAG = EditAkunActivity.class.getSimpleName();
    Toolbar toolbarPengaturan;
    LinearLayout pAkun, pPass;
    CircleImageView imgP;
    TextView TNama, TEmail;
    SessionManager sessionManager;
    String getId;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        toolbarPengaturan = findViewById(R.id.toolbarpengaturan);
        setSupportActionBar(toolbarPengaturan);
        getSupportActionBar().setTitle("Pengaturan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);

        pAkun = findViewById(R.id.pengaturanakun);
        pPass = findViewById(R.id.pengaturanpassword);
        imgP = findViewById(R.id.foto_p);
        TNama = findViewById(R.id.nama);
        TEmail = findViewById(R.id.emaill);

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

        new GetImageFromURL(imgP).execute(mFoto);


        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        pAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent gotoAkun = new Intent(PengaturanActivity.this, EditAkunActivity.class);
                startActivity(gotoAkun);

            }
        });
        pPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent gotoPass = new Intent(PengaturanActivity.this, EditPasswordActivity.class);
                startActivity(gotoPass);
            }
        });
    }

    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_READ_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.i(TAG, response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String Nama = object.getString("nama");
                                    String Alamat = object.getString("alamat");
                                    String Email = object.getString("email");
                                    String Username = object.getString("username");
                                    String Password = object.getString("password");
                                    String NIK = object.getString("nik");
                                    String Telp = object.getString("telp");
                                    String JenisKelamin = object.getString("jenis_kelamin");
                                    String Foto = object.getString("foto");
                                    String GrupUser = object.getString("grup_user");
                                    String Id = object.getString("id");
                                    TNama.setText(Nama);
                                    TEmail.setText(Email);


                                    /*edtNama.setText(Nama);
                                    edtAlamat.setText(Alamat);
                                    edtEmail.setText(Email);
                                    edtNik.setText(NIK);
                                    edtUsername.setText(Username);
                                    edtNoHp.setText(Telp);*/
                                    sessionManager.createSession(Nama, Alamat, Email, Username, Password, JenisKelamin, GrupUser, NIK, Telp, Foto, Id);


                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(PengaturanActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(PengaturanActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /* Toast.makeText(PengaturanActivity.this, "errorrr"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(PengaturanActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id", getId);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgV;

        public GetImageFromURL(ImageView imgV) {
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            imgP = null;
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
