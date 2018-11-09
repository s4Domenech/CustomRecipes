package com.example.s4domenech.customrecipes.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.DB;
import com.raizlabs.android.dbflow.data.Blob;

import java.io.ByteArrayOutputStream;

public class AddPresenter extends Presenter<AddPresenter.view, AddPresenter.navigator> {

    Context context;

    DB database;

    public AddPresenter(Context context, DBImpl database) {
        this.context = context;
        this.database = database;
    }

    @Override
    public void initialize() {

    }

    public void imageButtonClicked() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            view.takePhoto();
        } else {
            view.showPermissions();
        }
    }

    public void acceptButtonClicked(Bitmap bitmap, String name, String steps) {
        Recipe recipe = new Recipe();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        Blob blobImage = new Blob();
        blobImage.setBlob(bArray);

        recipe.setImageBlob(blobImage);
        recipe.setName(name);
        recipe.setSteps(steps);

        database.saveRecipe(recipe, new DB.GeneralListener() {
            @Override
            public void onSuccess() {
                System.out.println("Nice!");
                navigator.close();
            }

            @Override
            public void onError(String msg) {
                System.out.println("Error");
                navigator.close();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int REQUEST_CAMERA_PERMISSION) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                view.takePhoto();
            }
        }
    }

    public void cancelButtonClicked() {
        navigator.close();
    }

    public interface view {
        void showPermissions();
        void takePhoto();
    }

    public interface  navigator {
        void close();
    }
}