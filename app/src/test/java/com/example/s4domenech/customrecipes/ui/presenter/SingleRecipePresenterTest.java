package com.example.s4domenech.customrecipes.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.s4domenech.customrecipes.Data;
import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;
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

public class SingleRecipePresenterTest {

    SingleRecipePresenter presenter;

    @Mock SingleRecipePresenter.View mockView;
    @Mock SingleRecipePresenter.Navigator mockNavigator;
    @Mock Context mockContext;
    @Mock BlobConverter mockBlobConverter;
    @Mock DB mockDB;
    @Mock Intent mockIntent;
    @Mock Bundle mockBundle;
    @Mock Bitmap mockBitmap;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = createAMockedPresenter();
    }

    @Test
    public void shouldShowRecipeDataOnStart() {
        givenTheExtra(Data.ID, 1);
        givenTheExtra(Data.NAME, "Name");
        givenTheExtra(Data.STEPS, "Steps");
        givenTheExtra(Data.IMAGE, new byte[0]);
        givenBlobConvertedFromByte();
        givenBitmapConvertedFromByte();

        presenter.initialize();
        presenter.onExtrasReceived(mockIntent);

        verify(mockView).showName("Name");
        verify(mockView).showSteps("Steps");
        verify(mockView).showImage(mockBitmap);
    }

    @Test
    public void shouldNavigateToDetailActivityWhenEditButtonIsClicked() {
        presenter.initialize();
        presenter.onEditButtonPressed();

        verify(mockNavigator).navigateToEditActivity(any(Recipe.class));
    }

    @Test
    public void shouldShowMessageAndCloseActivityWhenDeleteButtonIsClicked() {
        givenAllStringsAreMocked();
        givenDeleteRecipeSuccess();

        presenter.initialize();
        presenter.onDeleteButtonPressed();

        verify(mockView).showMessage("Deleted");
        verify(mockNavigator).close();
    }

    private void givenAllStringsAreMocked() {
        when(mockContext.getString(R.string.deleted)).thenReturn("Deleted");
    }

    private void givenDeleteRecipeSuccess() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.GeneralListener listener = (DB.GeneralListener) invocation.getArguments()[1];
                listener.onSuccess();
                return null;
            }
        }).when(mockDB).deleteRecipe(any(Recipe.class), any(DB.GeneralListener.class));
    }

    private void givenBlobConvertedFromByte() {
        when(mockBlobConverter.byteToBlob(any(byte[].class))).thenReturn(new Blob());
    }

    private void givenBitmapConvertedFromByte() {
        when(mockBlobConverter.byteToBitmap(any(byte[].class))).thenReturn(mockBitmap);
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

    private SingleRecipePresenter createAMockedPresenter() {
        SingleRecipePresenter presenter = new SingleRecipePresenter(mockContext, mockBlobConverter, mockDB);
        presenter.setView(mockView);
        presenter.setNavigator(mockNavigator);
        return presenter;
    }
}