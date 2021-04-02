package com.samyotech.petstand.fragment.NearBy;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AdapterShop;
import com.samyotech.petstand.databinding.FragmentSalonBinding;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.NearByDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HostelFragment extends Fragment {

    View view;
    ArrayList<NearByDTO> shopList = new ArrayList<>();
    FragmentSalonBinding binding;
    AdapterShop adapterShop;
    private String TAG = HostelFragment.class.getSimpleName();
    private HashMap<String, String> paramsPet = new HashMap<>();
    private SharedPrefrence sharedPrefrence;
    private LoginDTO loginDTO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_salon, container, false);
        view = binding.getRoot();
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        findUI();
        return view;
    }

    private void findUI() {
       /* shopList = new ArrayList<>();
        shopList.add("ABC Salon");
        shopList.add("CBD Salon");
        shopList.add("123 Salon");
        shopList.add("XYZ Salon");
        shopList.add("321 Salon");*/

    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getNearByPet();
        }
    }

    public void getNearByPet() {

        paramsPet.put(Consts.NEAR_BY_ID, "4");
        paramsPet.put(Consts.USER_ID, loginDTO.getId());

        ProjectUtils.showProgressDialog(getActivity(), true, "Please wait....");
        new HttpsRequest(Consts.GET_ALL_NEAR_BY_VSS, paramsPet, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                shopList = new ArrayList<>();
                if (flag) {
                    ProjectUtils.showLong(getActivity(), msg);

                    try {
                        Type listType = new TypeToken<List<NearByDTO>>() {
                        }.getType();
                        shopList = (ArrayList<NearByDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        binding.rvSalon.setLayoutManager(mLayoutManager);
                        binding.rvSalon.setItemAnimator(new DefaultItemAnimator());
                        adapterShop = new AdapterShop(getActivity(), shopList);
                        binding.rvSalon.setAdapter(adapterShop);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(getActivity(), msg);
                    binding.ctvNoData.setVisibility(View.VISIBLE);
                }
            }
        });

    }


}
