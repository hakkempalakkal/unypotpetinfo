package com.samyotech.petstand.activity.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mobileET;
    private CardView submitIV;
    private Context mContext;
    private SharedPrefrence prefrence;
    private String otp;

    private SharedPreferences userDetails;
  //  private TelephonyManager telephonyManager;
    private CountryCodePicker countryCodePicker;
    HashMap<String, String> parms = new HashMap<>();
    private String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(LoginActivity.this);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDetails = LoginActivity.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", userDetails.getString(Consts.DEVICE_TOKAN, ""));
        //telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        initView();
    }

    public void initView() {
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        mobileET = (EditText) findViewById(R.id.mobileET);
        submitIV = findViewById(R.id.submitBTN);
        submitIV.setOnClickListener(this);




    }



    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_event));
        switch (v.getId()) {
            case R.id.submitBTN:

                Random otp1 = new Random();
                StringBuilder builder = new StringBuilder();
                for (int count = 0; count <= 3; count++) {
                    builder.append(otp1.nextInt(10));
                }
                otp = builder.toString();
                submitForm();
                break;
        }
    }

    public void submitForm() {
        if (!validatePhone()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                getLogin();

            } else {
                ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validatePhone() {
        if (mobileET.getText().toString().trim().equalsIgnoreCase("")) {
            mobileET.setError("Please enter mobile number");
            mobileET.requestFocus();
            return false;
        } else {
            if (!ProjectUtils.isPhoneNumberValid(mobileET.getText().toString().trim())) {
                mobileET.setError("Please enter valid mobile number");
                mobileET.requestFocus();
                return false;
            } else {
                mobileET.setError(null);
                mobileET.clearFocus();
                return true;
            }
        }
    }


    public void getLogin() {
        parms.put(Consts.MOBILE_NO, mobileET.getText().toString().trim());
        parms.put(Consts.DEVICE_ID, "12345");
        parms.put(Consts.OTP, otp);
        parms.put(Consts.COUNTRY_CODE, countryCodePicker.getSelectedCountryCode() + "");
        parms.put(Consts.OS_TYPE, "android");
        parms.put(Consts.DEVICE_TOKAN, userDetails.getString(Consts.DEVICE_TOKAN, ""));
        prefrence.setValue(Consts.OTP, otp);

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.SEND_OTP, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);

                    try {
                        LoginDTO loginDTO = new Gson().fromJson(response.getJSONObject("data").toString(), LoginDTO.class);
                        prefrence.setParentUser(loginDTO, Consts.LOGINDTO);

                        Intent in = new Intent(mContext, OTPActivity.class);
                        in.putExtra("number", "+" + countryCodePicker.getSelectedCountryCode() + "-" + mobileET.getText().toString());
                        in.putExtra("otp", otp);
                        startActivity(in);
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
    public void onBackPressed() {
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.walk_icon)
                .setTitle(R.string.app_name)
                .setMessage(getResources().getString(R.string.close_msg))
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

}
