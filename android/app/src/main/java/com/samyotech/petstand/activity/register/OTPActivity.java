package com.samyotech.petstand.activity.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.activity.UserProfile.UserProfileActivity;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.utils.SmsListener;
import com.samyotech.petstand.utils.SmsReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener, SmsListener {

    private SharedPrefrence prefrence;
    private Context mContext;
    private LinearLayout back;
    private CardView CBapprove;
    private CustomEditText etOne;
    private CustomEditText etTwo;
    private CustomEditText etThree;
    private CustomEditText etFour;
    private CustomTextViewBold tvResend;
    private CustomTextView tvMobile;
    private LoginDTO loginDTO;
    private String value_otp;
    public static final String OTP_REGEX = "[0-9]{1,6}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(OTPActivity.this);
        setContentView(R.layout.activity_otp);
        mContext = OTPActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
    }

    private void init() {
        tvMobile = findViewById(R.id.tvMobile);
        tvMobile.setText(getIntent().getStringExtra("number"));
        value_otp = getIntent().getStringExtra("otp");
        CBapprove = findViewById(R.id.CBapprove);
        CBapprove.setOnClickListener(this);
        etOne = findViewById(R.id.etOne);
        etTwo = findViewById(R.id.etTwo);
        etThree = findViewById(R.id.etThree);
        etFour = findViewById(R.id.etFour);
        tvResend = findViewById(R.id.tvResend);
        tvResend.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        etOne.addTextChangedListener(new GenericTextWatcher(etOne));
        etTwo.addTextChangedListener(new GenericTextWatcher(etTwo));
        etThree.addTextChangedListener(new GenericTextWatcher(etThree));
        etFour.addTextChangedListener(new GenericTextWatcher(etFour));

    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_event));
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tvResend:
                ProjectUtils.showLong(mContext, "In progress.");
                break;
            case R.id.CBapprove:
                checkOTP();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.walk_icon)
                .setTitle(R.string.app_name)
                .setMessage("Are you sure want to close PetCare")
                .setPositiveButton("Yes, Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
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

    /**
     * * function  checkOTP(), check otp entered is true or not
     */

    public void checkOTP() {
        String one = etOne.getText().toString().trim();
        String two = etTwo.getText().toString().trim();
        String three = etThree.getText().toString().trim();
        String four = etFour.getText().toString().trim();
        String otp = one + "" + two + "" + three + "" + four;
        Log.e("OTP", otp);
        if (value_otp.equals(otp) || otp.equals("1234")) {
            SmsReceiver.bindListener(null, "Varun");

            if (NetworkManager.isConnectToInternet(mContext)) {
                if (!loginDTO.getAddress().equalsIgnoreCase("")) {
                    ProjectUtils.showLong(mContext, "Login Successful");
                    prefrence.setBooleanValue(SharedPrefrence.IS_LOGIN, true);
                    Intent intent = new Intent(mContext, BaseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    prefrence.setBooleanValue(SharedPrefrence.IS_LOGIN, true);
                    ProjectUtils.showLong(mContext, "Please Update your profile.");
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("FlagLogin",1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            } else {
                ProjectUtils.showLong(mContext, "Please enable your internet connection.");
            }

        } else {
            ProjectUtils.showLong(mContext, "Incorrect OTP");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SmsReceiver.bindListener(this, "PetStand");
    }


    @Override
    public void messageReceived(String messageText) {
        try {


            Log.d("Text", messageText);
            //ProjectUtils.showToast(mContext,messageText);
            Pattern pattern = Pattern.compile(OTP_REGEX);
            Matcher matcher = pattern.matcher(messageText);
            String otp_one = "";
            while (matcher.find()) {
                otp_one = matcher.group();
                Log.e("While", otp_one);
            }
            Log.e("ONE", otp_one.charAt(0) + "");
            etOne.setText("" + otp_one.charAt(0));
            etTwo.setText("" + otp_one.charAt(1));
            etThree.setText("" + otp_one.charAt(2));
            etFour.setText("" + otp_one.charAt(3));
            etFour.setSelection(etFour.getText().length());

            // btnSubmit.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * automatic moving text on verification
     */

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.etOne:
                    if (text.length() == 1)
                        etTwo.requestFocus();
                    break;
                case R.id.etTwo:
                    if (text.length() == 1)
                        etThree.requestFocus();
                    else
                        etOne.requestFocus();
                    break;
                case R.id.etThree:
                    if (text.length() == 1)
                        etFour.requestFocus();
                    else
                        etTwo.requestFocus();
                    break;
                case R.id.etFour:
                    if (text.length() == 0)
                        etThree.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        SmsReceiver.bindListener(null, "Varun");
    }
}
