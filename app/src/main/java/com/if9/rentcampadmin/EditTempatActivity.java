package com.if9.rentcampadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.if9.rentcampadmin.Util.ServerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditTempatActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String TAG = EditTempatActivity.class.getSimpleName();

    Toolbar toolbarET;
    String idToko;
    CircleImageView foto;
    Button edt, simpan;
    EditText namaTk, alamatTk, latT, lngT;
    Marker marker;
    LatLng center, latlng;
    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;
    String title;
    String lati, longi;
    private Bitmap bitmap;
    private double mLatitude = 0;
    private double mLongitude = 0;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            Toast.makeText(this, "google maps", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_edit_tempat);
            initMap();
        } else {

        }

        toolbarET = findViewById(R.id.toolbaredttmpt);
        setSupportActionBar(toolbarET);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit toko");
        foto = findViewById(R.id.fotoprof);
        edt = findViewById(R.id.btnf);
        namaTk = findViewById(R.id.ednama);
        alamatTk = findViewById(R.id.edalamat);
        latT = findViewById(R.id.edlat);
        lngT = findViewById(R.id.edlng);
        simpan = findViewById(R.id.btnsave);

        Intent data = getIntent();

        idToko = data.getStringExtra("id_toko");


        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
            }
        });
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
        //  Toast.makeText(this, "masuk2", Toast.LENGTH_SHORT).show();
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                foto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadPicture(idToko, getStringImage(bitmap));

        }
    }

    private void UploadPicture(final String id, final String photo) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();
        StringRequest upload = new StringRequest(Request.Method.POST, ServerApi.URL_EDIT_UPLOAD_FOTO_TOKO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            progressDialog.dismiss();
                            if (success.equals("1")) {
                                startActivity(new Intent(EditTempatActivity.this, TokoActivity.class));
                                finish();
                                Toast.makeText(EditTempatActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*   Toast.makeText(EditTempatActivity.this, "gagal error"+e.toString(), Toast.LENGTH_SHORT).show();
                             */
                            Toast.makeText(EditTempatActivity.this, "gagal ", Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /* Toast.makeText(EditTempatActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(EditTempatActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();

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


    private void getTokoSpes() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_GET_TOKO_ADMIN_SPESIFIK,
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
                                    String Nama = object.getString("nama_toko");
                                    String Alamat = object.getString("alamat_toko");
                                    String Lat = object.getString("lat");
                                    String Lng = object.getString("lng");
                                    String Id = object.getString("id");
                                    title = Nama;

                                    namaTk.setText(Nama);
                                    alamatTk.setText(Alamat);
                                    latT.setText(Lat);
                                    lngT.setText(Lng);
                                    latlng = new LatLng(Double.parseDouble(object.getString("lat")), Double.parseDouble(object.getString("lng")));
                                    addMarker(latlng, title);
                                    goToLocationZoom(Double.parseDouble(object.getString("lat")), Double.parseDouble(object.getString("lng")), 15);


                                }
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(EditTempatActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(EditTempatActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /*Toast.makeText(EditTempatActivity.this, "errorrr"+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(EditTempatActivity.this, "jaringan buruk", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onResume() {
        super.onResume();
        getTokoSpes();
    }


    private void saveEdit() {

        final String nama = namaTk.getText().toString();
        final String alamat = alamatTk.getText().toString();
        final String lat = latT.getText().toString();
        final String lng = lngT.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_EDIT_TOKO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditTempatActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditTempatActivity.this, TokoActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            /*Toast.makeText(EditTempatActivity.this, "error1"+e.toString(), Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(EditTempatActivity.this, "gagal", Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        /* Toast.makeText(EditTempatActivity.this, "error2 "+error, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(EditTempatActivity.this, "jaringan buruk, gagal ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("id", idToko);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addMarker(LatLng latlng, final String title) {


        markerOptions.position(latlng);
        markerOptions.title(title);
        mMap.addMarker(markerOptions);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        lati = String.valueOf(mLatitude);
        longi = String.valueOf(mLongitude);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        center = new LatLng(-6.894796, 110.638413);
        cameraPosition = new CameraPosition.Builder().target(center).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        if (mMap != null) {

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    EditTempatActivity.this.setMarker("Local", latLng.latitude, latLng.longitude);
                }
            });
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                //kondisi saat marker selesai di drag
                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder gc = new Geocoder(EditTempatActivity.this);
                    LatLng ll = marker.getPosition();
                    double lat = ll.latitude;
                    double lng = ll.longitude;
                    List<Address> list = null;
                    try {
                        list = gc.getFromLocation(lat, lng, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address add = list.get(0);
                    marker.setTitle(add.getLocality());
                    marker.showInfoWindow();
                }
            });
            //menampilkan informasi
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {


                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);

                    TextView tvlocality = v.findViewById(R.id.tv_locality);
                    TextView tvlat = v.findViewById(R.id.tv_lat);
                    TextView tvlng = v.findViewById(R.id.tv_lng);
                    TextView tvsnippet = v.findViewById(R.id.tv_snippet);

                    LatLng ll = marker.getPosition();
                    tvlocality.setText(marker.getTitle());
                    tvlat.setText("Latitude :" + ll.latitude);
                    tvlng.setText("Longitude :" + ll.longitude);
                    tvsnippet.setText(marker.getSnippet());
                    return v;

                }
            });
        }

        //    getMarkers();
        // Add a marker in Sydney and move the camera
//           LatLng sydney = new LatLng(-34, 151);
//           mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//           mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //untuk mengetahui posisi kita
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);


       /* if (location != null) {
            onLocationChanged(location);
        }*/

        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;

        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "cant connect", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    private void setMarker(String locality, double lat, double lng) {
        if (marker != null) {
            marker.remove();
        }


        MarkerOptions option = new MarkerOptions().title(locality).draggable(true).position(new LatLng(lat, lng)).snippet("here");
        marker = mMap.addMarker(option);


    }


}
