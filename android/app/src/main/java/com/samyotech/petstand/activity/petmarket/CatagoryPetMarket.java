package com.samyotech.petstand.activity.petmarket;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.adapter.CatagoryPetMarketAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetCatList;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 16/2/18.
 */

public class CatagoryPetMarket extends AppCompatActivity implements View.OnClickListener {

    private View view;
    private BaseActivity baseActivity;
    private RecyclerView rvType;
    private CustomEditText etSearch;
    private ArrayList<PetCatList> petList;
    private LinearLayoutManager linearLayoutManager;
    private CatagoryPetMarketAdapter catagoryAdapter;
    private PetCatList petCatList;
    private SharedPrefrence prefrence;
    LoginDTO loginDTO;
    private String TAG = CatagoryPetMarket.class.getSimpleName();
    Context mContext;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(CatagoryPetMarket.this);
        setContentView(R.layout.activity_catagory_pet_market);
        mContext = CatagoryPetMarket.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();

    }

    private void init() {
        rvType = (RecyclerView) findViewById(R.id.rvType);
        etSearch = (CustomEditText) findViewById(R.id.etSearch);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    catagoryAdapter.filter(s.toString());
                } catch (Exception e) {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mContext)) {
            getPetType();
        } else {
            ProjectUtils.showLong(mContext, "Please enable your internet connection.");
        }
    }


    public void getPetType() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.GETALLPETTYPE, getparms(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    //ProjectUtils.showLong(mContext, msg);
                    Type listType = new TypeToken<List<PetCatList>>() {
                    }.getType();
                    try {
                        petList = new ArrayList<>();
                        petList = (ArrayList<PetCatList>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        catagoryAdapter = new CatagoryPetMarketAdapter(mContext, petList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        rvType.setLayoutManager(mLayoutManager);
                        rvType.setItemAnimator(new DefaultItemAnimator());
                        rvType.setAdapter(catagoryAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

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


    @Override
    public void onStart() {
        super.onStart();
        if (baseActivity != null)
            baseActivity.bmb1.setVisibility(View.VISIBLE);
    }
}
