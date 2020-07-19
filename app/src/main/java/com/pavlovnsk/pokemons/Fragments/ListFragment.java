package com.pavlovnsk.pokemons.Fragments;

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

import com.pavlovnsk.pokemons.App;
import com.pavlovnsk.pokemons.POJO.PokemonItem;
import com.pavlovnsk.pokemons.POJO.Result;
import com.pavlovnsk.pokemons.POJO.Stat;
import com.pavlovnsk.pokemons.POJO.Type;
import com.pavlovnsk.pokemons.POJO.Type_;
import com.pavlovnsk.pokemons.Adapters.PokemonRecyclerViewAdapter;
import com.pavlovnsk.pokemons.ViewModels.PokemonViewModel;
import com.pavlovnsk.pokemons.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment implements PokemonRecyclerViewAdapter.OnItemMovieClickListener {

    private RecyclerView pokemonRecyclerView;
    private PokemonRecyclerViewAdapter pokemonRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private PokemonViewModel pokemonViewModel;
    private ItemOnClickListListener itemOnClickListListener = null;

    private int offset = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int limit = 5;

    private Hashtable<Result, List<Stat>> detailsPokemon;

    public interface ItemOnClickListListener {
        void onItemClick(int pokemonNumber);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        setRetainInstance(true);

        layoutManager = new LinearLayoutManager(getContext());
        pokemonRecyclerView = view.findViewById(R.id.RV_pokemon_list);
        pokemonRecyclerView.setAdapter(pokemonRecyclerViewAdapter);
        pokemonRecyclerView.setLayoutManager(layoutManager);
        pokemonRecyclerView.setHasFixedSize(true);

        pokemonRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = pokemonRecyclerView.getChildCount(); //кол-во видимых элементов
                totalItemCount = layoutManager.getItemCount(); // кол-во элементов всписке
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition(); // позиция первого видимого элемента

                if (loading) {
                    if (totalItemCount > offset) {
                        loading = false;
                        offset = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + limit)) {
                    observeViewModel(limit, totalItemCount);
                    loading = true;
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsPokemon = new Hashtable<>();

        pokemonViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(PokemonViewModel.class);
        pokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter(getContext(), this, pokemonViewModel);
        pokemonRecyclerViewAdapter.notifyDataSetChanged();

        observeViewModel(limit, offset);
    }

    public PokemonRecyclerViewAdapter getPokemonRecyclerViewAdapter() {
        return pokemonRecyclerViewAdapter;
    }

    public Hashtable<Result, List<Stat>> getDetailsPokemon() {
        return detailsPokemon;
    }

    public RecyclerView getPokemonRecyclerView() {
        return pokemonRecyclerView;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void observeViewModel(int l, int o) {
        pokemonViewModel.getPokemonList(l, o).observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> pokemonList) {
                pokemonRecyclerViewAdapter.getPokemonList().addAll(pokemonList);
                pokemonRecyclerViewAdapter.notifyItemRangeInserted(totalItemCount, limit);

                for (int i = 0; i < pokemonList.size(); i++) {
                    detailsPokemon.put(pokemonList.get(i), loadStats(getPokemonNumber(pokemonList.get(i))));
                }
            }
        });
    }

    public void setOnClickListListener(ItemOnClickListListener onClickListListener) {
        this.itemOnClickListListener = onClickListListener;
    }

    @Override
    public void onItemClick(int position) {
        Result pokemon = pokemonRecyclerViewAdapter.getPokemonList().get(position);
        if (itemOnClickListListener != null) {
            itemOnClickListListener.onItemClick(getPokemonNumber(pokemon));
        }
    }

    public int getPokemonNumber(Result pokemon) {
        String url = pokemon.getUrl();
        String[] s = url.split("/");
        return Integer.parseInt(s[s.length - 1]);
    }



    public List<Stat> loadStats(int pokemonNumber) {
        List<Stat> stats = new ArrayList<>();
        App.getJSONPlaceHolderApi().getPokemonItem(pokemonNumber).enqueue(new Callback<PokemonItem>() {
            @Override
            public void onResponse(Call<PokemonItem> call, Response<PokemonItem> response) {
                stats.addAll(response.body().getStats());
            }

            @Override
            public void onFailure(Call<PokemonItem> call, Throwable t) {
                Toast.makeText(getContext(), "Error occurred while getting request!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        return stats;
    }

    public void loadPokemonParameters(int pokemonNumber, final ItemFragment itemFragment) {
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
