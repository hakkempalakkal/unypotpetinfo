package com.samyotech.petstand.activity.food;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.FoodListAdapter;
import com.samyotech.petstand.databinding.ActivitySearch2Binding;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.FoodListDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ActivitySearch2Binding binding;
    private ArrayList<FoodListDTO> foodList;
    private String TAG = SearchActivity.class.getSimpleName();
    Context mContext = SearchActivity.this;
    private FoodListAdapter foodListAdapter;
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    String p_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(SearchActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search2);
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
        listners();
    }

    public void init() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        binding.rcSearch.setLayoutManager(mLayoutManager);
        binding.rcSearch.setItemAnimator(new DefaultItemAnimator());

    }

    public void listners() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    p_name = newText;
                    getFoodList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //getCategoryies();
    }

    public void getFoodList() {
        //   ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.SEARCH, getparms(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                foodList = new ArrayList<>();
                if (flag) {
                    Type listType = new TypeToken<List<FoodListDTO>>() {
                    }.getType();
                    try {

                        foodList = (ArrayList<FoodListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        foodListAdapter = new FoodListAdapter(mContext, foodList, "", "");
                        binding.rcSearch.setAdapter(foodListAdapter);
                        binding.tvNo.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.tvNo.setVisibility(View.VISIBLE);

                    }

                } else {
                    binding.tvNo.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.P_NAME, p_name);
        Log.e("parms", parms.toString());
        return parms;
    }

}
