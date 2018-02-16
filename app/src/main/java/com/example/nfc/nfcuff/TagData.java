package com.example.nfc.nfcuff;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by felip on 16/02/2018.
 */

@IgnoreExtraProperties
public class TagData {

    String type = "";
    String size = "";
    String writable = "";
    String content = "";

    public TagData() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWritable() {
        return writable;
    }

    public void setWritable(String writable) {
        this.writable = writable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
