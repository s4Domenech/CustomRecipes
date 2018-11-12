package com.example.s4domenech.customrecipes.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;
import com.example.s4domenech.customrecipes.usecase.CheckPermissions;
import com.example.s4domenech.customrecipes.usecase.DB;

public class EditRecipePresenter extends Presenter<EditRecipePresenter.view, EditRecipePresenter.navigator> {

    Context context;

    BlobConverter blobConverter;
    DB database;
    CheckPermissions checkPermissions;

    Recipe recipe;

    public EditRecipePresenter(Context context, BlobConverter blobConverter, DB database, CheckPermissions checkPermissions) {
        this.context = context;
        this.blobConverter = blobConverter;
        this.database = database;
        this.checkPermissions = checkPermissions;
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

        view.writeName(name);
        view.writeSteps(steps);
        view.putImage(blobConverter.byteToBitmap(imageBytes));
    }

    public void onAcceptButtonPressed(String name, String steps, Bitmap image) {
        recipe.setName(name);
        recipe.setSteps(steps);
        recipe.setImageBlob(blobConverter.bitmapToBlob(image));

        database.updateRecipe(recipe, new DB.GeneralListener() {
            @Override
            public void onSuccess() {
                navigator.closeRefresh();
            }

            @Override
            public void onError(String msg) {
                System.out.println(msg);
            }
        });
    }

    public void onCancelButtonPressed() {
        navigator.close();
    }

    public void onImageButtonClicked() {
        if (checkPermissions.isPermissionGranted(Manifest.permission.CAMERA)) {
            view.takePhoto();
        } else {
            view.showPermissions();
        }
    }

    public interface view {
        void takePhoto();
        void showPermissions();
        void writeName(String name);
        void writeSteps(String steps);
        void putImage(Bitmap bitmap);
    }

    public interface navigator {
        void closeRefresh();
        void close();
    }
}