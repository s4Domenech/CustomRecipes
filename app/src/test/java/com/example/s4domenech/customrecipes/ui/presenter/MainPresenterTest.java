package com.example.s4domenech.customrecipes.ui.presenter;

import android.content.Context;

import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.DB;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class MainPresenterTest {

    MainPresenter presenter;

    @Mock Context mockContext;
    @Mock MainPresenter.View mockView;
    @Mock MainPresenter.Navigator mockNavigator;
    @Mock DB mockDB;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = createAMockedPresenter();
    }

    @Test
    public void shouldLoadRecipesWhenInitialize() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe());

        givenThereAreRecipes(recipes);

        presenter.initialize();

        verify(mockView).showRecipes(recipes);
    }

    @Test
    public void shouldReturnErrorWhenThereAreNoRecipes() {
        givenThereAreNoRecipes("Error");

        presenter.initialize();

        verify(mockView).showMessage("Error");
    }

    @Test
    public void shouldLoadRecipesWhenQuery() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe());

        givenThereAreRecipesWithQuery(recipes);

        presenter.onSearchSubmit("Query");

        verify(mockView).showRecipes(recipes);
    }

    @Test
    public void shouldReturnErrorWhenQueryAndThereAreNoRecipes() {
        givenThereAreNoRecipesWithQuery("Error");

        presenter.onSearchSubmit("Query");

        verify(mockView).showMessage("Error");
    }

    @Test
    public void shouldNavigateToDetailActivityWhenARecipeIsClicked() {
        Recipe recipe = new Recipe();

        presenter.onRecipeClicked(recipe);

        verify(mockNavigator).navigateToDetailRecipeActivity(recipe);
    }

    @Test
    public void shouldNavigateToAddActivityWhenAddButtonIsClicked() {
        presenter.onAddButtonClicked();

        verify(mockNavigator).navigateToAddActivity();
    }

    private void givenThereAreRecipesWithQuery(final List<Recipe> recipes) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.QueryListener listener = (DB.QueryListener) invocation.getArguments()[1];
                listener.onSuccess(recipes);
                return null;
            }
        }).when(mockDB).queryRecipes(any(String.class), any(DB.QueryListener.class));
    }

    private void givenThereAreNoRecipesWithQuery(final String error) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.QueryListener listener = (DB.QueryListener) invocation.getArguments()[1];
                listener.onError(error);
                return null;
            }
        }).when(mockDB).queryRecipes(any(String.class), any(DB.QueryListener.class));
    }

    private void givenThereAreRecipes(final List<Recipe> recipes) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.QueryListener listener = (DB.QueryListener) invocation.getArguments()[0];
                listener.onSuccess(recipes);
                return null;
            }
        }).when(mockDB).queryRecipes(any(DB.QueryListener.class));
    }

    private void givenThereAreNoRecipes(final String error) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                DB.QueryListener listener = (DB.QueryListener) invocation.getArguments()[0];
                listener.onError(error);
                return null;
            }
        }).when(mockDB).queryRecipes(any(DB.QueryListener.class));
    }

    MainPresenter createAMockedPresenter() {
        MainPresenter presenter = new MainPresenter(mockContext, mockDB);
        presenter.setView(mockView);
        presenter.setNavigator(mockNavigator);
        return presenter;
    }
}