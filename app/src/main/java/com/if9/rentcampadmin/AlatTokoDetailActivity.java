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
import android.widget.Button;
import android.widget.ImageView;
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

public class AlatTokoDetailActivity extends AppCompatActivity {
    private static final String TAG = AlatTokoDetailActivity.class.getSimpleName();
    Toolbar tDetail;
    String idAlat;
    TextView tnama, tharga, tstok;
    CircleImageView fotoAlat;
    Bitmap bitmap;

    Button hps, edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alat_toko_detail);

        tDetail = findViewById(R.id.toolbarDetail);
        setSupportActionBar(tDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail alat");

        Intent data = getIntent();
        String intent_id_alat = data.getStringExtra("id_alat");
        String intent_harga_alat = data.getStringExtra("nama_alat");
        String intent_stok = data.getStringExtra("stok");
        String intent_foto = data.getStringExtra("foto");

        idAlat = intent_id_alat;


        tnama = findViewById(R.id.t_namaAlat);
        tharga = findViewById(R.id.t_hargaAlat);
        tstok = findViewById(R.id.t_stokAlat);
        hps = findViewById(R.id.btnhapus);
        edt = findViewById(R.id.btnedit);
        fotoAlat = findViewById(R.id.fotoalat);


        hps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapus_alat();
            }
        });

        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(AlatTokoDetailActivity.this, AlatTokoEditActivity.class);
                go.putExtra("id_alat", idAlat);
                startActivity(go);


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAlatSpes();
    }

    private void getAlatSpes() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_ALAT_SPESIFIK,
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
                                    String Nama = object.getString("nama_alat");
                                    String Harga = object.getString("harga");
                                    //    String Lat = object.getString("lat");
                                    //    String Lng = object.getString("lng");
                                    String Stok = object.getString("stok");
                                    String foto = object.getString("foto");
                                    tnama.setText(Nama);
                                    tharga.setText(Harga);
                                    tstok.setText(Stok);
                                    new GetImageFromURL(fotoAlat).execute(foto);


                                }
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(AlatTokoDetailActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AlatTokoDetailActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_alat", idAlat);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void hapus_alat() {


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
                                Toast.makeText(AlatTokoDetailActivity.this, "berhasil menghapus", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AlatTokoDetailActivity.this, AlatTokoListActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(AlatTokoDetailActivity.this, "gagal jaringan buruk", Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AlatTokoDetailActivity.this, "gagal jaringan buruk", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_alat", idAlat);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgV;

        public GetImageFromURL(ImageView imgV) {
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            fotoAlat = null;
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
