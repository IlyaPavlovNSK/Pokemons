package com.pavlovnsk.pokemons.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavlovnsk.pokemons.Adapters.PokemonPagedListAdapter;
import com.pavlovnsk.pokemons.POJO.PokemonParameters;
import com.pavlovnsk.pokemons.R;
import com.pavlovnsk.pokemons.Utils;
import com.pavlovnsk.pokemons.ViewModels.PokemonViewModel;

import java.util.concurrent.Executors;

public class ListFragment extends Fragment implements com.pavlovnsk.pokemons.Adapters.PokemonPagedListAdapter.PagedListClickListener {

    private RecyclerView pokemonRecyclerView;
    private LinearLayoutManager layoutManager;
    private PokemonViewModel pokemonViewModel;
    private ItemOnClickListListener itemOnClickListListener;
    private PokemonPagedListAdapter pokemonPagedListAdapter;
    private DataSource.Factory<Integer, PokemonParameters> sourceFactory;
    private LiveData<PagedList<PokemonParameters>> pagedListLiveData;
    private PagedList.Config config;

    private int offset = 0;
    private int limit = 30;

    public interface ItemOnClickListListener {
        void onItemClick(int pokemonNumber);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pokemonViewModel = new ViewModelProvider(requireActivity()).get(PokemonViewModel.class);

        config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(limit)
                .setPrefetchDistance(limit / 2 - 1)
                .setInitialLoadSizeHint(limit)
                .build();

        pokemonPagedListAdapter = new PokemonPagedListAdapter(new DiffUtil.ItemCallback<PokemonParameters>() {
            @Override
            public boolean areItemsTheSame(@NonNull PokemonParameters oldItem, @NonNull PokemonParameters newItem) {
                int oldNumber = oldItem.getPokemonNumber();
                int newNumber = newItem.getPokemonNumber();
                return oldNumber == newNumber;
            }
            @Override
            public boolean areContentsTheSame(@NonNull PokemonParameters oldItem, @NonNull PokemonParameters newItem) {
                return oldItem.getAttackStats()==newItem.getAttackStats()
                        && oldItem.getDefenseStats()==newItem.getDefenseStats()
                        && oldItem.getHpStats()==newItem.getHpStats();
            }
        }, this);

        loadData("pokemonNumber");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        if (savedInstanceState != null) {
            Utils.visiblePosition = savedInstanceState.getInt("position");
        }

        setRetainInstance(true);
        layoutManager = new LinearLayoutManager(getContext());
        pokemonRecyclerView = view.findViewById(R.id.RV_pokemon_list);
        pokemonRecyclerView.setAdapter(pokemonPagedListAdapter);
        pokemonRecyclerView.setLayoutManager(layoutManager);
        pokemonRecyclerView.setHasFixedSize(true);
        getPokemonRecyclerView().smoothScrollToPosition(Utils.visiblePosition);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int i = layoutManager.findFirstVisibleItemPosition();
        outState.putInt("position", i);
    }

    private void loadData(String order) {
        sourceFactory = pokemonViewModel.getParametersFromBd(order);

        pagedListLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                .setInitialLoadKey(Utils.visiblePosition)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setBoundaryCallback(new PagedList.BoundaryCallback<PokemonParameters>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        if(offset==0){
                            getPokemonParametersFromWeb(limit, offset);
                            offset = offset + limit;
                        }
                    }
                    @Override
                    public void onItemAtEndLoaded(@NonNull PokemonParameters itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                        getPokemonParametersFromWeb(limit, offset);
                        offset = offset + limit;
                    }
                }).build();

        pagedListLiveData.observe(this, pokemonParameters -> {
            pokemonPagedListAdapter.submitList(pokemonParameters);
            sourceFactory.create().invalidate();
        });
    }


    public void getSortDataFromBd(String order) {
        sourceFactory = pokemonViewModel.getParametersFromBd(order);

        pagedListLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                .setInitialLoadKey(Utils.visiblePosition)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setBoundaryCallback(new PagedList.BoundaryCallback<PokemonParameters>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                    }
                    @Override
                    public void onItemAtEndLoaded(@NonNull PokemonParameters itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                        getPokemonParametersFromWeb(limit, offset);
                        offset = offset + limit;
                    }
                }).build();

        pagedListLiveData.observe(this, pokemonParameters -> {
            pokemonPagedListAdapter.submitList(pokemonParameters);
            sourceFactory.create().invalidate();
        });
    }


    public void getPokemonParametersFromWeb(int limit, int totalItemCount) {
        pokemonViewModel.getPokemonParametersFromWeb(limit, totalItemCount);
    }

    @Override
    public void onItemClick(int position) {
        PokemonParameters pokemon = pokemonPagedListAdapter.getCurrentList().get(position);
        if (itemOnClickListListener != null) {
            if (pokemon != null) {
                itemOnClickListListener.onItemClick(pokemon.getPokemonNumber());
            }
        }
    }

    public void setOnClickListListener(ItemOnClickListListener onClickListListener) {
        this.itemOnClickListListener = onClickListListener;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public RecyclerView getPokemonRecyclerView() {
        return pokemonRecyclerView;
    }

    public PokemonViewModel getPokemonViewModel() {
        return pokemonViewModel;
    }

    public int getLimit() {
        return limit;
    }
}
