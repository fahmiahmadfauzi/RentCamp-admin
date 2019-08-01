package com.if9.rentcampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAkunActivity extends AppCompatActivity {
    private static final String TAG = EditAkunActivity.class.getSimpleName();
    Toolbar toolbarAkunn;
    SessionManager sessionManager;
    EditText edtNama, edtAlamat, edtEmail, edtNoHp, edtUsername, edtNik;
    String getId;

    Button btnSimpan, btnF;
    CircleImageView foto;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_akun);
        sessionManager = new SessionManager(this);

        toolbarAkunn = findViewById(R.id.toolbarAkun);
        setSupportActionBar(toolbarAkunn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pengaturan akun");

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

        edtNama = findViewById(R.id.ednama);
        edtAlamat = findViewById(R.id.edalamat);
        edtEmail = findViewById(R.id.edemail);
        edtNik = findViewById(R.id.ednik);
        edtUsername = findViewById(R.id.edusername);
        edtNoHp = findViewById(R.id.ednohp);
        btnSimpan = findViewById(R.id.btnsave);
        foto = findViewById(R.id.fotoprof);
        btnF = findViewById(R.id.btnf);

        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditAkunActivity.this, "masuk", Toast.LENGTH_SHORT).show();
                chooseFile();
            }
        });


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
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

                                    edtNama.setText(Nama);
                                    edtAlamat.setText(Alamat);
                                    edtEmail.setText(Email);
                                    edtNik.setText(NIK);
                                    edtUsername.setText(Username);
                                    edtNoHp.setText(Telp);
                                    sessionManager.createSession(Nama, Alamat, Email, Username, Password, JenisKelamin, GrupUser, NIK, Telp, Foto, Id);


                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(EditAkunActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(EditAkunActivity.this, "kosong", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /*Toast.makeText(EditAkunActivity.this, "errorrr"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(EditAkunActivity.this, "gagal jaringan buruk", Toast.LENGTH_SHORT).show();

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

    private void saveEdit() {

        final String nama = edtNama.getText().toString();
        final String alamat = edtAlamat.getText().toString();
        final String email = edtEmail.getText().toString();
        final String nik = edtNik.getText().toString();
        final String username = edtUsername.getText().toString();
        final String telp = edtNoHp.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_EDIT_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditAkunActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditAkunActivity.this, PengaturanActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(EditAkunActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(EditAkunActivity.this, "gagal", Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /*Toast.makeText(EditAkunActivity.this, "error2 "+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(EditAkunActivity.this, "gagal jaringan buruk", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("email", email);
                params.put("telp", telp);
                params.put("username", username);
                params.put("nik", nik);
                params.put("id", getId);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Toast.makeText(this, "masuk2", Toast.LENGTH_SHORT).show();
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                foto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadPicture(getId, getStringImage(bitmap));

        }
    }

    private void UploadPicture(final String id, final String photo) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();

        StringRequest upload = new StringRequest(Request.Method.POST, ServerApi.URL_EDIT_UPLOAD_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(EditAkunActivity.this, "masuk", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                progressDialog.dismiss();
                                Toast.makeText(EditAkunActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditAkunActivity.this, PengaturanActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(EditAkunActivity.this, "gagal error"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(EditAkunActivity.this, "gagal jaringan buruk", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditAkunActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(EditAkunActivity.this, "error"+error, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("photo", photo);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(upload);

    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodedImage;
    }
}
