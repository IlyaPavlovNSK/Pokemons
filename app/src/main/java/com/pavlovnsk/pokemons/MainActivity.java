package com.pavlovnsk.pokemons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.pavlovnsk.pokemons.POJO.Result;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PokemonRecyclerViewAdapter.OnItemMovieClickListener {

    private RecyclerView pokemonRecyclerView;
    private PokemonRecyclerViewAdapter pokemonRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private PokemonViewModel pokemonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PokemonViewModel.class);

        layoutManager = new LinearLayoutManager(this);
        pokemonRecyclerView = findViewById(R.id.RV_pokemon_list);
        pokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter(this, this);

        pokemonRecyclerView.setAdapter(pokemonRecyclerViewAdapter);
        pokemonRecyclerView.setLayoutManager(layoutManager);
        pokemonRecyclerView.setHasFixedSize(true);

        observeViewModel();
    }

    private void observeViewModel() {
        pokemonViewModel.getPokemonList(60, 0).observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> pokemonList) {
                pokemonRecyclerViewAdapter.setPokemonList(pokemonList);
                pokemonRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}