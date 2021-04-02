package com.samyotech.petstand.activity.food;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.ShopifyFoodListAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.CartDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetCategoryDTO;
import com.samyotech.petstand.models.ProductSopifyDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 16/2/18.
 */

public class ShopifyFoodList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvFoodType;
    private LinearLayout back;
    private CustomTextView tvCardcount, tv_heading_text;
    private ArrayList<ProductSopifyDTO> foodList;
    private ShopifyFoodListAdapter foodListAdapter;
    private Context mContext;
    private String pet_type_id = "";
    private String pet_type_name = "";
    private String TAG = ShopifyFoodList.class.getSimpleName();
    private RelativeLayout llCart;
    private SearchView svFood;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private ArrayList<PetCategoryDTO> petCategoryDTOlist;
    private ArrayList<CartDTO> cartDTOList;
    CustomTextViewBold tvNo;
    MaterialBetterSpinner spinnerCatagory;
    String strSpinnerCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ShopifyFoodList.this);
        setContentView(R.layout.food_list_frag);
        mContext = ShopifyFoodList.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
    }

    private void init() {
        if (getIntent().hasExtra(Consts.P_PET_TYPE)) {
            pet_type_id = getIntent().getStringExtra(Consts.P_PET_TYPE);
            pet_type_name = getIntent().getStringExtra(Consts.P_PET_TYPE_NAME);
        }
        svFood = (SearchView) findViewById(R.id.svFood);
        svFood.setOnSearchClickListener(this);
        llCart = (RelativeLayout) findViewById(R.id.llCart);
        llCart.setOnClickListener(this);
        rvFoodType = (RecyclerView) findViewById(R.id.rvFoodType);
        tvCardcount = (CustomTextView) findViewById(R.id.tvCardcount);
        tv_heading_text = (CustomTextView) findViewById(R.id.tv_heading_text);
        tvNo = findViewById(R.id.tvNo);
        spinnerCatagory = findViewById(R.id.spinnerCatagory);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        tv_heading_text.setText(pet_type_name + " Food");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvFoodType.setLayoutManager(mLayoutManager);
        rvFoodType.setItemAnimator(new DefaultItemAnimator());

        svFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    foodListAdapter.filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        spinnerCatagory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strSpinnerCategory = petCategoryDTOlist.get(position).getId();
                getFoodList();
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
            case R.id.llCart:
                startActivity(new Intent(mContext, Cart.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFoodList();
        getCategoryies();
    }

    public void getFoodList() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        AndroidNetworking.get(Consts.GET_PRODUCTS)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.pauseProgressDialog();

                        foodList = new ArrayList<>();
                        getMyCart();
                        Type listType = new TypeToken<List<ProductSopifyDTO>>() {
                        }.getType();
                        try {

                            foodList = (ArrayList<ProductSopifyDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                            foodListAdapter = new ShopifyFoodListAdapter(mContext, foodList);
                            rvFoodType.setAdapter(foodListAdapter);
                            tvNo.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            tvNo.setVisibility(View.VISIBLE);
                            foodListAdapter = new ShopifyFoodListAdapter(mContext, foodList);
                            rvFoodType.setAdapter(foodListAdapter);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.P_PET_TYPE, pet_type_id);
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.P_TYPE, strSpinnerCategory);
        Log.e("parms", parms.toString());
        return parms;
    }


    public void getMyCart() {
        new HttpsRequest(Consts.GET_MY_CART, getparmsCart(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<CartDTO>>() {
                    }.getType();
                    try {
                        cartDTOList = new ArrayList<>();
                        cartDTOList = (ArrayList<CartDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        if (cartDTOList.size() > 0) {
                            tvCardcount.setVisibility(View.VISIBLE);
                            tvCardcount.setText(String.valueOf(cartDTOList.size()));
                        } else {
                            tvCardcount.setVisibility(View.GONE);
                            tvCardcount.setText(" " + 0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                    tvCardcount.setVisibility(View.GONE);
                    tvCardcount.setText(" " + 0);

                }
            }
        });

    }

    public HashMap<String, String> getparmsCart() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }

    public void getCategoryies() {
        new HttpsRequest(Consts.GETALLCAT, getparmsCart(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<PetCategoryDTO>>() {
                    }.getType();
                    try {
                        petCategoryDTOlist = new ArrayList<>();
                        petCategoryDTOlist = (ArrayList<PetCategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        ArrayAdapter<PetCategoryDTO> adapterthick = new ArrayAdapter<PetCategoryDTO>(mContext, android.R.layout.simple_list_item_1, petCategoryDTOlist);
                        adapterthick.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCatagory.setAdapter(adapterthick);

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
