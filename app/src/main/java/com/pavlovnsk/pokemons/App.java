package com.pavlovnsk.pokemons;

import android.app.Application;

import androidx.room.Room;

import com.pavlovnsk.pokemons.Data.AppDatabase;
import com.pavlovnsk.pokemons.Data.JSONPlaceHolderApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private Retrofit mRetrofit;
    private static JSONPlaceHolderApi jsonPlaceHolderApi;
    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = mRetrofit.create(JSONPlaceHolderApi.class);

        db =  Room.databaseBuilder(this, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static JSONPlaceHolderApi getJSONPlaceHolderApi() {
        return jsonPlaceHolderApi;
    }

    public static AppDatabase getDb() {
        return db;
    }
}
