package com.example.s4domenech.customrecipes.datasource;

import android.content.Context;

import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.DB;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

public class DBImpl implements DB {

    public DBImpl(Context context) {
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    @Override
    public void saveRecipe(Recipe recipe, GeneralListener listener) {
        if (recipe.save()) {
            listener.onSuccess();
        } else {
            listener.onError("Couldn't save to database");
        }
    }

    @Override
    public void queryRecipes(QueryListener listener) {
        List<Recipe> recipes = new Select().from(Recipe.class).queryList();
        listener.onSuccess(recipes);
    }

    @Override
    public void deleteRecipe(Recipe recipe, GeneralListener listener) {

    }

    @Override
    public void updateRecipe(GeneralListener listener) {

    }
}