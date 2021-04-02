package com.samyotech.petstand.activity.wall;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PublicWallDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPost extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ImageView ivBack, ivContentImg;
    private CustomEditText etComment;
    private CircleImageView ivUser;
    private String TAG = EditPost.class.getSimpleName();
    private PublicWallDTO publicWallDTO;
    private LinearLayout llSave;
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(EditPost.this);
        setContentView(R.layout.activity_edit_post);
        mContext = EditPost.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        if (getIntent().hasExtra(Consts.PUBLIC_WALL_DTO)) {
            publicWallDTO = (PublicWallDTO) getIntent().getSerializableExtra(Consts.PUBLIC_WALL_DTO);
        }
        init();
    }

    public void init() {
        ivUser = findViewById(R.id.ivUser);
        etComment = findViewById(R.id.etComment);
        ivContentImg = findViewById(R.id.ivContentImg);
        ivBack = findViewById(R.id.ivBack);
        llSave = findViewById(R.id.llSave);
        ivBack.setOnClickListener(this);
        llSave.setOnClickListener(this);

        etComment.setText(publicWallDTO.getContent());
        etComment.setSelection(etComment.getText().length());
        Glide
                .with(mContext)
                .load(publicWallDTO.getUser_image())
                .placeholder(R.drawable.dummy_user)
                .override(80, 80)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivUser);

        if (publicWallDTO.getPostType().equalsIgnoreCase("text")) {
            ivContentImg.setVisibility(View.GONE);
        } else if (publicWallDTO.getPostType().equalsIgnoreCase("image")) {
            ivContentImg.setVisibility(View.VISIBLE);
            Glide
                    .with(mContext)
                    .load(publicWallDTO.getMedia())
                    .placeholder(R.drawable.app_logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivContentImg);

        } else if (publicWallDTO.getPostType().equalsIgnoreCase("video")) {
            ivContentImg.setVisibility(View.VISIBLE);
            Glide
                    .with(mContext)
                    .load(publicWallDTO.getThumb_image())
                    .placeholder(R.drawable.thumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivContentImg);

        }


    }

    public boolean validateComment() {
        if (!ProjectUtils.isEditTextFilled(etComment)) {
            etComment.setError(getResources().getString(R.string.validation_comment));
            etComment.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public void submitForm() {
        if (!validateComment()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                editPost();
            } else {
                ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.llSave:
                submitForm();
                break;
        }
    }


    public void editPost() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.EDIT_POST_API, getPost(),  mContext).stringPost(TAG, new Helper() {
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

    public Map<String, String> getPost() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.POSTID, publicWallDTO.getPostID());
        params.put(Consts.CONTENT, ProjectUtils.getEditTextValue(etComment));
        params.put(Consts.COMUNITY_ID,loginDTO.getComunity_id()+"");
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
