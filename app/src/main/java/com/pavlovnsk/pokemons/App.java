package com.pavlovnsk.pokemons;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private Retrofit mRetrofit;
    private static JSONPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public void onCreate() {
        super.onCreate();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = mRetrofit.create(JSONPlaceHolderApi.class);
    }

    public static JSONPlaceHolderApi getJSONPlaceHolderApi() {
        return jsonPlaceHolderApi;
    }

}
