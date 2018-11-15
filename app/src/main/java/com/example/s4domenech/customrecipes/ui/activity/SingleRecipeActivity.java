package com.example.s4domenech.customrecipes.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s4domenech.customrecipes.Data;
import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.BlobConverterImpl;
import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.ui.presenter.SingleRecipePresenter;

import static com.example.s4domenech.customrecipes.Data.RESTART_ACTIVITY;

public class SingleRecipeActivity extends BaseActivity implements
        SingleRecipePresenter.View,
        SingleRecipePresenter.Navigator {

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
        tvSteps.setMovementMethod(new ScrollingMovementMethod());
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                presenter.onEditButtonPressed();
            }
        });
        btnDelete.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
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
    protected String titleToolbar() {
        return getString(R.string.detail_activity_name);
    }

    @Override
    public void showMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
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
    public void navigateToEditActivity(Recipe recipe) {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra(Data.ID, recipe.getId());
        intent.putExtra(Data.NAME, recipe.getName());
        intent.putExtra(Data.STEPS, recipe.getSteps());
        intent.putExtra(Data.IMAGE, recipe.getImageBlob().getBlob());
        startActivityForResult(intent, RESTART_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESTART_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void close() {
        setResult(RESULT_OK);
        finish();
    }
}