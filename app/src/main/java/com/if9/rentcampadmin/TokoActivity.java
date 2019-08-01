package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class TokoActivity extends AppCompatActivity {
    private static final String TAG = TokoActivity.class.getSimpleName();
    Toolbar toolbarTk;
    SessionManager sessionManager;
    String getId;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<ModelData> modelDataList;
    RecyclerView.LayoutManager manager;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);
        toolbarTk = findViewById(R.id.toolbarToko);
        setSupportActionBar(toolbarTk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Toko anda");

        sessionManager = new SessionManager(this);
        HashMap<String, String> userr = sessionManager.getUserDetail();
        getId = userr.get(SessionManager.ID);


        mRecyclerView = findViewById(R.id.recyclerTemp);
        modelDataList = new ArrayList<>();
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new AdapterData(TokoActivity.this, modelDataList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getToko();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutambahtoko, menu);
        return true;
    }

    public void tambah(MenuItem mi) {
        startActivity(new Intent(TokoActivity.this, TambahTokoActivity.class));


    }

    @Override
    protected void onResume() {
        super.onResume();
        // getToko();
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

                            Toast.makeText(TokoActivity.this, "kosong", Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(TokoActivity.this, "error reading detail"+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(TokoActivity.this, "Jaringan buruk", Toast.LENGTH_SHORT).show();

                        //Toast.makeText(TokoActivity.this, "error"+error, Toast.LENGTH_SHORT).show();
                       /* if(error instanceof NoConnectionError){
                            ConnectivityManager cm = (ConnectivityManager)mContext
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo activeNetwork = null;
                            if (cm != null) {
                                activeNetwork = cm.getActiveNetworkInfo();
                            }
                            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                                Toast.makeText(TokoActivity.this, "Server is not connected to internet.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TokoActivity.this, "Your device is not connected to internet.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                                || (error.getCause().getMessage() != null
                                && error.getCause().getMessage().contains("connection"))){
                            Toast.makeText(TokoActivity.this, "Your device is not connected to internet.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof MalformedURLException){
                            Toast.makeText(TokoActivity.this, "Bad Request.", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                                || error.getCause() instanceof JSONException
                                || error.getCause() instanceof XmlPullParserException){
                            Toast.makeText(TokoActivity.this, "Parse Error (because of invalid json or xml).",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error.getCause() instanceof OutOfMemoryError){
                            Toast.makeText(TokoActivity.this, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
                        }else if (error instanceof AuthFailureError){
                            Toast.makeText(TokoActivity.this, "server couldn't find the authenticated request.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                            Toast.makeText(TokoActivity.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
                        }else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                                || error.getCause() instanceof ConnectTimeoutException
                                || error.getCause() instanceof SocketException
                                || (error.getCause().getMessage() != null
                                && error.getCause().getMessage().contains("Connection timed out"))) {
                            Toast.makeText(TokoActivity.this, "Connection timeout error",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TokoActivity.this, "An unknown error occurred.",
                                    Toast.LENGTH_SHORT).show();
                        }*/
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
