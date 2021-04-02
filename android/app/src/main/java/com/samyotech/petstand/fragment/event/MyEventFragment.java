package com.samyotech.petstand.fragment.event;


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
import com.samyotech.petstand.adapter.EventListAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.EventDTO;
import com.samyotech.petstand.models.LoginDTO;
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

public class MyEventFragment extends Fragment {

    View view;
    RecyclerView rvEvent;
    private String TAG = MyEventFragment.class.getSimpleName();
    ArrayList<EventDTO> eventDTOlist;
    private SharedPrefrence sharedPrefrence;
    private LoginDTO loginDTO;
    EventListAdapter eventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_event, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        findUI();
        return view;
    }

    private void findUI() {
        rvEvent = view.findViewById(R.id.rvEvent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getMyEvent();
        } else {
            ProjectUtils.showToast(getActivity(), getString(R.string.internet_concation));
        }
    }

    public void getMyEvent() {
        new HttpsRequest(Consts.GETMYEVENT, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<EventDTO>>() {
                    }.getType();
                    try {
                        eventDTOlist = new ArrayList<>();
                        eventDTOlist = (ArrayList<EventDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rvEvent.setLayoutManager(mLayoutManager);
                        rvEvent.setItemAnimator(new DefaultItemAnimator());

                        eventListAdapter = new EventListAdapter(getActivity(), eventDTOlist);
                        rvEvent.setAdapter(eventListAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });

    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());

        return parms;
    }

}
