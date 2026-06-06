package com.guide.smartcity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TransportActivity extends AppCompatActivity {
    private String userId = "guest";
    CardView cardBus, cardJunction, cardPAP,
            cardRamaMandi, cardAirport;

    ImageButton saveBus, saveJunction, savePAP,
            saveRamaMandi, saveAirport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        View header = findViewById(R.id.headerCommon);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            userId = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid();
        }
        if (header != null) {

            TextView menuIcon = header.findViewById(R.id.tvMenuIcon);

            MenuHelper.setupMenu(this, menuIcon);
        }
        cardBus = findViewById(R.id.cardBus);
        cardJunction = findViewById(R.id.cardJunction);
        cardPAP = findViewById(R.id.cardPAP);
        cardRamaMandi = findViewById(R.id.cardRamaMandi);
        cardAirport = findViewById(R.id.cardAirport);

        saveBus = findViewById(R.id.saveBus);
        saveJunction = findViewById(R.id.saveJunction);
        savePAP = findViewById(R.id.savePAP);
        saveRamaMandi = findViewById(R.id.saveRamaMandi);
        saveAirport = findViewById(R.id.saveAirport);

        saveBus.setTag(0);
        saveJunction.setTag(0);
        savePAP.setTag(0);
        saveRamaMandi.setTag(0);
        saveAirport.setTag(0);



        cardBus.setOnClickListener(v ->
                openMap("Jalandhar Bus Stand"));

        cardJunction.setOnClickListener(v ->
                openMap("Jalandhar City Junction Railway Station"));

        cardPAP.setOnClickListener(v ->
                openMap("PAP Chowk Jalandhar"));

        cardRamaMandi.setOnClickListener(v ->
                openMap("Rama Mandi Jalandhar"));

        cardAirport.setOnClickListener(v ->
                openMap("Sri Guru Ram Das Ji International Airport Amritsar"));

        saveBus.setOnClickListener(v ->
                toggleSave(saveBus, "Jalandhar Bus Stand"));

        saveJunction.setOnClickListener(v ->
                toggleSave(saveJunction, "Jalandhar Railway Station"));

        savePAP.setOnClickListener(v ->
                toggleSave(savePAP, "PAP Chowk"));

        saveRamaMandi.setOnClickListener(v ->
                toggleSave(saveRamaMandi, "Rama Mandi"));

        saveAirport.setOnClickListener(v ->
                toggleSave(saveAirport, "Amritsar Airport"));


    }

    private void openMap(String location) {
        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    private void toggleSave(ImageButton button, String placeName) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            Toast.makeText(this,
                    "Please login first",
                    Toast.LENGTH_SHORT).show();

            Intent intent =
                    new Intent(TransportActivity.this,
                            LoginActivity.class);

            startActivity(intent);

            return;
        }

        android.content.SharedPreferences prefs =
                getSharedPreferences("SmartCityPrefs", MODE_PRIVATE);

        String saved =
                prefs.getString("saved_places_"+userId, "");

        if ((int) button.getTag() == 1) {

            button.setImageResource(R.drawable.bookmark_outline);

            button.setTag(0);

            saved = saved.replace(placeName + ",", "");

            Toast.makeText(this,
                    "Removed from saved",
                    Toast.LENGTH_SHORT).show();

        } else {

            button.setImageResource(R.drawable.bookmark_filled);

            button.setTag(1);

            if (!saved.contains(placeName + ",")) {

                saved += placeName + ",";
            }

            Toast.makeText(this,
                    "Place saved",
                    Toast.LENGTH_SHORT).show();
        }

        prefs.edit()
                .putString("saved_places", saved)
                .apply();
    }
}