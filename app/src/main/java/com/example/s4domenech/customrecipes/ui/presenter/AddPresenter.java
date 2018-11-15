package com.example.s4domenech.customrecipes.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;
import com.example.s4domenech.customrecipes.usecase.CheckPermissions;
import com.example.s4domenech.customrecipes.usecase.DB;
import com.raizlabs.android.dbflow.data.Blob;

import static com.example.s4domenech.customrecipes.Data.REQUEST_CAMERA_PERMISSION;

public class AddPresenter extends Presenter<AddPresenter.View, AddPresenter.Navigator> {

    Context context;

    DB database;
    CheckPermissions checkPermissions;
    BlobConverter blobConverter;

    public AddPresenter(Context context, DB database,
                        CheckPermissions checkPermissions, BlobConverter blobConverter) {
        this.context = context;
        this.database = database;
        this.checkPermissions = checkPermissions;
        this.blobConverter = blobConverter;
    }

    @Override
    public void initialize() {

    }

    public void imageButtonClicked() {
        if (checkPermissions.isPermissionGranted(Manifest.permission.CAMERA)) {
            view.takePhoto();
        } else {
            view.showPermissions();
        }
    }

    public void acceptButtonClicked(Bitmap bitmap, String name, String steps) {
        Recipe recipe = new Recipe();

        Blob blobImage = blobConverter.bitmapToBlob(bitmap);

        recipe.setImageBlob(blobImage);
        recipe.setName(name);
        recipe.setSteps(steps);

        database.saveRecipe(recipe, new DB.GeneralListener() {
            @Override
            public void onSuccess() {
                view.showMessage(context.getString(R.string.success));
                navigator.close();
            }

            @Override
            public void onError(String msg) {
                view.showMessage(msg);
                navigator.close();
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

    public void cancelButtonClicked() {
        navigator.close();
    }

    public interface View {
        void showMessage(String error);
        void showPermissions();
        void takePhoto();
    }

    public interface Navigator {
        void close();
    }
}