package com.example.s4domenech.customrecipes.ui.presenter;

import android.content.Context;

import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.DB;

import java.util.List;

public class MainPresenter extends Presenter<MainPresenter.view, MainPresenter.navigator> {

    Context context;
    DB database;

    public MainPresenter(Context context, DBImpl database) {
        this.context = context;
        this.database = database;
    }

    @Override
    public void initialize() {
        LoadRecipes();
    }

    void LoadRecipes() {
        database.queryRecipes(new DB.QueryListener() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                view.showRecipes(recipes);
            }

            @Override
            public void onError(String msg) {
                view.showMessage(msg);
            }
        });
    }

    public void onSearchSubmit(String query) {
        database.queryRecipes(query, new DB.QueryListener() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                view.showRecipes(recipes);
            }

            @Override
            public void onError(String msg) {
                view.showMessage(msg);
            }
        });
    }

    public void onRecipeClicked(Recipe recipe) {
        navigator.navigateToDetailRecipeActivity(recipe);
    }

    public void onAddButtonClicked() {
        navigator.navigateToAddActivity();
    }

    public interface view {
        void showMessage(String error);
        void showRecipes(List<Recipe> recipes);
    }

    public interface navigator {
        void navigateToAddActivity();
        void navigateToDetailRecipeActivity(Recipe recipe);
    }
}