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
    private String buildVersionDevice = "";
    private String buildSerial = "";
    private String buildModel = "";
    private String buildID = "";
    private String buildManufacturer = "";
    private String buildBrand = "";
    private String buildType = "";
    private String buildUser = "";
    private String buildVersionSDK = "";
    private String buildBoard = "";
    private String buildFingerprint = "";
    private String buildVersionRelease = "";


    NfcDeviceData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public NfcDeviceData(String deviceID, String tagUniqueID, String tagContent, String buildModel, String buildManufacturer, String buildBrand, String buildUser, String buildVersionSDK) {
        this.deviceUniqueID = deviceID;
        this.tagUniqueID = tagUniqueID;
        this.tagContent = tagContent;
        this.buildModel = buildModel;
        this.buildManufacturer = buildManufacturer;
        this.buildBrand = buildBrand;
        this.buildUser = buildUser;
        this.buildVersionSDK = buildVersionSDK;
    }

    public NfcDeviceData(String tagUniqueID, String tagContent, String tagAction, String buildVersionDevice, String buildSerial, String buildModel, String buildID, String buildManufacturer, String buildBrand, String buildType, String buildUser, String buildVersionSDK, String buildBoard, String buildFingerprint, String buildVersionRelease) {
        this.deviceUniqueID = deviceUniqueID;
        this.tagUniqueID = tagUniqueID;
        this.tagContent = tagContent;
        this.buildVersionDevice = buildVersionDevice;
        this.buildSerial = buildSerial;
        this.buildModel = buildModel;
        this.buildID = buildID;
        this.buildManufacturer = buildManufacturer;
        this.buildBrand = buildBrand;
        this.buildType = buildType;
        this.buildUser = buildUser;
        this.buildVersionSDK = buildVersionSDK;
        this.buildBoard = buildBoard;
        this.buildFingerprint = buildFingerprint;
        this.buildVersionRelease = buildVersionRelease;
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

    void setTagUniqueID(String tagUniqueID) {
        this.tagUniqueID = tagUniqueID;
    }

    public String getTagContent() {
        return tagContent;
    }

    void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public String getBuildVersionDevice() {
        return buildVersionDevice;
    }

    public void setBuildVersionDevice(String buildVersionDevice) {
        this.buildVersionDevice = buildVersionDevice;
    }

    public String getBuildSerial() {
        return buildSerial;
    }

    public void setBuildSerial(String buildSerial) {
        this.buildSerial = buildSerial;
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

    void setBuildID(String buildID) {
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

    public String getBuildVersionRelease() {
        return buildVersionRelease;
    }

    public void setBuildVersionRelease(String buildVersionRelease) {
        this.buildVersionRelease = buildVersionRelease;
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

