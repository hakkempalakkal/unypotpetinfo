package com.samyotech.petstand.activity.duties;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.samyotech.petstand.R;
import com.samyotech.petstand.SysApplication;
import com.samyotech.petstand.adapter.ReminderHistory;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.Reminder;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by welcome pc on 05-01-2018.
 */

public class HistoryReminder extends AppCompatActivity implements View.OnClickListener {
    public LoginDTO loginDTO;
    public SysApplication sysApplication;
    SharedPrefrence share;

    LinearLayout back;
    ListView reminder_list;
    private ReminderHistory adapter;
    List<Reminder> listItems = new ArrayList<>();
    ImageView no_reminder;
    private String TAG = HistoryReminder.class.getSimpleName();
    //ContentValue for request body
    HashMap<String, String> params = new HashMap<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(HistoryReminder.this);
        setContentView(R.layout.reminder_history);
        mContext = HistoryReminder.this;

        sysApplication = SysApplication.getInstance();
        share = SharedPrefrence.getInstance(mContext);
        loginDTO = share.getParentUser(Consts.LOGINDTO);
        initView();
        setParams();
    }

    public void initView() {
        reminder_list = (ListView) findViewById(R.id.reminder_list);
        no_reminder = (ImageView) findViewById(R.id.no_reminder);

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    public void setParams() {
        params.put(Consts.USER_ID, loginDTO.getId());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                finish();
                break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReminderHistory();

    }

    public void getReminderHistory() {
        params.put(Consts.CONFIRM_STATUS, "3");
        getReminders();
    }

    public void getReminders() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.HISTORY, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    handleResponse(response);
                    no_reminder.setVisibility(View.GONE);
                } else {
                    ProjectUtils.showLong(mContext, msg);
                    no_reminder.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void handleResponse(JSONObject response) {

            try {
                Reminder[] list = new Gson().fromJson(response.getJSONArray("data").toString(), Reminder[].class);
                if (list.length > 0) {
                    listItems = new LinkedList<>(Arrays.asList(list));
                    setListInAscendingOrder();
                    no_reminder.setVisibility(View.GONE);
                    adapter = new ReminderHistory(this, R.layout.duties_item, listItems);
                    reminder_list.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void clickOnItems(int position) {
        Intent intent = new Intent(this, ConfirmReminder.class);
        intent.putExtra("confirmObj", listItems.get(position));
        startActivity(intent);
    }

    public void setListInAscendingOrder() {
        Collections.sort(listItems, new Comparator<Reminder>() {
            public int compare(Reminder obj1, Reminder obj2) {
                // ## Ascending order
                // return obj1.getCategory().compareToIgnoreCase(obj2.getCategory()); // To compare string values
                return Long.valueOf(obj1.getAppointment_timestamp()).compareTo(Long.valueOf(obj2.getAppointment_timestamp())); // To compare integer values
                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
            }
        });
    }
}