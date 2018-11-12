package com.example.s4domenech.customrecipes.usecase;

import android.graphics.Bitmap;

import com.raizlabs.android.dbflow.data.Blob;

public interface BlobConverter {
    Bitmap blobToBitmap(Blob blob);
    Blob bitmapToBlob(Bitmap bitmap);
    Bitmap byteToBitmap(byte[] bytes);
    Blob byteToBlob(byte[] bytes);
}