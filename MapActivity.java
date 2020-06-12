package com.example.myapplication;






import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    final static String URI="https://webapi20190507095818.azurewebsites.net/api/values";
    JSONObject json;
    private final static int REQUEST_lOCATION = 90;
    private TextView txtLat;
    private TextView txtLong;
    private TextView txtDistance;
    private TextView txtSearch;
    private Button btnManLocation;
    private GoogleMap mMap;
    MarkerOptions markerOptions = new MarkerOptions();
    ArrayList<Marker> markers = new ArrayList<>();
    String lat, lng;
    Double latitude, longitude,latitudeB,longitudeB;
    Spinner spinner;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactList = new ArrayList<>();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_anaekran);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner=(Spinner)findViewById(R.id.spinner);
        txtLat=(TextView) findViewById(R.id.txtLat);
        txtLong=(TextView) findViewById(R.id.txtLong);
        btnManLocation=(Button) findViewById(R.id.btnManuelLocation);

        txtSearch=(TextView) findViewById(R.id.txtSearch);
        txtDistance=(TextView) findViewById(R.id.txtDistance);

        btnManLocation.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markers.clear();
                mMap.clear();
                lat = txtLat.getText().toString();
                latitude = Double.valueOf( lat ).doubleValue();

                lng = txtLong.getText().toString();
                longitude = Double.valueOf( lng ).doubleValue();

                LatLng latLng = new LatLng( latitude, longitude );
               // GirisControl(txtKullaniciAdi.getText().toString(),txtSifre.getText().toString());

                Marker marker = mMap.addMarker( new MarkerOptions().position( new LatLng( latitude, longitude ) ).title("Buradasınız").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                markers.add( marker ); //marker ekliyoruz
                mMap.animateCamera( CameraUpdateFactory.newLatLng( latLng ) );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                mMap.getUiSettings().setZoomControlsEnabled(true); // zoom butonları aktif edildi
                new Data().execute("FirmaAdi","LAT","LONG","KampanyaTur","KampanyaIcerik");
              //  ShouldAddMarker(41.025629, 28.974138,41.047967, 28.933790);
            }
        });


    }


        protected JSONObject readdata()  throws ClientProtocolException, IOException, JSONException  {
        HttpClient client=new DefaultHttpClient();
        HttpGet get=new HttpGet(URI);
        HttpResponse response=client.execute(get);
        StatusLine status=response.getStatusLine();
        int s=status.getStatusCode();
        if(s==200){

            HttpEntity e=response.getEntity();
            String data= EntityUtils.toString(e);
            JSONArray posts=new JSONArray(data);


            JSONObject last=posts.getJSONObject(0);
            return last;
        }
        return null;
    }

    public class Data extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://webapi20190507095818.azurewebsites.net/api/values/";
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {


                    // Getting JSON Array node
                    JSONArray jsonArray = new JSONArray(jsonStr);


                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String FirmaAdi = c.getString("FirmaAdi");
                        String LAT = c.getString("LAT");
                        String LONG = c.getString("LONG");
                        String KampanyaTur = c.getString("KampanyaTur");
                        String KampanyaIcerik = c.getString("KampanyaIcerik");



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("FirmaAdi", FirmaAdi);
                        contact.put("LAT", LAT);
                        contact.put("LONG", LONG);
                        contact.put("KampanyaTur", KampanyaTur);
                        contact.put("KampanyaIcerik", KampanyaIcerik);
                        // adding contact to contact list
                        contactList.add(contact);
                    }





                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        Double txtDistanceD;
        Double itemLat;
        Double itemLong;
        @Override
        protected void onPostExecute(String data) {
            //txtLat.setText(data);
            if(!txtDistance.getText().toString().equals("")) {
                txtDistanceD = Double.valueOf(txtDistance.getText().toString()).doubleValue();
            }
            else{
                txtDistanceD=100.0;
            }
            String filtre = spinner.getSelectedItem().toString();



            for(int i=0;i<contactList.size();i++) {


                if (!txtSearch.getText().toString().equals("")) {
                    if (contactList.get(i).get("FirmaAdi").toString().contains(txtSearch.getText().toString())) {


                        String title = contactList.get(i).get("FirmaAdi").toString();
                        String icerik = contactList.get(i).get("KampanyaIcerik").toString();
                        itemLat = 0.0;
                        itemLong = 0.0;
                        itemLat = Double.valueOf(contactList.get(i).get("LAT")).doubleValue();
                        itemLong = Double.valueOf(contactList.get(i).get("LONG")).doubleValue();


                        lat = txtLat.getText().toString();
                        latitude = Double.valueOf(lat).doubleValue();

                        lng = txtLong.getText().toString();
                        longitude = Double.valueOf(lng).doubleValue();


                        latitudeB = Double.valueOf(itemLat).doubleValue();

                        longitudeB = Double.valueOf(itemLong).doubleValue();






                        LatLng latLng = new LatLng(latitudeB, longitudeB);
                        // GirisControl(txtKullaniciAdi.getText().toString(),txtSifre.getText().toString());
                        markerOptions.position(new LatLng(latitudeB, longitudeB))
                                .title(title)
                                .snippet(icerik)
                        ;
                        Marker marker = mMap.addMarker(markerOptions);
                        markers.add(marker); //marker ekliyoruz



                    }
                }

                else {


                    if (contactList.get(i).get("KampanyaTur").toString().equals(filtre) || filtre.equals("Tümü")) {
                        // if(ShouldAddMarker(contactList.get(i).get("LAT"),contactList.get(i).get("LONG"))){


                        String title = contactList.get(i).get("FirmaAdi").toString();
                        String icerik = contactList.get(i).get("KampanyaIcerik").toString();
                        itemLat = 0.0;
                        itemLong = 0.0;
                        itemLat = Double.valueOf(contactList.get(i).get("LAT")).doubleValue();
                        itemLong = Double.valueOf(contactList.get(i).get("LONG")).doubleValue();


                        lat = txtLat.getText().toString();
                        latitude = Double.valueOf(lat).doubleValue();

                        lng = txtLong.getText().toString();
                        longitude = Double.valueOf(lng).doubleValue();


                        latitudeB = Double.valueOf(itemLat).doubleValue();

                        longitudeB = Double.valueOf(itemLong).doubleValue();

                        Location locationA = new Location("point A");

                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("point B");

                        locationB.setLatitude(latitudeB);
                        locationB.setLongitude(longitudeB);


                        float distance = locationA.distanceTo(locationB);
                        if (distance < txtDistanceD) {

                            LatLng latLng = new LatLng(latitudeB, longitudeB);
                            // GirisControl(txtKullaniciAdi.getText().toString(),txtSifre.getText().toString());
                            markerOptions.position(new LatLng(latitudeB, longitudeB))
                                    .title(title)
                                    .snippet(icerik)
                            ;
                            Marker marker = mMap.addMarker(markerOptions);
                            markers.add(marker); //marker ekliyoruz
                        }
                    }


                    // }


                }
            }

          /*  LatLng latLng = new LatLng( 41.047967, 28.933790 );
            // GirisControl(txtKullaniciAdi.getText().toString(),txtSifre.getText().toString());

            Marker marker = mMap.addMarker( new MarkerOptions().position( new LatLng( 41.047967, 28.933790 ) ) );
             marker = mMap.addMarker( new MarkerOptions().position( new LatLng( 41.047967, 28.933795 ) ) );
            markers.add( marker ); //marker ekliyoruz*/


            // GirisControl(txtKullaniciAdi.getText().toString(),txtSifre.getText().toString());





        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            mMap.setMyLocationEnabled( true );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_lOCATION );
            }

        }


/*




      //  LatLng galataKulesi = new LatLng(41.025629, 28.974138);
       // googleMap.addMarker(new MarkerOptions().position(galataKulesi).title("Burası Galata Kulesi"));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.047967, 28.933790))
                .title("BURADASINIZ")
                .snippet("Eyüp Sultan Cami"));

     //   googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(galataKulesi, 12));

        googleMap.getUiSettings().setMyLocationButtonEnabled(true); // konumumu göster butonu aktif edildi
        googleMap.getUiSettings().setCompassEnabled(true); // pusula butonu aktif edildi
        googleMap.getUiSettings().setZoomControlsEnabled(true); // zoom butonları aktif edildi

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // hibrit görünümü set edildi
//        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setTrafficEnabled(true); // trafik durumu aktif edildi


      /*  PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(41.053832, 28.972283))
                .add(new LatLng(41.055056, 28.992326))
                .add(new LatLng(41.048958, 28.990987))
                .add(new LatLng(41.040214, 28.979840));

        Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setStrokeColor(Color.GREEN); // poligon çizgileri yeşil olarak belirlendi*/

    }


   /* private boolean ShouldAddMarker(String latB, String lngB) {

        lat = txtLat.getText().toString();
        latitude = Double.valueOf( lat ).doubleValue();

        lng = txtLong.getText().toString();
        longitude = Double.valueOf( lng ).doubleValue();




        latitudeB = Double.valueOf( latB ).doubleValue();

        longitudeB = Double.valueOf( lngB ).doubleValue();

        Location locationA = new Location( "point A" );

        locationA.setLatitude( latitude );
        locationA.setLongitude( longitude );

        Location locationB = new Location( "point B" );

        locationB.setLatitude( latitudeB );
        locationB.setLongitude( longitudeB );


        float distance = locationA.distanceTo( locationB );
        if(distance<1000){

            return true;
        }
        return false;
    }*/
}