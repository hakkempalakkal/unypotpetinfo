package com.samyotech.petstand.fragment.NearBy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.filter.FilterActivity;
import com.samyotech.petstand.adapter.PetListFriendAda;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.DummyFilterDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.GPSTracker;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class PetListFrag extends Fragment {
    private View view;
    private String TAG = PetListFrag.class.getSimpleName();
    private ArrayList<PetListDTO> petListDTOList;
    private RecyclerView.LayoutManager mLayoutManager;
    private PetListFriendAda petListFriendAda;
    private RecyclerView rvFriendList;
    private HashMap<String, String> paramsPet = new HashMap<>();
    private SharedPrefrence share;
    private LoginDTO loginDTO;
    private LayoutInflater myInflater;
    private CustomEditText etSearch;
    AlertDialog alertDialog1;
    CharSequence[] values = {""};
    private ArrayList<PetListDTO> tempList;
    NearByFrag nearByFrag;
    HashMap<Integer, ArrayList<DummyFilterDTO>> map = new HashMap<>();
    private ArrayList<DummyFilterDTO> dummyFilterList = new ArrayList<>();
    LinearLayout llIcon;
    public GPSTracker gps;
    double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pet_list, container, false);
        myInflater = LayoutInflater.from(getActivity());

        share = SharedPrefrence.getInstance(getActivity());
        loginDTO = share.getParentUser(Consts.LOGINDTO);
        paramsPet.put(Consts.USER_ID, loginDTO.getId());
        //paramsPet.put(Consts.FOLLOWER_USER_ID, loginDTO.getId());

        gps = new GPSTracker(getActivity());

        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


            Log.e("Loction", "Lat" + latitude + "long" + longitude);
        } else {

            gps.showSettingsAlert();
        }



        rvFriendList = (RecyclerView) view.findViewById(R.id.rvFriendList);

        etSearch = (CustomEditText) view.findViewById(R.id.etSearch);
        llIcon = view.findViewById(R.id.llIcon);

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvFriendList.setLayoutManager(mLayoutManager);
        rvFriendList.setItemAnimator(new DefaultItemAnimator());


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().length() > 0) {
                        petListFriendAda.filter(s.toString());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        NearByFrag.IVfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CreateAlertDialogWithRadioButtonGroup();

                startActivity(new Intent(getActivity(), FilterActivity.class));
                map.clear();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNearByPet();
    }

    public void search() {
        new HttpsRequest(Consts.FILTER, getUpdateJson(), getActivity()).stringPostJson(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ///swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    Type listType = new TypeToken<List<PetListDTO>>() {
                    }.getType();
                    try {
                        petListDTOList = new ArrayList<>();
                        petListDTOList = (ArrayList<PetListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        if (petListDTOList.size() != 0) {
                            rvFriendList.setVisibility(View.VISIBLE);
                            llIcon.setVisibility(View.GONE);
                            petListFriendAda = new PetListFriendAda(getActivity(), petListDTOList, myInflater);
                            rvFriendList.setAdapter(petListFriendAda);
                        } else {
                            rvFriendList.setVisibility(View.GONE);
                            llIcon.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(getActivity(), msg);
                    rvFriendList.setVisibility(View.GONE);
                    llIcon.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void getNearByPet() {
        paramsPet.put(Consts.LATI, String.valueOf(latitude));
        paramsPet.put(Consts.LONGI, String.valueOf(longitude));

        ProjectUtils.showProgressDialog(getActivity(), true, "Please wait....");
        new HttpsRequest(Consts.GET_ALL_PETS, paramsPet, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    // ProjectUtils.showLong(mContext, msg);
                    Type listType = new TypeToken<List<PetListDTO>>() {
                    }.getType();
                    try {
                        petListDTOList = new ArrayList<>();
                        ArrayList<String> petType = new ArrayList<>();
                        petListDTOList = (ArrayList<PetListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        petListFriendAda = new PetListFriendAda(getActivity(), petListDTOList, myInflater);
                        rvFriendList.setAdapter(petListFriendAda);

                        for (int i = 0; i < petListDTOList.size(); i++) {
                            if (!petType.contains(petListDTOList.get(i).getPet_type_name())) {
                                petType.add(petListDTOList.get(i).getPet_type_name());
                            }
                        }

                        values = petType.toArray(new CharSequence[petType.size()]);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });

    }

    public void CreateAlertDialogWithRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort by");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                getJobsByStatus(petListDTOList, String.valueOf(values[item]));
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    public void getJobsByStatus(ArrayList<PetListDTO> plist, String sta) {
        tempList = new ArrayList<>();
        try {
            for (Iterator<PetListDTO> bookIterator = plist.iterator(); bookIterator.hasNext(); ) {
                PetListDTO petListDTO = bookIterator.next();
                if (petListDTO.getPet_type_name().equalsIgnoreCase(sta)) {

                    tempList.add(petListDTO);
                }
                if (tempList.size() > 0) {
                   /* tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);*/
                    petListFriendAda = new PetListFriendAda(getActivity(), tempList, myInflater);
                    rvFriendList.setAdapter(petListFriendAda);
                    petListFriendAda.notifyDataSetChanged();
                } else {
                   /* tvNo.setVisibility(View.VISIBLE);
                    RVhistorylist.setVisibility(View.GONE);*/
                }


            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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


}
