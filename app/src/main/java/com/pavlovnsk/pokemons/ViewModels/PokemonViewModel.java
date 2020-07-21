package com.pavlovnsk.pokemons.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pavlovnsk.pokemons.Data.AppRepository;
import com.pavlovnsk.pokemons.POJO.PokemonParameters;

import java.util.List;

public class PokemonViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<PokemonParameters>> pokemonParametersList;
    private LiveData<PokemonParameters> pokemonParameters;

    public PokemonViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository();
        pokemonParametersList = new MutableLiveData<>();
    }

    public void getPokemonParametersFromWeb(int limit, int offset) {
        appRepository.getPokemonParametersFromWeb(limit, offset);
    }

    public LiveData<List<PokemonParameters>> getPokemonParametersFromBd() {
        pokemonParametersList =  appRepository.getPokemonParametersFromBd();
        return pokemonParametersList;
    }

    public LiveData<PokemonParameters> getPokemonParameterItemFromBd(int pokemonNumber) {
        pokemonParameters = appRepository.getPokemonParameterItemFromBd(pokemonNumber);
        return pokemonParameters;
    }

    public void deleteAllParameters() {
        appRepository.deleteAllParameters();
    }
}
