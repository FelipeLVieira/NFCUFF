package com.example.nfc.nfcuff;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
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

        insertLastProduct(firebaseDeviceAndTagData
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

    public void insertLastProduct(String productID){
        FirebaseDatabase
                .getInstance()
                .getReference("parameters/lastProductRead").setValue(productID);
    }

    public void setImageURLfromFirebase(String childID, Activity activityMain, ImageView imageView){

        DatabaseReference jSettingsDatabase = FirebaseDatabase.getInstance().getReference("products");

        try {
            jSettingsDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    new DownloadImageFromInternet(activityMain, imageView)
                            .execute(dataSnapshot.child(childID).child("image").getValue().toString());

                    Toast.makeText(activityMain,
                            "Encontrado o ID childNode " + dataSnapshot.child(childID).child("model").getValue(),
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
        }
    }

    public void writeFromFirebase(String childID, Activity activityMain, TextView textView){

        DatabaseReference jSettingsDatabase = FirebaseDatabase.getInstance().getReference("products");

        try {
            jSettingsDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    textView.setText(dataSnapshot.child(childID).child("description").getValue().toString());

                    Toast.makeText(activityMain,
                            "Encontrado o ID childNode " + dataSnapshot.child(childID).child("model").getValue(),
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(Activity activityMain, ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(activityMain, "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
