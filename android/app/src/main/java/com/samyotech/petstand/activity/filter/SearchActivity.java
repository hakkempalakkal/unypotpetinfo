package com.samyotech.petstand.activity.filter;

import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.SearchAdap;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.DummyFilterDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LinearLayout llBack, llfilter;
    private RecyclerView recyclerview = null;
    private SearchAdap searchAdap;
    private LinearLayout llIcon;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String id = "";
    HashMap<Integer, ArrayList<DummyFilterDTO>> map;
    private ArrayList<DummyFilterDTO> dummyFilterList;
    private Context mContext;
    private String TAG = SearchActivity.class.getSimpleName();
    private ArrayList<PetListDTO> petListDTOList;
    private LayoutInflater myInflater;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPrefrence share;
    private LoginDTO loginDTO;
    private HashMap<String, String> paramsPet = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(SearchActivity.this);
        setContentView(R.layout.activity_search);
        mContext = SearchActivity.this;
        myInflater = LayoutInflater.from(mContext);
        share = SharedPrefrence.getInstance(mContext);
        loginDTO = share.getParentUser(Consts.LOGINDTO);
        paramsPet.put(Consts.FOLLOWER_USER_ID, loginDTO.getId());
        map = ProjectUtils.map;
        map.clear();
        init();
    }

    public void init() {
        llBack = findViewById(R.id.llBack);
        llfilter = findViewById(R.id.llfilter);

        llBack.setOnClickListener(this);
        llfilter.setOnClickListener(this);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        llIcon = (LinearLayout) findViewById(R.id.llIcon);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.llfilter:
                startActivity(new Intent(mContext, FilterActivity.class));
                map.clear();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(mContext)) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            search();

                                        } else {
                                            ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );
    }

    @Override
    public void onRefresh() {
        map.clear();

        search();
    }


    public JSONObject getUpdateJson() {
        map = ProjectUtils.map;

        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray near_by = new JSONArray();
            JSONArray by_breed = new JSONArray();
            JSONArray by_gender = new JSONArray();
            JSONArray by_country = new JSONArray();
            JSONArray by_state = new JSONArray();
            JSONArray by_city = new JSONArray();
            JSONArray by_pettype = new JSONArray();
            if (!map.isEmpty()) {


              /*  if (map.containsKey(0)) {
                    dummyFilterList = map.get(0);
                    jsonObject.put(Consts.NEAR_BY, getJsonArray(dummyFilterList));

                }*/
                if (map.containsKey(0)) {
                    dummyFilterList = map.get(0);
                    jsonObject.put(Consts.BY_BREED, getJsonArrayBreed(dummyFilterList));

                }
                if (map.containsKey(1)) {
                    dummyFilterList = map.get(1);
                    jsonObject.put(Consts.BY_GENDER, getJsonArray(dummyFilterList));

                }
                if (map.containsKey(2)) {
                    dummyFilterList = map.get(2);
                    jsonObject.put(Consts.BY_COUNTRY, getJsonArray(dummyFilterList));
                }
                if (map.containsKey(3)) {
                    dummyFilterList = map.get(3);
                    jsonObject.put(Consts.BY_STATE, getJsonArray(dummyFilterList));
                }


                if (map.containsKey(4)) {
                    dummyFilterList = map.get(4);
                    jsonObject.put(Consts.BY_CITY, getJsonArray(dummyFilterList));


                }
                if (map.containsKey(5)) {
                    dummyFilterList = map.get(5);
                    jsonObject.put(Consts.BY_PETTYPE, getJsonArrayBreed(dummyFilterList));


                }
                jsonObject.put(Consts.USER_ID, loginDTO.getId());
            } else {
                //jsonObject.put(Consts.NEAR_BY, near_by);
                jsonObject.put(Consts.BY_BREED, by_breed);
                jsonObject.put(Consts.BY_GENDER, by_gender);
                jsonObject.put(Consts.BY_COUNTRY, by_country);
                jsonObject.put(Consts.BY_STATE, by_state);
                jsonObject.put(Consts.BY_CITY, by_city);
                jsonObject.put(Consts.BY_PETTYPE, by_pettype);
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


    public void search() {
        new HttpsRequest(Consts.FILTER, getUpdateJson(), mContext).stringPostJson(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    Type listType = new TypeToken<List<PetListDTO>>() {
                    }.getType();
                    try {
                        petListDTOList = new ArrayList<>();
                        petListDTOList = (ArrayList<PetListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        if (petListDTOList.size() != 0) {
                            recyclerview.setVisibility(View.VISIBLE);
                            llIcon.setVisibility(View.GONE);
                            searchAdap = new SearchAdap(mContext, petListDTOList, myInflater);
                            mLayoutManager = new LinearLayoutManager(mContext);
                            recyclerview.setLayoutManager(mLayoutManager);
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(searchAdap);
                        } else {
                            recyclerview.setVisibility(View.GONE);
                            llIcon.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(mContext, msg);
                    recyclerview.setVisibility(View.GONE);
                    llIcon.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
