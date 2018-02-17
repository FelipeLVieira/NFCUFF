package com.example.nfc.nfcuff;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by felip on 1/30/2018.
 */

public class FirebaseManager {

    Activity activity;

    public FirebaseManager(Activity activity) {
        this.activity = activity;
    }

    public void storeNfcTagDataOnFirebase(FirebaseDeviceAndTagData firebaseDeviceAndTagData) {

        Toast.makeText(activity,
                "storeNfcTagDataOnFirebase()",
                Toast.LENGTH_SHORT).show();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("firebaseDeviceAndTagData")
                .child("Devices")
                .child(firebaseDeviceAndTagData.getDeviceUniqueID())
                .child("Tags")
                .child(firebaseDeviceAndTagData.getTagData().getUniqueId())
                .child(getDate(System.currentTimeMillis()));

        database.setValue(firebaseDeviceAndTagData);

    }

    public String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }
}
