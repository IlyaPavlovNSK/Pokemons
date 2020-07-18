package com.pavlovnsk.pokemons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavlovnsk.pokemons.POJO.PokemonItem;
import com.pavlovnsk.pokemons.POJO.Result;
import com.pavlovnsk.pokemons.POJO.Stat;
import com.pavlovnsk.pokemons.POJO.Type;
import com.pavlovnsk.pokemons.POJO.Type_;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment implements PokemonRecyclerViewAdapter.OnItemMovieClickListener {

    private RecyclerView pokemonRecyclerView;
    private PokemonRecyclerViewAdapter pokemonRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private PokemonViewModel pokemonViewModel;

    private int limit = 5;
    private int offset = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        pokemonViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(PokemonViewModel.class);

        layoutManager = new LinearLayoutManager(getContext());
        pokemonRecyclerView = view.findViewById(R.id.RV_pokemon_list);
        pokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter(getContext(), this, pokemonViewModel);

        pokemonRecyclerView.setAdapter(pokemonRecyclerViewAdapter);
        pokemonRecyclerView.setLayoutManager(layoutManager);
        pokemonRecyclerView.setHasFixedSize(true);

        observeViewModel(limit, offset);

        pokemonRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == pokemonRecyclerViewAdapter.getPokemonList().size() - 1) {
                    offset = offset + limit;
                    observeViewModel(limit, offset);
                }
            }
        });


        return view;
    }

    private void observeViewModel(int l, int o) {
        pokemonViewModel.getPokemonList(l, o).observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> pokemonList) {
                int i = pokemonList.size();
                pokemonRecyclerViewAdapter.getPokemonList().addAll(pokemonList);
                pokemonRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        final ItemFragment itemFragment = new ItemFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragment).addToBackStack(null).commit();

        Result pokemon = pokemonRecyclerViewAdapter.getPokemonList().get(position);
        String url = pokemon.getUrl();
        String[] s = url.split("/");
        int pokemonNumber = Integer.parseInt(s[s.length - 1]);

        App.getJSONPlaceHolderApi().getPokemonItem(pokemonNumber).enqueue(new Callback<PokemonItem>() {
            @Override
            public void onResponse(Call<PokemonItem> call, Response<PokemonItem> response) {

                int heightStats = response.body().getHeight();
                int weightStats = response.body().getWeight();

                StringBuilder types = new StringBuilder();
                List<Type> typeStats = response.body().getTypes();
                for (int i = 0; i < typeStats.size(); i++) {
                    Type type = typeStats.get(i);
                    Type_ type_ = type.getType();

                    types.append(type_.getName()).append(" ");
                }

                int attackStats = 0;
                int defenseStats = 0;
                int hpStats = 0;
                List<Stat> stats = response.body().getStats();
                for (int i = 0; i < stats.size(); i++) {
                    Stat stat = stats.get(i);

                    switch (i) {
                        case 0:
                            hpStats = stat.getBaseStat();
                            break;
                        case 1:
                            attackStats = stat.getBaseStat();
                            break;
                        case 2:
                            defenseStats = stat.getBaseStat();
                            break;
                    }
                }

                String poster = response.body().getSprites().getBackDefault();
                itemFragment.bind(poster, heightStats, weightStats, types.toString().trim(), attackStats, defenseStats, hpStats);
            }

            @Override
            public void onFailure(Call<PokemonItem> call, Throwable t) {
                Toast.makeText(getContext(), "Error occurred while getting request!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
