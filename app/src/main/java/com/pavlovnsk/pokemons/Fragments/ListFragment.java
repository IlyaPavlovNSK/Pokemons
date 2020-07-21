package com.pavlovnsk.pokemons.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavlovnsk.pokemons.Adapters.PokemonRecyclerViewAdapter;
import com.pavlovnsk.pokemons.POJO.PokemonParameters;
import com.pavlovnsk.pokemons.R;
import com.pavlovnsk.pokemons.Utils;
import com.pavlovnsk.pokemons.ViewModels.PokemonViewModel;

import java.util.List;

public class ListFragment extends Fragment implements PokemonRecyclerViewAdapter.OnItemPokemonClickListener {

    private RecyclerView pokemonRecyclerView;
    private PokemonRecyclerViewAdapter pokemonRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private PokemonViewModel pokemonViewModel;
    private ItemOnClickListListener itemOnClickListListener;

    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int offset = 0;
    private int limit = 5;

    public interface ItemOnClickListListener {
        void onItemClick(int pokemonNumber);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pokemonViewModel = new ViewModelProvider(requireActivity()).get(PokemonViewModel.class);

        pokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter(getActivity(), this);
        pokemonRecyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

        observeViewModel();
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

        if (layoutManager.getItemCount() < 0) {
            getPokemonParametersFromWeb(limit, offset);
        }

        pokemonRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = pokemonRecyclerView.getChildCount(); //кол-во видимых элементов
                totalItemCount = layoutManager.getItemCount() + Utils.addCount; // кол-во элементов всписке с учетом сдвига
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition() + Utils.addCount; // позиция первого видимого элемента с учетом сдвига

                if (loading) {
                    if (totalItemCount > offset) {
                        loading = false;
                        offset = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + limit)) {

                    if (pokemonViewModel.getPokemonParametersFromBd().getValue() == null) {
                        getPokemonParametersFromWeb(limit, totalItemCount);
                    }
                    loading = true;
                }
            }
        });
        return view;
    }

    public void getPokemonParametersFromWeb(int limit, int totalItemCount) {
        pokemonViewModel.getPokemonParametersFromWeb(limit, totalItemCount);
    }

    public void observeViewModel() {
        pokemonViewModel.getPokemonParametersFromBd().observe(this, new Observer<List<PokemonParameters>>() {
            @Override
            public void onChanged(List<PokemonParameters> pokemonList) {
                if (pokemonList != null) {
                    pokemonRecyclerViewAdapter.setPokemonList(pokemonList);
                    pokemonRecyclerViewAdapter.notifyItemRangeInserted(totalItemCount, limit);
                }
            }
        });
    }

    public void setOnClickListListener(ItemOnClickListListener onClickListListener) {
        this.itemOnClickListListener = onClickListListener;
    }

    @Override
    public void onItemClick(int position) {
        PokemonParameters pokemon = pokemonRecyclerViewAdapter.getPokemonList().get(position);
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

    public PokemonRecyclerViewAdapter getPokemonRecyclerViewAdapter() {
        return pokemonRecyclerViewAdapter;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }
}
