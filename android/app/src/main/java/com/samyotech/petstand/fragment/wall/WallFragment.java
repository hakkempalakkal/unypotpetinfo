package com.samyotech.petstand.fragment.wall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.wall.MyPost;
import com.samyotech.petstand.activity.wall.PostWall;
import com.samyotech.petstand.adapter.AdapterPublicWallNew;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PublicWallDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WallFragment extends Fragment implements View.OnClickListener, ListView.OnScrollListener {
    private View view;
    private String TAG = WallFragment.class.getSimpleName();
    private AdapterPublicWallNew adapterPublicWall;
    private ArrayList<PublicWallDTO> publicWallDTOList;
    ArrayList<PublicWallDTO> tempList;
    private ListView rvWall;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private RippleBackground Rippel;
    private ImageView centerImage;
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    boolean isFetching = false;
    int limit = 10;
    int offset = 0;
    int currentFirstVisibleItem = 0, currentVisibleItemCount = 0, scrollState = 0;
    private ImageView ivNoPost;
    private LayoutInflater adaptorInflator;
    private LinearLayout llPost;
    HashMap<String, String> parms = new HashMap<>();
    private CustomTextViewBold tvJoin;
    private CardView cardviewfd;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wall, container, false);
        mContext = getActivity();
        adaptorInflator = LayoutInflater.from(mContext);
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);

        setUI(view);
        return view;
    }

    public void setUI(View view) {
        cardviewfd = view.findViewById(R.id.cardviewfd);
        tvJoin = view.findViewById(R.id.tvJoin);
        ivNoPost = view.findViewById(R.id.ivNoPost);
        centerImage = view.findViewById(R.id.centerImage);
        Rippel = view.findViewById(R.id.Rippel);
        rvWall = view.findViewById(R.id.rvWall);
        llPost = view.findViewById(R.id.llPost);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        llPost.setOnClickListener(this);
        tvJoin.setOnClickListener(this);
        rvWall.setOnScrollListener(this);

        Rippel.startRippleAnimation();

        centerImage.setOnClickListener(this);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cardviewfd.setVisibility(View.GONE);
                limit = 10;
                offset = 0;
                tempList = new ArrayList<>();
                getWall();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (loginDTO.getComunity_id() != 0) {
            cardviewfd.setVisibility(View.GONE);
            limit = 10;
            offset = 0;
            tempList = new ArrayList<>();
            getWall();

        } else {
            cardviewfd.setVisibility(View.VISIBLE);
        }


    }

    public void showDataList() {
       /* layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvWall.setLayoutManager(layoutManager);*/

        tempList.addAll(publicWallDTOList);
        publicWallDTOList = tempList;
        if (publicWallDTOList.size() != 0) {
            ivNoPost.setVisibility(View.GONE);
            rvWall.setVisibility(View.VISIBLE);
            adapterPublicWall = new AdapterPublicWallNew(publicWallDTOList, getActivity(), adaptorInflator);
            rvWall.setAdapter(adapterPublicWall);
            rvWall.smoothScrollToPosition(currentVisibleItemCount);
            rvWall.setSelection(currentVisibleItemCount);
        } else {
            // ProjectUtils.showToast(this, "No Data Found");
            ivNoPost.setVisibility(View.VISIBLE);
            rvWall.setVisibility(View.GONE);

        }

    }

    public void getWall() {
        //  ProjectUtils.showProgressDialog(mContext, false, "Please wait....");
        new HttpsRequest(Consts.GET_POST_API, getPost(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    Type listType = new TypeToken<List<PublicWallDTO>>() {
                    }.getType();
                    try {

                        publicWallDTOList = new ArrayList<>();
                        publicWallDTOList = (ArrayList<PublicWallDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        showDataList();
                        isFetching = false;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    //ProjectUtils.showToast(mContext, msg);
                }
            }

        });
    }

    public Map<String, String> getPost() {
        HashMap<String, String> params = new HashMap<>();
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);

        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.LIMIT, String.valueOf(limit));
        params.put(Consts.OFFSET, String.valueOf(offset));
        params.put(Consts.COMUNITY_ID, loginDTO.getComunity_id() + "");
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.centerImage:
                startActivity(new Intent(mContext, PostWall.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.llPost:
                startActivity(new Intent(mContext, MyPost.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.tvJoin:
                join();
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        this.isScrollCompleted();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = view.getLastVisiblePosition()-1;
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount == publicWallDTOList.size() - 1 && this.scrollState == SCROLL_STATE_IDLE) {
            if (!isFetching) {
                offset = publicWallDTOList.size();
                //   limit = limit + 10;
                if (!isFetching) {
                    getWall();
                    Log.e("Call_getItem", "Call_getItem");
                } else {

                }

                isFetching = true;
            }
        }
    }

    public void join() {
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.COMUNITY_ID, "1");
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.JOIN_COMUNITY, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                  //  ProjectUtils.showLong(mContext, msg);

                    try {
                        LoginDTO loginDTO = new Gson().fromJson(response.getJSONObject("data").toString(), LoginDTO.class);
                        prefrence.setParentUser(loginDTO, Consts.LOGINDTO);
                        limit = 10;
                        offset = 0;
                        tempList = new ArrayList<>();
                        cardviewfd.setVisibility(View.GONE);
                        getWall();
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
