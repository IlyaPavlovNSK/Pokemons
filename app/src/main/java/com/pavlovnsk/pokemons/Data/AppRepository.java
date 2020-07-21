package com.pavlovnsk.pokemons.Data;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    private MutableLiveData<List<Result>> resultList;
    private LiveData<List<PokemonParameters>> pokemonParametersList;

    private JSONPlaceHolderApi jsonPlaceHolderApi;
    private AppDatabase appDatabase;

    public AppRepository() {
        jsonPlaceHolderApi = App.getJSONPlaceHolderApi();
        appDatabase = App.getDb();

        resultList = new MutableLiveData<>();
        pokemonParametersList = new MutableLiveData<>();
    }

    public LiveData<List<PokemonParameters>> getPokemonParametersFromBd() {
        pokemonParametersList = appDatabase.pokemonParametersDao().getAll();
        return pokemonParametersList;
    }

    public LiveData<PokemonParameters> getPokemonParameterItemFromBd(int pokemonNumber) {
        return appDatabase.pokemonParametersDao().getById(pokemonNumber);
    }

    public void getPokemonParametersFromWeb(int limit, int offset) {
        jsonPlaceHolderApi.getPokemonList(limit, offset).enqueue(new Callback<PokemonList>() {
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
                if (response.body() != null) {
                    Utils.count = response.body().getCount();

                    Log.d("TAG", "onResponse: " + "size - " + response.body().getResults().size() + "limit - " + limit + ", offset - " + offset);

                    List <Result> results = response.body().getResults();
                    for (int i = 0; i < results.size(); i++) {
                        int pokemonNumber = getPokemonNumber(results.get(i));
                        loadPokemonParameters(pokemonNumber);
                    }
                }
            }
            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
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
            public void onResponse(Call<PokemonItem> call, Response<PokemonItem> response) {
                PokemonItem pokemonItem = response.body();
                insertPokemonParametersList(new PokemonParameters(
                        pokemonItem.getName(),
                        pokemonNumber,
                        pokemonItem.getStats().get(1).getBaseStat(),
                        pokemonItem.getStats().get(2).getBaseStat(),
                        pokemonItem.getStats().get(0).getBaseStat(),
                        pokemonItem.getHeight(),
                        pokemonItem.getWeight(),
                        getType(pokemonItem),
                        pokemonItem.getSprites().getBackDefault()));
                Log.d("TAG", "onResponse: " + pokemonItem.getName() + " " + pokemonNumber);
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
            public void onFailure(Call<PokemonItem> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



    public void insertPokemonParametersList(PokemonParameters pokemonParametersList) {
        new AddPokemonParametersListAsyncTask(appDatabase).execute(pokemonParametersList);
    }

    public void deleteAllParameters() {
        deletePokemonParametersList();
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
