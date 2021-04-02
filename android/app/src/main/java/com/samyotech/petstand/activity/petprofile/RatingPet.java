package com.samyotech.petstand.activity.petprofile;

import android.content.Context;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AdapterPetRating;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetRatingDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingPet extends AppCompatActivity implements View.OnClickListener {
    private String TAG = RatingPet.class.getSimpleName();
    private RecyclerView lvChating;
    private AdapterPetRating adapterPetRating;
    private Context mContext;
    private ArrayList<PetRatingDTO> ratingDTOList;
    private ImageView ivBack;
    private RelativeLayout relative;
    private LinearLayoutManager layoutManager;
    private String pet_id = "";
    SharedPrefrence sharedPreferences;
    LoginDTO loginDTO;
    CustomTextViewBold tvNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(RatingPet.this);
        setContentView(R.layout.activity_pet_rating);
        sharedPreferences = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPreferences.getParentUser(Consts.LOGINDTO);
        mContext = RatingPet.this;
        if (getIntent().hasExtra("pet_id")) {
            pet_id = getIntent().getStringExtra("pet_id");
        }
        init();
    }

    public void init() {
        relative = (RelativeLayout) findViewById(R.id.relative);
        lvChating = findViewById(R.id.lvChating);
        ivBack = findViewById(R.id.ivBack);
        tvNo = findViewById(R.id.tvNo);
        ivBack.setOnClickListener(this);

        getRating();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;

        }
    }


    public void getRating() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.GET_ALL_RATING, getCommentParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    Type listType = new TypeToken<List<PetRatingDTO>>() {
                    }.getType();
                    try {
                        ratingDTOList = new ArrayList<>();
                        ratingDTOList = (ArrayList<PetRatingDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        showDataList();
                        tvNo.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    tvNo.setVisibility(View.VISIBLE);
                }


            }

        });
    }

    public Map<String, String> getCommentParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.PET_ID, pet_id);
        params.put(Consts.USER_ID, loginDTO.getId());
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }

    public void showDataList() {
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        lvChating.setLayoutManager(layoutManager);
        adapterPetRating = new AdapterPetRating(RatingPet.this, ratingDTOList);
        lvChating.setAdapter(adapterPetRating);

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
