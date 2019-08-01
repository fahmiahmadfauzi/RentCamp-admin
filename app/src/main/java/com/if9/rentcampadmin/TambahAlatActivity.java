package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahAlatActivity extends AppCompatActivity {
    Toolbar toolbarAlat;
    EditText edNama, edHarga, edStok;
    Button btnSimpan;
    String intent_id_toko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_alat);
        toolbarAlat = findViewById(R.id.toolbarTToko);

        setSupportActionBar(toolbarAlat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edNama = findViewById(R.id.ednama);
        edHarga = findViewById(R.id.edharga);
        edStok = findViewById(R.id.edstok);
        btnSimpan = findViewById(R.id.btnsave);
        Intent data = getIntent();
        intent_id_toko = data.getStringExtra("id_toko");

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlat();
            }
        });


    }

    private void addAlat() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();
        StringRequest simpan = new StringRequest(Request.Method.POST, ServerApi.URL_TAMBAH_ALAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject send = new JSONObject(response);
                            if (!send.getBoolean("error")) {
                                //      Toast.makeText(TambahAlatActivity.this, "error json", Toast.LENGTH_SHORT).show();
                                Toast.makeText(TambahAlatActivity.this, "gagal", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        progressDialog.dismiss();
                        Toast.makeText(TambahAlatActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TambahAlatActivity.this, AlatTokoListActivity.class));
                        finish();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(TambahAlatActivity.this, "gagal menambakan, jaringan buruk", Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("nama_alat", edNama.getText().toString());
                map.put("harga_alat", edHarga.getText().toString());
                map.put("stok", edStok.getText().toString());
                map.put("id_toko", intent_id_toko);


                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(simpan);

    }

}
