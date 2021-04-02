package com.samyotech.petstand.activity.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.samyotech.petstand.models.EventDTO;
import com.samyotech.petstand.models.JoinedUserDTO;
import com.samyotech.petstand.models.LoginDTO;
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

public class MyEventDescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    CustomTextView CTVdescription, CTVaddress, CTVDateTime, CTVpettype;
    CustomTextViewBold CTVBheader;
    LinearLayout back;
    Context mContext;
    EventDTO eventDTO;
    ImageView IVEvent;
    CustomButton CBShowevent;
    RecyclerView rvJoinedList;

    private SharedPrefrence sharedPrefrence;
    private LoginDTO loginDTO;
    private String TAG = EventCreate.class.getSimpleName();
    ArrayList<JoinedUserDTO> joinedUserDTOList;
    JoinedUserListAdapter joinedUserListAdapter;
    ImageView IVedit, IVdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(MyEventDescriptionActivity.this);
        setContentView(R.layout.activity_my_event_description);
        mContext = MyEventDescriptionActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        eventDTO = (EventDTO) getIntent().getSerializableExtra(Consts.EVENT_DTO);
        findUI();
    }

    private void findUI() {
        CTVdescription = findViewById(R.id.CTVdescription);
        CTVaddress = findViewById(R.id.CTVaddress);
        CTVDateTime = findViewById(R.id.CTVDateTime);
        CTVBheader = findViewById(R.id.CTVBheader);
        back = findViewById(R.id.back);
        IVEvent = findViewById(R.id.IVEvent);
        CBShowevent = findViewById(R.id.CBShowevent);
        CTVpettype = findViewById(R.id.CTVpettype);
        rvJoinedList = findViewById(R.id.rvJoinedList);
        IVdelete = findViewById(R.id.IVdelete);
        IVedit = findViewById(R.id.IVedit);

        back.setOnClickListener(this);
        CBShowevent.setOnClickListener(this);
        IVdelete.setOnClickListener(this);
        IVedit.setOnClickListener(this);

        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEventUser();
    }

    private void setData() {
        CTVdescription.setText(eventDTO.getEvent_desc());
        CTVDateTime.setText(eventDTO.getEvent_date());
        CTVBheader.setText(eventDTO.getEvent_name());
        CTVaddress.setText(eventDTO.getAddress());
        CTVpettype.setText(eventDTO.getPet_type());

        Glide.with(mContext)
                .load(eventDTO.getImage())
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
            case R.id.IVedit:
                Intent inn = new Intent(mContext, EventCreate.class);
                inn.putExtra(Consts.FLAG, 1);
                inn.putExtra(Consts.EVENT_DTO, eventDTO);
                startActivity(inn);
                break;
            case R.id.IVdelete:
                deleteEventDialog();
                break;
            case R.id.CBShowevent:
                Intent in = new Intent(mContext, ShowJoinedEventUserActivity.class);
                in.putExtra(Consts.EVENT_DTO, eventDTO);
                startActivity(in);

                break;
        }
    }


    public void removeEvent() {

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.DELETE_EVENT, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                   // ProjectUtils.showLong(mContext, msg);
                    try {
                        finish();
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
        parms.put(Consts.EVENT_ID, eventDTO.getId());

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
        parms.put(Consts.EVENT_ID, eventDTO.getId());
        parms.put(Consts.USER_ID, loginDTO.getId());

        return parms;
    }

    public void deleteEventDialog() {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
        alertbox.setMessage("Are you sure want to delete?");
        alertbox.setTitle("Remove Event");
        alertbox.setIcon(R.drawable.cards_cancel);

        alertbox.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        removeEvent();

                    }
                });
        alertbox.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                    }
                });


        alertbox.show();
    }

}
