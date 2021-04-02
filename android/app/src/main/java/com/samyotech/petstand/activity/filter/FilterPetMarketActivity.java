package com.samyotech.petstand.activity.filter;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AdapterFilterItem;
import com.samyotech.petstand.adapter.AdapterFilterPetMarketCategory;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.DummyFilterDTO;
import com.samyotech.petstand.models.GeneralPetMarketDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FilterPetMarketActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = FilterPetMarketActivity.class.getSimpleName();
    private Context mContext;
    private ListView lvCategory, lvItem;
    private GeneralPetMarketDTO generalDTO;
    private ImageView ivFilter, ivBack;
    private ArrayList<DummyFilterDTO> dummyFilterList;

    private ArrayList<String> categoryList;
    private ArrayList<GeneralPetMarketDTO.Country> countryDTOList = new ArrayList<>();
    private ArrayList<GeneralPetMarketDTO.City> cityDTOList = new ArrayList<>();
    private ArrayList<GeneralPetMarketDTO.PetType> petTypeList = new ArrayList<>();


    private AdapterFilterItem adapterFilterItem;
    private AdapterFilterPetMarketCategory adapterFilterCategory;
    HashMap<Integer, ArrayList<DummyFilterDTO>> map;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(FilterPetMarketActivity.this);
        setContentView(R.layout.activity_filter);
        mContext = FilterPetMarketActivity.this;
        map = ProjectUtils.mapPetMarket;
        init();
    }

    public void init() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivFilter = (ImageView) findViewById(R.id.ivFilter);
        ivBack.setOnClickListener(this);
        ivFilter.setOnClickListener(this);

        lvCategory = (ListView) findViewById(R.id.lvCategory);
        lvItem = (ListView) findViewById(R.id.lvItem);

        categoryList = new ArrayList<>();
        //  categoryList.add("Near By");


        categoryList.add("By Country");
        categoryList.add("By City");
       // categoryList.add("PetType");

        adapterFilterCategory = new AdapterFilterPetMarketCategory(mContext, categoryList, FilterPetMarketActivity.this);
        lvCategory.setAdapter(adapterFilterCategory);

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                showData(position);

            }
        });
        if (map.containsKey(0)) {
            dummyFilterList = map.get(0);
            adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
            lvItem.setAdapter(adapterFilterItem);
        } else {
            getData();

        }

    }


    private void getData() {

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.GET_MARKET_FILTER_ITEM, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        generalDTO = new Gson().fromJson(response.getJSONObject("data").toString(), GeneralPetMarketDTO.class);

                        savetomap();
                        if (map.containsKey(0)) {
                            dummyFilterList = map.get(0);
                            shortlistlowtohigh();
                            adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
                            lvItem.setAdapter(adapterFilterItem);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }


    public void showData(int pos) {

        if (pos == 0) {
            if (map.containsKey(0)) {
                dummyFilterList = map.get(0);
                shortlistlowtohigh();
                adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
                lvItem.setAdapter(adapterFilterItem);

            }
        } else if (pos == 1) {
            if (map.containsKey(1)) {
                dummyFilterList = map.get(1);
                shortlistlowtohigh();
                adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
                lvItem.setAdapter(adapterFilterItem);
            }
        } else if (pos == 2) {
            if (map.containsKey(2)) {
                dummyFilterList = map.get(2);
                shortlistlowtohigh();
                adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
                lvItem.setAdapter(adapterFilterItem);

            }
        }

    }


    public void savetomap() {

        dummyFilterList = new ArrayList<>();
        countryDTOList = generalDTO.getCountry();
        for (int i = 0; i < countryDTOList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(countryDTOList.get(i).getCountry()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(0, dummyFilterList);


        dummyFilterList = new ArrayList<>();
        cityDTOList = generalDTO.getCity();

        for (int i = 0; i < cityDTOList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(cityDTOList.get(i).getCity()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(1, dummyFilterList);

        dummyFilterList = new ArrayList<>();
        petTypeList = generalDTO.getPet_type();

        for (int i = 0; i < petTypeList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(petTypeList.get(i).getId(),petTypeList.get(i).getPet_name()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(2, dummyFilterList);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                map.clear();
                finish();
                break;
            case R.id.ivFilter:
                finish();
                break;
        }
    }

    public void shortlistlowtohigh() {
        Collections.sort(dummyFilterList, new Comparator<DummyFilterDTO>() {

            public int compare(DummyFilterDTO obj1, DummyFilterDTO obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName());

            }
        });
    }

}
