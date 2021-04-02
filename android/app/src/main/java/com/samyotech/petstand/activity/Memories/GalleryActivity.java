package com.samyotech.petstand.activity.Memories;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.ImageGalleryAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.GalleryDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 20/2/18.
 */

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageGalleryAdapter imageGalleryAdapter;
    private ArrayList<GalleryDTO> galleryDTOList;
    private String TAG = GalleryActivity.class.getSimpleName();
    private RecyclerView.LayoutManager layoutManager;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(GalleryActivity.this);
        setContentView(R.layout.activity_space_gallery);
        recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        llBack = (LinearLayout) findViewById(R.id.llBack);
        prefrence = SharedPrefrence.getInstance(GalleryActivity.this);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);

        petListImage();

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void petListImage() {
        ProjectUtils.showProgressDialog(GalleryActivity.this, true, "Please Wait!!");
        new HttpsRequest(Consts.GET_MEMORIES, getparm(), GalleryActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        galleryDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<GalleryDTO>>() {
                        }.getType();
                        galleryDTOList = (ArrayList<GalleryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        if (galleryDTOList.size() != 0) {
                            imageGalleryAdapter = new ImageGalleryAdapter(GalleryActivity.this, galleryDTOList);

                            layoutManager = new GridLayoutManager(GalleryActivity.this, 3);

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(imageGalleryAdapter);
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(GalleryActivity.this, msg);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }

}
