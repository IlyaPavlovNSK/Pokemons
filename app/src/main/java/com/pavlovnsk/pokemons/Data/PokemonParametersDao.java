package com.pavlovnsk.pokemons.Data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pavlovnsk.pokemons.POJO.PokemonParameters;

import java.util.List;

@Dao
public interface PokemonParametersDao {

    @Query("SELECT * FROM pokemonParameters")
    LiveData<List<PokemonParameters>> getAll();

    @Query("SELECT * FROM pokemonParameters WHERE pokemonNumber = :id")
    LiveData<PokemonParameters> getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonParameters(PokemonParameters pokemonParametersLister);

    @Query("SELECT * FROM pokemonParameters WHERE pokemonNumber BETWEEN :limit AND :offset")
    LiveData<List<PokemonParameters>> getPokemonList(int limit, int offset);

    @Query("DELETE FROM pokemonParameters")
    void deleteAllParameters();


}
