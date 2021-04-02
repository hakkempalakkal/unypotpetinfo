package com.samyotech.petstand.activity.wall;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AdapterComment;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.CommentDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class Comment extends AppCompatActivity implements View.OnClickListener {
    private String TAG = Comment.class.getSimpleName();
    private RecyclerView lvChating;
    private AdapterComment adapterComment;
    private Context mContext;
    private ArrayList<CommentDTO> commentDTOList;
    private ImageView ivBack, ivEmoji, ivSendMsg;
    private EmojiconEditText edittextMessage;
    private EmojIconActions emojIcon;
    private RelativeLayout relative;
   // private InputMethodManager inputManager;
    private LinearLayoutManager layoutManager;
    private String postId = "";
    private String postUserID = "";
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(Comment.this);
        setContentView(R.layout.activity_comment);
        mContext = Comment.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
       // inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getIntent().hasExtra(Consts.POSTID)) {
            postId = getIntent().getStringExtra(Consts.POSTID);
            postUserID = getIntent().getStringExtra(Consts.USER_ID);
        }
        init();
    }

    public void init() {
        relative = (RelativeLayout) findViewById(R.id.relative);
        lvChating = findViewById(R.id.lvChating);
        ivBack = findViewById(R.id.ivBack);
        ivSendMsg = findViewById(R.id.ivSendMsg);
        ivEmoji = findViewById(R.id.ivEmoji);

        ivSendMsg.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        edittextMessage = (EmojiconEditText) findViewById(R.id.edittextMessage);

        emojIcon = new EmojIconActions(this, relative, edittextMessage, ivEmoji, "#495C66", "#DCE1E2", "#E6EBEF");
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });

        getComment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
//                startActivity(new Intent(mContext, PublicWall.class));
//                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
                break;
            case R.id.ivSendMsg:
                submit();
                break;
        }
    }

    public void submit() {
        if (!validateMessage()) {
            return;
        } else {
           /* inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);*/
            if (NetworkManager.isConnectToInternet(mContext)) {
                doComment();
            } else {
                ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validateMessage() {
        if (edittextMessage.getText().toString().trim().length() <= 0) {
            edittextMessage.setError("Please enter comment");
            edittextMessage.requestFocus();
            return false;
        } else {
            edittextMessage.setError(null);
            edittextMessage.clearFocus();
            return true;
        }


    }


    public void getComment() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.GET_COMMENTS_API, getCommentParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    Type listType = new TypeToken<List<CommentDTO>>() {
                    }.getType();
                    try {
                        commentDTOList = new ArrayList<>();
                        commentDTOList = (ArrayList<CommentDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        showDataList();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                }


            }

        });
    }

    public Map<String, String> getCommentParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.POSTID, postId);
        params.put(Consts.USER_ID, loginDTO.getId());
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }

    public void showDataList() {
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        lvChating.setLayoutManager(layoutManager);
        adapterComment = new AdapterComment(Comment.this, commentDTOList, postUserID);
        lvChating.setAdapter(adapterComment);

    }


    public void doComment() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.COMMENT_API, doCommentParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    edittextMessage.setText("");
                    getComment();
                    ProjectUtils.showLong(mContext, msg);
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }


            }

        });
    }

    public Map<String, String> doCommentParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.POSTID, postId);
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.CONTENT, edittextMessage.getText().toString().trim());
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        startActivity(new Intent(mContext, PublicWall.class));
//        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
