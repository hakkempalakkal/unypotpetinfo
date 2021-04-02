package com.samyotech.petstand.fragment.userauth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.google.gson.Gson;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.activity.UserProfile.UserProfileActivity;
import com.samyotech.petstand.activity.register.ForgotpasswordActivity;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment implements View.OnClickListener {

    View view;
    private CustomButton CBlogin;
    private CustomEditText CETemail, CETpassword;
    CustomTextViewBold CTVforgot;
    LoginDTO loginDTO;
    SharedPrefrence sharedPrefrence;
    ImageView text_visible3;
    LinearLayout LLvisible;
    private boolean isHide = false;
    String strcountryCode;
    private SharedPreferences userDetails;
   // private TelephonyManager telephonyManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDetails = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", userDetails.getString(Consts.TOKAN, ""));
     //   telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
      //  Log.e("LOGIN_ID", "my id: " + telephonyManager.getDeviceId() + "  co" + telephonyManager.getSimCountryIso());
        init();
        return view;
    }

    public void init() {
        CBlogin = (CustomButton) view.findViewById(R.id.CBlogin);
        text_visible3 = (ImageView) view.findViewById(R.id.text_visible3);
        LLvisible = (LinearLayout) view.findViewById(R.id.LLvisible);

        CETemail = (CustomEditText) view.findViewById(R.id.CETemail);
        CETpassword = (CustomEditText) view.findViewById(R.id.CETpassword);

        CTVforgot = (CustomTextViewBold) view.findViewById(R.id.CTVforgot);

        CBlogin.setOnClickListener(this);
        CTVforgot.setOnClickListener(this);
        LLvisible.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CBlogin:
                loginSubmit();
                break;
            case R.id.CTVforgot:
                startActivity(new Intent(getActivity(), ForgotpasswordActivity.class));
                break;

            case R.id.LLvisible:
                passwordShow();
                break;
        }
    }

    private void passwordShow() {
        if (isHide) {
            text_visible3.setImageResource(R.drawable.visible_black);
            CETpassword.setTransformationMethod(null);
            isHide = false;
        } else {
            text_visible3.setImageResource(R.drawable.invisible_black);
            CETpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isHide = true;
        }

    }

    private void loginSubmit() {
        if (!validateEmail()) {
            return;
        } else if (!validatePassword()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(getActivity())) {
                login();
            } else {
                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
            }
        }

    }

    public boolean validatePassword() {
        if (CETpassword.getText().toString().trim().equalsIgnoreCase("")) {
            CETpassword.setError(getString(R.string.password_val));
            CETpassword.requestFocus();
            return false;
        } else {
            if (!ProjectUtils.isPasswordValid(CETpassword.getText().toString().trim())) {
                CETpassword.setError(getString(R.string.password_val1));
                CETpassword.requestFocus();
                return false;
            } else {
                CETpassword.setError(null);
                CETpassword.clearFocus();
                return true;
            }
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


    public void login() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        AndroidNetworking.post(Consts.BASE_URL + Consts.LOGIN)
                .addBodyParameter(getParms())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.pauseProgressDialog();
                        JSONParser jsonParser = new JSONParser(getActivity(), response);

                        if (jsonParser.RESULT) {
                            try {
                                loginDTO = new Gson().fromJson(response.getJSONObject("data").toString(), LoginDTO.class);
                                sharedPrefrence.setParentUser(loginDTO, Consts.LOGINDTO);
                                next();
                                ProjectUtils.showToast(getActivity(), jsonParser.MESSAGE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            ProjectUtils.showToast(getActivity(), jsonParser.MESSAGE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Log", anError.getErrorBody());
                        ProjectUtils.pauseProgressDialog();
                    }
                });
    }

    public Map<String, String> getParms() {

        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.EMAIL, CETemail.getText().toString().trim());
        params.put(Consts.PASSWORD, CETpassword.getText().toString().trim());
        params.put(Consts.DEVICE_TOKAN, userDetails.getString(Consts.TOKAN, ""));
        params.put(Consts.DEVICE_ID, "123456");
        params.put(Consts.OS_TYPE, "android");

        Log.e("Login", params.toString());
        return params;
    }

    public void next() {
        if (NetworkManager.isConnectToInternet(getActivity())) {
            if (!loginDTO.getAddress().equalsIgnoreCase("")) {
                ProjectUtils.showLong(getActivity(), "Login Successful");
                sharedPrefrence.setBooleanValue(SharedPrefrence.IS_LOGIN, true);
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            } else {
                sharedPrefrence.setBooleanValue(SharedPrefrence.IS_LOGIN, true);
                ProjectUtils.showLong(getActivity(), "Please Update your profile.");
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                intent.putExtra("FlagLogin", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        } else {
            ProjectUtils.showLong(getActivity(), "Please enable your internet connection.");
        }
    }


}
