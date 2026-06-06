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

public class TourismActivity extends AppCompatActivity {
    private String userId = "guest";
    CardView cardDeviTalab, cardScienceCity, cardLPU,
            cardWonderland, cardStadium, cardTalhan, cardNikkuPark, cardJungeazadi;

    ImageButton saveDeviTalab, saveScienceCity, saveLPU,
            saveWonderland, saveStadium, saveTalhan,
            saveNikkuPark, saveJungeazadi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourim);
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
        // Link XML cards
        cardDeviTalab = findViewById(R.id.cardDeviTalab);
        cardScienceCity = findViewById(R.id.cardScienceCity);
        cardLPU = findViewById(R.id.cardLPU);
        cardWonderland = findViewById(R.id.cardWonderland);
        cardStadium = findViewById(R.id.cardStadium);
        cardTalhan = findViewById(R.id.cardTalhan);
        cardNikkuPark = findViewById(R.id.cardNikkuPark);
        cardJungeazadi= findViewById(R.id.cardJungeazadi);

        saveDeviTalab = findViewById(R.id.saveDeviTalab);
        saveScienceCity = findViewById(R.id.saveScienceCity);
        saveLPU = findViewById(R.id.saveLPU);
        saveWonderland = findViewById(R.id.saveWonderland);
        saveStadium = findViewById(R.id.saveStadium);
        saveTalhan = findViewById(R.id.saveTalhan);
        saveNikkuPark = findViewById(R.id.saveNikkuPark);
        saveJungeazadi = findViewById(R.id.saveJungeazadi);

        saveDeviTalab.setTag(0);
        saveScienceCity.setTag(0);
        saveLPU.setTag(0);
        saveWonderland.setTag(0);
        saveStadium.setTag(0);
        saveTalhan.setTag(0);
        saveNikkuPark.setTag(0);
        saveJungeazadi.setTag(0);

        // Click events
        cardDeviTalab.setOnClickListener(v ->
                openMap("Devi Talab Mandir, Jalandhar"));

        cardScienceCity.setOnClickListener(v ->
                openMap("Pushpa Gujral Science City, Jalandhar"));

        cardLPU.setOnClickListener(v ->
                openMap("Lovely Professional University, Phagwara"));

        cardWonderland.setOnClickListener(v ->
                openMap("Wonderland Amusement Park, Jalandhar"));

        cardStadium.setOnClickListener(v ->
                openMap("Guru Gobind Singh Stadium, Jalandhar"));

        cardTalhan.setOnClickListener(v ->
                openMap("Gurudwara Talhan Sahib Ji, Jalandhar"));

        cardNikkuPark.setOnClickListener(v ->
                openMap("Nikku Park, Jalandhar"));

        cardJungeazadi.setOnClickListener(v ->
                openMap("Jung e Azadi, Jalandhar"));



        saveDeviTalab.setOnClickListener(v -> toggleSave(saveDeviTalab, "Devi Talab Mandir"));
        saveScienceCity.setOnClickListener(v -> toggleSave(saveScienceCity, "Science City"));
        saveLPU.setOnClickListener(v -> toggleSave(saveLPU, "LPU"));
        saveWonderland.setOnClickListener(v -> toggleSave(saveWonderland, "Wonderland"));
        saveStadium.setOnClickListener(v -> toggleSave(saveStadium, "Stadium"));
        saveTalhan.setOnClickListener(v -> toggleSave(saveTalhan, "Talhan Sahib"));
        saveNikkuPark.setOnClickListener(v -> toggleSave(saveNikkuPark, "Nikku Park"));
        saveJungeazadi.setOnClickListener(v -> toggleSave(saveJungeazadi, "Jung-e-Azadi"));


    }



    private void openMap(String location) {
        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
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
                prefs.getString("saved_places_"+userId,"");

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
