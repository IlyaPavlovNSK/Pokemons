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

import com.pavlovnsk.pokemons.R;
import com.squareup.picasso.Picasso;

public class ItemFragment extends Fragment {

    private ImageView poster;
    private TextView heightStats;
    private TextView weightStats;
    private TextView typeStats;
    private TextView attackStats;
    private TextView defenseStats;
    private TextView hpStats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container ,false);

        poster = view.findViewById(R.id.IV_poster_item);
        heightStats = view.findViewById(R.id.TV_height_stats);
        weightStats = view.findViewById(R.id.TV_weight_stats);
        typeStats = view.findViewById(R.id.TV_type_stats);
        attackStats = view.findViewById(R.id.TV_attack_stats);
        defenseStats = view.findViewById(R.id.TV_defense_stats);
        hpStats = view.findViewById(R.id.TV_hp_stats);

        return view;
    }

    public void bind (String poster, int heightStats, int weightStats, String typeStats, int attackStats, int defenseStats, int hpStats){
        this.heightStats.setText(String.valueOf(heightStats));
        this.weightStats.setText(String.valueOf(weightStats));
        this.typeStats.setText(String.valueOf(typeStats));
        this.attackStats.setText(String.valueOf(attackStats));
        this.defenseStats.setText(String.valueOf(defenseStats));
        this.hpStats.setText(String.valueOf(hpStats));

        Picasso.get().load(poster).fit().centerInside().into(this.poster);
    }
}
