package com.example.nfc.nfcuff;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by felip on 16/11/2017.
 */

@IgnoreExtraProperties
public class NfcDeviceData {

    private String deviceUniqueID = "";
    private String tagUniqueID = "";
    private String tagContent = "";
    private String buildVersionRelease = "";
    private String buildModel = "";
    private String buildID = "";
    private String buildManufacturer = "";
    private String buildBrand = "";
    private String buildType = "";
    private String buildUser = "";
    private String buildVersionSDK = "";
    private String buildBoard = "";
    private String buildFingerprint = "";
    private String date = "";


    NfcDeviceData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public NfcDeviceData(String deviceUniqueID, String tagUniqueID, String tagContent, String buildVersionRelease,
                         String buildModel, String buildID, String buildManufacturer,
                         String buildBrand, String buildType, String buildUser, String buildVersionSDK,
                         String buildBoard, String buildFingerprint, String date) {
        this.deviceUniqueID = deviceUniqueID;
        this.tagUniqueID = tagUniqueID;
        this.tagContent = tagContent;
        this.buildVersionRelease = buildVersionRelease;
        this.buildModel = buildModel;
        this.buildID = buildID;
        this.buildManufacturer = buildManufacturer;
        this.buildBrand = buildBrand;
        this.buildType = buildType;
        this.buildUser = buildUser;
        this.buildVersionSDK = buildVersionSDK;
        this.buildBoard = buildBoard;
        this.buildFingerprint = buildFingerprint;
        this.date = date;
    }

    public String getDeviceUniqueID() {
        return deviceUniqueID;
    }

    public void setDeviceUniqueID(String deviceUniqueID) {
        this.deviceUniqueID = deviceUniqueID;
    }

    public String getTagUniqueID() {
        return tagUniqueID;
    }

    public void setTagUniqueID(String tagUniqueID) {
        this.tagUniqueID = tagUniqueID;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public String getBuildVersionRelease() {
        return buildVersionRelease;
    }

    public void setBuildVersionRelease(String buildVersionRelease) {
        this.buildVersionRelease = buildVersionRelease;
    }

    public String getBuildModel() {
        return buildModel;
    }

    public void setBuildModel(String buildModel) {
        this.buildModel = buildModel;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getBuildManufacturer() {
        return buildManufacturer;
    }

    public void setBuildManufacturer(String buildManufacturer) {
        this.buildManufacturer = buildManufacturer;
    }

    public String getBuildBrand() {
        return buildBrand;
    }

    public void setBuildBrand(String buildBrand) {
        this.buildBrand = buildBrand;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getBuildUser() {
        return buildUser;
    }

    public void setBuildUser(String buildUser) {
        this.buildUser = buildUser;
    }

    public String getBuildVersionSDK() {
        return buildVersionSDK;
    }

    public void setBuildVersionSDK(String buildVersionSDK) {
        this.buildVersionSDK = buildVersionSDK;
    }

    public String getBuildBoard() {
        return buildBoard;
    }

    public void setBuildBoard(String buildBoard) {
        this.buildBoard = buildBoard;
    }

    public String getBuildFingerprint() {
        return buildFingerprint;
    }

    public void setBuildFingerprint(String buildFingerprint) {
        this.buildFingerprint = buildFingerprint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

