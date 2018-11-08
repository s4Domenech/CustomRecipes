package com.example.s4domenech.customrecipes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.s4domenech.customrecipes.Adapter.RecipeAdapter;
import com.example.s4domenech.customrecipes.Model.Recipe;
import com.example.s4domenech.customrecipes.Presenter.MainPresenter;
import com.example.s4domenech.customrecipes.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements MainPresenter.view, MainPresenter.navigator {

    MainPresenter presenter;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this);

        presenter.setView(this);
        presenter.setNavigator(this);

        presenter.initialize();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

/*
        adapter = new RecipeAdapter(recipes, new RecipeAdapter.onRecipeClicked() {
            @Override
            public void recipeClicked(Recipe recipe) {
                presenter.onRecipeClicked();
            }
        });
        recyclerView.setAdapter(adapter);
 */
        btnInsert = findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddButtonClicked();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void navigateToAddActivity() {
        startActivity(new Intent(this, AddActivity.class));
    }

    @Override
    public void navigateToDetailRecipeActivity() {

    }
}