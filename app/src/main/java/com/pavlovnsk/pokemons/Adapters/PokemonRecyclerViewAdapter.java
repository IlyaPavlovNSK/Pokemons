package com.pavlovnsk.pokemons.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pavlovnsk.pokemons.App;
import com.pavlovnsk.pokemons.POJO.PokemonItem;
import com.pavlovnsk.pokemons.POJO.PokemonParameters;
import com.pavlovnsk.pokemons.POJO.Result;
import com.pavlovnsk.pokemons.ViewModels.PokemonViewModel;
import com.pavlovnsk.pokemons.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<PokemonRecyclerViewAdapter.PokemonViewHolder> {

    public interface OnItemPokemonClickListener {
        void onItemClick(int position);
    }

    private Context context;
    private List<PokemonParameters> pokemonParametersList;
    private OnItemPokemonClickListener onItemPokemonClickListener;


    public PokemonRecyclerViewAdapter(Context context, OnItemPokemonClickListener onItemPokemonClickListener) {
        this.context = context;
        pokemonParametersList = new ArrayList<>();
        this.onItemPokemonClickListener = onItemPokemonClickListener;
    }

    public void setPokemonList(List<PokemonParameters> pokemonParametersList) {
        this.pokemonParametersList = pokemonParametersList;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pokemon_item, parent, false);
        return new PokemonViewHolder(view, onItemPokemonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        holder.bind(pokemonParametersList.get(position));
    }

    @Override
    public int getItemCount() {
        return pokemonParametersList.size();
    }

    public List<PokemonParameters> getPokemonList() {
        return pokemonParametersList;
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImageView;
        private TextView nameTextView;
        private OnItemPokemonClickListener onItemPokemonClickListener;

        public PokemonViewHolder(@NonNull View itemView, OnItemPokemonClickListener onItemPokemonClickListener) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.IV_pokemon_item);
            nameTextView = itemView.findViewById(R.id.TV_pokemon_item);
            this.onItemPokemonClickListener = onItemPokemonClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemPokemonClickListener.onItemClick(getAdapterPosition());
        }

        private void bind(final PokemonParameters pokemon) {
            nameTextView.setText(pokemon.getPokemonName());
            Picasso.get().load(pokemon.getPoster()).fit().centerInside().into(posterImageView);
        }
    }
}

