package com.guide.smartcity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.view.View;
import android.net.Uri; // Added this import 
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.List;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
    private List<Place> jalandharPlaces;
    private String userId = "guest";
    private EditText searchBox;
    private Button searchBtn;
    private androidx.cardview.widget.CardView cardNews1;
    private androidx.cardview.widget.CardView cardNews2;

    private TextView tvNews1;
    private TextView tvNews2;

    private String newsUrl1 = "";
    private String newsUrl2 = "";
    private TextView tvWeatherCity;
    private TextView tvTemperature;
    private TextView tvWeatherCondition;
    private TextView tvHumidity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            userId = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid();
        }
        searchBox = findViewById(R.id.searchBox);
        searchBtn = findViewById(R.id.searchBtn);
        cardNews1 = findViewById(R.id.cardNews1);
        cardNews2 = findViewById(R.id.cardNews2);

        tvNews1 = ((TextView)((android.widget.LinearLayout)
                cardNews1.getChildAt(0)).getChildAt(0));

        tvNews2 = ((TextView)((android.widget.LinearLayout)
                cardNews2.getChildAt(0)).getChildAt(0));
        tvWeatherCity = findViewById(R.id.tvWeatherCity);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeatherCondition = findViewById(R.id.tvWeatherCondition);
        tvHumidity = findViewById(R.id.tvHumidity);
        setupCityData(); 

        searchBtn.setOnClickListener(v -> performSearch()); 

        setupNavigationMenu(); 
        setupCategoryCards(); 

        // --- ADDED THIS LINE ---
//        setupNewsSection(); // This activates your news click listeners
        fetchWeather();
        fetchNews();

        cardNews1.setOnClickListener(v -> {

            if (!newsUrl1.isEmpty()) {

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(newsUrl1)
                );

                startActivity(intent);
            }
        });

        cardNews2.setOnClickListener(v -> {

            if (!newsUrl2.isEmpty()) {

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(newsUrl2)
                );

                startActivity(intent);
            }
        });
        Button getStartedBtn = findViewById(R.id.btnGetStarted);  
        if (getStartedBtn != null) {
            getStartedBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setupCityData() {

        jalandharPlaces = new ArrayList<>();

        // 🔥 TOURISM
        jalandharPlaces.add(new Place("Wonderland Amusement Park", "Tourism", "4.5", ""));
        jalandharPlaces.add(new Place("Devi Talab Mandir", "Tourism", "4.8", ""));
        jalandharPlaces.add(new Place("Pushpa Gujral Science City", "Tourism", "4.6", ""));
        jalandharPlaces.add(new Place("Lovely Professional University", "Tourism", "4.7", ""));
        jalandharPlaces.add(new Place("Guru Gobind Singh Stadium", "Tourism", "4.4", ""));
        jalandharPlaces.add(new Place("Nikku Park", "Tourism", "4.2", ""));
        jalandharPlaces.add(new Place("Jung-e-Azadi", "Tourism", "4.5", ""));
        jalandharPlaces.add(new Place("Talhan Sahib", "Tourism", "4.8", ""));

        // 🔥 HOTELS
        jalandharPlaces.add(new Place("Radisson Hotel", "Hotels", "4.6", ""));
        jalandharPlaces.add(new Place("Ramada Hotel", "Hotels", "4.5", ""));
        jalandharPlaces.add(new Place("Best Western", "Hotels", "4.4", ""));
        jalandharPlaces.add(new Place("Fortune Avenue", "Hotels", "4.3", ""));
        jalandharPlaces.add(new Place("Sarovar Portico", "Hotels", "4.5", ""));
        jalandharPlaces.add(new Place("Golden Tulip", "Hotels", "4.2", ""));
        jalandharPlaces.add(new Place("Park Inn by Radisson", "Hotels", "4.4", ""));
        jalandharPlaces.add(new Place("Hotel Mariton", "Hotels", "4.1", ""));
        jalandharPlaces.add(new Place("Bloom Hotel", "Hotels", "4.3", ""));

        // 🔥 FOOD
        jalandharPlaces.add(new Place("Haveli", "Food", "4.7", ""));
        jalandharPlaces.add(new Place("The Orchid", "Food", "4.6", ""));
        jalandharPlaces.add(new Place("The Great Kabab Factory", "Food", "4.5", ""));
        jalandharPlaces.add(new Place("The Dolce", "Food", "4.4", ""));
        jalandharPlaces.add(new Place("Food Bazar", "Food", "4.3", ""));
        jalandharPlaces.add(new Place("Heebee Coffee", "Food", "4.6", ""));
        jalandharPlaces.add(new Place("The Balcony Cafe", "Food", "4.5", ""));
        jalandharPlaces.add(new Place("Chatter Box", "Food", "4.4", ""));
        jalandharPlaces.add(new Place("Eastwood Village", "Food", "4.5", ""));

        // 🔥 HOSPITALS
        jalandharPlaces.add(new Place("PIMS Jalandhar", "Hospitals", "4.2", ""));
        jalandharPlaces.add(new Place("Civil Hospital", "Hospitals", "4.0", ""));
        jalandharPlaces.add(new Place("Patel Hospital", "Hospitals", "4.1", ""));
        jalandharPlaces.add(new Place("Capitol Hospital", "Hospitals", "4.3", ""));
        jalandharPlaces.add(new Place("Star Hospital", "Hospitals", "4.2", ""));
        jalandharPlaces.add(new Place("Unity Critical Hospital", "Hospitals", "4.4", ""));
        jalandharPlaces.add(new Place("Tagore Hospital", "Hospitals", "4.5", ""));
        jalandharPlaces.add(new Place("SGL super speciality NHS hospital", "Hospitals", "4.1", ""));
        jalandharPlaces.add(new Place("jammu Hospital", "Hospitals", "4.0", ""));

        // 🔥 TRANSPORT
        jalandharPlaces.add(new Place("Jalandhar Bus Stand", "Transport", "4.0", ""));
        jalandharPlaces.add(new Place("Jalandhar Railway Station", "Transport", "4.3", ""));
        jalandharPlaces.add(new Place("PAP Chowk", "Transport", "4.1", ""));
        jalandharPlaces.add(new Place("Rama Mandi", "Transport", "4.0", ""));
        jalandharPlaces.add(new Place("PAP Chownk", "Transport", "4.2", ""));
        jalandharPlaces.add(new Place("Jalandhar Rama Mandi", "Transport", "4.1", ""));
        jalandharPlaces.add(new Place("Jalandhar Cantt Station", "Transport", "4.4", ""));
    }

    private void performSearch() {

        String query = searchBox.getText().toString().trim().toLowerCase();
        saveRecentSearch(query);
        // ❌ EMPTY SEARCH
        if (query.isEmpty()) {

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Search Required")
                    .setMessage("Please enter a place or category.")
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }

        boolean found = false;

        // 🔥 SEARCH THROUGH ALL PLACES
        for (Place place : jalandharPlaces) {

            String title = place.getTitle().toLowerCase();
            String category = place.getCategory().toLowerCase();

            // MATCH PLACE TITLE
            if (title.contains(query) || category.contains(query)) {

                found = true;

                switch (place.getCategory()) {

                    case "Tourism":
                        startActivity(new Intent(this, TourismActivity.class));
                        break;

                    case "Hotels":
                        startActivity(new Intent(this, HotelActivity.class));
                        break;

                    case "Food":
                        startActivity(new Intent(this, FoodActivity.class));
                        break;

                    case "Hospitals":
                        startActivity(new Intent(this, HospitalActivity.class));
                        break;

                    case "Transport":
                        startActivity(new Intent(this, TransportActivity.class));
                        break;
                }

                break;
            }
        }

        // ❌ NOT FOUND
        if (!found) {

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Place Not Found")
                    .setMessage("Sorry, we couldn't find that place in Jalandhar.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
    private void saveRecentSearch(String query) {

        android.content.SharedPreferences prefs =
                getSharedPreferences("SmartCityPrefs", MODE_PRIVATE);

        String oldSearches = prefs.getString("recent_searches_"+userId, "");

        // Avoid duplicates
        if (!oldSearches.contains(query)) {

            String newSearches = query + "," + oldSearches;

            prefs.edit()
                    .putString("recent_searches_"+userId, newSearches)
                    .apply();
        }
    }

    private void fetchWeather() {

        WeatherApi api = RetrofitWeatherClient
                .getClient()
                .create(WeatherApi.class);

        Call<WeatherResponse> call = api.getWeather(
                "Jalandhar",
                "083e005ff81bb8ef3f8816983b1536bc",
                "metric"
        );

        call.enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(Call<WeatherResponse> call,
                                   Response<WeatherResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    WeatherResponse data = response.body();

                    tvWeatherCity.setText("📍 " + data.name);

                    tvTemperature.setText(
                            ((int) data.main.temp) + "°C"
                    );

                    tvWeatherCondition.setText(
                            data.weather.get(0).main
                    );

                    tvHumidity.setText(
                            "Humidity: " + data.main.humidity + "%"
                    );
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call,
                                  Throwable t) {

                Toast.makeText(MainActivity.this,
                        "Weather failed to load",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchNews() {

        NewsApi api = RetrofitNewsClient
                .getClient()
                .create(NewsApi.class);

        Call<NewsResponse> call = api.getNews(
                "Jalandhar",
                "publishedAt",
                "7dfb952d37e544da885c2b54a894b19d"
        );

        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(Call<NewsResponse> call,
                                   Response<NewsResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().articles.size() >= 2) {

                    NewsResponse.Article article1 =
                            response.body().articles.get(0);

                    NewsResponse.Article article2 =
                            response.body().articles.get(1);

                    tvNews1.setText(article1.title);
                    tvNews2.setText(article2.title);

                    newsUrl1 = article1.url;
                    newsUrl2 = article2.url;
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call,
                                  Throwable t) {

                Toast.makeText(MainActivity.this,
                        "News failed to load",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToCategory(String category) {
        Intent intent;
        switch (category) {
            case "Tourism":
                intent = new Intent(this, TourismActivity.class);
                break;
            case "Hotels":
                intent = new Intent(this, HotelActivity.class);
                break;
            case "Hospitals":
                intent = new Intent(this, HospitalActivity.class);
                break;
            case "Food":
                intent = new Intent(this, FoodActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent); 
    }

    private void setupNewsSection() {
        CardView cardNews1 = findViewById(R.id.cardNews1);  
        if (cardNews1 != null) {
            cardNews1.setOnClickListener(v -> {
                openWebSource("https://www.tribuneindia.com/jalandhar/"); 
            });
        }

        CardView cardNews2 = findViewById(R.id.cardNews2);  
        if (cardNews2 != null) {
            cardNews2.setOnClickListener(v -> {
                openWebSource("https://www.hindustantimes.com/punjab/jalandhar"); 
            });
        }
    }

    private void openWebSource(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
        startActivity(intent);
    }

    private void setupNavigationMenu() {

        View header = findViewById(R.id.headerHome);

        if (header != null) {

            TextView menuIcon = header.findViewById(R.id.tvMenuIcon);

            MenuHelper.setupMenu(this, menuIcon);
        }
    }

    private void setupCategoryCards() {
        CardView cardHospitals = findViewById(R.id.cardHospitals);  
        if (cardHospitals != null) {
            cardHospitals.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, HospitalActivity.class)));
        }

        CardView cardTransport = findViewById(R.id.cardTransport);  
        if (cardTransport != null) {
            cardTransport.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, TransportActivity.class))); 
        }

        CardView cardTourism = findViewById(R.id.cardTourism);  
        if (cardTourism != null) {
            cardTourism.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, TourismActivity.class)));
        }

        CardView cardHotels = findViewById(R.id.cardHotels);  
        if (cardHotels != null) {
            cardHotels.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, HotelActivity.class)));
        }

        CardView cardFood = findViewById(R.id.cardFood);  
        if (cardFood != null) {
            cardFood.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, FoodActivity.class)));
        }

        CardView cardEmergency = findViewById(R.id.cardEmergency);  
        if (cardEmergency != null) {
            cardEmergency.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, ContactActivity.class))); 
        }
    }
}