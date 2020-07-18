package com.pavlovnsk.pokemons;

import android.content.Context;
import android.graphics.Movie;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pavlovnsk.pokemons.POJO.PokemonItem;
import com.pavlovnsk.pokemons.POJO.PokemonList;
import com.pavlovnsk.pokemons.POJO.Result;
import com.pavlovnsk.pokemons.POJO.Sprites;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {

    private Context context;
    private MutableLiveData<List<Result>> pokemonList = new MutableLiveData<>();
    private MutableLiveData<PokemonItem> pokemonItem = new MutableLiveData<>();

    private JSONPlaceHolderApi jsonPlaceHolderApi;

    public AppRepository(Context context) {
        this.context = context;
        jsonPlaceHolderApi = App.getJSONPlaceHolderApi();
    }

    public MutableLiveData<List<Result>> getPokemonList(int limit, int offset) {
        jsonPlaceHolderApi.getPokemonList(limit, offset).enqueue(new Callback<PokemonList>() {
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
                pokemonList.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
                Toast.makeText(context, "Error occurred while getting request!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        return pokemonList;
    }

    public MutableLiveData<PokemonItem> getPokemonItem (int pokemonNumber){
        jsonPlaceHolderApi.getPokemonItem(pokemonNumber).enqueue(new Callback<PokemonItem>() {
            @Override
            public void onResponse(Call<PokemonItem> call, Response<PokemonItem> response) {
                pokemonItem.setValue(response.body());
            }

            @Override
            public void onFailure(Call<PokemonItem> call, Throwable t) {
                Toast.makeText(context, "Error occurred while getting request!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        return pokemonItem;
    }
}
