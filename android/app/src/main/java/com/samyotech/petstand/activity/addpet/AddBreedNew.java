package com.samyotech.petstand.activity.addpet;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AddBreedAdapterNew;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.BreedDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddBreedNew extends Fragment implements View.OnClickListener, AddBreedAdapterNew.UpdateDataClickListener {

    private View rootView;
    ImageView ivSearchBreeds;
    LinearLayout llSearchBreeds;
    CustomEditText etSearchBreeds;
    boolean editstatus = false;

    private View view;
    private RecyclerView rvBreed;
    private LinearLayout back;
    private CustomTextView ctvNumber;
    public AddPetSlides addPetSlides;
    private ArrayList<BreedDTO> breedDTOS = new ArrayList<>();
    private AddBreedAdapterNew addBreedAdapterNew;
    public BreedDTO breed_dto;
    private SharedPrefrence prefrence;
    LoginDTO loginDTO;
    private String TAG = AddBreedNew.class.getSimpleName();
    private TextView txtMsg;
    public String breedId = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_breed, null, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init(rootView);

        return rootView;
    }


    public void init(View view) {
        txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        ivSearchBreeds = (ImageView) view.findViewById(R.id.ivSearchBreeds);
        llSearchBreeds = (LinearLayout) view.findViewById(R.id.llSearchBreeds);
        etSearchBreeds = (CustomEditText) view.findViewById(R.id.etSearchBreeds);
        rvBreed = (RecyclerView) view.findViewById(R.id.rvBreed);
        //llSearchBreeds.setVisibility(View.GONE);
        //ivSearchBreeds.setVisibility(View.VISIBLE);

        ivSearchBreeds.setOnClickListener(this);
        // getBreed();

        etSearchBreeds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBreedAdapterNew.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!(breedDTOS.size() > 0))
                getBreed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearchBreeds: {
                if (breedDTOS.size() > 0) {
                    etSearchBreeds.setEnabled(true);
                } else {
                    etSearchBreeds.setEnabled(false);
                    ProjectUtils.showLong(getActivity(), "No breed avaliable to search.");
                }


            }
            break;
        }
    }

/*
    public void updateList(int pos) {
        for (int i = 0; i < breedDTOS.size(); i++) {
            if (i == pos) {
                breedDTOS.get(i).setSelected(true);
                breedId = breedDTOS.get(i).getId();
                breed_dto = breedDTOS.get(i);
                Log.e("Breed_Id", breedId);
            } else {
                breedDTOS.get(i).setSelected(false);
            }
        }
        rvBreed.setAdapter(addBreedAdapterNew);
        addBreedAdapterNew.notifyDataSetChanged();


    }
*/

    public void getBreed() {
        ProjectUtils.showProgressDialog(getActivity(), true, "Please Wait!!");
        new HttpsRequest(Consts.GETALLBREEDBYTYPE, getparms(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                   // ProjectUtils.showLong(getActivity(), msg);
                    try {
                        // breedDTOS = new ArrayList<>();
                        Type getBreedList = new TypeToken<List<BreedDTO>>() {
                        }.getType();
                        breedDTOS = (ArrayList<BreedDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getBreedList);
                        if (breedDTOS.size() > 0) {
                            txtMsg.setVisibility(View.GONE);
                            rvBreed.setVisibility(View.VISIBLE);

                            addBreedAdapterNew = new AddBreedAdapterNew(AddBreedNew.this, getActivity(), breedDTOS);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rvBreed.setLayoutManager(mLayoutManager);
                            rvBreed.setItemAnimator(new DefaultItemAnimator());
                            rvBreed.setAdapter(addBreedAdapterNew);
                            addBreedAdapterNew.setOnItemClickListener(AddBreedNew.this);
                            etSearchBreeds.setEnabled(true);

                        } else {
                            breedDTOS = new ArrayList<>();
                            txtMsg.setVisibility(View.VISIBLE);
                            rvBreed.setVisibility(View.GONE);
                            etSearchBreeds.setEnabled(false);
                            ProjectUtils.showLong(getActivity(), "No breed avaliable to search.");


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    breedDTOS = new ArrayList<>();
                    txtMsg.setVisibility(View.VISIBLE);
                    rvBreed.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.PET_TYPE, addPetSlides.type);
       // parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addPetSlides = (AddPetSlides) activity;
    }


    @Override
    public void onItemClick(int position) {
        addBreedAdapterNew.selected(position);
        breedId = breedDTOS.get(position).getId();
        breed_dto = breedDTOS.get(position);
    }
}