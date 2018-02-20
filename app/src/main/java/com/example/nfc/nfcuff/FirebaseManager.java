package com.example.nfc.nfcuff;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

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

        incrementCounter(firebaseDeviceAndTagData
                .getTagData()
                .getContent());

        database.setValue(firebaseDeviceAndTagData);
    }

    public String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void incrementCounter(String productID) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("products")
                    .child(productID)
                    .child("counter");

            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long value = mutableData.getValue(Long.class);
                    if (value == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue(value + 1);
                    }

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                }
                
            });
        }
        catch(Exception ex){
            throw ex;
        }
    }
}
