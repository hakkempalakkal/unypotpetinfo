package com.samyotech.petstand.activity.event;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.JoinedUserListAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.EventDTO;
import com.samyotech.petstand.models.JoinedUserDTO;
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

public class ShowJoinedEventUserActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvJoinedList;
    Context mContext;
    ArrayList<JoinedUserDTO> joinedUserDTOList;
    JoinedUserListAdapter joinedUserListAdapter;
    private SharedPrefrence sharedPrefrence;
    private LoginDTO loginDTO;
    private String TAG = EventCreate.class.getSimpleName();
    LinearLayout back;
    EventDTO eventDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ShowJoinedEventUserActivity.this);
        setContentView(R.layout.activity_show_joined_event_user);
        mContext = ShowJoinedEventUserActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        eventDTO = (EventDTO) getIntent().getSerializableExtra(Consts.EVENT_DTO);
        findUI();
    }

    private void findUI() {
        rvJoinedList = findViewById(R.id.rvJoinedList);
        back = findViewById(R.id.back);

        back.setOnClickListener(this);
    }

    public void getEventUser() {
        new HttpsRequest(Consts.JOIN_EVENT_USER_LIST, getuserparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<JoinedUserDTO>>() {
                    }.getType();
                    try {
                        joinedUserDTOList = new ArrayList<>();
                        joinedUserDTOList = (ArrayList<JoinedUserDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 5);
                        rvJoinedList.setLayoutManager(mLayoutManager);

                        joinedUserListAdapter = new JoinedUserListAdapter(mContext, joinedUserDTOList);
                        rvJoinedList.setAdapter(joinedUserListAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }

    public HashMap<String, String> getuserparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EVENT_ID, eventDTO.getId());
        parms.put(Consts.USER_ID, loginDTO.getId());

        return parms;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
