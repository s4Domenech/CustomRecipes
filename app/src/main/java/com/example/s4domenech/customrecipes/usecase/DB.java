package com.example.s4domenech.customrecipes.usecase;

import com.example.s4domenech.customrecipes.datasource.database.Recipe;

import java.util.List;

public interface DB {

    void saveRecipe(Recipe recipe, GeneralListener listener);
    void queryRecipes(QueryListener listener);
    void queryRecipes(String query, QueryListener listener);
    void deleteRecipe(Recipe recipe, GeneralListener listener);
    void updateRecipe(Recipe recipe, GeneralListener listener);

    interface QueryListener {
        void onSuccess(List<Recipe> recipes);
        void onError(String msg);
    }

    interface GeneralListener {
        void onSuccess();
        void onError(String msg);
    }
}