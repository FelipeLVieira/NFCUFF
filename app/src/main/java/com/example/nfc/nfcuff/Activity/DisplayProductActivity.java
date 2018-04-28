package com.example.nfc.nfcuff.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.nfc.nfcuff.R;

public class DisplayProductActivity extends AppCompatActivity {

    private TextView conteudo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
