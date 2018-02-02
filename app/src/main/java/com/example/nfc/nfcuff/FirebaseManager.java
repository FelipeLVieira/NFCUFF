package com.example.nfc.nfcuff;

import android.app.Activity;
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

    public void storeNfcTagDataOnFirebase(NfcDeviceData nfcDeviceData) {

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Toast.makeText(activity,
                "storeNfcTagDataOnFirebase()",
                Toast.LENGTH_SHORT).show();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("nfcDeviceData");

        database.child(nfcDeviceData.getDeviceUniqueID()).setValue(nfcDeviceData);

    }
}
