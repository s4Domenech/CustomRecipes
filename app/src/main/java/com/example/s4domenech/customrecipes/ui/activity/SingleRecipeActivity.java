package com.example.s4domenech.customrecipes.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.BlobConverterImpl;
import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.ui.presenter.SingleRecipePresenter;

import static com.example.s4domenech.customrecipes.ui.activity.MainActivity.RESTART_ACTIVITY;

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
        tvSteps.setMovementMethod(new ScrollingMovementMethod());
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
    protected String titleToolbar() {
        return "Detail";
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
        intent.putExtra("id", recipe.getId());
        intent.putExtra("name", recipe.getName());
        intent.putExtra("steps", recipe.getSteps());
        intent.putExtra("blobImage", recipe.getImageBlob().getBlob());
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