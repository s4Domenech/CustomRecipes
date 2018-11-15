package com.example.s4domenech.customrecipes.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.s4domenech.customrecipes.Data;
import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;
import com.example.s4domenech.customrecipes.usecase.CheckPermissions;
import com.example.s4domenech.customrecipes.usecase.DB;
import com.raizlabs.android.dbflow.data.Blob;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditRecipePresenterTest {

    EditRecipePresenter presenter;

    @Mock EditRecipePresenter.View mockView;
    @Mock EditRecipePresenter.Navigator mockNavigator;
    @Mock Context mockContext;
    @Mock BlobConverter mockBlobConverter;
    @Mock DB mockDB;
    @Mock CheckPermissions mockCheckPermissions;
    @Mock Intent mockIntent;
    @Mock Bundle mockBundle;
    @Mock Bitmap mockBitmap;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = createAMockedPresenter();
    }

    @Test
    public void shouldWriteRecipeDataOnExtrasReceived() {
        givenTheExtra(Data.ID, 1);
        givenTheExtra(Data.NAME, "Name");
        givenTheExtra(Data.STEPS, "Steps");
        givenTheExtra(Data.IMAGE, new byte[0]);
        givenBlobConvertedFromByte();
        givenBitmapConvertedFromByte();

        presenter.initialize();
        presenter.onExtrasReceived(mockIntent);

        verify(mockView).writeName("Name");
        verify(mockView).writeSteps("Steps");
        verify(mockView).putImage(mockBitmap);
    }

    @Test
    public void shouldShowMessageAndCloseActivityOnAcceptButtonPressed() {
        givenBlobConvertedFromBitmap();
        givenAllStringsAreMocked();
        givenUpdateRecipeSuccess();

        presenter.initialize();
        presenter.onAcceptButtonPressed("new name", "new steps", mockBitmap);

        verify(mockView).showMessage("Updated");
        verify(mockNavigator).closeRefresh();
    }

    @Test
    public void shouldShowErrorWhenDBReturnsErrorOnAcceptButtonPressed() {
        givenBlobConvertedFromBitmap();
        givenUpdateRecipeError("Error");

        presenter.initialize();
        presenter.onAcceptButtonPressed("new name", "new steps", mockBitmap);

        verify(mockView).showMessage("Error");
    }

    @Test
    public void shouldCloseActivityWhenCancelButtonIsPressed() {
        presenter.onCancelButtonPressed();

        verify(mockNavigator).close();
    }

    @Test
    public void shouldShowCheckPermissionsWhenImageButtonIsClicked() {
        presenter.onImageButtonClicked();

        verify(mockView).showPermissions();
    }

    @Test
    public void shouldTakePhotoWhenImageButtonIsClickedAndGrantedPermissions() {
        givenThePermissions(Manifest.permission.CAMERA);

        presenter.onImageButtonClicked();

        verify(mockView).takePhoto();
    }

    private void givenThePermissions(String permission) {
        when(mockCheckPermissions.isPermissionGranted(permission)).thenReturn(true);
    }

    private void givenBlobConvertedFromByte() {
        when(mockBlobConverter.byteToBlob(any(byte[].class))).thenReturn(new Blob());
    }

    private void givenBitmapConvertedFromByte() {
        when(mockBlobConverter.byteToBitmap(any(byte[].class))).thenReturn(mockBitmap);
    }

    private void givenBlobConvertedFromBitmap() {
        when(mockBlobConverter.bitmapToBlob(mockBitmap)).thenReturn(new Blob());
    }

    private void givenUpdateRecipeSuccess() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.GeneralListener listener = (DB.GeneralListener) invocation.getArguments()[1];
                listener.onSuccess();
                return null;
            }
        }).when(mockDB).updateRecipe(any(Recipe.class), any(DB.GeneralListener.class));
    }

    private void givenUpdateRecipeError(final String error) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.GeneralListener listener = (DB.GeneralListener) invocation.getArguments()[1];
                listener.onError(error);
                return null;
            }
        }).when(mockDB).updateRecipe(any(Recipe.class), any(DB.GeneralListener.class));
    }

    private void givenTheExtra(String extra, int result) {
        when(mockIntent.getExtras()).thenReturn(mockBundle);
        when(mockIntent.getExtras().getInt(extra)).thenReturn(result);
    }

    private void givenTheExtra(String extra, String result) {
        when(mockIntent.getExtras()).thenReturn(mockBundle);
        when(mockIntent.getExtras().getString(extra)).thenReturn(result);
    }

    private void givenTheExtra(String extra, byte[] result) {
        when(mockIntent.getExtras()).thenReturn(mockBundle);
        when(mockIntent.getExtras().getByteArray(extra)).thenReturn(result);
    }

    private void givenAllStringsAreMocked() {
        when(mockContext.getString(R.string.updated)).thenReturn("Updated");
    }

    private EditRecipePresenter createAMockedPresenter() {
        EditRecipePresenter presenter = new EditRecipePresenter(
                mockContext, mockBlobConverter, mockDB, mockCheckPermissions
        );
        presenter.setView(mockView);
        presenter.setNavigator(mockNavigator);
        return presenter;
    }

}