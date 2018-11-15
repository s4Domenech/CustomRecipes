package com.example.s4domenech.customrecipes.datasource.device;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.example.s4domenech.customrecipes.usecase.CheckPermissions;

public class CheckPermissionsImpl implements CheckPermissions {

    Context context;

    public CheckPermissionsImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}