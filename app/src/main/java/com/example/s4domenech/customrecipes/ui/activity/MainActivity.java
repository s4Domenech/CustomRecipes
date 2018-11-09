package com.example.s4domenech.customrecipes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.ui.adapter.RecipeAdapter;
import com.example.s4domenech.customrecipes.ui.presenter.MainPresenter;
import com.example.s4domenech.customrecipes.R;

import java.util.List;

public class MainActivity extends BaseActivity implements MainPresenter.view, MainPresenter.navigator {

    static final int ADD_RECIPE = 1;

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
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void showRecipes(List<Recipe> recipes) {
        adapter = new RecipeAdapter(recipes, new RecipeAdapter.OnRecipeClicked() {
            @Override
            public void recipeClicked(Recipe recipe) {
                presenter.onRecipeClicked(recipe);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void navigateToAddActivity() {
        startActivityForResult(new Intent(this, AddActivity.class), ADD_RECIPE);
    }

    @Override
    public void navigateToDetailRecipeActivity(Recipe recipe) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " / " + resultCode);
        if (requestCode == ADD_RECIPE) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }
}