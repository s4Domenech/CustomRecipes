package com.example.s4domenech.customrecipes.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.s4domenech.customrecipes.Data;
import com.example.s4domenech.customrecipes.datasource.BlobConverterImpl;
import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.ui.adapter.RecipeAdapter;
import com.example.s4domenech.customrecipes.ui.presenter.MainPresenter;
import com.example.s4domenech.customrecipes.R;

import java.util.List;

import static com.example.s4domenech.customrecipes.Data.RESTART_ACTIVITY;

public class MainActivity extends BaseActivity implements MainPresenter.View, MainPresenter.Navigator {

    MainPresenter presenter;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this, new DBImpl(this));

        presenter.setView(this);
        presenter.setNavigator(this);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnInsert = findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                presenter.onAddButtonClicked();
            }
        });

        presenter.initialize();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String titleToolbar() {
        return getString(R.string.main_activity_name);
    }

    @Override
    public void showMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        adapter = new RecipeAdapter(recipes, new BlobConverterImpl(), new RecipeAdapter.OnRecipeClicked() {
            @Override
            public void recipeClicked(Recipe recipe) {
                presenter.onRecipeClicked(recipe);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void navigateToAddActivity() {
        startActivityForResult(new Intent(this, AddActivity.class), RESTART_ACTIVITY);
    }

    @Override
    public void navigateToDetailRecipeActivity(Recipe recipe) {
        Intent intent = new Intent(this, SingleRecipeActivity.class);
        intent.putExtra(Data.ID, recipe.getId());
        intent.putExtra(Data.NAME, recipe.getName());
        intent.putExtra(Data.STEPS, recipe.getSteps());
        intent.putExtra(Data.IMAGE, recipe.getImageBlob().getBlob());
        startActivityForResult(intent, RESTART_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESTART_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            EditText txtSearch = (searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
            txtSearch.setHint(getString(R.string.search));
            txtSearch.setHintTextColor(Color.BLACK);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    presenter.onSearchSubmit(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    presenter.onSearchSubmit(s);
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }
}