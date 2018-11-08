package com.example.s4domenech.customrecipes.Presenter;

import android.content.Context;

public class AddPresenter extends Presenter<AddPresenter.view, AddPresenter.navigator> {

    Context context;

    public AddPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void initialize() {

    }

    public void ImageButtonClicked() {

    }

    public void AcceptButtonClicked() {

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