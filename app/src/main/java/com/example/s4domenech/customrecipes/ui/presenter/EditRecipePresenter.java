package com.example.s4domenech.customrecipes.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.s4domenech.customrecipes.Data;
import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;
import com.example.s4domenech.customrecipes.usecase.CheckPermissions;
import com.example.s4domenech.customrecipes.usecase.DB;

import static com.example.s4domenech.customrecipes.Data.REQUEST_CAMERA_PERMISSION;

public class EditRecipePresenter extends Presenter<EditRecipePresenter.View, EditRecipePresenter.Navigator> {

    Context context;

    BlobConverter blobConverter;
    DB database;
    CheckPermissions checkPermissions;

    Recipe recipe;

    public EditRecipePresenter(Context context, BlobConverter blobConverter,
                               DB database, CheckPermissions checkPermissions) {
        this.context = context;
        this.blobConverter = blobConverter;
        this.database = database;
        this.checkPermissions = checkPermissions;
    }

    @Override
    public void initialize() {
        recipe = new Recipe();
    }

    public void onExtrasReceived(Intent intent) {
        int id = intent.getExtras().getInt(Data.ID);
        String name = intent.getExtras().getString(Data.NAME);
        String steps = intent.getExtras().getString(Data.STEPS);
        byte[] imageBytes = intent.getExtras().getByteArray(Data.IMAGE);

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
                view.showMessage(context.getString(R.string.updated));
                navigator.closeRefresh();
            }

            @Override
            public void onError(String msg) {
                view.showMessage(msg);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                view.takePhoto();
            }
        }
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

    public interface View {
        void showMessage(String error);
        void takePhoto();
        void showPermissions();
        void writeName(String name);
        void writeSteps(String steps);
        void putImage(Bitmap bitmap);
    }

    public interface Navigator {
        void closeRefresh();
        void close();
    }
}