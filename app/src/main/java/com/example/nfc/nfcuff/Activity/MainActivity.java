package com.example.nfc.nfcuff.Activity;

import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfc.nfcuff.Manager.FirebaseManager;
import com.example.nfc.nfcuff.Manager.NfcManager;
import com.example.nfc.nfcuff.Model.FirebaseDeviceAndTagData;
import com.example.nfc.nfcuff.Model.TagData;
import com.example.nfc.nfcuff.R;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class MainActivity extends AppCompatActivity {

    /*implements CompoundButton.OnCheckedChangeListener*/

    // Constants
    public static final String MIME_TEXT_PLAIN = "text/plain";
    Uri EMAIL_ACCOUNTS_DATABASE_CONTENT_URI =
            Uri.parse("content://com.android.email.provider/account");

    // Model variables
    FirebaseDeviceAndTagData firebaseDeviceAndTagData = null;
    String userEmail;

    // Screen objects
    private TextView title = null;
    //private TextView txtTagContent = null;
    //private Switch tagContentSwitch = null;
    private TextView txtDescription = null;
    private ImageView ivProduct = null;

    // Managers
    private NfcManager nfcManager = new NfcManager(this);
    private FirebaseManager firebaseManager = new FirebaseManager(this);

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);

        /*Toast.makeText(this,
                "onCreate()",
                Toast.LENGTH_SHORT).show();*/

        //Link objects
        title = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        ivProduct = findViewById(R.id.ivProduct);
        //txtTagContent = findViewById(R.id.txtTagConent);
        //tagContentSwitch = findViewById(R.id.tagContentSwitch);
        //tagContentSwitch.setOnCheckedChangeListener(this);

        // Verify if the device is NFC ready
        try {
            nfcManager.verifyNFC();
            title.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NfcManager.NFCNotSupported nfcnsup) {
            title.setText("Dispositivo está apto a ler uma tag NFC.");
        } catch (NfcManager.NFCNotEnabled nfcnEn) {
            title.setText("Este dispositivo não está habilitado para leitura de NFC.");
        }

        // Display the linked Google accounts to the device and let the user pick one
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Log.e("Catch onCreate() error. Message:", e.getMessage());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*Toast.makeText(this,
                "onResume()",
                Toast.LENGTH_SHORT).show();*/

        try {
            //Verify the NFC adapter
            getAdapter();
            //Habilitar o ForegroundDispatch

        } catch (Exception ex) {
            title.setText("onResume getAdapter() exception. Message: " + ex.getMessage());
        }

        try {
            // Enable foreground dispatch
            nfcManager.setupForegroundDispatch(this);
        } catch (Exception e) {
            Log.e("Catch onResume() error. Message:", e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*Toast.makeText(this,
                "onPause()",
                Toast.LENGTH_SHORT).show();*/

        try {
            // Disable foreground dispatch
            nfcManager.disableDispatch();
        } catch (Exception e) {
            Log.e("Catch onPause() error. Message:", e.getMessage());
        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        /*Toast.makeText(this,
                "onNewIntent()",
                Toast.LENGTH_SHORT).show();*/

        try {
            // Try to handle a new intent from NFC read event
            handleIntent(intent);
        } catch (Exception e) {
            Toast.makeText(this, "TAG inválida.", Toast.LENGTH_SHORT).show();
            Log.e("Catch onNewIntent() error. Message:", e.getMessage());
        }
    }

    private void handleIntent(Intent intent) {

        /*Toast.makeText(this,
                "handleIntent()",
                Toast.LENGTH_SHORT).show();*/

        // Device unique ID
        String deviceUniqueId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Get TAG content
        TagData tagData = nfcManager.getTextContentFromTag(intent);

        // Create a new read event object and pass the TAG data, device infos
        firebaseDeviceAndTagData = new FirebaseDeviceAndTagData(deviceUniqueId,
                tagData, Build.VERSION.RELEASE,
                Build.MODEL, Build.ID, Build.MANUFACTURER, Build.BRAND,
                Build.TYPE, Build.USER, Build.VERSION.SDK,
                Build.BOARD, Build.FINGERPRINT, System.currentTimeMillis(), userEmail);

        //Check if the TAG contains the promotion code
        if (!firebaseDeviceAndTagData.getTagData().getContent().equals("POSTERTAGNFCUFF")) {
            Toast.makeText(this, "TAG inválida.", Toast.LENGTH_SHORT).show();
            return;
        }

        /*Show read event object info on the android screen
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
                "\n User E-mail: " + firebaseDeviceAndTagData.getUserEmail());*/

        //Persist the read event object on Firebase
        boolean stored = firebaseManager.storeNfcTagDataOnFirebase(firebaseDeviceAndTagData);

        // Get success image URL and show on screen if stored new user
        if (stored) {
            firebaseManager.setImageURLfromFirebase(firebaseDeviceAndTagData.getTagData().getContent(), this, ivProduct);
            firebaseManager.writeFromFirebase(firebaseDeviceAndTagData.getTagData().getContent(),
                    firebaseDeviceAndTagData.getUserEmail(),
                    this, txtDescription);
        }

    }


    /*@Override
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
    }*/

    private void getAdapter() {
        if (nfcManager == null || firebaseManager == null) {
            nfcManager = new NfcManager(this);
            firebaseManager = new FirebaseManager(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Save selected Google account e-mail in a variable
        if (requestCode == 1 && resultCode == RESULT_OK) {
            userEmail = encodeUserEmail(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
            //email.setText(accountName);
        }
    }

    String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}
