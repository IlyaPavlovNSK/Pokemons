package com.pavlovnsk.pokemons.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

    private int offset = 0;
    private int limit = 30;

    public interface ItemOnClickListListener {
        void onItemClick(int pokemonNumber);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pokemonViewModel = new ViewModelProvider(requireActivity()).get(PokemonViewModel.class);
        sourceFactory = pokemonViewModel.getParametersFromBd();

        if (savedInstanceState != null) {
            Utils.visiblePosition = savedInstanceState.getInt("position");
        }

        // PagedList
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(limit)
                .setPrefetchDistance(limit / 2 - 1)
                .setInitialLoadSizeHint(limit)
                .build();

        pagedListLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                .setInitialLoadKey(Utils.visiblePosition)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setBoundaryCallback(new PagedList.BoundaryCallback<PokemonParameters>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        getPokemonParametersFromWeb(limit, offset);
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull PokemonParameters itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                        getPokemonParametersFromWeb(limit, offset);
                        offset = offset + limit;
                    }
                })
                .build();


        pokemonPagedListAdapter = new PokemonPagedListAdapter(new DiffUtil.ItemCallback<PokemonParameters>() {
            @Override
            public boolean areItemsTheSame(@NonNull PokemonParameters oldItem, @NonNull PokemonParameters newItem) {
                int oldNumber = oldItem.getId();
                int newNumber = newItem.getId();
                return oldNumber == newNumber;
            }

            @Override
            public boolean areContentsTheSame(@NonNull PokemonParameters oldItem, @NonNull PokemonParameters newItem) {
                return oldItem.getPokemonName().equals(newItem.getPokemonName());
            }
        }, this);

        pagedListLiveData.observe(this, new Observer<PagedList<PokemonParameters>>() {
            @Override
            public void onChanged(PagedList<PokemonParameters> pokemonParameters) {
                pokemonPagedListAdapter.submitList(pokemonParameters);
                sourceFactory.create().invalidate();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

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
        int p = layoutManager.findFirstVisibleItemPosition();
        outState.putInt("position", p);
    }

    public void getPokemonParametersFromWeb(int limit, int totalItemCount) {
        pokemonViewModel.getPokemonParametersFromWeb(limit, totalItemCount);
    }

    public void setOnClickListListener(ItemOnClickListListener onClickListListener) {
        this.itemOnClickListListener = onClickListListener;
    }

    @Override
    public void onItemClick(int position) {
        PokemonParameters pokemon = pokemonPagedListAdapter.getCurrentList().get(position);
        if (itemOnClickListListener != null) {
            itemOnClickListListener.onItemClick(pokemon.getPokemonNumber());
        }
    }

    public RecyclerView getPokemonRecyclerView() {
        return pokemonRecyclerView;
    }

    public PokemonViewModel getPokemonViewModel() {
        return pokemonViewModel;
    }

    public PokemonPagedListAdapter getPokemonPagedListAdapter() {
        return pokemonPagedListAdapter;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }
}
