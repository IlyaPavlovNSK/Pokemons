package com.pavlovnsk.pokemons.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pavlovnsk.pokemons.POJO.PokemonParameters;
import com.pavlovnsk.pokemons.R;
import com.pavlovnsk.pokemons.ViewModels.PokemonViewModel;
import com.squareup.picasso.Picasso;

public class ItemFragment extends Fragment {

    private ImageView poster;
    private TextView heightStats;
    private TextView weightStats;
    private TextView typeStats;
    private TextView attackStats;
    private TextView defenseStats;
    private TextView hpStats;
    private int pokemonNumber;

    private PokemonViewModel pokemonViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pokemonViewModel = new ViewModelProvider(requireActivity()).get(PokemonViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container ,false);

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("pokemonNumber")) {
            pokemonNumber = bundle.getInt("pokemonNumber");
        }

        poster = view.findViewById(R.id.IV_poster_item);
        heightStats = view.findViewById(R.id.TV_height_stats);
        weightStats = view.findViewById(R.id.TV_weight_stats);
        typeStats = view.findViewById(R.id.TV_type_stats);
        attackStats = view.findViewById(R.id.TV_attack_stats);
        defenseStats = view.findViewById(R.id.TV_defense_stats);
        hpStats = view.findViewById(R.id.TV_hp_stats);

        pokemonViewModel.getPokemonParameterItemFromBd(pokemonNumber).observe(getViewLifecycleOwner(), new Observer<PokemonParameters>() {
            @Override
            public void onChanged(PokemonParameters pokemonParameters) {
                bind(pokemonParameters);
            }
        });
        return view;
    }

    public void bind (PokemonParameters pokemonParameters){
        this.heightStats.setText(String.valueOf(pokemonParameters.getHeightStats()));
        this.weightStats.setText(String.valueOf(pokemonParameters.getWeightStats()));
        this.typeStats.setText(String.valueOf(pokemonParameters.getTypes()));
        this.attackStats.setText(String.valueOf(pokemonParameters.getAttackStats()));
        this.defenseStats.setText(String.valueOf(pokemonParameters.getDefenseStats()));
        this.hpStats.setText(String.valueOf(pokemonParameters.getHpStats()));

        Picasso.get().load(pokemonParameters.getPoster()).fit().centerInside().into(this.poster);
    }
}
