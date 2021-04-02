package com.samyotech.petstand.activity.petprofile;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.SearchAdap;
import com.samyotech.petstand.adapter.UserPetProfileforWallAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserPetProfileForWallActivity extends AppCompatActivity {

    Context mContext;
    ImageView IVprofile;
    CustomTextView tvLocation, tvMobileNo;
    CustomTextViewBold tvName;
    RecyclerView RVpetlist;
    UserPetProfileforWallAdapter userPetProfileforWallAdapter;
    private ArrayList<PetListDTO> petListDTOList;
    private LoginDTO loginDTO;
    private LoginDTO loginProfileDTO;
    private SharedPrefrence sharedPrefrence;
    private SearchAdap petListFriendAda;
    private LayoutInflater myInflater;
    public HashMap<String, String> paramsPet = new HashMap<>();
    private String TAG = UserPetProfileForWallActivity.class.getSimpleName();
    private RecyclerView.LayoutManager mLayoutManager;
    String userId = "";
    LinearLayout back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(UserPetProfileForWallActivity.this);
        setContentView(R.layout.activity_user_pet_profile_for_wall);
        mContext = UserPetProfileForWallActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        userId = getIntent().getStringExtra(Consts.USER_ID);
        findUI();
    }

    private void findUI() {

        IVprofile = findViewById(R.id.IVprofile);
        tvLocation = findViewById(R.id.tvLocation);
        tvMobileNo = findViewById(R.id.tvMobileNo);
        back = findViewById(R.id.back);
        tvName = findViewById(R.id.tvName);
        RVpetlist = findViewById(R.id.RVpetlist);
        getPetByUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getPetByUser() {
        paramsPet.put(Consts.USER_ID, userId);
        paramsPet.put(Consts.FOLLOWER_USER_ID, loginDTO.getId());

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

                        loginProfileDTO = new Gson().fromJson(response.getJSONObject("user_info").toString(), LoginDTO.class);
                        showData();

                        petListFriendAda = new SearchAdap(mContext, petListDTOList, myInflater);

                        mLayoutManager = new LinearLayoutManager(mContext);
                        RVpetlist.setLayoutManager(mLayoutManager);
                        RVpetlist.setItemAnimator(new DefaultItemAnimator());
                        RVpetlist.setAdapter(petListFriendAda);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void showData() {
        Glide.with(mContext)
                .load(loginProfileDTO.getProfile_pic())
                .placeholder(R.drawable.ears_icon)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(IVprofile);

        tvLocation.setText(loginProfileDTO.getAddress());
        tvMobileNo.setText(loginProfileDTO.getMobile_no());
        tvName.setText(loginProfileDTO.getFirst_name());
    }
}
