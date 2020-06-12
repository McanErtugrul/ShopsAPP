package com.example.myapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {


    private Button btnGiris;
    private TextView txtKullaniciAdi;
    private TextView txtSifre;
    private TextView txtError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnGiris= (Button) findViewById(R.id.loginbutton);
        txtKullaniciAdi=(TextView) findViewById(R.id.usernametext);
        txtSifre=(TextView)findViewById(R.id.passwordtext);

        txtError=(TextView)findViewById(R.id.txtError);


        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GirisControl(txtKullaniciAdi.getText().toString(),txtSifre.getText().toString());

            }
        });


    }
    SoapObject soapObject;
    SoapSerializationEnvelope soapSerializationEnvelope;
    HttpTransportSE httpTransportSE;

    private static final String METHOD_NAME = "KullaniciDogrulama";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION = "http://tempuri.org/KullaniciDogrulama";
    private static final String URL = "https://api20190509020106.azurewebsites.net/WebService1.asmx";
    public void GirisControl(String kullaniciadi,String sifre) {


        final String METHOD_NAME = "KullaniciDogrulama";

        final String SOAP_ACTION = "http://tempuri.org/KullaniciDogrulama";
        soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
        soapObject.addProperty("kullaniciadi", kullaniciadi);
        soapObject.addProperty("sifre", sifre);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpTransportSE = new HttpTransportSE(URL);
        httpTransportSE.debug = true;
        try {
            httpTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);
            SoapPrimitive  soapPrimitive = (SoapPrimitive) soapSerializationEnvelope.getResponse();
            System.out.println(soapPrimitive.toString());

            if(soapPrimitive.toString().equals("Basarili")){
                Intent intocan = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intocan);
                txtError.setVisibility(View.INVISIBLE);
            }
            else{

                txtError.setVisibility(View.VISIBLE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void yenikayitbutton(View v)
    {
        Intent intocan = new Intent(MainActivity.this, YeniKayit.class);
        startActivity(intocan);

    }
    public void sifreyenilebutton(View v)
    {
        Intent intocan = new Intent(MainActivity.this, SifreYenile.class);
        startActivity(intocan);

    }
}
