package com.pavlovnsk.pokemons;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pavlovnsk.pokemons.Fragments.ItemFragment;
import com.pavlovnsk.pokemons.Fragments.ListFragment;
import com.pavlovnsk.pokemons.POJO.Result;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements ListFragment.ItemOnClickListListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static long back_pressed;
    private CheckBox attackCheckBox;
    private CheckBox defenseCheckBox;
    private CheckBox hpCheckBox;
    private Button updateButton;

    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attackCheckBox = findViewById(R.id.CB_attack);
        defenseCheckBox = findViewById(R.id.CB_defense);
        hpCheckBox = findViewById(R.id.CB_hp);
        updateButton = findViewById(R.id.B_update);

        attackCheckBox.setOnCheckedChangeListener(this);
        defenseCheckBox.setOnCheckedChangeListener(this);
        hpCheckBox.setOnCheckedChangeListener(this);
        updateButton.setOnClickListener(this);

        listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        listFragment.setOnClickListListener(MainActivity.this);

        LinearLayout btnSheetLL = findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(btnSheetLL);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull final View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheet.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BottomSheetBehavior bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
                            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }, 1500);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("TAG", "onBackPressed: " + getSupportFragmentManager().getBackStackEntryCount());
        } else {
            Log.d("TAG", "onBackPressed: " + getSupportFragmentManager().getBackStackEntryCount());
            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
                Toast.makeText(getBaseContext(), getText(R.string.click_again_to_exit), Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onItemClick(int pokemonNumber) {
        ItemFragment itemFragment = new ItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragment).addToBackStack(null).commit();

        listFragment.loadPokemonParameters(pokemonNumber, itemFragment);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.B_update:
                updateList();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.CB_attack:
                if (attackCheckBox.isChecked()) {
                    showMaxAttack();
                }
                break;

            case R.id.CB_defense:
                if (defenseCheckBox.isChecked()) {
                    showMaxDefense();
                }
                break;

            case R.id.CB_hp:
                if (hpCheckBox.isChecked()) {
                    showMaxHp();
                }
                break;
        }
    }

    private void updateList() {
        listFragment.getPokemonRecyclerViewAdapter().getPokemonList().clear();
        listFragment.getDetailsPokemon().clear();
        int itemCount = Utils.count-listFragment.getLimit()-1;
        int randomItem = (int) (Math.random()*++itemCount);
        listFragment.observeViewModel(listFragment.getLimit(), randomItem);
        listFragment.getPokemonRecyclerViewAdapter().notifyDataSetChanged();
        listFragment.setOffset(0);
    }

    private void showMaxAttack() {
        Result result = Collections.max(listFragment.getDetailsPokemon().entrySet(), (e1, e2) -> e1.getValue().get(1).getBaseStat() - e2.getValue().get(1).getBaseStat()).getKey();
        ArrayList<Result> results = (ArrayList<Result>) listFragment.getPokemonRecyclerViewAdapter().getPokemonList();
        int i = results.indexOf(result);
        Result item = results.remove(i);
        results.add(0, item);
        listFragment.getPokemonRecyclerViewAdapter().notifyDataSetChanged();
        listFragment.getPokemonRecyclerView().smoothScrollToPosition(0);
    }

    private void showMaxHp() {
        Result result = Collections.max(listFragment.getDetailsPokemon().entrySet(), (e1, e2) -> e1.getValue().get(0).getBaseStat() - e2.getValue().get(0).getBaseStat()).getKey();
        ArrayList<Result> results = (ArrayList<Result>) listFragment.getPokemonRecyclerViewAdapter().getPokemonList();
        int i = results.indexOf(result);
        Result item = results.remove(i);
        results.add(0, item);
        listFragment.getPokemonRecyclerViewAdapter().notifyDataSetChanged();
        listFragment.getPokemonRecyclerView().smoothScrollToPosition(0);
    }

    private void showMaxDefense() {
        Result result = Collections.max(listFragment.getDetailsPokemon().entrySet(), (e1, e2) -> e1.getValue().get(2).getBaseStat() - e2.getValue().get(2).getBaseStat()).getKey();
        ArrayList<Result> results = (ArrayList<Result>) listFragment.getPokemonRecyclerViewAdapter().getPokemonList();
        int i = results.indexOf(result);
        Result item = results.remove(i);
        results.add(0, item);
        listFragment.getPokemonRecyclerViewAdapter().notifyDataSetChanged();
        listFragment.getPokemonRecyclerView().smoothScrollToPosition(0);
    }
}