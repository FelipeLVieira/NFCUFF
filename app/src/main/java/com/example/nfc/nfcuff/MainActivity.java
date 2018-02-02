package com.example.nfc.nfcuff;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private TextView title;
    private TextView txtTagContent;
    private Switch tagContentSwitch;
    private NfcAdapter nfcAdapter;
    private NfcManager nfcManager;


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,
                "onCreate()",
                Toast.LENGTH_SHORT).show();

        //Views
        title = findViewById(R.id.txtTitle);
        txtTagContent = findViewById(R.id.txtTagConent);
        tagContentSwitch = findViewById(R.id.tagContentSwitch);
        tagContentSwitch.setOnCheckedChangeListener(this);

        //Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //Manager
        nfcManager = new NfcManager(this);

        //Check if the device is NFC ready
        try {
            nfcManager.verifyNFC();
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NfcManager.NFCNotSupported nfcnsup) {
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NfcManager.NFCNotEnabled nfcnEn) {
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
        } catch (NfcManager.NFCNotSupported nfcnsup) {
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NfcManager.NFCNotEnabled nfcnEn) {
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

        String tagUniqueId = nfcManager.getTagUniqueIdFromIntent(intent);
        String tagContent = nfcManager.getTextContentFromTag(intent);
        String deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        NfcDeviceData nfcDeviceData = new NfcDeviceData(deviceId, tagUniqueId, tagContent, Build.MODEL, Build.MANUFACTURER, Build.BRAND, Build.USER, Build.VERSION.SDK);

        txtTagContent.setText("Device Unique ID: " + nfcDeviceData.getDeviceUniqueID() +
                "\nTag Unique ID: " + nfcDeviceData.getTagUniqueID() +
                "\n Content: " + nfcDeviceData.getTagContent() +
                "\n Build Model: " + nfcDeviceData.getBuildModel() +
                "\n Manufcturer: " + nfcDeviceData.getBuildManufacturer() +
                "\n Brand: " + nfcDeviceData.getBuildBrand() +
                "\n User: " + nfcDeviceData.getBuildUser() +
                "\n Version SDK: " + nfcDeviceData.getBuildVersionSDK());

        firebaseManager.storeNfcTagDataOnFirebase(nfcDeviceData);
    }

    private void toggleTagInfo() {
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.tagContentSwitch:
                if (isChecked == true) {
                    txtTagContent.setVisibility(View.VISIBLE);
                } else {
                    txtTagContent.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
