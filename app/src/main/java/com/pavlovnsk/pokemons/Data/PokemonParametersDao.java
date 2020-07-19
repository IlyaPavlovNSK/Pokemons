package com.pavlovnsk.pokemons.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pavlovnsk.pokemons.POJO.PokemonParameters;

import java.util.List;

@Dao
public interface PokemonParametersDao {

    @Query("SELECT * FROM pokemonParameters")
    List<PokemonParameters> getAll();

    @Query("SELECT * FROM pokemonParameters WHERE pokemonNumber = :id")
    PokemonParameters getById(int id);

    @Insert
    void insert(PokemonParameters employee);

    @Update
    void update(PokemonParameters employee);

    @Delete
    void delete(PokemonParameters employee);
}
