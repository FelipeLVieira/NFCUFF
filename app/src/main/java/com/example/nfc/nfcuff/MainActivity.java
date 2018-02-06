package com.example.nfc.nfcuff;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.system.Os;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private TextView title;
    private TextView txtTagContent;
    private Switch tagContentSwitch;
    private NfcAdapter nfcAdapter;
    private NfcManager nfcManager;
    private FirebaseManager firebaseManager;


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,
                "onCreate()",
                Toast.LENGTH_SHORT).show();

        //Referências dos objetos da activity
        title = findViewById(R.id.txtTitle);
        txtTagContent = findViewById(R.id.txtTagConent);
        tagContentSwitch = findViewById(R.id.tagContentSwitch);
        tagContentSwitch.setOnCheckedChangeListener(this);

        //Instanciar os managers
        nfcManager = new NfcManager(this);
        firebaseManager = new FirebaseManager(this);

        //Verificar se o dispositivo possui suporte a NFC
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

        //Configurar o ForegroundDispatch
        nfcManager.setupForegroundDispatch(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this,
                "onPause()",
                Toast.LENGTH_SHORT).show();

        //Desabilitar o ForegroundDispatch
        nfcManager.disableDispatch();
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this,
                "onNewIntent()",
                Toast.LENGTH_SHORT).show();

        //Intent com os dados do NFC é enviada para o método de tratamento
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Toast.makeText(this,
                "handleIntent()",
                Toast.LENGTH_SHORT).show();

        //São extraídas as informações da tag e do dispositivo para serem armazenadas
        String tagUniqueId = nfcManager.getTagUniqueIdFromIntent(intent);
        //O ID único do dispositivo
        String deviceUniqueId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //O conteúdo que foi escrito na tag
        String tagContent = nfcManager.getTextContentFromTag(intent);
        //É criado uma data no formato de string
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();

        //É chamado o construtor do objeto de leitura e são passadas todas as informações que serão persistidas
        NfcDeviceData nfcDeviceData = new NfcDeviceData(deviceUniqueId, tagUniqueId, tagContent, Build.VERSION.RELEASE,
                Build.MODEL, Build.ID, Build.MANUFACTURER, Build.BRAND, Build.TYPE, Build.USER, Build.VERSION.SDK,
                Build.BOARD, Build.FINGERPRINT, strDate);

        //Os dados de leitura que foram salvos no objeto nfcDeviceData também são printados numa caixa de texto no app
        txtTagContent.setText("Device Unique ID: " + nfcDeviceData.getDeviceUniqueID() +
                "\nTag Unique ID: " + nfcDeviceData.getTagUniqueID() +
                "\n Content: " + nfcDeviceData.getTagContent() +
                "\n Build Version Release: " + nfcDeviceData.getBuildVersionRelease() +
                "\n Build Model: " + nfcDeviceData.getBuildModel() +
                "\n Build ID: " + nfcDeviceData.getBuildID() +
                "\n Build Manufcturer: " + nfcDeviceData.getBuildManufacturer() +
                "\n Build Brand: " + nfcDeviceData.getBuildBrand() +
                "\n Build Type: " + nfcDeviceData.getBuildType() +
                "\n Build User: " + nfcDeviceData.getBuildUser() +
                "\n Version SDK: " + nfcDeviceData.getBuildVersionSDK() +
                "\n Build Board: " + nfcDeviceData.getBuildBoard() +
                "\n Build Fingerprint: " + nfcDeviceData.getBuildFingerprint() +
                "\n Date: " + strDate);

        //É chamado o Firebase manager para efetuar a persistência dos dados da leitura
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
