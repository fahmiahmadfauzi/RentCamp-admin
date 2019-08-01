package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.if9.rentcampadmin.Adapter.AdapterDataTransaksi;
import com.if9.rentcampadmin.Model.ModelDataTransaksi;
import com.if9.rentcampadmin.Session.SessionManager;
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiwayatListActivity extends AppCompatActivity {
    private static final String TAG = RiwayatListActivity.class.getSimpleName();
    Toolbar toolbarRiwayat;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<ModelDataTransaksi> modelDataList;
    RecyclerView.LayoutManager manager;
    SessionManager sessionManager;
    String idToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_list);
        toolbarRiwayat = findViewById(R.id.toolbarriwayat);
        setSupportActionBar(toolbarRiwayat);
        getSupportActionBar().setTitle("Riwayat List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);

        Intent get = getIntent();
        idToko = get.getStringExtra("id_toko");
        mRecyclerView = findViewById(R.id.recyclerTempTransaksi);
        modelDataList = new ArrayList<>();
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new AdapterDataTransaksi(RiwayatListActivity.this, modelDataList);
        mRecyclerView.setAdapter(mAdapter);
        getRiwayatTransaksi();
    }

    private void getRiwayatTransaksi() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_GET_RIWAYAT_TRANSAKSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getObject = jsonObject.getString("transaksi");
                            JSONArray jsonArray = new JSONArray(getObject);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                ModelDataTransaksi modelData = new ModelDataTransaksi();

                                String IdTransaksi = object.getString("id_transaksi");
                                String tgl = object.getString("tgl");
                                String Nama = object.getString("nama_alat");
                                String Harga = object.getString("harga_alat");
                                String idToko = object.getString("id_toko");
                                String Status = object.getString("status");


                                modelData.setId_transaksi(IdTransaksi);
                                modelData.setTgl(tgl);
                                modelData.setNama_alat(Nama);
                                modelData.setHarga_alat(Harga);
                                modelData.setId_toko(idToko);
                                modelData.setStatus(Status);

                                modelDataList.add(modelData);


                            }
                            mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            //Toast.makeText(RiwayatListActivity.this, "error reading detail"+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(RiwayatListActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /*Toast.makeText(RiwayatListActivity.this, "error"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(RiwayatListActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_toko", idToko);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
