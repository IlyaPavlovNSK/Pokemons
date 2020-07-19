package com.pavlovnsk.pokemons.Data;

import com.pavlovnsk.pokemons.POJO.PokemonItem;
import com.pavlovnsk.pokemons.POJO.PokemonList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    @GET("pokemon") //https://pokeapi.co/api/v2/pokemon?limit=60&offset=0
    Call<PokemonList> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{pokemonNumber}/") //https://pokeapi.co/api/v2/pokemon/3/
    Call<PokemonItem> getPokemonItem(@Path("pokemonNumber") int pokemonNumber);

}
