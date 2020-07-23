package com.pavlovnsk.pokemons.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.pavlovnsk.pokemons.Data.AppRepository;
import com.pavlovnsk.pokemons.POJO.PokemonParameters;

public class PokemonViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    public PokemonViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository();
    }

    public DataSource.Factory<Integer, PokemonParameters> getParametersFromBd(String order) {
        return appRepository.getParametersFromBd(order);
    }

    public LiveData<PokemonParameters> getPokemonParameterItemFromBd(int pokemonNumber) {
        return appRepository.getPokemonParameterItemFromBd(pokemonNumber);
    }

    public void deleteAllParameters() {
        appRepository.deleteAllParameters();
    }

    public void getPokemonParametersFromWeb(int limit, int offset) {
        appRepository.getPokemonParametersFromWeb(limit, offset);
    }
}
