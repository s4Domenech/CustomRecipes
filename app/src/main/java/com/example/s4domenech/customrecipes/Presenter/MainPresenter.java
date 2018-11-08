package com.example.s4domenech.customrecipes.Presenter;

import android.content.Context;

public class MainPresenter extends Presenter<MainPresenter.view, MainPresenter.navigator> {

    Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void initialize() {

    }

    public void OnRecipeClicked() {

    }

    public interface view {

    }

    public interface navigator {
        void navigateToAddActivity();
        void navigateToDetailRecipeActivity();
    }
}