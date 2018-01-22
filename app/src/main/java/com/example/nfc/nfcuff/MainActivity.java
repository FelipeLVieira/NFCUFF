package com.example.nfc.nfcuff;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView txtTagContent;
    private NfcAdapter nfcAdapter;
    private NFCManager nfcManager;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";


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
        }
        catch(NFCManager.NFCNotSupported nfcnsup) {
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        }
        catch(NFCManager.NFCNotEnabled nfcnEn) {
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
        }
        catch(NFCManager.NFCNotSupported nfcnsup) {
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        }
        catch(NFCManager.NFCNotEnabled nfcnEn) {
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

        /*String action = intent.getAction();


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new AsyncNdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new AsyncNdefReaderTask().execute(tag);
                    break;
                }
            }
        }*/
    }



    public void salvarInformacoesNoFirebase(String tagContent, Tag tagNFC, String tagAction) {

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Toast.makeText(this,
                "salvarInformacoesNoFirebase()",
                Toast.LENGTH_SHORT).show();

        //String tagNFCString = bytesToHexString(tagNFC.getId());
        //CoordenadasGPS coordenadasGPS = getCoordenadasGPS();
        //String endereco = getEndereco(coordenadasGPS.getLatitude(), coordenadasGPS.getLongitude());

        LeituraNFC leitura = new LeituraNFC();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("leituras");

        database.setValue(leitura);

    }


    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }


    public String getEndereco(double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault()); //it is Geocoder
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }
            return builder.toString(); //This is the complete address.
        } catch (IOException | NullPointerException e) {
        }

        return "";
    }


    public CoordenadasGPS getCoordenadasGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Location location = null;
            if (lm != null) {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                CoordenadasGPS coordenadasGPS = new CoordenadasGPS();

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                coordenadasGPS.setLatitude(latitude);
                coordenadasGPS.setLongitude(longitude);

                return coordenadasGPS;
            }
        }
        return null;
    }
}
