package com.guide.smartcity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("everything")
    Call<NewsResponse> getNews(

            @Query("q") String query,

            @Query("sortBy") String sortBy,

            @Query("apiKey") String apiKey
    );
}