package com.pavlovnsk.pokemons.Data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.pavlovnsk.pokemons.App;
import com.pavlovnsk.pokemons.POJO.PokemonItem;
import com.pavlovnsk.pokemons.POJO.PokemonList;
import com.pavlovnsk.pokemons.POJO.PokemonParameters;
import com.pavlovnsk.pokemons.POJO.Result;
import com.pavlovnsk.pokemons.POJO.Type;
import com.pavlovnsk.pokemons.POJO.Type_;
import com.pavlovnsk.pokemons.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {

    private JSONPlaceHolderApi jsonPlaceHolderApi;
    private AppDatabase appDatabase;
    private DataSource.Factory<Integer, PokemonParameters> dataSource;

    public AppRepository() {
        jsonPlaceHolderApi = App.getJSONPlaceHolderApi();
        appDatabase = App.getDb();
    }

    public DataSource.Factory<Integer, PokemonParameters> getParametersFromBd() {
        dataSource = appDatabase.pokemonParametersDao().getPokemonList();
        return dataSource;
    }

    public LiveData<PokemonParameters> getPokemonParameterItemFromBd(int pokemonNumber) {
        return appDatabase.pokemonParametersDao().getByNumber(pokemonNumber);
    }

    public void deleteAllParameters() {
        deletePokemonParametersList();
    }

    public void getPokemonParametersFromWeb(int limit, int offset) {
        jsonPlaceHolderApi.getPokemonList(limit, offset).enqueue(new Callback<PokemonList>() {
            @Override
            public void onResponse(@NonNull Call<PokemonList> call, @NonNull Response<PokemonList> response) {
                if (response.body() != null) {
                    Utils.count = response.body().getCount();

                    List <Result> results = response.body().getResults();
                    for (int i = 0; i < results.size(); i++) {
                        int pokemonNumber = getPokemonNumber(results.get(i));
                        loadPokemonParameters(pokemonNumber);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<PokemonList> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private int getPokemonNumber(Result result) {
        String url = result.getUrl();
        String[] s = url.split("/");
        return Integer.parseInt(s[s.length - 1]);
    }

    private void loadPokemonParameters(int pokemonNumber) {
        jsonPlaceHolderApi.getPokemonItem(pokemonNumber).enqueue(new Callback<PokemonItem>() {
            @Override
            public void onResponse(@NonNull Call<PokemonItem> call,@NonNull Response<PokemonItem> response) {
                PokemonItem pokemonItem = response.body();
                if (pokemonItem != null) {
                    insertPokemonParameters(new PokemonParameters(
                            pokemonItem.getName(),
                            pokemonNumber,
                            pokemonItem.getStats().get(1).getBaseStat(),
                            pokemonItem.getStats().get(2).getBaseStat(),
                            pokemonItem.getStats().get(0).getBaseStat(),
                            pokemonItem.getHeight(),
                            pokemonItem.getWeight(),
                            getType(pokemonItem),
                            pokemonItem.getSprites().getBackDefault()));
                }
            }

            private String getType(PokemonItem pokemonItem) {
                StringBuilder types = new StringBuilder();
                List<Type> typeStats = pokemonItem.getTypes();
                for (int i = 0; i < typeStats.size(); i++) {
                    Type type = typeStats.get(i);
                    Type_ type_ = type.getType();
                    types.append(type_.getName()).append(" ");
                }
                return types.toString();
            }

            @Override
            public void onFailure(@NonNull Call<PokemonItem> call,@NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void insertPokemonParameters(PokemonParameters pokemonParameters) {
        new AddPokemonParametersListAsyncTask(appDatabase).execute(pokemonParameters);
    }

    private static class AddPokemonParametersListAsyncTask extends AsyncTask<PokemonParameters, Void, Void> {
        private AppDatabase appDatabase;

        public AddPokemonParametersListAsyncTask(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        protected Void doInBackground(PokemonParameters...lists) {
            appDatabase.pokemonParametersDao().insertPokemonParameters(lists[0]);
            return null;
        }
    }

    public void deletePokemonParametersList() {
        new DeletePokemonParametersListAsyncTask(appDatabase).execute();
    }

    private static class DeletePokemonParametersListAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase appDatabase;

        public DeletePokemonParametersListAsyncTask(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDatabase.pokemonParametersDao().deleteAllParameters();
            return null;
        }
    }

}
