package com.pavlovnsk.pokemons.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pavlovnsk.pokemons.POJO.PokemonParameters;

@Database(entities = {PokemonParameters.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PokemonParametersDao pokemonParametersDao();
}
