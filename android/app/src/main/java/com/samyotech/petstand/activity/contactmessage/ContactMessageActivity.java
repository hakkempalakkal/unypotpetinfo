package com.samyotech.petstand.activity.contactmessage;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.MyMessageAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.GetMyMessageDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    SharedPrefrence sharedPrefrence;
    LoginDTO loginDTO;

    ImageView ivBack;
    RecyclerView RVitemlistt;
    ArrayList<GetMyMessageDTO> myMessageDTOlist;
    private LinearLayoutManager mLayoutManager;
    CustomTextViewBold tvNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ContactMessageActivity.this);
        setContentView(R.layout.activity_contact_message);
        mContext = ContactMessageActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        init();
    }

    private void init() {
        RVitemlistt = findViewById(R.id.RVitemlistt);
        tvNo = findViewById(R.id.tvNo);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mContext)) {
            conatactMessage();
        } else {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }


    public void conatactMessage() {
        new HttpsRequest(Consts.GETCONTACTREQUEST, getParms(), mContext).stringPost("Contact", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {
                        myMessageDTOlist = new ArrayList<>();
                        Type listType = new TypeToken<List<GetMyMessageDTO>>() {
                        }.getType();

                        myMessageDTOlist = (ArrayList<GetMyMessageDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        showData();
                        tvNo.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    // ProjectUtils.showToast(mContext, msg);
                    ProjectUtils.pauseProgressDialog();
                    tvNo.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    public void showData() {
        mLayoutManager = new LinearLayoutManager(mContext);
        RVitemlistt.setLayoutManager(mLayoutManager);

        MyMessageAdapter notificationAdapter = new MyMessageAdapter(mContext, myMessageDTOlist);
        RVitemlistt.setAdapter(notificationAdapter);


    }

    public Map<String, String> getParms() {

        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, loginDTO.getId());

        Log.e("contact", params.toString());
        return params;
    }
}
