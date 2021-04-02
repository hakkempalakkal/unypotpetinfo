package com.samyotech.petstand.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;


public class ContactActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomEditText etName, etMobile, etMessage;
    private CustomButton btnSubmit;
    private CustomTextView tvChar;
    private LinearLayout back;
    private SharedPrefrence prefrence;
    private Context mContext;
    private LoginDTO loginDTO;
    private String TAG = ContactActivity.class.getSimpleName();
    private HashMap<String, String> values = new HashMap<>();
    private PetListDTO petListDTO;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ContactActivity.this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_contact);
        mContext = ContactActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        values.put(Consts.USER_ID, loginDTO.getId());

        if (getIntent().hasExtra(Consts.PET_PROFILE)) {
            petListDTO = (PetListDTO) getIntent().getSerializableExtra(Consts.PET_PROFILE);
            values.put(Consts.USER_ID_OWNER, petListDTO.getUser_id());
            values.put(Consts.PET_ID, petListDTO.getId());
        }
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etMessage = findViewById(R.id.etMessage);
        tvChar = findViewById(R.id.tvChar);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        etMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                tvChar.setText(String.valueOf(s.length()) + "/500");

            }
        });

        etName.setText(loginDTO.getFirst_name());
        etMobile.setText(loginDTO.getMobile_no());
    }

    public void submitForm() {
        if (!validateMessage()) {
            return;
        } else {
            addRequestBuy();
        }
    }


    public boolean validateMessage() {
        if (etMessage.getText().toString().trim().length() <= 0) {
            etMessage.setError("please enter your message");
            etMessage.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btnSubmit:
                submitForm();
                break;
        }

    }

    public void addRequestBuy() {
        values.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(etMessage));
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ADD_CONTACT, values, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
