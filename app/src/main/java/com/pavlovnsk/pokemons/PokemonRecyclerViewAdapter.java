package com.pavlovnsk.pokemons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pavlovnsk.pokemons.POJO.PokemonList;
import com.pavlovnsk.pokemons.POJO.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<PokemonRecyclerViewAdapter.PokemonViewHolder> {

    public interface OnItemMovieClickListener {
        void onItemClick(int position);
    }

    private Context context;
    private List<Result> pokemonList;
    private OnItemMovieClickListener onItemMovieClickListener;

    public PokemonRecyclerViewAdapter(Context context, OnItemMovieClickListener onItemMovieClickListener) {
        this.context = context;
        pokemonList = new ArrayList<>();
        this.onItemMovieClickListener = onItemMovieClickListener;
    }

    public void setPokemonList(List<Result> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pokemon_item, parent, false);
        return new PokemonViewHolder(view, onItemMovieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        holder.bind(pokemonList.get(position));
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public List<Result> getPokemonList() {
        return pokemonList;
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImageView;
        private TextView nameTextView;
        private OnItemMovieClickListener onItemMovieClickListener;

        public PokemonViewHolder(@NonNull View itemView, OnItemMovieClickListener onItemMovieClickListener) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.IV_pokemon_item);
            nameTextView = itemView.findViewById(R.id.TV_pokemon_item);
            this.onItemMovieClickListener = onItemMovieClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemMovieClickListener.onItemClick(getAdapterPosition());
        }

        private void bind(final Result pokemon) {
            nameTextView.setText(pokemon.getName());
           // Picasso.get().load(pokemon.getPoster()).fit().centerInside().into(posterImageView);
        }
    }
}
