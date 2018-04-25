package com.example.nfc.nfcuff.Activity;

import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfc.nfcuff.Manager.FirebaseManager;
import com.example.nfc.nfcuff.Manager.NfcManager;
import com.example.nfc.nfcuff.Model.FirebaseDeviceAndTagData;
import com.example.nfc.nfcuff.Model.TagData;
import com.example.nfc.nfcuff.R;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    //Constantes
    public static final String MIME_TEXT_PLAIN = "text/plain";
    Uri EMAIL_ACCOUNTS_DATABASE_CONTENT_URI =
            Uri.parse("content://com.android.email.provider/account");

    //Elementos da Tela
    private TextView title = null;
    private TextView txtDescription = null;
    private TextView txtTagContent = null;
    private Switch tagContentSwitch = null;
    private ImageView ivProduct = null;

    //Managers
    private NfcManager nfcManager = new NfcManager(this);
    private FirebaseManager firebaseManager = new FirebaseManager(this);

    FirebaseDeviceAndTagData firebaseDeviceAndTagData = null;
    String userEmail;


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,
                "onCreate()",
                Toast.LENGTH_SHORT).show();

        //Referências dos objetos da activity
        title = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtTagContent = findViewById(R.id.txtTagConent);
        tagContentSwitch = findViewById(R.id.tagContentSwitch);
        tagContentSwitch.setOnCheckedChangeListener(this);
        ivProduct = findViewById(R.id.ivProduct);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Verificar se o dispositivo possui suporte a NFC
        try {
            nfcManager.verifyNFC();
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NfcManager.NFCNotSupported nfcnsup) {
            txtTagContent.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NfcManager.NFCNotEnabled nfcnEn) {
            txtTagContent.setText("Este dispositivo não está habilitado para leitura de NFC.");
        }

        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false, null, null, null, null);
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            // TODO
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this,
                "onResume()",
                Toast.LENGTH_SHORT).show();

        try {
            //Verificar o adaptador do
            getAdapter();
            //Habilitar o ForegroundDispatch
            nfcManager.setupForegroundDispatch(this);
        } catch (Exception ex){
            txtTagContent.setText("Try getAdapter() or setupForgroundDispatch exception. Message: " + ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this,
                "onPause()",
                Toast.LENGTH_SHORT).show();

        try {
            //Desabilitar o ForegroundDispatch
            nfcManager.disableDispatch();
        } catch (Exception ex) {
            txtTagContent.setText("Try disableDispatch exception. Message: " + ex.getMessage());
        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this,
                "onNewIntent()",
                Toast.LENGTH_SHORT).show();

        try {
            //Intent com os dados do NFC é enviada para o método de tratamento
            handleIntent(intent);
        } catch (Exception ex){
            txtTagContent.setText("Try handleIntent exception. Message: " + ex.getMessage());
        }
    }

    private void handleIntent(Intent intent) {

        Toast.makeText(this,
                "handleIntent()",
                Toast.LENGTH_SHORT).show();


        //O ID único do dispositivo
        String deviceUniqueId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        //O conteúdo que foi escrito na tag
        TagData tagData = nfcManager.getTextContentFromTag(intent);

        //É chamado o construtor do objeto de leitura e são passadas todas as informações que serão persistidas
        firebaseDeviceAndTagData = new FirebaseDeviceAndTagData(deviceUniqueId,
                tagData, Build.VERSION.RELEASE,
                Build.MODEL, Build.ID, Build.MANUFACTURER, Build.BRAND,
                Build.TYPE, Build.USER, Build.VERSION.SDK,
                Build.BOARD, Build.FINGERPRINT, System.currentTimeMillis(), userEmail);

        //Os dados de leitura que foram salvos no objeto firebaseDeviceAndTagData também são printados numa caixa de texto no app
        txtTagContent.setText("Device Unique ID: " + firebaseDeviceAndTagData.getDeviceUniqueID() +
                "\n Tag Unique ID: " + firebaseDeviceAndTagData.getTagData().getUniqueId() +
                "\n Tag Content: " + firebaseDeviceAndTagData.getTagData().getContent() +
                "\n Tag Size: " + firebaseDeviceAndTagData.getTagData().getSize() +
                "\n Tag Writable: " + firebaseDeviceAndTagData.getTagData().getWritable() +
                "\n Tag Type: " + firebaseDeviceAndTagData.getTagData().getType() +
                "\n Build Version Release: " + firebaseDeviceAndTagData.getBuildVersionRelease() +
                "\n Build Model: " + firebaseDeviceAndTagData.getBuildModel() +
                "\n Build ID: " + firebaseDeviceAndTagData.getBuildID() +
                "\n Build Manufcturer: " + firebaseDeviceAndTagData.getBuildManufacturer() +
                "\n Build Brand: " + firebaseDeviceAndTagData.getBuildBrand() +
                "\n Build Type: " + firebaseDeviceAndTagData.getBuildType() +
                "\n Build User: " + firebaseDeviceAndTagData.getBuildUser() +
                "\n Version SDK: " + firebaseDeviceAndTagData.getBuildVersionSDK() +
                "\n Build Board: " + firebaseDeviceAndTagData.getBuildBoard() +
                "\n Build Fingerprint: " + firebaseDeviceAndTagData.getBuildFingerprint() +
                "\n Date: " + System.currentTimeMillis() +
                "\n User E-mail: " + firebaseDeviceAndTagData.getUserEmail());


        //Pegar a URL da imagem referente ao ID do produto bem como a descrição
        firebaseManager.setImageURLfromFirebase(firebaseDeviceAndTagData.getTagData().getContent(), this, ivProduct);
        firebaseManager.writeFromFirebase(firebaseDeviceAndTagData.getTagData().getContent(), this, txtDescription);

        //É chamado o Firebase manager para efetuar a persistência dos dados da leitura
        firebaseManager.storeNfcTagDataOnFirebase(firebaseDeviceAndTagData);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        //Alterar a visibilidade do switch
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

    private void getAdapter() {
        if (nfcManager == null || firebaseManager == null) {
            nfcManager = new NfcManager(this);
            firebaseManager = new FirebaseManager(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            userEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            //email.setText(accountName);
        }
    }
}
