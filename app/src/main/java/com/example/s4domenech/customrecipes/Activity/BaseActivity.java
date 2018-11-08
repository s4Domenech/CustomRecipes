package com.example.s4domenech.customrecipes.Activity;

import android.os.Bundle;

public abstract class BaseActivity extends android.app.Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
    }

    protected abstract int getLayoutId();
}