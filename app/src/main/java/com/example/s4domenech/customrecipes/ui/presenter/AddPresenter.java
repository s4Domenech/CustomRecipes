package com.example.s4domenech.customrecipes.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;

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

    public void ImageButtonClicked() {

    }

    public void AcceptButtonClicked(Bitmap bitmap, String name, String steps) {
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

    public void CancelButtonClicked() {
        navigator.close();
    }

    public interface view {

    }

    public interface  navigator {
        void close();
    }
}