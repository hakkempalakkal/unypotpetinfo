package com.samyotech.petstand.fragment.AddPet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.activity.addpet.AddPetSlides;
import com.samyotech.petstand.activity.filter.SearchActivity;
import com.samyotech.petstand.adapter.PetListAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mayank on 14/2/18.
 */

public class PetList extends AppCompatActivity implements View.OnClickListener {
    private Context mcontext;
    private View view;
    private RecyclerView rvPetList;
    private ImageView ivNoPet, IVsearch;
    public BaseActivity baseActivity;
    private ArrayList<PetListDTO> petList;
    private LinearLayoutManager linearLayoutManager;
    private PetListAdapter petListAdapter;
    private SharedPrefrence prefrence;
    private String TAG = PetList.class.getSimpleName();
    private LoginDTO loginDTO;
    private
    CardView cvAddPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(PetList.this);
        setContentView(R.layout.pet_list_frag);

        prefrence = SharedPrefrence.getInstance(mcontext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
    }

    private void init() {
        rvPetList = (RecyclerView) view.findViewById(R.id.rvPetList);
        ivNoPet = (ImageView) view.findViewById(R.id.ivNoPet);
        IVsearch = (ImageView) view.findViewById(R.id.IVsearch);
        cvAddPet = view.findViewById(R.id.cvAddPet);

        cvAddPet.setOnClickListener(this);
        IVsearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.click_event));
        switch (view.getId()) {
            case R.id.cvAddPet: {
                startActivity(new Intent(mcontext, AddPetSlides.class));
            }
            break;
            case R.id.IVsearch:
                startActivity(new Intent(mcontext, SearchActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mcontext)) {
            PetList();
        } else {
            ProjectUtils.showLong(mcontext, getResources().getString(R.string.internet_concation));
        }
    }


    public void PetList() {
        ProjectUtils.showProgressDialog(mcontext, true, "Please Wait!!");
        new HttpsRequest(Consts.GETMYPET, getparm(), mcontext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        petList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<PetListDTO>>() {
                        }.getType();
                        petList = (ArrayList<PetListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        if (petList.size() != 0) {
                            rvPetList.setVisibility(View.VISIBLE);
                            ivNoPet.setVisibility(View.GONE);
                            petListAdapter = new PetListAdapter(mcontext, petList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
                            rvPetList.setLayoutManager(layoutManager);
                            rvPetList.setItemAnimator(new DefaultItemAnimator());
                            rvPetList.setAdapter(petListAdapter);
                        } else {
                            rvPetList.setVisibility(View.GONE);
                            ivNoPet.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    rvPetList.setVisibility(View.GONE);
                    ivNoPet.setVisibility(View.VISIBLE);
                    ProjectUtils.showLong(mcontext, msg);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }
}