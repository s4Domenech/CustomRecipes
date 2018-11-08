package com.example.s4domenech.customrecipes.Presenter;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class MainPresenter extends Presenter<MainPresenter.view, MainPresenter.navigator> {

    Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void initialize() {
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    public void onRecipeClicked() {

    }

    public void onAddButtonClicked() {
        navigator.navigateToAddActivity();
    }

    public interface view {

    }

    public interface navigator {
        void navigateToAddActivity();
        void navigateToDetailRecipeActivity();
    }
}