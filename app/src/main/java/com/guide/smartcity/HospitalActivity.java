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


public class HospitalActivity extends AppCompatActivity {
    private String userId = "guest";
    CardView cardPIMS, cardCapitol, cardStar,
            cardUnity, cardPatel, cardTagore,
            cardSGL, cardNHS, cardJammu;

    ImageButton savePIMS, saveCapitol, saveStar,
            saveUnity, savePatel, saveTagore,
            saveSGL, saveNHS, saveJammu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
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
        cardPIMS = findViewById(R.id.cardPIMS);
        cardCapitol = findViewById(R.id.cardCapitol);
        cardStar = findViewById(R.id.cardStar);
        cardUnity = findViewById(R.id.cardUnity);
        cardPatel = findViewById(R.id.cardPatel);
        cardTagore = findViewById(R.id.cardTagore);
        cardSGL = findViewById(R.id.cardSGL);
        cardNHS = findViewById(R.id.cardNHS);
        cardJammu = findViewById(R.id.cardJammu);

        savePIMS = findViewById(R.id.savePIMS);
        saveCapitol = findViewById(R.id.saveCapitol);
        saveStar = findViewById(R.id.saveStar);
        saveUnity = findViewById(R.id.saveUnity);
        savePatel = findViewById(R.id.savePatel);
        saveTagore = findViewById(R.id.saveTagore);
        saveSGL = findViewById(R.id.saveSGL);
        saveNHS = findViewById(R.id.saveNHS);
        saveJammu = findViewById(R.id.saveJammu);

        savePIMS.setTag(0);
        saveCapitol.setTag(0);
        saveStar.setTag(0);
        saveUnity.setTag(0);
        savePatel.setTag(0);
        saveTagore.setTag(0);
        saveSGL.setTag(0);
        saveNHS.setTag(0);
        saveJammu.setTag(0);

        cardPIMS.setOnClickListener(v ->
                openMap("PIMS Jalandhar"));

        cardCapitol.setOnClickListener(v ->
                openMap("Capitol Hospital Jalandhar"));

        cardStar.setOnClickListener(v ->
                openMap("Star Hospital Jalandhar"));

        cardUnity.setOnClickListener(v ->
                openMap("Unity Critical Care Hospital Jalandhar"));

        cardPatel.setOnClickListener(v ->
                openMap("Patel Hospital Jalandhar"));

        cardTagore.setOnClickListener(v ->
                openMap("Tagore Hospital Jalandhar"));

        cardSGL.setOnClickListener(v ->
                openMap("SGL Super Speciality Hospital Jalandhar"));

        cardNHS.setOnClickListener(v ->
                openMap("NHS Hospital Jalandhar"));

        cardJammu.setOnClickListener(v ->
                openMap("Jammu Hospital Jalandhar"));


        savePIMS.setOnClickListener(v -> toggleSave(savePIMS, "PIMS Hospital"));
        saveCapitol.setOnClickListener(v -> toggleSave(saveCapitol, "Capitol Hospital"));
        saveStar.setOnClickListener(v -> toggleSave(saveStar, "Star Hospital"));
        saveUnity.setOnClickListener(v -> toggleSave(saveUnity, "Unity Critical Care"));
        savePatel.setOnClickListener(v -> toggleSave(savePatel, "Patel Hospital"));
        saveTagore.setOnClickListener(v -> toggleSave(saveTagore, "Tagore Hospital"));
        saveSGL.setOnClickListener(v -> toggleSave(saveSGL, "SGL Super Speciality"));
        saveNHS.setOnClickListener(v -> toggleSave(saveNHS, "NHS Hospital"));
        saveJammu.setOnClickListener(v -> toggleSave(saveJammu, "Jammu Hospital"));
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