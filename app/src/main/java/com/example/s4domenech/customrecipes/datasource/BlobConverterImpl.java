package com.example.s4domenech.customrecipes.datasource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.s4domenech.customrecipes.usecase.BlobConverter;
import com.raizlabs.android.dbflow.data.Blob;

import java.io.ByteArrayOutputStream;

public class BlobConverterImpl implements BlobConverter {

    @Override
    public Bitmap blobToBitmap(Blob blob) {
        byte[] byteArray = blob.getBlob();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @Override
    public Blob bitmapToBlob(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        Blob blobImage = new Blob();
        blobImage.setBlob(bArray);
        return blobImage;
    }

    @Override
    public Bitmap byteToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @Override
    public Blob byteToBlob(byte[] bytes) {
        Blob blobImage = new Blob();
        blobImage.setBlob(bytes);
        return blobImage;
    }
}