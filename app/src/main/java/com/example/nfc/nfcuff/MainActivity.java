package com.example.nfc.nfcuff;

import android.content.Intent;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private TextView textView1;
    private TextView txtTagContent;
    private NfcAdapter nfcAdapter;
    private NFCManager nfcManager;

    //Define a request code to send to Google Play services
    LocationManager locationManager;
    String provider;
    private double currentLatitude;
    private double currentLongitude;


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,
                "onCreate()",
                Toast.LENGTH_SHORT).show();

        //Views
        textView1 = findViewById(R.id.tv);
        txtTagContent = findViewById(R.id.txtTagConent);

        //Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //Manager
        nfcManager = new NFCManager(this);

        //Validar se o dispositivo possui suporte a NFC
        try {
            nfcManager.verifyNFC();
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NFCManager.NFCNotSupported nfcnsup) {
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NFCManager.NFCNotEnabled nfcnEn) {
            txtTagContent.setText("Este dispositivo não está habilitado para leitura de NFC.");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this,
                "onResume()",
                Toast.LENGTH_SHORT).show();

        try {
            nfcManager.verifyNFC();
            nfcManager.setupForegroundDispatch(this);
        } catch (NFCManager.NFCNotSupported nfcnsup) {
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NFCManager.NFCNotEnabled nfcnEn) {
            txtTagContent.setText("Este dispositivo não está habilitado para leitura de NFC.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this,
                "onPause()",
                Toast.LENGTH_SHORT).show();

        nfcManager.disableDispatch();
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this,
                "onNewIntent()",
                Toast.LENGTH_SHORT).show();

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Toast.makeText(this,
                "handleIntent()",
                Toast.LENGTH_SHORT).show();

        FirebaseManager firebaseManager = new FirebaseManager(this);

        String tagContent = nfcManager.getTextContentFromTag(intent);
        txtTagContent.setText( tagContent + "\n Model: " + Build.MODEL + "\n Brand: " + Build.BRAND);

        firebaseManager.salvarInformacoesNoFirebase(tagContent);
    }
}
