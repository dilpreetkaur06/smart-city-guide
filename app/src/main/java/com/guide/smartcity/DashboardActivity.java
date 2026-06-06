package com.guide.smartcity;
import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 101;
    private String userId = "guest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageView profileImage =
                findViewById(R.id.imgProfile);

        if (profileImage != null) {
            profileImage.setOnClickListener(v -> {

                String[] options = {
                        "Choose Photo",
                        "Remove Photo"
                };

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Profile Photo")
                        .setItems(options, (dialog, which) -> {

                            if (which == 0) {

                                Intent intent =
                                        new Intent(Intent.ACTION_OPEN_DOCUMENT);

                                intent.addCategory(
                                        Intent.CATEGORY_OPENABLE
                                );

                                intent.setType("image/*");

                                startActivityForResult(
                                        intent,
                                        PICK_IMAGE_REQUEST
                                );

                            } else {

                                profileImage.setImageResource(
                                        R.drawable.user_small
                                );

                                getSharedPreferences(

                                        "SmartCityPrefs",
                                        MODE_PRIVATE
                                )
                                        .edit()
                                        .remove("profile_image_"+userId)
                                        .apply();

                                Toast.makeText(
                                        DashboardActivity.this,
                                        "Profile photo removed",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        })
                        .show();
            });
        }

        TextView tvUserName =
                findViewById(R.id.tvUserName);
        TextView tvUserEmail =
                findViewById(R.id.tvUserEmail);

        FirebaseUser user =
                FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            String email = user.getEmail();
            if (tvUserEmail != null) tvUserEmail.setText(email);

            String name = user.getDisplayName();
            if (name != null && !name.isEmpty()) {
                if (tvUserName != null) tvUserName.setText(name);
            } else {
                if (tvUserName != null) tvUserName.setText("Guest User");
            }
        } else {
            if (tvUserName != null) tvUserName.setText("Guest User");
            if (tvUserEmail != null) tvUserEmail.setText("guest@email.com");
        }

        SharedPreferences prefs =
                getSharedPreferences(
                        "SmartCityPrefs",
                        MODE_PRIVATE
                );

        userId = "guest";

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            userId = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid();
        }
        String imageUri =
                getSharedPreferences("SmartCityPrefs", MODE_PRIVATE)
                        .getString(
                                "profile_image_" + userId,
                                null
                        );
        if (imageUri != null && profileImage != null) {

            try {

                profileImage.setImageURI(
                        Uri.parse(imageUri)
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        Button logoutBtn =
                findViewById(R.id.btnLogout);
        TextView clearRecent =
                findViewById(R.id.tvClearRecent);

        if (clearRecent != null) {
            clearRecent.setOnClickListener(v -> {
                prefs.edit().remove("recent_searches_"+userId).apply();
                Toast.makeText(this, "Recent searches cleared", Toast.LENGTH_SHORT).show();
                recreate();
            });
        }

        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        // 🔥 MENU SETUP

        View header = findViewById(R.id.headerCommon);

        if (header != null) {

            TextView menuIcon = header.findViewById(R.id.tvMenuIcon);

            MenuHelper.setupMenu(this, menuIcon);

            // 🔥 LOAD RECENT SEARCHES

            LinearLayout recentContainer =
                    findViewById(R.id.recentSearchContainer);

            String searches =
                    prefs.getString("recent_searches_"+userId, "");

            if (recentContainer != null && !searches.isEmpty()) {
                String[] searchArray = searches.split(",");
                for (String search : searchArray) {
                    if (!search.trim().isEmpty()) {

                        TextView chip = new TextView(this);

                        chip.setText(search);

                        chip.setTextSize(15);

                        chip.setTextColor(
                                android.graphics.Color.parseColor("#163C3C")
                        );

                        chip.setPadding(40, 20, 40, 20);

                        chip.setBackgroundResource(R.drawable.chip_bg);

                        LinearLayout.LayoutParams params =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        params.setMargins(0, 0, 25, 0);

                        chip.setLayoutParams(params);

                        recentContainer.addView(chip);
                    }
                }
            }
        }
        // 🔥 LOAD SAVED PLACES

        LinearLayout savedContainer =
                findViewById(R.id.savedPlacesContainer);

        if (savedContainer != null) {
            String savedPlaces = prefs.getString("saved_places_"+userId, "");

            if (!savedPlaces.isEmpty()) {
                String[] placeArray = savedPlaces.split(",");
                for (String place : placeArray) {
                    if (!place.trim().isEmpty()) {
                        androidx.cardview.widget.CardView card = new androidx.cardview.widget.CardView(this);
                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        cardParams.setMargins(20, 20, 20, 0);
                        card.setLayoutParams(cardParams);
                        card.setRadius(28);
                        card.setCardElevation(4);
                        card.setCardBackgroundColor(android.graphics.Color.WHITE);

                        android.widget.ImageView image = new android.widget.ImageView(this);
                        image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500));
                        image.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                        image.setImageResource(getPlaceImage(place));

                        LinearLayout inside = new LinearLayout(this);
                        inside.setOrientation(LinearLayout.VERTICAL);
                        inside.setPadding(40, 40, 40, 40);

                        TextView title = new TextView(this);
                        title.setText(place);
                        title.setTextSize(22);
                        title.setTextColor(android.graphics.Color.parseColor("#163C3C"));
                        title.setTypeface(null, android.graphics.Typeface.BOLD);

                        TextView subtitle = new TextView(this);
                        subtitle.setText("Saved Place • Jalandhar");
                        subtitle.setTextSize(14);
                        subtitle.setTextColor(android.graphics.Color.parseColor("#777777"));
                        subtitle.setPadding(0, 10, 0, 0);

                        Button shareBtn = new Button(this);
                        shareBtn.setText("Open");
                        shareBtn.setOnClickListener(v -> openMap(place));
                        shareBtn.setAllCaps(false);
                        shareBtn.setTextColor(android.graphics.Color.parseColor("#163C3C"));
                        shareBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#D9A63A")));

                        Button removeBtn = new Button(this);
                        removeBtn.setText("Unsave");
                        removeBtn.setAllCaps(false);
                        removeBtn.setTextColor(android.graphics.Color.WHITE);
                        removeBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1F3D3B")));

                        removeBtn.setOnClickListener(v -> {
                            String currentSaved = prefs.getString("saved_places_"+userId, "");
                            String[] currentArray = currentSaved.split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String s : currentArray) {
                                if (!s.trim().equalsIgnoreCase(place.trim()) && !s.trim().isEmpty()) {
                                    if (sb.length() > 0) sb.append(",");
                                    sb.append(s.trim());
                                }
                            }
                            prefs.edit().putString("saved_places_"+userId, sb.toString()).apply();
                            Toast.makeText(this, "Removed from saved places", Toast.LENGTH_SHORT).show();
                            recreate();
                        });

                        LinearLayout btnLayout = new LinearLayout(this);
                        btnLayout.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        btnParams.setMargins(0, 25, 20, 0);
                        shareBtn.setLayoutParams(btnParams);
                        removeBtn.setLayoutParams(btnParams);

                        btnLayout.addView(shareBtn);
                        btnLayout.addView(removeBtn);

                        inside.addView(image);
                        inside.addView(title);
                        inside.addView(subtitle);
                        inside.addView(btnLayout);
                        card.addView(inside);
                        savedContainer.addView(card);
                    }
                }
            }
        }
    }
    private int getPlaceImage(String place) {

        switch (place.toLowerCase()) {

            case "wonderland":
                return R.drawable.wonderland2;

            case "devi talab mandir":
                return R.drawable.devitalab;

            case "science city":
                return R.drawable.pushpa_gujral;

            case "lpu":
                return R.drawable.lovely;

            case "haveli jalandhar":
                return R.drawable.haveli;

            case "haveli":
                return R.drawable.haveli;

            case "stadium":
                return R.drawable.stadium2;

            case "radisson hotel":
                return R.drawable.rad;

            case "fortune avenue":
                return R.drawable.fortune2;

            case "sarovar portico":
                return R.drawable.sarover2;

            case "golden tulip":
                return R.drawable.golden2;

            case "park inn by radisson":
                return R.drawable.parkinn;

            case "ramada by wyndham":
                return R.drawable.ramada;

            case "hotel mariton jalandhar":
                return R.drawable.mariton;

            case "bloom hotel":
                return R.drawable.bloom;

            case "talhan sahib":
                return R.drawable.gurudwara;

            case "nikku park":
                return R.drawable.nikkupark;

            case "jang-e-azadi":
                return R.drawable.jang;

            case "pims jalandhar":
            case "pims hospital":
                return R.drawable.pims;

            case "capitol hospital":
                return R.drawable.capitol;

            case "star hospital":
                return R.drawable.star;

            case "unity critical care hospital":
            case "unity critical care":
                return R.drawable.unity;

            case "patel hospital":
                return R.drawable.patel;

            case "tagore hospital & health care centre":
            case "tagore hospital":
                return R.drawable.tagore;

            case "sgl super speciality charitable hospital":
            case "sgl super speciality":
                return R.drawable.sgl;

            case "nhs hospital":
                return R.drawable.nhs;

            case "jammu hospital":
                return R.drawable.jamm;

            case "the orchid jalandhar":
            case "the orchid":
                return R.drawable.orchid;

            case "the great kabab factory jalandhar":
            case "the great kabab factory":
                return R.drawable.kebabfactory;

            case "the dolce jalandhar":
            case "the dolce":
                return R.drawable.dolce;

            case "food bazar jalandhar":
            case "food bazar":
                return R.drawable.foodbazar;

            case "heebee coffee jalandhar":
            case "heebee coffee":
                return R.drawable.heebeec;

            case "the balcony cafe jalandhar":
            case "the balcony cafe":
                return R.drawable.balcony;

            case "chatter box jalandhar":
            case "chatter box":
                return R.drawable.chatter;

            case "eastwood village":
                return R.drawable.eastwood;

            case "jalandhar bus stand":
                return R.drawable.buss;

            case "jalandhar railway station":
                return R.drawable.rail_station;

            case "pap chowk":
                return R.drawable.pap;

            case "rama mandi":
                return R.drawable.ramamandi;

            case "amritsar airport":
                return R.drawable.airport;

            default:
                return R.drawable.city_bg;
        }
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {

            Uri imageUri = data.getData();

            // Persist permissions so it doesn't crash on next app open
            try {

                getContentResolver()
                        .takePersistableUriPermission(
                                imageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

            } catch (SecurityException e) {

                e.printStackTrace();
            }

            ImageView profileImage =
                    findViewById(R.id.imgProfile);
            if (profileImage != null) profileImage.setImageURI(imageUri);

            getSharedPreferences("SmartCityPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("profile_image_"+userId, imageUri.toString())
                    .apply();
        }
    }
    private void openMap(String location) {

        android.net.Uri uri =
                android.net.Uri.parse(
                        "geo:0,0?q=" +
                                android.net.Uri.encode(location)
                );

        android.content.Intent intent =
                new android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        uri
                );

        startActivity(intent);
    }

}