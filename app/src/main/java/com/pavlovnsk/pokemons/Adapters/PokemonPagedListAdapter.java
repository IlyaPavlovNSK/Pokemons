package com.pavlovnsk.pokemons.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavlovnsk.pokemons.POJO.PokemonParameters;
import com.pavlovnsk.pokemons.R;
import com.squareup.picasso.Picasso;

public class PokemonPagedListAdapter extends PagedListAdapter<PokemonParameters, PokemonPagedListAdapter.PokemonParametersViewHolder> {

    public interface PagedListClickListener {
        void onItemClick(int position);
    }

    private PagedListClickListener pagedListClickListener;

    public PokemonPagedListAdapter(DiffUtil.ItemCallback<PokemonParameters> diffUtilCallback, PagedListClickListener pagedListClickListener) {
        super(diffUtilCallback);
        this.pagedListClickListener = pagedListClickListener;
    }

    @NonNull
    @Override
    public PokemonParametersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_item, parent, false);
        return new PokemonParametersViewHolder(view, pagedListClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonParametersViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class PokemonParametersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImageView;
        private TextView nameTextView;
        private PagedListClickListener pagedListClickListener;

        public PokemonParametersViewHolder(@NonNull View itemView, PagedListClickListener pagedListClickListener) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.IV_pokemon_item);
            nameTextView = itemView.findViewById(R.id.TV_pokemon_item);
            this.pagedListClickListener = pagedListClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            pagedListClickListener.onItemClick(getLayoutPosition());
        }

        private void bind(final PokemonParameters pokemon) {
            nameTextView.setText(pokemon.getPokemonName());
            Picasso.get().load(pokemon.getPoster()).fit().centerInside().into(posterImageView);
        }
    }
}
