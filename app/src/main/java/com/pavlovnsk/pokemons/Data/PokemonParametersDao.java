package com.pavlovnsk.pokemons.Data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pavlovnsk.pokemons.POJO.PokemonParameters;

@Dao
public interface PokemonParametersDao {

    @Query("SELECT * FROM pokemonParameters WHERE pokemonNumber = :number")
    LiveData<PokemonParameters> getByNumber(int number);

    @Query("SELECT * FROM pokemonParameters ORDER BY CASE WHEN :order = 'pokemonNumber' THEN pokemonNumber END ASC, CASE :order WHEN 'attackStats' THEN attackStats WHEN 'defenseStats' THEN defenseStats WHEN 'hpStats' THEN hpStats END DESC")
    DataSource.Factory<Integer, PokemonParameters> getPokemonList(String order);

    @Query("DELETE FROM pokemonParameters")
    void deleteAllParameters();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonParameters(PokemonParameters pokemonParameters);
}
