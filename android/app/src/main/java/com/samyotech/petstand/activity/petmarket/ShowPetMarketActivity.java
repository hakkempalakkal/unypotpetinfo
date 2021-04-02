package com.samyotech.petstand.activity.petmarket;

import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.filter.FilterPetMarketActivity;
import com.samyotech.petstand.adapter.ShowPetMarketAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.DummyFilterDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowPetMarketActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout back, llIcon;
    RecyclerView rvPet;
    Context mContext;
    CardView adPetBTN;
    CustomTextViewBold CTVaddPost, CTVmyad;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    ArrayList<PetMarketDTO> petMarketDTOlist;
    ShowPetMarketAdapter showPetMarketAdapter;
    private String TAG = ShowPetMarketActivity.class.getSimpleName();
    ImageView IVfilter;
    HashMap<Integer, ArrayList<DummyFilterDTO>> map;
    private ArrayList<DummyFilterDTO> dummyFilterList;
    String pet_type_id = "";
    SwipeRefreshLayout swipe_refresh_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ShowPetMarketActivity.this);
        setContentView(R.layout.activity_show_pet_market);
        mContext = ShowPetMarketActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        pet_type_id = getIntent().getStringExtra(Consts.P_PET_TYPE);
        findUI();
    }

    private void findUI() {
        back = findViewById(R.id.back);
        rvPet = findViewById(R.id.rvPet);
        adPetBTN = findViewById(R.id.adPetBTN);
        CTVaddPost = findViewById(R.id.CTVaddPost);
        CTVmyad = findViewById(R.id.CTVmyad);
        IVfilter = findViewById(R.id.IVfilter);
        llIcon = findViewById(R.id.llIcon);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);

        back.setOnClickListener(this);
        CTVaddPost.setOnClickListener(this);
        CTVmyad.setOnClickListener(this);
        IVfilter.setOnClickListener(this);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPetList();
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvPet.setLayoutManager(mLayoutManager);
        rvPet.setItemAnimator(new DefaultItemAnimator());

        rvPet.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
                Animation sliddown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);

                if (dy > 0 && adPetBTN.getVisibility() == View.VISIBLE) {
                    adPetBTN.setVisibility(View.GONE);
                    adPetBTN.startAnimation(sliddown);
                } else if (dy < 0 && adPetBTN.getVisibility() != View.VISIBLE) {
                    adPetBTN.setVisibility(View.VISIBLE);
                    adPetBTN.startAnimation(slideUp);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_event));
        switch (v.getId()) {
            case R.id.back:
                finish();
                map.clear();
                break;
            case R.id.CTVaddPost:
                startActivity(new Intent(mContext, AddPetMarketActivity.class));
                break;
            case R.id.CTVmyad:
                startActivity(new Intent(mContext, ShowMyPetAdActivity.class));
                break;
            case R.id.IVfilter:
                startActivity(new Intent(mContext, FilterPetMarketActivity.class));
                map.clear();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        map.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPetList();
    }

    public void getPetList() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.PET_MARKET_FILTER, getUpdateJson(), mContext).stringPostJson(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                petMarketDTOlist = new ArrayList<>();
                if (flag) {
                    swipe_refresh_layout.setRefreshing(false);
                    Type listType = new TypeToken<List<PetMarketDTO>>() {
                    }.getType();
                    try {

                        petMarketDTOlist = (ArrayList<PetMarketDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        showPetMarketAdapter = new ShowPetMarketAdapter(mContext, petMarketDTOlist);
                        rvPet.setAdapter(showPetMarketAdapter);
                        rvPet.setVisibility(View.VISIBLE);
                        llIcon.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        rvPet.setVisibility(View.GONE);
                        llIcon.setVisibility(View.VISIBLE);

                    }

                } else {
                    swipe_refresh_layout.setRefreshing(false);
                    ProjectUtils.showLong(mContext, msg);
                    rvPet.setVisibility(View.GONE);
                    llIcon.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public JSONObject getUpdateJson() {
        map = ProjectUtils.mapPetMarket;

        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray by_country = new JSONArray();
            JSONArray by_city = new JSONArray();
            JSONArray by_pettype = new JSONArray();
            if (!map.isEmpty()) {


                if (map.containsKey(0)) {
                    dummyFilterList = map.get(0);
                    jsonObject.put(Consts.BY_COUNTRY, getJsonArray(dummyFilterList));
                }
                if (map.containsKey(1)) {
                    dummyFilterList = map.get(1);
                    jsonObject.put(Consts.BY_CITY, getJsonArray(dummyFilterList));


                }
                if (map.containsKey(2)) {
                    dummyFilterList = map.get(2);
                    jsonObject.put(Consts.BY_TYPE_PET, getJsonArrayBreed(dummyFilterList));


                }
                jsonObject.put(Consts.BY_TYPE_PET, pet_type_id);
                jsonObject.put(Consts.USER_ID, loginDTO.getId());


            } else {

                jsonObject.put(Consts.BY_COUNTRY, by_country);
                jsonObject.put(Consts.BY_CITY, by_city);
                jsonObject.put(Consts.BY_TYPE_PET, by_pettype);
                jsonObject.put(Consts.BY_TYPE_PET, pet_type_id);
                jsonObject.put(Consts.USER_ID, loginDTO.getId());


            }


            Log.e("update_json", jsonObject.toString());

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }


    public JSONArray getJsonArray(ArrayList<DummyFilterDTO> dummyList) {
        JSONArray roleArray = new JSONArray();

        try {
            for (int i = 0; i < dummyList.size(); i++) {
                if (dummyList.get(i).isChecked()) {
                    roleArray.put(dummyList.get(i).getName());

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleArray;
    }

    public JSONArray getJsonArrayBreed(ArrayList<DummyFilterDTO> dummyList) {
        JSONArray roleArray = new JSONArray();

        try {
            for (int i = 0; i < dummyList.size(); i++) {
                if (dummyList.get(i).isChecked()) {
                    roleArray.put(dummyList.get(i).getId());

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleArray;
    }


}
