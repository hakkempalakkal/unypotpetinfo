package com.samyotech.petstand.activity.event;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.JoinedUserListAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.JoinedUserDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.OtherEventDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherEventDescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    CustomTextView CTVdescription, CTVaddress, CTVDateTime, CTVpettype;
    CustomTextViewBold CTVBheader;
    LinearLayout back;
    Context mContext;
    OtherEventDTO otherEventDTO;
    ImageView IVEvent;
    CustomButton CBjoinevent;
    RecyclerView rvJoinedList;

    private SharedPrefrence sharedPrefrence;
    private LoginDTO loginDTO;
    private String TAG = EventCreate.class.getSimpleName();
    ArrayList<JoinedUserDTO> joinedUserDTOList;
    JoinedUserListAdapter joinedUserListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(OtherEventDescriptionActivity.this);
        setContentView(R.layout.activity_other_event_description);
        mContext = OtherEventDescriptionActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        otherEventDTO = (OtherEventDTO) getIntent().getSerializableExtra(Consts.EVENT_DTO);
        findUI();
    }

    private void findUI() {
        CTVdescription = findViewById(R.id.CTVdescription);
        CTVaddress = findViewById(R.id.CTVaddress);
        CTVDateTime = findViewById(R.id.CTVDateTime);
        CTVBheader = findViewById(R.id.CTVBheader);
        back = findViewById(R.id.back);
        IVEvent = findViewById(R.id.IVEvent);
        CBjoinevent = findViewById(R.id.CBjoinevent);
        CTVpettype = findViewById(R.id.CTVpettype);
        rvJoinedList = findViewById(R.id.rvJoinedList);

        back.setOnClickListener(this);
        CBjoinevent.setOnClickListener(this);

        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEventUser();
    }

    private void setData() {
        CTVdescription.setText(otherEventDTO.getEvent_desc());
        CTVDateTime.setText(otherEventDTO.getEvent_date());
        CTVBheader.setText(otherEventDTO.getEvent_name());
        CTVaddress.setText(otherEventDTO.getAddress());
        CTVpettype.setText(otherEventDTO.getPet_type());

        if (otherEventDTO.getIs_join().equals("1")) {
            CBjoinevent.setText("Left event");
        } else if (otherEventDTO.getIs_join().equals("0")) {
            CBjoinevent.setText("Join event");
        }

        Glide.with(mContext)
                .load(otherEventDTO.getImage())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(IVEvent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.CBjoinevent:
                if (otherEventDTO.getIs_join().equals("1")) {
                    removeEvent();
                } else if (otherEventDTO.getIs_join().equals("0")) {
                    joinEvent();
                }

                break;
        }
    }


    public void joinEvent() {

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.JOIN_EVENT, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    try {
                        //finish();
                        otherEventDTO.setIs_join("1");

                        CBjoinevent.setText("Left event");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void removeEvent() {

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.REMOVE_USER, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    try {
                        //finish();
                        otherEventDTO.setIs_join("0");

                        CBjoinevent.setText("Join event");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }


    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();

        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.EVENT_ID, otherEventDTO.getId());

        return parms;
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
        parms.put(Consts.EVENT_ID, otherEventDTO.getId());
        parms.put(Consts.USER_ID, loginDTO.getId());

        return parms;
    }

}
