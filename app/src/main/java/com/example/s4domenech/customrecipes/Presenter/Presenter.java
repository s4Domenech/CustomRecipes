package com.example.s4domenech.customrecipes.Presenter;

public abstract class Presenter<T1, T2> {

    public abstract void initialize();

    protected T1 view;

    protected T2 navigator;

    public void setView(T1 v) {
        view = v;
    }

    public void setNavigator(T2 n) {
        navigator = n;
    }
}