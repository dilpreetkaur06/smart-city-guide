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

public class FoodActivity extends AppCompatActivity {
    private String userId = "guest";
    CardView cardHaveli, cardOrchid, cardKabab,
            cardDolce, cardFoodBazar, cardHeebee,
            cardBalcony, cardChatter, cardEastwood;

    ImageButton saveHaveli, saveOrchid, saveKabab,
            saveDolce, saveFoodBazar, saveHeebee,
            saveBalcony, saveChatter, saveEastwood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
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
        cardHaveli = findViewById(R.id.cardHaveli);
        cardOrchid = findViewById(R.id.cardOrchid);
        cardKabab = findViewById(R.id.cardKabab);
        cardDolce = findViewById(R.id.cardDolce);
        cardFoodBazar = findViewById(R.id.cardFoodBazar);
        cardHeebee = findViewById(R.id.cardHeebee);
        cardBalcony = findViewById(R.id.cardBalcony);
        cardChatter = findViewById(R.id.cardChatter);
        cardEastwood = findViewById(R.id.cardEastwood);

        saveHaveli = findViewById(R.id.saveHaveli);
        saveOrchid = findViewById(R.id.saveOrchid);
        saveKabab = findViewById(R.id.saveKabab);
        saveDolce = findViewById(R.id.saveDolce);
        saveFoodBazar = findViewById(R.id.saveFoodBazar);
        saveHeebee = findViewById(R.id.saveHeebee);
        saveBalcony = findViewById(R.id.saveBalcony);
        saveChatter = findViewById(R.id.saveChatter);
        saveEastwood = findViewById(R.id.saveEastwood);

        saveHaveli.setOnClickListener(v -> toggleSave(saveHaveli, "Haveli"));
        saveOrchid.setOnClickListener(v -> toggleSave(saveOrchid, "The Orchid"));
        saveKabab.setOnClickListener(v -> toggleSave(saveKabab, "Great Kabab Factory"));
        saveDolce.setOnClickListener(v -> toggleSave(saveDolce, "The Dolce"));
        saveFoodBazar.setOnClickListener(v -> toggleSave(saveFoodBazar, "Food Bazar"));
        saveHeebee.setOnClickListener(v -> toggleSave(saveHeebee, "Heebee Coffee"));
        saveBalcony.setOnClickListener(v -> toggleSave(saveBalcony, "The Balcony Cafe"));
        saveChatter.setOnClickListener(v -> toggleSave(saveChatter, "Chatter Box"));
        saveEastwood.setOnClickListener(v -> toggleSave(saveEastwood, "Eastwood Village"));

        saveHaveli.setTag(0);
        saveOrchid.setTag(0);
        saveKabab.setTag(0);
        saveDolce.setTag(0);
        saveFoodBazar.setTag(0);
        saveHeebee.setTag(0);
        saveBalcony.setTag(0);
        saveChatter.setTag(0);
        saveEastwood.setTag(0);

        cardHaveli.setOnClickListener(v ->
                openMap("Haveli Jalandhar"));

        cardOrchid.setOnClickListener(v ->
                openMap("The Orchid Jalandhar"));

        cardKabab.setOnClickListener(v ->
                openMap("The Great Kabab Factory Jalandhar"));

        cardDolce.setOnClickListener(v ->
                openMap("The Dolce Jalandhar"));

        cardFoodBazar.setOnClickListener(v ->
                openMap("Food Bazar Jalandhar"));

        cardHeebee.setOnClickListener(v ->
                openMap("Heebee Coffee Jalandhar"));

        cardBalcony.setOnClickListener(v ->
                openMap("The Balcony Cafe Jalandhar"));

        cardChatter.setOnClickListener(v ->
                openMap("Chatter Box Jalandhar"));

        cardEastwood.setOnClickListener(v ->
                openMap("Eastwood Village Jalandhar"));


        saveHaveli.setOnClickListener(v ->
                toggleSave(saveHaveli, "Haveli"));

        saveOrchid.setOnClickListener(v ->
                toggleSave(saveOrchid, "The Orchid"));

        saveKabab.setOnClickListener(v ->
                toggleSave(saveKabab, "Great Kabab Factory"));

        saveDolce.setOnClickListener(v ->
                toggleSave(saveDolce, "The Dolce"));

        saveFoodBazar.setOnClickListener(v ->
                toggleSave(saveFoodBazar, "Food Bazar"));

        saveHeebee.setOnClickListener(v ->
                toggleSave(saveHeebee, "Heebee Coffee"));

        saveBalcony.setOnClickListener(v ->
                toggleSave(saveBalcony, "The Balcony Cafe"));

        saveChatter.setOnClickListener(v ->
                toggleSave(saveChatter, "Chatter Box"));

        saveEastwood.setOnClickListener(v ->
                toggleSave(saveEastwood, "Eastwood Village"));
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
                    new Intent(FoodActivity.this,
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
                .putString("saved_places_"+userId, saved)
                .apply();
    }

}