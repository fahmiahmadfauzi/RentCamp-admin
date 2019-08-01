package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btn_Register;
    TextView link_Login;
    RadioButton jk_p, jk_l;
    String jenisKelamin = "";
    String picture = "http://192.168.0.8/RentCamp/android/admin/profile_image/default.jpg";
    String grupUser = "admin";
    EditText edtNama, edtAlamat, edtEmail, edtUsername, edtPassword, edtPassword2, edtTelp, edtNik;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        link_Login = findViewById(R.id.link_login);

        edtNama = findViewById(R.id.nama_lengkap);
        edtAlamat = findViewById(R.id.alamat);
        edtEmail = findViewById(R.id.email);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        edtPassword2 = findViewById(R.id.pasword2);
        edtNik = findViewById(R.id.nik);
        edtTelp = findViewById(R.id.telp);
        jk_l = findViewById(R.id.lakilaki);
        jk_p = findViewById(R.id.perempuan);
        btn_Register = findViewById(R.id.register);
        progressDialog = new ProgressDialog(this);

        if (jk_l.isClickable()) {
            jenisKelamin = "laki-laki";
        } else {
            jenisKelamin = "perempuan";
        }


        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().equals(edtPassword2.getText().toString())) {
                    // Toast.makeText(RegisterActivity.this, "password sama", Toast.LENGTH_SHORT).show();
                    register();
                } else {
                    Toast.makeText(RegisterActivity.this, "password tidak sama", Toast.LENGTH_SHORT).show();
                }
            }
        });


        link_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });


    }

    private void register() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();
        StringRequest simpan = new StringRequest(Request.Method.POST, ServerApi.URL_REGSITER_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject send = new JSONObject(response);
                            if (!send.getBoolean("error")) {
                                /*Toast.makeText(RegisterActivity.this, "error json", Toast.LENGTH_SHORT).show();*/
                                Toast.makeText(RegisterActivity.this, "gagal menambahkan", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegisterActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "gagal menambakan, jaringan buruk", Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("nama", edtNama.getText().toString());
                map.put("alamat", edtAlamat.getText().toString());
                map.put("email", edtEmail.getText().toString());
                map.put("username", edtUsername.getText().toString());
                map.put("password", edtPassword.getText().toString());
                map.put("jenis_kelamin", jenisKelamin);
                map.put("grup_user", grupUser);
                map.put("telp", edtTelp.getText().toString());
                map.put("foto", picture);
                map.put("nik", edtNik.getText().toString());


                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(simpan);

    }
}
