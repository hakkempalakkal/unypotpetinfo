package com.samyotech.petstand.fragment.userauth;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.register.LoginSignupactivity;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pushpraj on 19/2/18.
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {

    View view;
    private CustomButton CBsignup;

    private CustomEditText CETYname, CETCpassword, CETemail, CETpassword;

    LoginDTO loginDTO = new LoginDTO();
    LinearLayout LLvisible,LLCvisible;
    SharedPrefrence sharedPrefrence;
    ImageView text_visible3,text_Cvisible3;
    private boolean isHide = false;
    private boolean isCHide = false;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    LoginFragment loginFragment = new LoginFragment();


    private SharedPreferences userDetails;
 //   private TelephonyManager telephonyManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        init();
        userDetails = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", userDetails.getString(Consts.TOKAN, ""));
      /*  telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        Log.e("LOGIN_ID", "my id: " + telephonyManager.getDeviceId() + "  co" + telephonyManager.getSimCountryIso());*/
        return view;
    }

    private void init() {
        CBsignup = (CustomButton) view.findViewById(R.id.CBsignup);
        CETYname = (CustomEditText) view.findViewById(R.id.CETYname);
        CETCpassword = (CustomEditText) view.findViewById(R.id.CETCpassword);
        CETemail = (CustomEditText) view.findViewById(R.id.CETemail);
        CETpassword = (CustomEditText) view.findViewById(R.id.CETpassword);
        LLvisible = (LinearLayout) view.findViewById(R.id.LLvisible);
        LLCvisible = (LinearLayout) view.findViewById(R.id.LLCvisible);

        text_visible3 = (ImageView) view.findViewById(R.id.text_visible3);
        text_Cvisible3 = (ImageView) view.findViewById(R.id.text_Cvisible3);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        CBsignup.setOnClickListener(this);
        LLvisible.setOnClickListener(this);
        LLCvisible.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CBsignup:
                submit();
                break;
            case R.id.LLvisible:
                eyePassword();
                break;

                case R.id.LLCvisible:
                eyeCPassword();
                break;

        }
    }


    private void eyePassword() {
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
    private void eyeCPassword() {
        if (isCHide) {
            text_Cvisible3.setImageResource(R.drawable.visible_black);
            CETCpassword.setTransformationMethod(null);
            isCHide = false;
        } else {
            text_Cvisible3.setImageResource(R.drawable.invisible_black);
            CETCpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isCHide = true;
        }
    }

    private void submit() {
        if (!validateName()) {
            return;
        } else if (!validateEmail()) {
            return;
        } else if (!validatePassword(CETpassword)) {
            return;
        } else if (!checkpass()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(getActivity())) {
                signUp();
            } else {
                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
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


    public boolean validateName() {
        if (!ProjectUtils.isEditTextFilled(CETYname)) {
            CETYname.setError("Please Enter name");
            CETYname.requestFocus();
            return false;
        } else {
            CETYname.setError(null);
            CETYname.clearFocus();
            return true;
        }
    }


    public boolean validatePassword(EditText editText) {
        if (editText.getText().toString().trim().equalsIgnoreCase("")) {
            editText.setError(getResources().getString(R.string.password_val));
            editText.requestFocus();
            return false;
        } else {
            if (!ProjectUtils.isPasswordValid(editText.getText().toString().trim())) {
                editText.setError(getResources().getString(R.string.password_val1));
                editText.requestFocus();
                return false;
            } else {
                editText.setError(null);
                editText.clearFocus();
                return true;
            }
        }

    }

    private boolean checkpass() {
        if (CETCpassword.getText().toString().trim().equals("")) {
            CETCpassword.setError(getResources().getString(R.string.password_val1));
            return false;
        } else if (!CETpassword.getText().toString().trim().equals(CETCpassword.getText().toString().trim())) {
            CETCpassword.setError(getResources().getString(R.string.password_val2));
            return false;
        }
        return true;
    }

    public void signUp() {

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        AndroidNetworking.post(Consts.BASE_URL + Consts.SIGN_UP)
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
                                FragmentTransaction signupTransaction = fragmentManager.beginTransaction();
                                signupTransaction.setCustomAnimations(R.anim.pop_enter, R.anim.push_out_left);
                                signupTransaction.replace(R.id.logisignframe, loginFragment, "login");
                                signupTransaction.commitAllowingStateLoss();

                                CETYname.setText("");
                                CETCpassword.setText("");
                                CETemail.setText("");
                                CETpassword.setText("");

                                LoginSignupactivity.IVsignup.setVisibility(View.GONE);
                                LoginSignupactivity.IVlog.setVisibility(View.VISIBLE);
                                LoginSignupactivity.CTVBsignup.setTextColor(getResources().getColor(R.color.gray));
                                LoginSignupactivity.CTVBlogin.setTextColor(getResources().getColor(R.color.black));

                                ProjectUtils.showToast(getActivity(), jsonParser.MESSAGE);
                            } catch (Exception e) {
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
        params.put(Consts.FIRST_NAME, CETYname.getText().toString().trim());
        params.put(Consts.PASSWORD, CETpassword.getText().toString().trim());

        Log.e("Login", params.toString());
        return params;
    }
}
