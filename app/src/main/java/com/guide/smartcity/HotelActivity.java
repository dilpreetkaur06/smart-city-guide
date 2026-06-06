package com.guide.smartcity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class HotelActivity extends AppCompatActivity {
    private String userId = "guest";
    CardView cardRadisson, cardFortune, cardSarovar,
            cardGoldenTulip, cardParkInn, cardRamada,
            cardMariton, cardBloom;
    ImageButton saveRadisson, saveFortune, saveSarovar,
            saveGoldenTulip, saveParkInn, saveRamada,
            saveMariton, saveBloom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
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
        cardRadisson = findViewById(R.id.cardRadisson);
        cardFortune = findViewById(R.id.cardFortune);
        cardSarovar = findViewById(R.id.cardSarovar);
        cardGoldenTulip = findViewById(R.id.cardGoldenTulip);
        cardParkInn = findViewById(R.id.cardParkInn);
        cardRamada = findViewById(R.id.cardRamada);
        cardMariton = findViewById(R.id.cardMariton);
        cardBloom = findViewById(R.id.cardBloom);

        saveRadisson = findViewById(R.id.saveRadisson);
        saveFortune = findViewById(R.id.saveFortune);
        saveSarovar = findViewById(R.id.saveSarovar);
        saveGoldenTulip = findViewById(R.id.saveGoldenTulip);
        saveParkInn = findViewById(R.id.saveParkInn);
        saveRamada = findViewById(R.id.saveRamada);
        saveMariton = findViewById(R.id.saveMariton);
        saveBloom = findViewById(R.id.saveBloom);

        saveRadisson.setTag(0);
        saveFortune.setTag(0);
        saveSarovar.setTag(0);
        saveGoldenTulip.setTag(0);
        saveParkInn.setTag(0);
        saveRamada.setTag(0);
        saveMariton.setTag(0);
        saveBloom.setTag(0);

        cardRadisson.setOnClickListener(v ->
                openMap("Radisson Hotel Jalandhar"));

        cardFortune.setOnClickListener(v ->
                openMap("Fortune Avenue Jalandhar"));

        cardSarovar.setOnClickListener(v ->
                openMap("Sarovar Portico Jalandhar"));

        cardGoldenTulip.setOnClickListener(v ->
                openMap("Golden Tulip Jalandhar"));

        cardParkInn.setOnClickListener(v ->
                openMap("Park Inn by Radisson Jalandhar"));

        cardRamada.setOnClickListener(v ->
                openMap("Ramada by Wyndham Jalandhar"));

        cardMariton.setOnClickListener(v ->
                openMap("Hotel Mariton Jalandhar"));

        cardBloom.setOnClickListener(v ->
                openMap("Bloom Hotel Jalandhar"));

        saveRadisson.setOnClickListener(v -> toggleSave(saveRadisson, "Radisson Hotel"));
        saveFortune.setOnClickListener(v -> toggleSave(saveFortune, "Fortune Avenue"));
        saveSarovar.setOnClickListener(v -> toggleSave(saveSarovar, "Sarovar Portico"));
        saveGoldenTulip.setOnClickListener(v -> toggleSave(saveGoldenTulip, "Golden Tulip"));
        saveParkInn.setOnClickListener(v -> toggleSave(saveParkInn, "Park Inn by Radisson"));
        saveRamada.setOnClickListener(v -> toggleSave(saveRamada, "Ramada by Wyndham"));
        saveMariton.setOnClickListener(v -> toggleSave(saveMariton, "Hotel Mariton"));
        saveBloom.setOnClickListener(v -> toggleSave(saveBloom, "Bloom Hotel"));
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
                .putString("saved_places_"+userId, saved)
                .apply();
    }
}

