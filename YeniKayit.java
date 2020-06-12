package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class YeniKayit extends AppCompatActivity {

    private TextView txtKullaniciAdi;
    private TextView txtSifre;
    private TextView txtError;
    private Button btnKayit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_yeni_kayit);

        txtKullaniciAdi=(TextView) findViewById(R.id.usernamekayittext);
        txtSifre=(TextView)findViewById(R.id.passwordkayittext);
        txtError=(TextView)findViewById(R.id.txtYeniError);
        btnKayit=(Button) findViewById(R.id.loginbutton);

        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YeniKayit(txtKullaniciAdi.getText().toString(),txtSifre.getText().toString());

            }
        });
    }

    SoapObject soapObject;
    SoapSerializationEnvelope soapSerializationEnvelope;
    HttpTransportSE httpTransportSE;

    private static final String METHOD_NAME = "KullaniciYeni";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION = "http://tempuri.org/KullaniciYeni";
    private static final String URL = "https://api20190509020106.azurewebsites.net/WebService1.asmx";
    public void YeniKayit(String kullaniciadi,String yenisifre) {
        if(kullaniciadi.equals("") || yenisifre.equals("")){
            txtError.setText("Kullanici Adini veya Sifreyi boş geçemezsiniz");
            txtError.setVisibility(View.VISIBLE);
        }
        else {
            final String METHOD_NAME = "KullaniciYeni";

            final String SOAP_ACTION = "http://tempuri.org/KullaniciYeni";
            soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
            soapObject.addProperty("kullaniciadi", kullaniciadi);
            soapObject.addProperty("yenisifre", yenisifre);

            soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapSerializationEnvelope.dotNet = true;
            soapSerializationEnvelope.setOutputSoapObject(soapObject);

            httpTransportSE = new HttpTransportSE(URL);
            httpTransportSE.debug = true;
            try {
                httpTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);
                SoapObject soapPrimitive = (SoapObject) soapSerializationEnvelope.getResponse();
                // System.out.println(soapPrimitive.toString());
                txtError.setText("Kayit eklenmiştir");
                txtError.setVisibility(View.VISIBLE);


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
