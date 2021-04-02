package com.samyotech.petstand.activity.food;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;

import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class WriteReview extends AppCompatActivity implements View.OnClickListener {
    private RatingBar rbReview;
    private CustomTextView tvCharReview;
    private CustomEditText yourReviewET, etTitle;
    private CardView submitBTN;
    private Context mContext;
    private SharedPrefrence prefrence;
    private float myrating;
    private String id = "";
    private LoginDTO loginDTO;
    private HashMap<String, String> parmsWriteReview = new HashMap<>();
    private String TAG = WriteReview.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_write_review);
        mContext = WriteReview.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        if (getIntent().hasExtra(Consts.PRODUCT_ID)) {
            id = getIntent().getStringExtra(Consts.PRODUCT_ID);
            parmsWriteReview.put(Consts.PRODUCT_ID, id);
        }

        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        parmsWriteReview.put(Consts.USER_ID, loginDTO.getId());
        init();
    }

    public void init() {
        rbReview = (RatingBar) findViewById(R.id.rbReview);
        tvCharReview = (CustomTextView) findViewById(R.id.tvCharReview);
        yourReviewET = (CustomEditText) findViewById(R.id.yourReviewET);
        etTitle = (CustomEditText) findViewById(R.id.etTitle);
        submitBTN = (CardView) findViewById(R.id.submitBTN);

        submitBTN.setOnClickListener(this);
        /*
         *
         * handling rating bar
         *
         */

        rbReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myrating = ratingBar.getRating();
            }
        });

        yourReviewET.addTextChangedListener(new TextWatcher() {

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
                tvCharReview.setText(String.valueOf(s.length()) + "/200");

            }
        });

    }

    public void submit() {
        if (!validateTitle()) {
            return;
        } else if (!validateReview()) {
            return;
        } else {
            parmsWriteReview.put(Consts.RATING, String.valueOf(myrating));
            parmsWriteReview.put(Consts.TITLE, ProjectUtils.getEditTextValue(etTitle));
            parmsWriteReview.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(yourReviewET));
            writeReview();
        }
    }

    /////checking validation
    public boolean validateReview() {
        if (yourReviewET.getText().toString().trim().length() <= 0) {
            yourReviewET.setError(getString(R.string.message_validation));
            yourReviewET.requestFocus();
            return false;
        } else {
            yourReviewET.setError(null);
            yourReviewET.clearFocus();
            return true;
        }
    }

    public boolean validateTitle() {
        if (etTitle.getText().toString().trim().length() <= 0) {
            etTitle.setError(getString(R.string.title_validation));
            etTitle.requestFocus();
            return false;
        } else {
            etTitle.setError(null);
            etTitle.clearFocus();
            return true;
        }
    }


    /*
     *
     * method  onclick()  is handling  the button event
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBTN:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    submit();
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
                }

                break;
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public void writeReview() {
        new HttpsRequest(Consts.REVIEW_PRODUCT, parmsWriteReview, mContext).stringPost(TAG, new Helper() {
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