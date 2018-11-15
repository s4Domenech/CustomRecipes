package com.example.s4domenech.customrecipes.datasource;

import android.content.Context;

import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.datasource.database.Recipe_Table;
import com.example.s4domenech.customrecipes.usecase.DB;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

public class DBImpl implements DB {

    Context context;

    public DBImpl(Context context) {
        this.context = context;
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    @Override
    public void saveRecipe(Recipe recipe, GeneralListener listener) {
        if (recipe.save()) {
            listener.onSuccess();
        } else {
            listener.onError(context.getString(R.string.error_save));
        }
    }

    @Override
    public void queryRecipes(QueryListener listener) {
        List<Recipe> recipes = new Select().from(Recipe.class).queryList();
        if (recipes.size() != 0) {
            listener.onSuccess(recipes);
        } else {
            listener.onError(context.getString(R.string.empty));
        }
    }

    @Override
    public void queryRecipes(String query, QueryListener listener) {
        List<Recipe> recipes = new Select().from(Recipe.class).queryList();
        if (recipes.size() != 0) {
            recipes = new Select().from(Recipe.class).where(Recipe_Table.name.like("%" + query + "%")).queryList();
            listener.onSuccess(recipes);
        } else {
            listener.onError(context.getString(R.string.empty));
        }
    }

    @Override
    public void deleteRecipe(Recipe recipe, GeneralListener listener) {
        if (SQLite.select().from(Recipe.class).where(Recipe_Table.id.eq(recipe.getId())).count() != 0) {
            SQLite.delete().from(Recipe.class).where(Recipe_Table.id.eq(recipe.getId())).async().execute();
            listener.onSuccess();
        } else {
            listener.onError(context.getString(R.string.error_delete));
        }
    }

    @Override
    public void updateRecipe(Recipe recipe, GeneralListener listener) {
        if (SQLite.select().from(Recipe.class).where(Recipe_Table.id.eq(recipe.getId())).count() != 0) {
            SQLite.update(Recipe.class)
                .set(Recipe_Table.imageBlob.eq(recipe.getImageBlob()), Recipe_Table.name.eq(recipe.getName()), Recipe_Table.steps.eq(recipe.getSteps()))
                .where(Recipe_Table.id.eq(recipe.getId()))
                .async()
                .execute();
            listener.onSuccess();
        } else {
            listener.onError(context.getString(R.string.error_update));
        }
    }
}