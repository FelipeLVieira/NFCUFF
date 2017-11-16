package com.example.nfc.nfcuff;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by felip on 16/11/2017.
 */

@IgnoreExtraProperties
public class LeituraNFC {

    public String buildVersionDevice;
    public String buildSerial;
    public String buildModel;
    public String buildID;
    public String buildManufacturer;
    public String buildBrand;
    public String buildType;
    public String buildUser;
    public String buildVersionSDK;
    public String buildBoard;
    public String buildFingerprint;
    public String buildVersionRelease;


    public LeituraNFC() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    /*

    Log.i("TAG", "SERIAL: " + Build.SERIAL);
    Log.i("TAG","MODEL: " + Build.MODEL);
    Log.i("TAG","ID: " + Build.ID);
    Log.i("TAG","Manufacture: " + Build.MANUFACTURER);
    Log.i("TAG","brand: " + Build.BRAND);
    Log.i("TAG","type: " + Build.TYPE);
    Log.i("TAG","user: " + Build.USER);
    Log.i("TAG","BASE: " + Build.VERSION_CODES.BASE);
    Log.i("TAG","INCREMENTAL " + Build.VERSION.INCREMENTAL);
    Log.i("TAG","SDK  " + Build.VERSION.SDK);
    Log.i("TAG","BOARD: " + Build.BOARD);
    Log.i("TAG","HOST " + Build.HOST);
    Log.i("TAG","FINGERPRINT: "+Build.FINGERPRINT);
    Log.i("TAG","Version Code: " + Build.VERSION.RELEASE);
    Build.VERSION.DEVICE;
*/

}

