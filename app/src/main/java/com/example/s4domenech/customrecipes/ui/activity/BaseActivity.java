package com.example.s4domenech.customrecipes.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.s4domenech.customrecipes.R;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(titleToolbar());
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
    }

    protected abstract int getLayoutId();

    protected abstract String titleToolbar();
}