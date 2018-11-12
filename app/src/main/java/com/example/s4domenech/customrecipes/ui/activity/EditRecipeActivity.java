package com.example.s4domenech.customrecipes.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.BlobConverterImpl;
import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.device.CheckPermissionsImpl;
import com.example.s4domenech.customrecipes.ui.presenter.EditRecipePresenter;

import static com.example.s4domenech.customrecipes.ui.activity.AddActivity.REQUEST_CAMERA_PERMISSION;
import static com.example.s4domenech.customrecipes.ui.activity.AddActivity.REQUEST_IMAGE_CAPTURE;

public class EditRecipeActivity extends BaseActivity implements EditRecipePresenter.view, EditRecipePresenter.navigator{

    EditRecipePresenter presenter;

    ImageButton ibRecipe;
    EditText etName, etSteps;
    Button btnOk, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new EditRecipePresenter(this, new BlobConverterImpl(), new DBImpl(this), new CheckPermissionsImpl(this));
        presenter.setView(this);
        presenter.setNavigator(this);

        ibRecipe = findViewById(R.id.ib_recipe);
        ibRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onImageButtonClicked();
            }
        });
        etName = findViewById(R.id.et_name);
        etSteps = findViewById(R.id.et_steps);
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)ibRecipe.getDrawable()).getBitmap();

                presenter.onAcceptButtonPressed(etName.getText().toString(), etSteps.getText().toString(), bitmap);
            }
        });
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCancelButtonPressed();
            }
        });

        presenter.initialize();
        presenter.onExtrasReceived(getIntent());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    protected String titleToolbar() {
        return "Edit";
    }

    @Override
    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void showPermissions() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ibRecipe.setImageBitmap((Bitmap) data.getExtras().get("data"));
        }
    }

    @Override
    public void writeName(String name) {
        etName.setText(name);
    }

    @Override
    public void writeSteps(String steps) {
        etSteps.setText(steps);
    }

    @Override
    public void putImage(Bitmap bitmap) {
        ibRecipe.setImageBitmap(bitmap);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void closeRefresh() {
        setResult(RESULT_OK);
        finish();
    }
}