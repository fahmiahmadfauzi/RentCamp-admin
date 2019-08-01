package com.if9.rentcampadmin;

import android.app.ProgressDialog;
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
import com.if9.rentcampadmin.Adapter.AdapterData;
import com.if9.rentcampadmin.Model.ModelData;
import com.if9.rentcampadmin.Session.SessionManager;
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiwayatActivity extends AppCompatActivity {
    private static final String TAG = RiwayatActivity.class.getSimpleName();
    Toolbar toolbarRiwayat;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<ModelData> modelDataList;
    RecyclerView.LayoutManager manager;
    SessionManager sessionManager;
    String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        toolbarRiwayat = findViewById(R.id.toolbarriwayat);
        setSupportActionBar(toolbarRiwayat);
        getSupportActionBar().setTitle("Riwayat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);
        HashMap<String, String> userr = sessionManager.getUserDetail();
        getId = userr.get(SessionManager.ID);


        mRecyclerView = findViewById(R.id.recyclerTemp);
        modelDataList = new ArrayList<>();
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new AdapterData(RiwayatActivity.this, modelDataList);
        mRecyclerView.setAdapter(mAdapter);
        getToko();
    }

    private void getToko() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_GET_TOKO_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getObject = jsonObject.getString("toko");
                            JSONArray jsonArray = new JSONArray(getObject);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                ModelData modelData = new ModelData();

                                String IdToko = object.getString("id_toko");
                                String Nama = object.getString("nama_toko");
                                String Alamat = object.getString("alamat_toko");
                                String Foto = object.getString("foto");
                                String Id = object.getString("id");

                                modelData.setId_toko(IdToko);
                                modelData.setNama(Nama);
                                modelData.setAlamat(Alamat);
                                modelData.setFoto(Foto);
                                modelDataList.add(modelData);


                            }
                            mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*     Toast.makeText(RiwayatActivity.this, "error reading detail"+e.toString(), Toast.LENGTH_SHORT).show();
                             */
                            Toast.makeText(RiwayatActivity.this, "kosong", Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /*Toast.makeText(RiwayatActivity.this, "error"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(RiwayatActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();
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
}
