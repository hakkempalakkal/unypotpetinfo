package com.samyotech.petstand.activity.food;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.FoodListAdapter;
import com.samyotech.petstand.adapter.SubCatagoryAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.CartDTO;
import com.samyotech.petstand.models.FoodListDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetCategoryDTO;
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

public class SubCategoryList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvFoodType;
    private LinearLayout back;
    private CustomTextView tvCardcount, tv_heading_text;
    private ArrayList<FoodListDTO> foodList;
    private FoodListAdapter foodListAdapter;
    private Context mContext;
    private String pet_type_id = "";
    private String pet_type_name = "";
    private String TAG = SubCategoryList.class.getSimpleName();
    private RelativeLayout llCart;
    private SearchView svFood;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private ArrayList<PetCategoryDTO> petCategoryDTOlist;
    private ArrayList<CartDTO> cartDTOList;
    CustomTextViewBold tvNo;
    MaterialBetterSpinner spinnerCatagory;
    String strSpinnerCategory = "";
    String strcountryID = "";
    SubCatagoryAdapter subCatagoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(SubCategoryList.this);
        setContentView(R.layout.food_list_frag);
        mContext = SubCategoryList.this;
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
        tv_heading_text.setText("Sub Category");


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
                    subCatagoryAdapter.filter(newText);
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
                //  getFoodList();
            }
        });

        strcountryID = GetCountryZipCode();
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
        getCategoryies();
        getMyCart();
    }

    public String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();

        if (CountryID == "" || CountryID == null) {
            CountryID = manager.getNetworkCountryIso().toUpperCase();
        }
        if (CountryID == "" || CountryID == null) {
            CountryID = getResources().getConfiguration().locale.getCountry();
        }
       /* String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryID = g[0];
                break;
            }
        }*/

        return CountryID;
    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.P_PET_TYPE, pet_type_id);
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.P_TYPE, strSpinnerCategory);
        parms.put(Consts.COUNTRY, strcountryID);
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
                    // ProjectUtils.showLong(mContext, msg);
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
        new HttpsRequest(Consts.GETALLCAT, getParmsSubCat(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<PetCategoryDTO>>() {
                    }.getType();
                    try {
                        petCategoryDTOlist = new ArrayList<>();
                        petCategoryDTOlist = (ArrayList<PetCategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        subCatagoryAdapter = new SubCatagoryAdapter(mContext, petCategoryDTOlist, pet_type_id);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        rvFoodType.setLayoutManager(mLayoutManager);
                        rvFoodType.setItemAnimator(new DefaultItemAnimator());
                        rvFoodType.setAdapter(subCatagoryAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //   ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public HashMap<String, String> getParmsSubCat() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.PET_TYPE, pet_type_id);
        Log.e("parms", parms.toString());
        return parms;
    }
}
