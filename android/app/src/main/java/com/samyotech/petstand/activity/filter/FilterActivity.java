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
import com.samyotech.petstand.adapter.AdapterFilterCategory;
import com.samyotech.petstand.adapter.AdapterFilterItem;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.DummyFilterDTO;
import com.samyotech.petstand.models.GenderDTO;
import com.samyotech.petstand.models.GeneralDTO;
import com.samyotech.petstand.models.NearyByDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = FilterActivity.class.getSimpleName();
    private Context mContext;
    private ListView lvCategory, lvItem;
    private GeneralDTO generalDTO;
    private ImageView ivFilter, ivBack;
    private ArrayList<DummyFilterDTO> dummyFilterList;

    private ArrayList<String> categoryList;
    private ArrayList<GeneralDTO.Breed> breedDTOList = new ArrayList<>();
    private ArrayList<GeneralDTO.Country> countryDTOList = new ArrayList<>();
    private ArrayList<GeneralDTO.State> stateDTOList = new ArrayList<>();
    private ArrayList<GeneralDTO.City> cityDTOList = new ArrayList<>();
    private ArrayList<GeneralDTO.PetType> petTypeList = new ArrayList<>();
    private ArrayList<GenderDTO> genderDTOList;
    private ArrayList<NearyByDTO> nearyByDTOList;

    private AdapterFilterItem adapterFilterItem;
    private AdapterFilterCategory adapterFilterCategory;
    HashMap<Integer, ArrayList<DummyFilterDTO>> map;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(FilterActivity.this);
        setContentView(R.layout.activity_filter);
        mContext = FilterActivity.this;
        map = ProjectUtils.map;
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
        categoryList.add("By Breed");
        categoryList.add("By Gender");
        categoryList.add("By Country");
        categoryList.add("By State");
        categoryList.add("By City");
        categoryList.add("PetType");
        //categoryList.add("My Friends");


        genderDTOList = new ArrayList<>();
        genderDTOList.add(new GenderDTO("Male"));
        genderDTOList.add(new GenderDTO("Female"));
        genderDTOList.add(new GenderDTO("Both"));

      /*  nearyByDTOList = new ArrayList<>();
        nearyByDTOList.add(new NearyByDTO("500 Meter"));
        nearyByDTOList.add(new NearyByDTO("1 KM"));
        nearyByDTOList.add(new NearyByDTO("3 Km"));
        nearyByDTOList.add(new NearyByDTO("5 Km"));*/


        adapterFilterCategory = new AdapterFilterCategory(mContext, categoryList, FilterActivity.this);
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
        new HttpsRequest(Consts.GET_FILTER_ITEM, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        generalDTO = new Gson().fromJson(response.getJSONObject("data").toString(), GeneralDTO.class);

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
        } else if (pos == 3) {
            if (map.containsKey(3)) {
                dummyFilterList = map.get(3);
                shortlistlowtohigh();
                adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
                lvItem.setAdapter(adapterFilterItem);
            }

        } else if (pos == 4) {
            if (map.containsKey(4)) {
                dummyFilterList = map.get(4);
                shortlistlowtohigh();
                adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
                lvItem.setAdapter(adapterFilterItem);
            }

        } else if (pos == 5) {
            if (map.containsKey(5)) {
                dummyFilterList = map.get(5);
                shortlistlowtohigh();
                adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
                lvItem.setAdapter(adapterFilterItem);
            }

        }

    }


    public void savetomap() {

        dummyFilterList = new ArrayList<>();
        breedDTOList = generalDTO.getBreeds();
        for (int i = 0; i < breedDTOList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(breedDTOList.get(i).getId(), breedDTOList.get(i).getBreed_name()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(0, dummyFilterList);


        dummyFilterList = new ArrayList<>();
        for (int i = 0; i < genderDTOList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(genderDTOList.get(i).getGender()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(1, dummyFilterList);


        dummyFilterList = new ArrayList<>();
        countryDTOList = generalDTO.getCountry();
        for (int i = 0; i < countryDTOList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(countryDTOList.get(i).getCountry()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(2, dummyFilterList);


        dummyFilterList = new ArrayList<>();
        stateDTOList = generalDTO.getState();

        for (int i = 0; i < stateDTOList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(stateDTOList.get(i).getState()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(3, dummyFilterList);

        dummyFilterList = new ArrayList<>();
        cityDTOList = generalDTO.getCity();

        for (int i = 0; i < cityDTOList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(cityDTOList.get(i).getCity()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(4, dummyFilterList);

        dummyFilterList = new ArrayList<>();
        petTypeList = generalDTO.getPet_type();

        for (int i = 0; i < petTypeList.size(); i++) {
            dummyFilterList.add(new DummyFilterDTO(petTypeList.get(i).getId(),petTypeList.get(i).getPet_name()));
        }
        adapterFilterItem = new AdapterFilterItem(mContext, dummyFilterList);
        lvItem.setAdapter(adapterFilterItem);
        map.put(5, dummyFilterList);

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
