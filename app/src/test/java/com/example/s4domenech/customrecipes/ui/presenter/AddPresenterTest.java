package com.example.s4domenech.customrecipes.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;
import com.example.s4domenech.customrecipes.usecase.CheckPermissions;
import com.example.s4domenech.customrecipes.usecase.DB;

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

public class AddPresenterTest {

    AddPresenter presenter;

    @Mock AddPresenter.View mockView;
    @Mock AddPresenter.Navigator mockNavigator;
    @Mock Context mockContext;
    @Mock DB mockDB;
    @Mock CheckPermissions mockCheckPermissions;
    @Mock BlobConverter mockBlobConverter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = createAMockedPresenter();
    }

    @Test
    public void shouldShowPermissionOnImageClicked() {
        presenter.imageButtonClicked();

        verify(mockView).showPermissions();
    }

    @Test
    public void shouldTakePhotoOnImageClicked() {
        givenPermission(Manifest.permission.CAMERA);

        presenter.imageButtonClicked();

        verify(mockView).takePhoto();
    }

    @Test
    public void shouldShowMessageWhenAddedRecipe() {
        givenAllStringsAreMocked();
        givenAddRecipeSuccess();

        presenter.acceptButtonClicked(any(Bitmap.class), "Name Recipe", "Steps of Recipe");

        verify(mockView).showMessage("Success");
    }

    @Test
    public void shouldCloseActivityWhenAddedRecipe() {
        givenAllStringsAreMocked();
        givenAddRecipeSuccess();

        presenter.acceptButtonClicked(any(Bitmap.class), "Name Recipe", "Steps of Recipe");

        verify(mockNavigator).close();
    }

    @Test
    public void shouldShowErrorWhenDBReturnsErrorOnAdd() {
        givenAddRecipeError("Error");

        presenter.acceptButtonClicked(any(Bitmap.class), "Name Recipe", "Steps of Recipe");

        verify(mockView).showMessage("Error");
    }

    @Test
    public void shouldCloseActivityWhenDBReturnsErrorOnAdd() {
        givenAddRecipeError("Error");

        presenter.acceptButtonClicked(any(Bitmap.class), "Name Recipe", "Steps of Recipe");

        verify(mockNavigator).close();
    }

    @Test
    public void shouldFinishActivityWhenCancelButtonIsClicked() {
        presenter.cancelButtonClicked();

        verify(mockNavigator).close();
    }

    private void givenAllStringsAreMocked() {
        when(mockContext.getString(R.string.success)).thenReturn("Success");
    }

    private void givenAddRecipeError(final String error) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.GeneralListener listener = (DB.GeneralListener) invocation.getArguments()[1];
                listener.onError(error);
                return null;
            }
        }).when(mockDB).saveRecipe(any(Recipe.class), any(DB.GeneralListener.class));
    }

    private void givenAddRecipeSuccess() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.GeneralListener listener = (DB.GeneralListener) invocation.getArguments()[1];
                listener.onSuccess();
                return null;
            }
        }).when(mockDB).saveRecipe(any(Recipe.class), any(DB.GeneralListener.class));
    }

    private void givenPermission(String permission) {
        when(mockCheckPermissions.isPermissionGranted(permission)).thenReturn(true);
    }

    private AddPresenter createAMockedPresenter() {
        AddPresenter presenter = new AddPresenter(mockContext, mockDB, mockCheckPermissions, mockBlobConverter);
        presenter.setView(mockView);
        presenter.setNavigator(mockNavigator);
        return presenter;
    }
}