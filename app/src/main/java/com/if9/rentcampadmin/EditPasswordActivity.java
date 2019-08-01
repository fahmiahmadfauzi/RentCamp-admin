package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class EditPasswordActivity extends AppCompatActivity {

    private static final String TAG = EditPasswordActivity.class.getSimpleName();
    Toolbar toolbarPass;
    SessionManager sessionManager;
    EditText edPass1, edPass2, edPass3;
    Button btnSimpan;
    String getId, sPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        toolbarPass = findViewById(R.id.toolbarpass);
        setSupportActionBar(toolbarPass);
        getSupportActionBar().setTitle("Ganti password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);

        edPass1 = findViewById(R.id.edtpass1);
        edPass2 = findViewById(R.id.edtpass2);
        edPass3 = findViewById(R.id.edtpass3);
        btnSimpan = findViewById(R.id.btnsimpan);


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

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edPass1.getText().toString().equals(sPass)) {
                    Toast.makeText(EditPasswordActivity.this, "bisa ubah pass", Toast.LENGTH_SHORT).show();
                    if (edPass2.getText().toString().equals(edPass3.getText().toString())) {
                        saveEditPass();
                        //Toast.makeText(EditPasswordActivity.this, "pass sama", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(EditPasswordActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditPasswordActivity.this, "password salah", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void saveEditPass() {

        final String password = edPass3.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();


        StringRequest stringRequestPass = new StringRequest(Request.Method.POST, ServerApi.URL_EDIT_PASS_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditPasswordActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditPasswordActivity.this, PengaturanActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /* Toast.makeText(EditPasswordActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(EditPasswordActivity.this, "gagal", Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /* Toast.makeText(EditPasswordActivity.this, "error2 "+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(EditPasswordActivity.this, "jaringan buruk ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("id", getId);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequestPass);
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
                                    sPass = Password;


                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(EditPasswordActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(EditPasswordActivity.this, "kosong ", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /*Toast.makeText(EditPasswordActivity.this, "errorrr"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(EditPasswordActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();

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

}
