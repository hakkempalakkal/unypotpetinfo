package com.samyotech.petstand.activity.Memories;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.ImagesAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.multipleimagepicker.MultiImageSelector;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddMultipleImage extends AppCompatActivity implements View.OnClickListener {
    private String TAG = AddMultipleImage.class.getSimpleName();
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerViewImages;
    private GridLayoutManager gridLayoutManager;
    CustomTextView TVimage;
    ImageView ivback;

    public  ArrayList<String> mSelectedImagesList = new ArrayList<>();

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;
    private final int REQUEST_IMAGE = 301;

    private MultiImageSelector mMultiImageSelector;
    private ImagesAdapter mImagesAdapter;

    HashMap<String, File> Imagelist;
    int count = 0;
    File file;
    private final int MAX_IMAGE_SELECTION_LIMIT = 5;
    int i = 5;
    private HashMap<String, String> values = new HashMap<>();
    private PetListDTO petListDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multiple_image);

        if (getIntent().hasExtra(Consts.PET_PROFILE)) {
            petListDTO = (PetListDTO) getIntent().getSerializableExtra(Consts.PET_PROFILE);

            values.put(Consts.USER_ID, petListDTO.getUser_id());
            values.put(Consts.PET_ID, petListDTO.getId());
            values.put(Consts.DESCRIPTION, "sfsfsdfds");
        }


        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        recyclerViewImages = (RecyclerView) findViewById(R.id.recycler_view_images);
        TVimage = (CustomTextView) findViewById(R.id.TVimage);
        ivback = (ImageView) findViewById(R.id.ivback);


        gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(gridLayoutManager);
        mMultiImageSelector = MultiImageSelector.create();



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAndRequestPermissions()) {
                    mMultiImageSelector.showCamera(true);
                    mMultiImageSelector.count(i);
                    mMultiImageSelector.multi();
                    mMultiImageSelector.origin(mSelectedImagesList);
                    mMultiImageSelector.start(AddMultipleImage.this, REQUEST_IMAGE);

                }
            }
        });


        TVimage.setOnClickListener(this);
        ivback.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivback:
                finish();
                break;
            case R.id.TVimage:

                count = mSelectedImagesList.size();
                for (int i = 0; i < mSelectedImagesList.size(); i++) {
                    file = new File(mSelectedImagesList.get(i));
                    Imagelist.put(Consts.USERFILES + i, file);
                }
                try {
                    if (mSelectedImagesList.size() != 0) {

                        uploadimage();
                        Log.e("hashmap", Imagelist.toString());
                        Log.e("count", String.valueOf(count));
                    } else {
                        ProjectUtils.showLong(AddMultipleImage.this, "Please select image");
                    }
                } catch (Exception e) {
                    ProjectUtils.showLong(AddMultipleImage.this, "Please select image");
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            try {
                Imagelist = new HashMap<>();
                mSelectedImagesList = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);


                mImagesAdapter = new ImagesAdapter(this, mSelectedImagesList);
                recyclerViewImages.setAdapter(mImagesAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadimage() {
        values.put("count", count + "");
        ProjectUtils.showProgressDialog(AddMultipleImage.this, true, "Please wait...");
        new HttpsRequest(Consts.IMAGE_UPLOAD_FOR_LIST, values, Imagelist, AddMultipleImage.this).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(AddMultipleImage.this, msg);
                    finish();

                } else {
                    ProjectUtils.showLong(AddMultipleImage.this, msg);
                }

            }
        });

/*
        AndroidNetworking.upload(Consts.BASE_URL + Consts.IMAGE_UPLOAD_FOR_LIST)
                .addMultipartParameter(values)
                .addMultipartFile(Imagelist)
                .setTag("uploadTest")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        ProjectUtils.pauseProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String message = jsonObject.getString("message");
                            boolean status = jsonObject.getBoolean("status");

                            if (status) {
                                //String image = jsonObject1.getString("image");
                                ProjectUtils.showLong(AddMultipleImage.this, message);

                            } else {
                                ProjectUtils.pauseProgressDialog();
                                ProjectUtils.showLong(AddMultipleImage.this, message);
                            }

                        } catch (JSONException e) {

                            ProjectUtils.pauseProgressDialog();
                            e.printStackTrace();
                            Log.e("Exception", e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                    }
                });
*/
    }


    private boolean checkAndRequestPermissions() {
        int externalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            floatingActionButton.performClick();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
