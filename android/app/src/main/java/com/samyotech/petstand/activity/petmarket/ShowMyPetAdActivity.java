package com.samyotech.petstand.activity.petmarket;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.ShowMyPetAdAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowMyPetAdActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout back;
    RecyclerView rvPet;
    Context mContext;

    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    ArrayList<PetMarketDTO> petMarketDTOlist;
    ShowMyPetAdAdapter showPetMarketAdapter;
    private String TAG = ShowMyPetAdActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ShowMyPetAdActivity.this);
        setContentView(R.layout.activity_show_my_pet_market);
        mContext = ShowMyPetAdActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        findUI();
    }

    private void findUI() {
        back = findViewById(R.id.back);
        rvPet = findViewById(R.id.rvPet);


        back.setOnClickListener(this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvPet.setLayoutManager(mLayoutManager);
        rvPet.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_event));
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPetList();
    }

    public void getPetList() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.GET_MY_PETS_MARKET, getparms(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                petMarketDTOlist = new ArrayList<>();
                if (flag) {

                    Type listType = new TypeToken<List<PetMarketDTO>>() {
                    }.getType();
                    try {

                        petMarketDTOlist = (ArrayList<PetMarketDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        showPetMarketAdapter = new ShowMyPetAdAdapter(mContext, petMarketDTOlist);
                        rvPet.setAdapter(showPetMarketAdapter);
                        //tvNo.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  tvNo.setVisibility(View.VISIBLE);

                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                    //  tvNo.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());

        Log.e("parms", parms.toString());
        return parms;
    }


}
