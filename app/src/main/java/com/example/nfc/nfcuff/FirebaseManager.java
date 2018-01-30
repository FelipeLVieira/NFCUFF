package com.example.nfc.nfcuff;

import android.app.Activity;
import android.nfc.Tag;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by felip on 1/30/2018.
 */

public class FirebaseManager {

    Activity activity;

    public FirebaseManager(Activity activity) {
        this.activity = activity;
    }

    public void salvarInformacoesNoFirebase(String tagContent, Tag tagNFC, String tagAction) {

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Toast.makeText(activity,
                "salvarInformacoesNoFirebase()",
                Toast.LENGTH_SHORT).show();

        String tagNFCString = bytesToHexString(tagNFC.getId());
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
}
