package com.pavlovnsk.pokemons;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.pavlovnsk.pokemons.POJO.Result;

import java.util.List;

public class PokemonViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    public PokemonViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository(application);
    }

    public MutableLiveData<List<Result>> getPokemonList(int limit, int offset) {
        return appRepository.getPokemonList(limit, offset);
    }
}
