package com.example.s4domenech.customrecipes.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.ui.presenter.AddPresenter;
import com.example.s4domenech.customrecipes.R;

public class AddActivity extends BaseActivity implements AddPresenter.view, AddPresenter.navigator {

    AddPresenter presenter;

    ImageButton ibRecipe;
    Button btnAccept, btnCancel;
    EditText etName, etSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new AddPresenter(this, new DBImpl(this));

        presenter.setView(this);
        presenter.setNavigator(this);

        ibRecipe = findViewById(R.id.ib_recipe);
        ibRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.ImageButtonClicked();
            }
        });

        btnAccept = findViewById(R.id.btn_ok);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)ibRecipe.getDrawable()).getBitmap();

                presenter.AcceptButtonClicked(bitmap, etName.getText().toString(), etSteps.getText().toString());
            }
        });

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.CancelButtonClicked();
            }
        });

        etName = findViewById(R.id.et_name);
        etSteps = findViewById(R.id.et_steps);

        presenter.initialize();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    public void close() {
        finish();
    }
}