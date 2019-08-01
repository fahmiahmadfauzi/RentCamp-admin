package com.if9.rentcampadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.if9.rentcampadmin.Adapter.AdapterDataAlat;
import com.if9.rentcampadmin.Model.ModelDataAlat;
import com.if9.rentcampadmin.Session.SessionManager;
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlatTokoListActivity extends AppCompatActivity {
    private static final String TAG = AlatTokoActivity.class.getSimpleName();
    Toolbar toolbarTk;
    SessionManager sessionManager;
    String getId_toko;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<ModelDataAlat> modelDataList;
    RecyclerView.LayoutManager manager;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText nama_Alat, harga_Alat, stok_Alat;
    Button batal, save;
    String Id_toko;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alat_toko_list);
        toolbarTk = findViewById(R.id.toolbarToko);
        setSupportActionBar(toolbarTk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List barang anda");
        sessionManager = new SessionManager(this);

        // Intent get=getIntent();
        // getId_toko=get.getStringExtra("id_toko");

        HashMap<String, String> userr = sessionManager.getIdTokoo();
        Id_toko = userr.get(SessionManager.ID_TOKO);


        mRecyclerView = findViewById(R.id.recyclerTempAlat);
        modelDataList = new ArrayList<>();
        //getAlat();
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new AdapterDataAlat(AlatTokoListActivity.this, modelDataList);
        mRecyclerView.setAdapter(mAdapter);
        //   mAdapter.notifyDataSetChanged();

        getAlat();


    }

    public void tambah(MenuItem mi) {
        /*Intent go =new Intent(AlatTokoListActivity.this, TambahAlatActivity.class);
        go.putExtra("id_toko",getId_toko);
        startActivity(go);*/
        dialogForm();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutambahtoko, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getAlat() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_GET_ALAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getObject = jsonObject.getString("alat");
                            JSONArray jsonArray = new JSONArray(getObject);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                ModelDataAlat modelData = new ModelDataAlat();

                                String Nama = object.getString("nama_alat");
                                String Harga = object.getString("harga");
                                String Foto = object.getString("foto");
                                String Id_Alat = object.getString("id_alat");
                                String Stok = object.getString("stok");

                                modelData.setId_alat(Id_Alat);
                                modelData.setNama(Nama);
                                modelData.setHarga(Harga);
                                modelData.setFoto(Foto);
                                modelData.setStok(Stok);
                                modelDataList.add(modelData);


                            }
                            mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(AlatTokoListActivity.this, "error reading detail"+e.toString(), Toast.LENGTH_SHORT).show();
                             */
                            Toast.makeText(AlatTokoListActivity.this, "kosong", Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /*Toast.makeText(AlatTokoListActivity.this, "error"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(AlatTokoListActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_toko", Id_toko);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void dialogForm() {
        dialog = new AlertDialog.Builder(AlatTokoListActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_alat, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Tambah alat");

        nama_Alat = dialogView.findViewById(R.id.ednama);
        harga_Alat = dialogView.findViewById(R.id.edharga);
        stok_Alat = dialogView.findViewById(R.id.edstok);

        dialog.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nA = nama_Alat.getText().toString();
                String hA = harga_Alat.getText().toString();
                String sA = stok_Alat.getText().toString();
                addAlat(nA, hA, sA);
                dialog.dismiss();
                //  mAdapter.notifyDataSetChanged();
                //   getAlat();
            }
        });
        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });


        dialog.show();


    }

    private void addAlat(final String nama, final String harga, final String stok) {
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
                                Toast.makeText(AlatTokoListActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        progressDialog.dismiss();
                        Toast.makeText(AlatTokoListActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                       /* startActivity(new Intent(TambahAlatActivity.this, AlatTokoListActivity.class));
                        finish();*/

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AlatTokoListActivity.this, "gagal menambakan, jaringan buruk", Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("nama_alat", nama);
                map.put("harga_alat", harga);
                map.put("stok", stok);
                map.put("id_toko", Id_toko);


                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(simpan);

    }


    public void hapus_alat(final String id_alat) {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menghapus...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_DELETE_ALAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(AlatTokoListActivity.this, "berhasil menghapus", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AlatTokoListActivity.this, TokoActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(AlatTokoListActivity.this, "error" + e.toString(), Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AlatTokoListActivity.this, "error2 " + error, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_alat", id_alat);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
