package com.samyotech.petstand.activity.duties;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aigestudio.wheelpicker.WheelPicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.samyotech.petstand.R;
import com.samyotech.petstand.SysApplication;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.Reminder;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.DialogUtility;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by welcome pc on 03-01-2018.
 */

public class EditReminder extends AppCompatActivity implements View.OnClickListener, WheelPicker.OnItemSelectedListener {
    public LoginDTO loginDTO;
    SysApplication sysApplication;
    SharedPrefrence share;

    CustomTextView save, cateName;
    ImageView cate_img;
    RelativeLayout schedule_time_click;
    CustomTextView date_time;

    RelativeLayout remind_click;
    CustomTextView remind_txt;

    RelativeLayout repeat_click;
    CustomTextView repeat;
    EditText description;

    LinearLayout cancelWheel;
    LinearLayout bottomWheelView;
    LinearLayout back;
    //CustomTextView doneWheel;

    private WheelPicker wheelCenter;
    //    List<ReminderAdvance> remindLst = new ArrayList<>();
//    List<ReminderRepeat> repeatLst = new ArrayList<>();
    boolean flag = false;

    //ReminderCategory object for Cat name and Image
    Reminder reminder;

    //ContentValue for request body
    HashMap<String, String> params = new HashMap<>();
    List<String> repeatItems = new ArrayList<>();
    List<String> remindItems = new ArrayList<>();

    TimeZone tz;
    public Context mContext;
    private String TAG = EditReminder.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(EditReminder.this);
        setContentView(R.layout.edit_reminder);
        mContext = EditReminder.this;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sysApplication = SysApplication.getInstance();
        share = SharedPrefrence.getInstance(this);
        loginDTO = share.getParentUser(Consts.LOGINDTO);
        tz = TimeZone.getDefault();
        initView();
        reminder = (Reminder) getIntent().getSerializableExtra("reminderObj");
        setInfo();
    }

    public void initView() {
        save = (CustomTextView) findViewById(R.id.save);
        save.setOnClickListener(this);
        date_time = (CustomTextView) findViewById(R.id.date_time);
        repeat = (CustomTextView) findViewById(R.id.repeat);
        remind_txt = (CustomTextView) findViewById(R.id.remind_txt);
        cateName = (CustomTextView) findViewById(R.id.cateName);
        cate_img = (ImageView) findViewById(R.id.cate_img);
        description = (EditText) findViewById(R.id.description);
        schedule_time_click = (RelativeLayout) findViewById(R.id.schedule_time_click);
        schedule_time_click.setOnClickListener(this);
        remind_click = (RelativeLayout) findViewById(R.id.remind_click);
        remind_click.setOnClickListener(this);
        repeat_click = (RelativeLayout) findViewById(R.id.repeat_click);
        repeat_click.setOnClickListener(this);

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        cancelWheel = (LinearLayout) findViewById(R.id.cancelWheel);
        cancelWheel.setOnClickListener(this);
        bottomWheelView = (LinearLayout) findViewById(R.id.bottomWheelView);
        wheelCenter = (WheelPicker) findViewById(R.id.main_wheel_center);
        wheelCenter.setOnItemSelectedListener(this);
        bottomWheelView.setVisibility(View.GONE);

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                params.put(Consts.REMINDERS_REMARK, s.toString());
            }
        });

    }

    public void setInfo() {
        cate_img.setImageResource(sysApplication.reminderMapping.get(reminder.getCategory()));
        cateName.setText(reminder.getCategory());

        date_time.setText(reminder.getDate_string());
        remind_txt.setText(ProjectUtils.getRemind(reminder.getAdvance()));
        repeat.setText(ProjectUtils.getRepeat(reminder.getRepeat()));
        description.setText(reminder.getRemark());

        params.put(Consts.REMINDER_CATEGORY, reminder.getCategory());
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.REMINDERS_PETID, reminder.getPet_id());
        params.put(Consts.REMINDER_APP_ID, reminder.getId());
        params.put(Consts.REMINDERS_REMARK, reminder.getRemark());
        params.put(Consts.REMINDER_DATE, reminder.getDate_string());
        params.put(Consts.REMINDERS_TIMEZONE, tz.getDisplayName(false, TimeZone.SHORT));
        params.put(Consts.REMINDERS_REPEAT, reminder.getRepeat());
        params.put(Consts.REMINDERS_REMIND, reminder.getAdvance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] repeat_array = getResources().getStringArray(R.array.repeat);
        String[] remind_array = getResources().getStringArray(R.array.remind);
        repeatItems = Arrays.asList(repeat_array);
        remindItems = Arrays.asList(remind_array);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_event));
        switch (v.getId()) {
            case R.id.schedule_time_click: {
                clickScheduleDateTime();
                break;
            }
            case R.id.remind_click: {
                clickRemind();
                break;
            }
            case R.id.repeat_click: {
                clickRepeat();
                break;
            }
            case R.id.cancelWheel: {
                cancelWheel();
                break;
            }
            case R.id.save: {
                create();
                break;
            }
            case R.id.back: {
                finish();
                break;
            }
        }
    }

    public void clickScheduleDateTime() {
        new SingleDateAndTimePickerDialog.Builder(this)
                .curved().backgroundColor(getResources().getColor(R.color.white))
                .mainColor(getResources().getColor(R.color.point_color))
                .titleTextColor(getResources().getColor(R.color.white))
                .mustBeOnFuture()
                .title("Schedule Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        date_time.setText(ProjectUtils.getReminderDateTimeFormat(date));
                        params.put(Consts.REMINDER_DATE, date_time.getText().toString());
                    }
                })
                .display();
    }

    public void clickRemind() {
        bottomWheelView.setVisibility(View.VISIBLE);
        wheelCenter.performClick();
        wheelCenter.setData(remindItems);
        flag = false;
    }

    public void cancelWheel() {
        bottomWheelView.setVisibility(View.GONE);
    }
//    public void doneWheel() {
//       int pos = wheelCenter.getCurrentItemPosition();
//        wheelCenter.getCurrentItemPosition();
//        wheelCenter.setVisibility(View.VISIBLE);
//        wheelCenter.performClick();
//    }

    public void clickRepeat() {
        bottomWheelView.setVisibility(View.VISIBLE);
        wheelCenter.performClick();
        wheelCenter.setData(repeatItems);
        flag = true;
    }

    public void create() {
        params.put(Consts.REMINDERS_TIMEZONE, tz.getDisplayName(false, TimeZone.SHORT));
        if (!params.containsKey(Consts.REMINDER_DATE))
            DialogUtility.showToast(this, "Please schedule your time.");
        else if (!params.containsKey(Consts.REMINDERS_REMIND))
            DialogUtility.showToast(this, "When you want to get reminded?");
        else if (!params.containsKey(Consts.REMINDERS_REPEAT))
            DialogUtility.showToast(this, "Tell us the repetition?");
        else if (!params.containsKey(Consts.REMINDERS_REMARK))
            DialogUtility.showToast(this, "Any special request?");
        else
            createReminder();
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        String text = "";
        switch (picker.getId()) {

            case R.id.main_wheel_center:
                if (flag) {
                    int value = sysApplication.getRepeatList().get(position).getValue();
                    params.put(Consts.REMINDERS_REPEAT, value + "");
                    repeat.setText(String.valueOf(data));
                } else {
                    int value = sysApplication.getRemindList().get(position).getValue();
                    params.put(Consts.REMINDERS_REMIND, value + "");
                    remind_txt.setText(String.valueOf(data));
                }

                break;

        }
        // Toast.makeText(this, text + String.valueOf(data), Toast.LENGTH_SHORT).show();
    }

    public void createReminder() {

        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.CREATE_REMINDER, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }
}


/*
*
* */