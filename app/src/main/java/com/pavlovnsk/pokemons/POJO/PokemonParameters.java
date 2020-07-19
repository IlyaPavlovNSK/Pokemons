package com.pavlovnsk.pokemons.POJO;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PokemonParameters {

    @PrimaryKey
    private int pokemonNumber;
    private int attackStats;
    private int defenseStats;
    private int hpStats;
    private int heightStats;
    private int weightStats;
    private String types;
    private String poster;

    public PokemonParameters(int pokemonNumber, int attackStats, int defenseStats, int hpStats, int heightStats, int weightStats, String types, String poster) {
        this.pokemonNumber = pokemonNumber;
        this.attackStats = attackStats;
        this.defenseStats = defenseStats;
        this.hpStats = hpStats;
        this.heightStats = heightStats;
        this.weightStats = weightStats;
        this.types = types;
        this.poster = poster;
    }

    public int getPokemonNumber() {
        return pokemonNumber;
    }

    public void setPokemonNumber(int pokemonNumber) {
        this.pokemonNumber = pokemonNumber;
    }

    public int getAttackStats() {
        return attackStats;
    }

    public void setAttackStats(int attackStats) {
        this.attackStats = attackStats;
    }

    public int getDefenseStats() {
        return defenseStats;
    }

    public void setDefenseStats(int defenseStats) {
        this.defenseStats = defenseStats;
    }

    public int getHpStats() {
        return hpStats;
    }

    public void setHpStats(int hpStats) {
        this.hpStats = hpStats;
    }

    public int getHeightStats() {
        return heightStats;
    }

    public void setHeightStats(int heightStats) {
        this.heightStats = heightStats;
    }

    public int getWeightStats() {
        return weightStats;
    }

    public void setWeightStats(int weightStats) {
        this.weightStats = weightStats;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
