package com.samyotech.petstand.activity.duties;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.SysApplication;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.Reminder;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by welcome pc on 29-12-2017.
 */

public class ConfirmReminder extends AppCompatActivity implements View.OnClickListener {
    public LoginDTO loginDTO;
    public SysApplication sysApplication;
    public SharedPrefrence share;
    public Context mContext;

    private CustomTextView cateName;
    private ImageView cate_img, ivPetImg;
    private CustomTextView date_time, remind_txt, repeat, description;
    private CardView cvTime, cvRemind_click, cvRepeat, cvRemark;
    private LinearLayout llback, llEdit, llComplete, llMissed, llDelete;
    public HashMap<String, String> params = new HashMap<>();

    private Reminder reminder;
    private String TAG = ConfirmReminder.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ConfirmReminder.this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.confirm_reminder);
        sysApplication = SysApplication.getInstance();
        share = SharedPrefrence.getInstance(this);
        loginDTO = share.getParentUser(Consts.LOGINDTO);
        mContext = ConfirmReminder.this;
        initView();
        reminder = (Reminder) getIntent().getSerializableExtra("confirmObj");
        setInfo();
    }

    public void initView() {
        ivPetImg = (ImageView) findViewById(R.id.ivPetImg);

        date_time = (CustomTextView) findViewById(R.id.date_time);
        repeat = (CustomTextView) findViewById(R.id.repeat);
        remind_txt = (CustomTextView) findViewById(R.id.remind_txt);
        cateName = (CustomTextView) findViewById(R.id.cateName);
        cate_img = (ImageView) findViewById(R.id.cate_img);
        description = (CustomTextView) findViewById(R.id.description);

        llback = (LinearLayout) findViewById(R.id.llback);
        llback.setOnClickListener(this);
        llEdit = (LinearLayout) findViewById(R.id.llEdit);
        llEdit.setOnClickListener(this);

        cvTime = (CardView) findViewById(R.id.cvTime);
        cvTime.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.fadein4));
        cvRemind_click = (CardView) findViewById(R.id.cvRemind_click);
        cvRemind_click.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.fadein3));
        cvRepeat = (CardView) findViewById(R.id.cvRepeat);
        cvRepeat.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.fadein2));
        cvRemark = (CardView) findViewById(R.id.cvRemark);
        cvRemark.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.fadein));

        llComplete = (LinearLayout) findViewById(R.id.llComplete);
        llComplete.setOnClickListener(this);
        llMissed = (LinearLayout) findViewById(R.id.llMissed);
        llMissed.setOnClickListener(this);
        llDelete = (LinearLayout) findViewById(R.id.llDelete);
        llDelete.setOnClickListener(this);

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
        params.put(Consts.REMINDERS_CONFIRM, reminder.getId());
        Glide.with(mContext)
                .load(reminder.getPet_img_path())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPetImg);


    }


    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
        switch (v.getId()) {
            case R.id.llComplete: {
                clickCompleted();
                break;
            }
            case R.id.llMissed: {
                clickMissed();
                break;
            }
            case R.id.cancelWheel: {
                clickMissed();
                break;
            }
            case R.id.llEdit: {
                clickEdit();
                break;
            }
            case R.id.llback: {
                finish();
                break;
            }
            case R.id.llDelete: {
                clickDelete();
                break;
            }
        }
    }


    public void clickCompleted() {
        params.put(Consts.CONFIRM_STATUS, "3");
        changeStatus();
    }

    public void clickEdit() {

        Intent intent = new Intent(this, EditReminder.class);
        intent.putExtra("reminderObj", reminder);
        startActivity(intent);
        finish();

    }

    public void clickMissed() {
        params.put(Consts.CONFIRM_STATUS, "2");
        changeStatus();
    }

    public void clickDelete() {
        clickDeleteAlert();
    }

    public void changeStatus() {
        confirmReminder();
    }

    public void confirmReminder() {
        params.put(Consts.USER_ID, loginDTO.getId());
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.CONFIRM_REMINDER, params, mContext).stringPost(TAG, new Helper() {
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


    public void clickDeleteAlert() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.app_icon)
                .setTitle(reminder.getCategory())
                .setMessage("Are you sure want to delete reminder?")
                .setPositiveButton("Yes, Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        params.put(Consts.CONFIRM_STATUS, "4");
                        changeStatus();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
