package com.guide.smartcity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        View header = findViewById(R.id.headerCommon);

        if (header != null) {

            TextView menuIcon = header.findViewById(R.id.tvMenuIcon);

            MenuHelper.setupMenu(this, menuIcon);
        }
        // Call Police (100)
        findViewById(R.id.btnCallPolice).setOnClickListener(v -> makeCall("100"));

        // Call Ambulance (108)
        findViewById(R.id.btnCallAmbulance).setOnClickListener(v -> makeCall("108"));

        // Fire Brigade (112)
        findViewById(R.id.btnCallFireBrigade).setOnClickListener(v -> makeCall("112"));
    }

    private void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }
}