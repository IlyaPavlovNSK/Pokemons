package com.pavlovnsk.pokemons.Data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pavlovnsk.pokemons.POJO.PokemonParameters;

@Dao
public interface PokemonParametersDao {

    @Query("SELECT * FROM pokemonParameters WHERE pokemonNumber = :number")
    LiveData<PokemonParameters> getByNumber(int number);

    @Query("SELECT * FROM pokemonParameters")
    DataSource.Factory<Integer, PokemonParameters> getPokemonList();

    @Query("DELETE FROM pokemonParameters")
    void deleteAllParameters();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonParameters(PokemonParameters pokemonParameters);
}
