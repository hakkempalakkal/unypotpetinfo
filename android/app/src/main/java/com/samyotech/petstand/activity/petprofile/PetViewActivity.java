package com.samyotech.petstand.activity.petprofile;

import android.content.Context;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AdapterShowPetView;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.GetViewDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
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

public class PetViewActivity extends AppCompatActivity implements View.OnClickListener {


    JSONObject jsonObject = new JSONObject();
    Context mContext;
    private String TAG = PetViewActivity.class.getSimpleName();
    String pettId = "";
    ArrayList<GetViewDTO> getViewDTOlist;
    AdapterShowPetView adapterShowPetView;
    ImageView ivBack;
    RecyclerView RVview;
    CustomTextViewBold tvNo;
    SwipeRefreshLayout swipeRefreshLayout;
    private HashMap<String, String> params = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    LoginDTO loginDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(PetViewActivity.this);
        setContentView(R.layout.activity_show_pet_view);
        mContext = PetViewActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        pettId = getIntent().getStringExtra(Consts.PET_ID);
        findUI();
    }

    private void findUI() {

        ivBack = findViewById(R.id.ivBack);
        RVview = findViewById(R.id.RVview);
        tvNo = findViewById(R.id.tvNo);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        ivBack.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkManager.isConnectToInternet(mContext)) {
                    getView();
                } else {
                    ProjectUtils.showToast(mContext, getString(R.string.internet_concation));
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finishBack();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mContext)) {
            getView();
        } else {
            ProjectUtils.showToast(mContext, getString(R.string.internet_concation));
        }
    }

    private void finishBack() {
        finish();
    }


    public void getView() {

        params.put(Consts.PET_ID, pettId);
        params.put(Consts.USER_ID, loginDTO.getId());
        //  ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_MY_PET_VIEWER, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                // ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {

                    try {
                        getViewDTOlist = new ArrayList<>();
                        Type getDTO = new TypeToken<List<GetViewDTO>>() {
                        }.getType();
                        getViewDTOlist = (ArrayList<GetViewDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getDTO);


                        adapterShowPetView = new AdapterShowPetView(mContext, getViewDTOlist);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        RVview.setLayoutManager(mLayoutManager);
                        RVview.setItemAnimator(new DefaultItemAnimator());
                        RVview.setAdapter(adapterShowPetView);

                        tvNo.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                } else {
                   // ProjectUtils.showToast(mContext, msg);
                    tvNo.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
