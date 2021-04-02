package com.samyotech.petstand.activity.nearbypetlist;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.PetListFriendAda;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.NearByUserDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 21/2/18.
 */

public class FriendPetProfile extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView lvFriendList;
    private LinearLayout back;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Context mContext;
    private NearByUserDTO nearByUserDTO;
    public HashMap<String, String> paramsPet = new HashMap<>();
    private String TAG = FriendPetProfile.class.getSimpleName();
    private ArrayList<PetListDTO> petListDTOList;
    private RecyclerView.LayoutManager mLayoutManager;
    private PetListFriendAda petListFriendAda;
    private LayoutInflater myInflater;
    private ImageView civFriendPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(FriendPetProfile.this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.user_pet_profile_frag);
        mContext = FriendPetProfile.this;
        myInflater = LayoutInflater.from(mContext);
        init();

    }

    public void init() {

        if (getIntent().hasExtra(Consts.USER_PROFILE)) {
            nearByUserDTO = (NearByUserDTO) getIntent().getSerializableExtra(Consts.USER_PROFILE);
        }
        paramsPet.put(Consts.USER_ID, nearByUserDTO.getId());
        paramsPet.put(Consts.FOLLOWER_USER_ID, nearByUserDTO.getId());
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        civFriendPic = (ImageView) findViewById(R.id.civFriendPic);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        collapsingToolbarLayout.setTitle(nearByUserDTO.getFirst_name());

        Glide
                .with(mContext)
                .load(nearByUserDTO.getProfile_pic())
                .placeholder(R.drawable.user_profile)
                .into(civFriendPic);


        collapsingToolbarLayout.setScrimAnimationDuration(500);
        collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_CENTER);

        ((AppCompatActivity) mContext).setSupportActionBar(toolbar);

        lvFriendList = (RecyclerView) findViewById(R.id.rvFriendList);

        getPetByUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }


    public void getPetByUser() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.GET_PET_BY_USER, paramsPet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<PetListDTO>>() {
                    }.getType();
                    try {
                        petListDTOList = new ArrayList<>();
                        petListDTOList = (ArrayList<PetListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        petListFriendAda = new PetListFriendAda(mContext, petListDTOList, myInflater);

                        mLayoutManager = new LinearLayoutManager(mContext);
                        lvFriendList.setLayoutManager(mLayoutManager);
                        lvFriendList.setItemAnimator(new DefaultItemAnimator());
                        lvFriendList.setAdapter(petListFriendAda);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }

}