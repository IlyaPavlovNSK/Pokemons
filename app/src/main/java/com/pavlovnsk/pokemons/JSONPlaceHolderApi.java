package com.pavlovnsk.pokemons;

import com.pavlovnsk.pokemons.POJO.PokemonList;
import com.pavlovnsk.pokemons.POJO.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    @GET("pokemon") //https://pokeapi.co/api/v2/pokemon?limit=60&offset=0
    Call<PokemonList> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);

}
