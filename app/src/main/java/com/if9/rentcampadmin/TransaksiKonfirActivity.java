package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.if9.rentcampadmin.Model.ModelDataTransaksi;
import com.if9.rentcampadmin.Session.SessionManager;
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransaksiKonfirActivity extends AppCompatActivity {
    private static final String TAG = TransaksiListActivity.class.getSimpleName();
    Toolbar toolbarTransaksi;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<ModelDataTransaksi> modelDataList;
    RecyclerView.LayoutManager manager;
    SessionManager sessionManager;
    String idTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_konfir);

        Intent get = getIntent();
        idTransaksi = get.getStringExtra("id_transaksi");
        konfir();
    }

    private void konfir() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_KONFIR_TRANSAKSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TransaksiKonfirActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TransaksiKonfirActivity.this, TransaksiActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
//                            Toast.makeText(TransaksiKonfirActivity.this, "error1"+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(TransaksiKonfirActivity.this, "gagal", Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //  Toast.makeText(TransaksiKonfirActivity.this, "error2 "+error, Toast.LENGTH_SHORT).show();
                        Toast.makeText(TransaksiKonfirActivity.this, "jaringan buruk ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_transaksi", idTransaksi);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
