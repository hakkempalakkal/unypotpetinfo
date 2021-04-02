package com.samyotech.petstand.activity.wall;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AdapterMyPostNew;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PublicWallDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyPost extends AppCompatActivity implements ListView.OnScrollListener {

    private String TAG = MyPost.class.getSimpleName();
    private AdapterMyPostNew adapterMyPost;
    private ArrayList<PublicWallDTO> publicWallDTOList;
    ArrayList<PublicWallDTO> tempList;
    private ListView rvWall;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    boolean isFetching = false;
    int limit = 10;
    int offset = 0;
    int currentFirstVisibleItem = 0, currentVisibleItemCount = 0, scrollState = 0;
    private ImageView ivNoPost;
    private LayoutInflater adaptorInflator;
    HashMap<String, String> params = new HashMap<>();
    private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(MyPost.this);
        setContentView(R.layout.fragment_my_post);
        mContext = MyPost.this;
        adaptorInflator = LayoutInflater.from(mContext);
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        setUi();
    }


    public void setUi() {
        ivNoPost = findViewById(R.id.ivNoPost);

        rvWall = findViewById(R.id.rvWall);
        llBack = findViewById(R.id.llBack);

        rvWall.setOnScrollListener(this);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        limit = 10;
        offset = 0;
        tempList = new ArrayList<>();
        getPost();
    }

    public void showDataList() {
       /* layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvWall.setLayoutManager(layoutManager);*/

        tempList.addAll(publicWallDTOList);
        publicWallDTOList = tempList;
        if (publicWallDTOList.size() != 0) {
            ivNoPost.setVisibility(View.GONE);
            rvWall.setVisibility(View.VISIBLE);
            adapterMyPost = new AdapterMyPostNew(publicWallDTOList, mContext, adaptorInflator);
            rvWall.setAdapter(adapterMyPost);
            rvWall.smoothScrollToPosition(currentVisibleItemCount);
            rvWall.setSelection(currentVisibleItemCount);
        } else {
            // ProjectUtils.showToast(this, "No Data Found");
            ivNoPost.setVisibility(View.VISIBLE);
            rvWall.setVisibility(View.GONE);

        }

    }


    public void getPost() {
        //  ProjectUtils.showProgressDialog(mContext, false, "Please wait....");
        new HttpsRequest(Consts.GET_MY_POST_API, getPostParam(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

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
                    ProjectUtils.showLong(mContext, msg);

                }
            }

        });
    }

    public Map<String, String> getPostParam() {
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.LIMIT, String.valueOf(limit));
        params.put(Consts.OFFSET, String.valueOf(offset));
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        this.isScrollCompleted();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = view.getLastVisiblePosition();
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount == publicWallDTOList.size() - 1 && this.scrollState == SCROLL_STATE_IDLE) {
            if (!isFetching) {
                offset = publicWallDTOList.size();
                //   limit = limit + 10;
                if (!isFetching) {
                    getPost();
                    Log.e("Call_getItem", "Call_getItem");
                } else {

                }


                isFetching = true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
