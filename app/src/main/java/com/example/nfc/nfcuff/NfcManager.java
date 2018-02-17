package com.example.nfc.nfcuff;

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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Locale;

public class NfcManager {

    private Activity activity;
    private NfcAdapter nfcAdapter;

    public NfcManager(Activity activity) {
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

    public void writeTag(Tag tag, NdefMessage message) {
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
                } else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NdefMessage createExternalMessage(String content) {
        NdefRecord externalRecord = NdefRecord.createExternal("com.survivingwithandroid", "data", content.getBytes());

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{externalRecord});

        return ndefMessage;
    }

    public Tag getTagFromIntent(Intent intent) {
        return (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
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

    public TagData getTextContentFromTag(Intent intent) {
        Ndef ndef = Ndef.get(getTagFromIntent(intent));

        TagData returnTagData = new TagData();

        try {
            ndef.connect();

            returnTagData.setUniqueId(getTagUniqueIdFromIntent(intent));
            returnTagData.setType(ndef.getType().toString());
            returnTagData.setSize(String.valueOf(ndef.getMaxSize()) + " bytes ");
            returnTagData.setWritable((ndef.isWritable() ? " Verdadeiro " : " Falso "));

            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();

                String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

                int languageSize = payload[0] & 0063;

                String text = new String(payload, languageSize + 1, payload.length -
                        languageSize - 1, textEncoding);

                //String text = new String(payload);
                returnTagData.setContent(text);

                ndef.close();

            }
        } catch (Exception e) {
            Toast.makeText(null, "Não foi possível ler a tag.", Toast.LENGTH_LONG).show();
        }
        return returnTagData;
    }

    public String getTagUniqueIdFromIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagUniqueId = bytesToHexString(tag.getId());
        return tagUniqueId;
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
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
}
