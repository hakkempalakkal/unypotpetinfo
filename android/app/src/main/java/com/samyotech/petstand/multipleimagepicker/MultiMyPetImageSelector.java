package com.samyotech.petstand.multipleimagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.samyotech.petstand.R;
import com.samyotech.petstand.utils.Consts;

import java.util.ArrayList;


public class MultiMyPetImageSelector {

    public static final String EXTRA_RESULT = MultiImageSelectorActivity.EXTRA_RESULT;

    private boolean mShowCamera = true;
    private int mMaxCount = 9;
    private int mMinCount = 1;
    private int mMode = MultiImageSelectorActivity.MODE_MULTI;
    private ArrayList<String> mOriginData;
    private static MultiMyPetImageSelector sSelector;
    String pet_id = "";

    private MultiMyPetImageSelector() {
    }

    public static MultiMyPetImageSelector create() {
        if (sSelector == null) {
            sSelector = new MultiMyPetImageSelector();
        }
        return sSelector;
    }

    public MultiMyPetImageSelector showCamera(boolean show) {
        mShowCamera = show;
        return sSelector;
    }

    public MultiMyPetImageSelector count(int count) {
        mMaxCount = count;
        return sSelector;
    }

    public MultiMyPetImageSelector minCount(int count) {
        mMinCount = count;
        return sSelector;
    }

    public MultiMyPetImageSelector single() {
        mMode = MultiImageSelectorActivity.MODE_SINGLE;
        return sSelector;
    }

    public MultiMyPetImageSelector multi() {
        mMode = MultiImageSelectorActivity.MODE_MULTI;
        return sSelector;
    }

    public MultiMyPetImageSelector origin(ArrayList<String> images) {
        mOriginData = images;
        return sSelector;
    }

    public void start(Activity activity, int requestCode, String pet_id) {
        final Context context = activity;
        this.pet_id = pet_id;
        if (hasPermission(context)) {
            activity.startActivityForResult(createIntent(context), requestCode);
        } else {
            Toast.makeText(context, R.string.error_no_permission, Toast.LENGTH_SHORT).show();
        }
    }

    public void start(Fragment fragment, int requestCode) {
        final Context context = fragment.getContext();
        if (hasPermission(context)) {
            fragment.startActivityForResult(createIntent(context), requestCode);
        } else {
            Toast.makeText(context, R.string.error_no_permission, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Permission was added in API Level 16
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private Intent createIntent(Context context) {
        Intent intent = new Intent(context, MultiMyPetImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, mShowCamera);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, mMaxCount);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MIN_COUNT, mMinCount);
        intent.putExtra(Consts.PET_ID, pet_id);
        if (mOriginData != null) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData);
        }
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mMode);
        return intent;
    }
}
