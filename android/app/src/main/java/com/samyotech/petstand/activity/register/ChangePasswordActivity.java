package com.samyotech.petstand.activity.register;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.samyotech.petstand.R;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;


import java.util.HashMap;
import java.util.Map;


public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private Context mContext;
    private CustomButton cbSubmit;
    private CustomEditText cetNewPass, cetConformPass, cetOldPass;
    private ImageView text_visible2, text_visible3, text_visibleold;
    private boolean isHide = false;
    private boolean isHideold = false;
    SharedPrefrence prefrence;
    LoginDTO loginDTO;
    LinearLayout LLrepass, LLnewpass, LLOldpass;
    CustomTextViewBold ctvprofilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ChangePasswordActivity.this);
        setContentView(R.layout.activity_change_password);

        mContext = ChangePasswordActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
    }

    public void init() {
        /*Header Name*/
        ivBack = (ImageView) findViewById(R.id.ivBack);
        LLnewpass = (LinearLayout) findViewById(R.id.LLnewpass);
        LLrepass = (LinearLayout) findViewById(R.id.LLrepass);
        LLOldpass = (LinearLayout) findViewById(R.id.LLOldpass);


        ivBack.setOnClickListener(this);
        /*Header Name*/

        cbSubmit = (CustomButton) findViewById(R.id.cbSubmit);
        cbSubmit.setOnClickListener(this);

        cetNewPass = (CustomEditText) findViewById(R.id.cetNewPass);
        cetOldPass = (CustomEditText) findViewById(R.id.cetOldPass);

        cetConformPass = (CustomEditText) findViewById(R.id.cetConformPass);

        text_visible2 = (ImageView) findViewById(R.id.text_visible2);
        LLnewpass.setOnClickListener(this);
        text_visible3 = (ImageView) findViewById(R.id.text_visible3);
        text_visibleold = (ImageView) findViewById(R.id.text_visibleold);
        LLrepass.setOnClickListener(this);
        LLOldpass.setOnClickListener(this);

    }

    public void submitForm() {
        if (!validateNewPin(cetOldPass, "Please Enter Old Password.")) {
            return;
        } else if (!validateNewPin(cetNewPass, getString(R.string.new_pwd_required_validation))) {
            return;
        } else if (!validateNewPin(cetConformPass, getString(R.string.confirm_pwd_required_validation))) {
            return;
        } else {
            checkpass();
        }
    }


    public boolean validateNewPin(EditText editText, String msg) {

        if (editText.getText().toString().trim().equalsIgnoreCase("")) {
            editText.setError(msg);
            editText.requestFocus();
            return false;
        } else {
            if (!ProjectUtils.isPasswordValid(editText.getText().toString().trim())) {
                editText.setError(getString(R.string.password_val1));
                editText.requestFocus();
                return false;
            } else {
                editText.setError(null);
                editText.clearFocus();
                return true;
            }
        }
    }

    public void checkpass() {

        if (cetNewPass.getText().toString().trim().equals("")) {
            cetNewPass.setError(getString(R.string.new_pwd_required_validation));
        } else if (cetConformPass.getText().toString().trim().equals("")) {
            cetConformPass.setError(getString(R.string.confirm_pwd_required_validation));
        } else if (!cetNewPass.getText().toString().trim().equals(cetConformPass.getText().toString().trim())) {
            cetConformPass.setError(getString(R.string.confirm_pwd_validation));
        } else {
            cetConformPass.setError(null);//removes error
            cetConformPass.clearFocus();    //clear focus from edittext
            if (NetworkManager.isConnectToInternet(mContext)) {
                changePassword();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.cbSubmit:
                submitForm();
                break;
            case R.id.LLOldpass:
                oldPasswordShow();
                break;
            case R.id.LLnewpass:
                newPasswordShow();
                break;
            case R.id.LLrepass:
                reConformPasswordShow();
                break;


        }
    }

    private void reConformPasswordShow() {
        if (isHide) {
            text_visible3.setImageResource(R.drawable.visible_black);
            cetConformPass.setTransformationMethod(null);
            isHide = false;
        } else {
            text_visible3.setImageResource(R.drawable.invisible_black);
            cetConformPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isHide = true;
        }
    }

    private void newPasswordShow() {
        if (isHide) {
            text_visible2.setImageResource(R.drawable.visible_black);
            cetNewPass.setTransformationMethod(null);
            isHide = false;
        } else {
            text_visible2.setImageResource(R.drawable.invisible_black);
            cetNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isHide = true;
        }
    }

    private void oldPasswordShow(){
        if (isHideold) {
            text_visibleold.setImageResource(R.drawable.visible_black);
            cetOldPass.setTransformationMethod(null);
            isHideold = false;
        } else {
            text_visibleold.setImageResource(R.drawable.invisible_black);
            cetOldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isHideold = true;
        }
    }

    public void changePassword() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        AndroidNetworking.post(Consts.BASE_URL + Consts.CHANGE_PASSWORD)
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
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.PASSWORD, cetOldPass.getText().toString().trim());
        params.put(Consts.NEW_PASSWORD, cetNewPass.getText().toString().trim());


        Log.e("Login", params.toString());
        return params;
    }


}
