package com.example.s4domenech.customrecipes.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;
import com.example.s4domenech.customrecipes.usecase.DB;

public class SingleRecipePresenter extends Presenter<SingleRecipePresenter.view, SingleRecipePresenter.navigator> {

    Context context;

    BlobConverter blobConverter;
    DB database;

    Recipe recipe;

    public SingleRecipePresenter(Context context, BlobConverter blobConverter, DB database) {
        this.context = context;
        this.blobConverter = blobConverter;
        this.database = database;
    }

    @Override
    public void initialize() {

    }

    public void onExtrasReceived(Intent intent) {
        int id = intent.getExtras().getInt("id");
        String name = intent.getExtras().getString("name");
        String steps = intent.getExtras().getString("steps");
        byte[] imageBytes = intent.getExtras().getByteArray("blobImage");

        recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setSteps(steps);
        recipe.setImageBlob(blobConverter.byteToBlob(imageBytes));

        view.showImage(blobConverter.byteToBitmap(imageBytes));
        view.showName(name);
        view.showSteps(steps);
    }

    public void onEditButtonPressed() {
        navigator.navigateToEditActivity(recipe);
    }

    public void onDeleteButtonPressed() {
        database.deleteRecipe(recipe, new DB.GeneralListener() {
            @Override
            public void onSuccess() {
                view.showMessage("Deleted");
                navigator.close();
            }

            @Override
            public void onError(String msg) {
                view.showMessage(msg);
            }
        });
    }

    public interface view {
        void showMessage(String error);
        void showImage(Bitmap bitmap);
        void showName(String name);
        void showSteps(String steps);
    }

    public interface navigator {
        void navigateToEditActivity(Recipe recipe);
        void close();
    }
}