package com.example.s4domenech.customrecipes.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.s4domenech.customrecipes.datasource.BlobConverterImpl;
import com.example.s4domenech.customrecipes.datasource.DBImpl;
import com.example.s4domenech.customrecipes.datasource.device.CheckPermissionsImpl;
import com.example.s4domenech.customrecipes.ui.presenter.AddPresenter;
import com.example.s4domenech.customrecipes.R;

public class AddActivity extends BaseActivity implements AddPresenter.view, AddPresenter.navigator {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA_PERMISSION = 2;

    AddPresenter presenter;

    ImageButton ibRecipe;
    Button btnAccept, btnCancel;
    EditText etName, etSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new AddPresenter(this, new DBImpl(this), new CheckPermissionsImpl(this), new BlobConverterImpl());

        presenter.setView(this);
        presenter.setNavigator(this);

        ibRecipe = findViewById(R.id.ib_recipe);
        ibRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.imageButtonClicked();
            }
        });

        btnAccept = findViewById(R.id.btn_ok);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)ibRecipe.getDrawable()).getBitmap();

                presenter.acceptButtonClicked(bitmap, etName.getText().toString(), etSteps.getText().toString());
            }
        });

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.cancelButtonClicked();
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
    protected String titleToolbar() {
        return "Add";
    }

    @Override
    public void showMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPermissions() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ibRecipe.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void close() {
        setResult(RESULT_OK);
        finish();
    }
}