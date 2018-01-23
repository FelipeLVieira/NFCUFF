package com.example.nfc.nfcuff;
/*
 * Copyright (C) 2016, francesco Azzola 
 *
 *(http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 03/01/16
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

public class NFCManager {

    private Activity activity;
    private NfcAdapter nfcAdapter;

    public NFCManager(Activity activity) {
        this.activity = activity;
    }

    public void verifyNFC() throws NFCNotSupported, NFCNotEnabled {

        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);

        if (nfcAdapter == null)
            throw new NFCNotSupported();

        if (!nfcAdapter.isEnabled())
            throw new NFCNotEnabled();

    }

    public void setupForegroundDispatch(final Activity activity) {
        Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, null, null);
    }

    public void disableDispatch() {
        nfcAdapter.disableForegroundDispatch(activity);
    }

    public static class NFCNotSupported extends Exception {

        public NFCNotSupported() {
            super();
        }
    }

    public static class NFCNotEnabled extends Exception {

        public NFCNotEnabled() {
            super();
        }
    }


    public void writeTag(Tag tag, NdefMessage message)  {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);

                if (ndefTag == null) {
                    // Let's try to format the Tag in NDEF
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                }
                else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public NdefMessage createUriMessage(String content, String type) {
        NdefRecord record = NdefRecord.createUri(type + content);
        NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
        return msg;

    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NdefMessage createExternalMessage(String content) {
        NdefRecord externalRecord = NdefRecord.createExternal("com.survivingwithandroid", "data", content.getBytes());

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] { externalRecord });

        return ndefMessage;
    }

    public String readTextFromNdefMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            return tagContent;

        }else{
            return "No NDEF records found!";
        }
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {

            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length -
                    languageSize - 1, textEncoding);

        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    public boolean formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormat = NdefFormatable.get(tag);
            if (ndefFormat != null) {
                ndefFormat.connect();
                ndefFormat.format(ndefMessage);
                ndefFormat.close();
                return true;
            }
        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
        return false;
    }

    public Tag getTagFromIntent(Intent intent) {
        return (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    public NdefMessage getExtraNdefMessageFromIntent(Intent intent) {
        NdefMessage ndefMessage = null;
        Parcelable[] extra = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (extra != null && extra.length > 0) {
            ndefMessage = (NdefMessage) extra[0];
        }
        return ndefMessage;
    }

    public NdefRecord getFirstNdefRecord(NdefMessage ndefMessage) {
        NdefRecord ndefRecord = null;
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            ndefRecord = ndefRecords[0];
        }
        return ndefRecord;
    }

    public boolean isNdefRecordOfTnfAndRdt(NdefRecord ndefRecord, short tnf, byte[] rdt) {
        return ndefRecord.getTnf() == tnf && Arrays.equals(ndefRecord.getType(), rdt);
    }

    public String readTagFromCache(Tag tag){
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            return "NDEF is not supported by this Tag!";
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        if (ndefMessage == null) {
            return "The tag is empty !";
        }

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return getTextFromNdefRecord(ndefRecord);
                } catch (Exception e) {
                }
            }
        }
        return "";
    }

    public String readFromTag(Intent intent){
        Ndef ndef = Ndef.get(getTagFromIntent(intent));

        String txtTagContent = "";

        try{
            ndef.connect();

            txtTagContent += "Type: " + ndef.getType().toString() + "\n";
            txtTagContent += "Size: " + String.valueOf(ndef.getMaxSize()) + "\n";
            txtTagContent += "Writable: " + (ndef.isWritable() ? "True" : "False") + "\n";

            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String text = new String(payload);
                txtTagContent += "Content: " + text + "\n";


                ndef.close();

            }
        }
        catch (Exception e) {
            Toast.makeText(null, "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
        return txtTagContent;
    }
}
