package com.samyotech.petstand.activity.duties;

import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.aigestudio.wheelpicker.WheelPicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.SysApplication;
import com.samyotech.petstand.adapter.PetPagerAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.models.ReminderCategory;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.DialogUtility;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by welcome pc on 27-12-2017.
 */

public class CreateReminder extends AppCompatActivity implements View.OnClickListener, WheelPicker.OnItemSelectedListener, ViewPager.OnPageChangeListener {
    private Context mContext;
    public LoginDTO loginDTO;
    private String TAG = AddReminder.class.getSimpleName();
    SysApplication sysApplication;
    SharedPrefrence share;
    LinearLayout save;
    CustomTextView cateName;
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
    private WheelPicker wheelCenter;
    boolean flag = false;
    public ReminderCategory reminderCategory;

    //ContentValue for request body
    HashMap<String, String> params = new HashMap<>();
    List<String> repeatItems = new ArrayList<>();
    List<String> remindItems = new ArrayList<>();

    //    private RecyclerView lvUpperScroll;
//    private LinearLayoutManager horizontalLayoutManagaer;
    // private PetAdapteReminder petAdapteReminder;
    private ArrayList<PetListDTO> petListDTOList;
    public HashMap<String, String> paramsPet = new HashMap<>();
    private LayoutInflater myInflater;
    public String SELECTED_PET = "";

    private ViewPager viewpagerBar;
    public int dotsCount;
    private ImageView[] dots;
    private LinearLayout viewPagerCountDots;
    private PetPagerAdapter petPagerAdapter;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  ProjectUtils.setStatusBarGradiant(CreateReminder.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.create_reminder_new);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mContext = CreateReminder.this;
        myInflater = LayoutInflater.from(mContext);
        sysApplication = SysApplication.getInstance();
        share = SharedPrefrence.getInstance(this);
        loginDTO = share.getParentUser(Consts.LOGINDTO);
        paramsPet.put(Consts.USER_ID, loginDTO.getId());
        initView();
        reminderCategory = (ReminderCategory) getIntent().getSerializableExtra("reminderObj");
        setInfo();
    }

    public void initView() {
//        lvUpperScroll = (RecyclerView) findViewById(R.id.lvUpperScroll);
//        horizontalLayoutManagaer
//                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        viewPagerCountDots = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        viewpagerBar = (ViewPager) findViewById(R.id.viewpagerBar);
        save = (LinearLayout) findViewById(R.id.save);
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
        scrollView = findViewById(R.id.scrollView);

        back.setOnClickListener(this);
//        doneWheel = (CustomTextView) findViewById(R.id.doneWheel);
//        doneWheel.setOnClickListener(this);
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

        getPetList();

    }

    public void setInfo() {
        cate_img.setImageResource(reminderCategory.getImgSource());
        cateName.setText(reminderCategory.getName());
        params.put(Consts.REMINDER_CATEGORY, reminderCategory.getName());
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.REMINDERS_REMARK, "");
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
                focusOnView();
                break;
            }
            case R.id.repeat_click: {
                clickRepeat();
                focusOnView();
                break;
            }
            case R.id.cancelWheel: {
                cancelWheel();
                break;
            }
            case R.id.save: {
                Log.e("PET_ID", SELECTED_PET);

                if (SELECTED_PET.equals("")) {

                    ProjectUtils.showLong(mContext, "Please select pet first");
                } else {
                    params.put(Consts.REMINDERS_PETID, SELECTED_PET);

                    create();

                }
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
        // TimeZone tz = TimeZone.getDefault();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("ZZZZ", Locale.getDefault());

        String localTime = date.format(currentLocalTime);
        params.put(Consts.REMINDERS_TIMEZONE, localTime);
        if (!params.containsKey(Consts.REMINDER_DATE))
            DialogUtility.showToast(this, "Please schedule your time.");
        else if (!params.containsKey(Consts.REMINDERS_REMIND))
            DialogUtility.showToast(this, "When you want to get reminded?");
        else if (!params.containsKey(Consts.REMINDERS_REPEAT))
            DialogUtility.showToast(this, "Tell us the repetition?");
        else if (!params.containsKey(Consts.REMINDERS_REMARK))
            DialogUtility.showToast(this, "any special request?");
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


    public void getPetList() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.GET_MY_PET, paramsPet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<PetListDTO>>() {
                    }.getType();
                    try {
                        petListDTOList = new ArrayList<>();
                        petListDTOList = (ArrayList<PetListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        petPagerAdapter = new PetPagerAdapter(CreateReminder.this, petListDTOList);
                        viewpagerBar.setAdapter(petPagerAdapter);
                        viewpagerBar.setPageTransformer(true, new ZoomOutSlideTransformer());
                        viewpagerBar.setCurrentItem(0);
                        viewpagerBar.setOnPageChangeListener(CreateReminder.this);
                        setPageViewIndicator(petPagerAdapter, viewpagerBar, viewPagerCountDots);

                        SELECTED_PET = petListDTOList.get(viewpagerBar.getCurrentItem()).getId();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("###onPageSelected, pos ", String.valueOf(position));
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(mContext.getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {

        } else {

        }
        SELECTED_PET = petListDTOList.get(position).getId();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setPageViewIndicator(PetPagerAdapter bpa, final ViewPager viewpagerBar, LinearLayout linearLayout) {

        Log.d("###setPageViewIndicator", " : called");
        dotsCount = bpa.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(mContext);
            dots[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20,
                    20
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    viewpagerBar.setCurrentItem(presentPosition);
                    return true;
                }

            });


            linearLayout.addView(dots[i], params);
        }

        dots[0].setImageDrawable(mContext.getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    private final void focusOnView() {
        try {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, bottomWheelView.getBottom());
                }
            });
        } catch (Exception e) {

        }
    }


}
