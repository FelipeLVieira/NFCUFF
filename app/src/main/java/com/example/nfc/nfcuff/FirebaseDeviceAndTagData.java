package com.example.nfc.nfcuff;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by felip on 16/11/2017.
 */

@IgnoreExtraProperties
public class FirebaseDeviceAndTagData {

    private String deviceUniqueID = "";
    private String tagUniqueID = "";
    private TagData tagData = new TagData();
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
    private Long currentTimeMillis = 0l;


    FirebaseDeviceAndTagData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FirebaseDeviceAndTagData(String deviceUniqueID, String tagUniqueID, TagData tagData, Long currentTimeMillis) {
        this.deviceUniqueID = deviceUniqueID;
        this.tagUniqueID = tagUniqueID;
        this.tagData = tagData;
        this.currentTimeMillis = currentTimeMillis;
    }

    public FirebaseDeviceAndTagData(String deviceUniqueID, String tagUniqueID, TagData tagData, String buildVersionRelease,
                                    String buildModel, String buildID, String buildManufacturer,
                                    String buildBrand, String buildType, String buildUser, String buildVersionSDK,
                                    String buildBoard, String buildFingerprint, Long currentTimeMillis) {
        this.deviceUniqueID = deviceUniqueID;
        this.tagUniqueID = tagUniqueID;
        this.tagData = tagData;
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
        this.currentTimeMillis = currentTimeMillis;
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

    public TagData getTagData() {
        return tagData;
    }

    public void setTagData(TagData tagData) {
        this.tagData = tagData;
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

    public Long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(Long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
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
