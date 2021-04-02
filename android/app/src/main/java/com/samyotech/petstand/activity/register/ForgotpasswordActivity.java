package com.samyotech.petstand.activity.register;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.samyotech.petstand.R;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;


import java.util.HashMap;
import java.util.Map;

public class ForgotpasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    CustomButton CBforgot;
    CustomEditText CETemail;
    Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ForgotpasswordActivity.this);
        setContentView(R.layout.activity_forgotpassword);
        mContext = ForgotpasswordActivity.this;
        init();
    }

    public void init() {
        ivBack = findViewById(R.id.ivBack);
        CETemail = findViewById(R.id.CETemail);
        CBforgot = findViewById(R.id.CBforgot);


        CBforgot.setOnClickListener(this);
        ivBack.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CBforgot:
                if (!validateEmail()) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        forgotPassword();
                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }
                }
                break;
            case R.id.ivBack:
                finish();
                break;

        }
    }

    public boolean validateEmail() {
        if (!ProjectUtils.isEmailValid(CETemail.getText().toString().trim())) {
            CETemail.setError("Please enter correct email.");
            CETemail.requestFocus();
            return false;
        } else {
            CETemail.setError(null);
            CETemail.clearFocus();
            return true;
        }
    }

    public void forgotPassword() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        AndroidNetworking.post(Consts.BASE_URL + Consts.FORGOTPASSWORD)
                .addBodyParameter(getParms())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtils.pauseProgressDialog();
                        JSONParser jsonParser = new JSONParser(mContext, response);

                        if (jsonParser.RESULT) {
                            try {
                                ProjectUtils.showToast(mContext, jsonParser.MESSAGE);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                            ProjectUtils.showToast(mContext, jsonParser.MESSAGE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public Map<String, String> getParms() {


        HashMap<String, String> params = new HashMap<>();

        params.put(Consts.EMAIL, CETemail.getText().toString().trim());


        Log.e("Login", params.toString());
        return params;
    }


}
