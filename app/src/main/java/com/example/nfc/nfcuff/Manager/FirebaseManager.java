package com.example.nfc.nfcuff.Manager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfc.nfcuff.Model.FirebaseDeviceAndTagData;
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

    public boolean storeNfcTagDataOnFirebase(FirebaseDeviceAndTagData firebaseDeviceAndTagData) {

        /*Toast.makeText(activity,
                "storeNfcTagDataOnFirebase()",
                Toast.LENGTH_SHORT).show();*/

        //Check if e-mail account already exists


        //Get database reference
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("firebaseDeviceAndTagData")
                .child("Accounts")
                .child(firebaseDeviceAndTagData.getUserEmail())
                .child("Devices")
                .child(firebaseDeviceAndTagData.getDeviceUniqueID())
                .child("Tags")
                .child(firebaseDeviceAndTagData.getTagData().getUniqueId())
                .child(getDate(System.currentTimeMillis()));

        incrementIDCounter("POSTERTAGNFCUFF");

        insertLastProduct(firebaseDeviceAndTagData
                .getTagData()
                .getContent());

        database.setValue(firebaseDeviceAndTagData);

        return true;
    }

    public String getDate(long timeStamp) {
        SimpleDateFormat sdf = null;
        Date netDate = null;
        try {
            sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            netDate = (new Date(timeStamp));

        } catch (Exception e) {
            Log.e("Get date excpt.", e.getMessage());
        }
        return sdf.format(netDate);
    }

    public void setFalse(boolean var) {
        var = false;
    }

    public void incrementIDCounter(final String productID) {
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
        } catch (Exception e) {
            Log.e("Increment excpt.", e.getMessage());
        }
    }

    public void insertLastProduct(String productID) {
        FirebaseDatabase
                .getInstance()
                .getReference("parameters/lastProductRead").setValue(productID);
    }

    public void setImageURLfromFirebase(final String childID, final Activity activityMain, final ImageView imageView) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("products");

        try {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //POSTERTAGNFCUFF node in Firebase has the reference for the image
                    new DownloadImageFromInternet(activityMain, imageView)
                            .execute(dataSnapshot.child("POSTERTAGNFCUFF").child("image").getValue().toString());

                    /*Toast.makeText(activityMain,
                            "Encontrado o ID childNode " + dataSnapshot.child(childID).child("model").getValue(),
                            Toast.LENGTH_SHORT).show();*/

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e("Set image URL excpt.", e.getMessage());
        }
    }

    public void writeFromFirebase(final String childID, final String userEmail, final Activity activityMain, final TextView textView) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("products");

        try {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //POSTERTAGNFCUFF node in Firebase has the reference for the description
                    textView.setText(dataSnapshot.child("POSTERTAGNFCUFF").child("description").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e("Write Firebase excpt.", e.getMessage());
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(Activity activityMain, ImageView imageView) {
            this.imageView = imageView;
            //Toast.makeText(activityMain, "Processando...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            //Load image URL
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Loading bitmap excpt.", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
