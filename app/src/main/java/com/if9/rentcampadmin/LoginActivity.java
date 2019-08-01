package com.if9.rentcampadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    EditText edt_Username, edt_Password;
    TextView link_Register;
    Button btn_Login;
    ProgressBar Loading;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        edt_Username = findViewById(R.id.username);
        edt_Password = findViewById(R.id.password);
        link_Register = findViewById(R.id.link_register);
        btn_Login = findViewById(R.id.login);
        Loading = findViewById(R.id.loading);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((edt_Username.getText().toString().isEmpty()) || (edt_Password.getText().toString().isEmpty())) {
                    Toast.makeText(LoginActivity.this, "harus diisi", Toast.LENGTH_SHORT).show();

                } else {

                    //   Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                    //   startActivity(home);
                    login();
                    // finish();

                }
            }
        });

        link_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });


    }


    private void login() {
        Loading.setVisibility(View.VISIBLE);
        StringRequest masuk = new StringRequest(Request.Method.POST, ServerApi.URL_LOGIN_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
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
                                    sessionManager.createSession(Nama, Alamat, Email, Username, Password, JenisKelamin, GrupUser, NIK, Telp, Foto, Id);
                                 /*   Intent ParsingToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                    ParsingToHome.putExtra(key_nama, Nama);
                                    ParsingToHome.putExtra(key_alamat, Alamat);
                                    ParsingToHome.putExtra(key_email, Email);
                                    ParsingToHome.putExtra(key_username, Username);
                                    ParsingToHome.putExtra(key_password, Password);
                                    ParsingToHome.putExtra(key_nik, NIK);
                                    ParsingToHome.putExtra(key_telp, Telp);
                                    ParsingToHome.putExtra(key_jk, JenisKelamin);
                                    ParsingToHome.putExtra(key_foto, Foto);
                                    ParsingToHome.putExtra(key_grup_user, GrupUser);
                                    startActivity(ParsingToHome);*/
                                   /* if (GrupUser.equals("admin")){
                                        startActivity(new Intent(LoginActivity.this, HomeAdminActivity.class));
                                    }else if (GrupUser.equals("customer")){
                                      startActivity(new Intent(LoginActivity.this, HomeUserActivity.class));
                                    }*/
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();

                                    Loading.setVisibility(View.GONE);


                                    // Toast.makeText(MainActivity.this, "Success Login. \n name :"+nam+"\n email :"+emai, Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Loading.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Username dan Password salah !!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "gagal login", Toast.LENGTH_SHORT).show();
                            Loading.setVisibility(View.GONE);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Loading.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "gagal login", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", edt_Username.getText().toString());
                map.put("password", edt_Password.getText().toString());


                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(masuk);

    }
}
