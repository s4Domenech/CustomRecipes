package com.example.s4domenech.customrecipes.ui.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.BlobConverterImpl;
import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.ui.presenter.SingleRecipePresenter;

public class SingleRecipeActivity extends BaseActivity implements SingleRecipePresenter.view, SingleRecipePresenter.navigator {

    SingleRecipePresenter presenter;

    ImageView ivRecipe;
    TextView tvName, tvSteps;

    Button btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new SingleRecipePresenter(this, new BlobConverterImpl(), new DBImpl(this));
        presenter.setView(this);
        presenter.setNavigator(this);

        ivRecipe = findViewById(R.id.iv_recipe);
        tvName = findViewById(R.id.tv_name);
        tvSteps = findViewById(R.id.tv_steps);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEditButtonPressed();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDeleteButtonPressed();
            }
        });

        presenter.initialize();
        presenter.onExtrasReceived(getIntent());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_recipe;
    }

    @Override
    public void showImage(Bitmap bitmap) {
        ivRecipe.setImageBitmap(bitmap);
    }

    @Override
    public void showName(String name) {
        tvName.setText(name);
    }

    @Override
    public void showSteps(String steps) {
        tvSteps.setText(steps);
    }

    @Override
    public void close() {
        setResult(RESULT_OK);
        finish();
    }
}