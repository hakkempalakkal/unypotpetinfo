package com.samyotech.petstand.activity.wall;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PublicWallDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.utils.TouchImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPost extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView ivUser;
    private CustomTextView tvCommentCount, tvUserName, tvComment, tvLikes, tvLikeCol;
    private ImageView ivBack, ivLikeCol;
    private TouchImageView ivImg;
    private VideoView videoPreview;
    private PublicWallDTO publicWallDTO;
    private Context mContext;
    public LinearLayout llLike, llComment, llShare;
    private String TAG = ViewPost.class.getSimpleName();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    String media = "";
    HashMap<String, String> parms = new HashMap<>();
    String postId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ViewPost.this);
        setContentView(R.layout.activity_view_post);
        mContext = ViewPost.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        if (getIntent().hasExtra(Consts.POSTID)) {
            //  publicWallDTO = (PublicWallDTO) getIntent().getSerializableExtra(Consts.PUBLIC_WALL_DTO);
            postId = getIntent().getStringExtra(Consts.POSTID);
        }
        init();
        getPost();
    }

    public void init() {
        ivUser = findViewById(R.id.ivUser);
        tvUserName = findViewById(R.id.tvUserName);
        tvLikes = findViewById(R.id.tvLikes);
        tvComment = findViewById(R.id.tvComment);
        tvLikeCol = findViewById(R.id.tvLikeCol);
        videoPreview = findViewById(R.id.videoPreview);
        ivImg = findViewById(R.id.ivImg);
        ivLikeCol = findViewById(R.id.ivLikeCol);
        ivBack = findViewById(R.id.ivBack);
        llLike = findViewById(R.id.llLike);
        llComment = findViewById(R.id.llComment);
        llShare = findViewById(R.id.llShare);
        tvCommentCount = findViewById(R.id.tvCommentCount);

        ivBack.setOnClickListener(this);
        llLike.setOnClickListener(this);
        llComment.setOnClickListener(this);
        llShare.setOnClickListener(this);
        tvLikes.setOnClickListener(this);
        //setData();
    }

    private void setData() {


        tvUserName.setText(publicWallDTO.getUser_name());
        tvComment.setText(publicWallDTO.getContent());

        tvComment.setText(publicWallDTO.getContent());
        tvLikes.setText(publicWallDTO.getLikes() + " Likes");
        tvCommentCount.setText(publicWallDTO.getComments() + " comments");
        if (publicWallDTO.isIs_like()) {
            tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.gradiant));
            ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like_selected));

        } else {
            tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.black));
            ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like));
        }

        Glide
                .with(mContext)
                .load(publicWallDTO.getUser_image())
                .placeholder(R.drawable.dummy_user)
                .override(80, 80)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivUser);

        if (publicWallDTO.getPostType().equalsIgnoreCase("text")) {
            ivImg.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
        } else if (publicWallDTO.getPostType().equalsIgnoreCase("image")) {
            ivImg.setVisibility(View.VISIBLE);
            videoPreview.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(publicWallDTO.getMedia())
                    .error(R.drawable.app_icon)
                    .into(ivImg);

/*
            Glide
                    .with(mContext)
                    .load(publicWallDTO.getMedia())
                    .placeholder(R.drawable.dummy_img)
                    //.override(225, 400)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImg);
*/


        } else if (publicWallDTO.getPostType().equalsIgnoreCase("video")) {
            ivImg.setVisibility(View.GONE);
            videoPreview.setVisibility(View.VISIBLE);

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoPreview);

            videoPreview.setMediaController(mediaController);
            videoPreview.setVideoPath(publicWallDTO.getMedia());
            videoPreview.requestFocus();
            videoPreview.start();

            videoPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                                ProjectUtils.showProgressDialog(mContext, false, "Buffering, Please wait...");
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                                ProjectUtils.pauseProgressDialog();
                            return false;
                        }
                    });
                }
            });
            videoPreview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    ProjectUtils.pauseProgressDialog();
                    return false;
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.llLike:
                if (!publicWallDTO.getUser_id().equals(loginDTO.getId())) {
                    if (!publicWallDTO.isIs_like()) {
                        likes();
                    } else {
                        dislikes();
                    }
                } else {
                    ProjectUtils.showLong(mContext, "This is your post");
                }

                break;
            case R.id.llComment:
                Intent in = new Intent(mContext, Comment.class);
                in.putExtra(Consts.POSTID, publicWallDTO.getPostID());
                in.putExtra(Consts.USER_ID, publicWallDTO.getUser_id());
                mContext.startActivity(in);
                break;
            case R.id.tvLikes:
                Intent inn = new Intent(mContext, PetVoteActivity.class);
                inn.putExtra(Consts.POSTID, publicWallDTO.getPostID());
                mContext.startActivity(inn);
                break;
            case R.id.llShare:
                share();
                break;

        }
    }

    public void share() {
        try {
            if (!publicWallDTO.getMedia().equals("")) {
                media = ". Media link: " + publicWallDTO.getMedia();
            }
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, publicWallDTO.getTitle() + "\n" + publicWallDTO.getContent() + "\n" + media);
            mContext.startActivity(Intent.createChooser(sharingIntent, "PetStand"));
        } catch (Exception e) {
            e.printStackTrace();
            ProjectUtils.showLong(mContext, "Opps!! It seems that you have not installed any sharing app.");
        }


    }

    public void likes() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait...");
        new HttpsRequest(Consts.LIKE_API, likeParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                  //  ProjectUtils.showLong(mContext, msg);
                    publicWallDTO.setLikes(publicWallDTO.getLikes() + 1);
                    publicWallDTO.setIs_like(true);
                    tvLikes.setText(publicWallDTO.getLikes() + " Likes");
                    tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.gradiant));
                    ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like_selected));

                } else {
                    ProjectUtils.showLong(mContext, msg);


                }

            }
        });
    }

    public void dislikes() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait...");
        new HttpsRequest(Consts.DISLIKE_API, likeParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                   // ProjectUtils.showLong(mContext, msg);
                    publicWallDTO.setLikes(publicWallDTO.getLikes() - 1);
                    publicWallDTO.setIs_like(false);
                    tvLikes.setText(publicWallDTO.getLikes() + " Likes");
                    tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.black));
                    ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like));

                } else {
                    ProjectUtils.showLong(mContext, msg);


                }

            }
        });
    }

    public Map<String, String> likeParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.POSTID, publicWallDTO.getPostID());
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }


    public void getPost() {
        parms.put(Consts.POSTID, postId);
        parms.put(Consts.USER_ID, loginDTO.getId());
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.GET_POST_BY_ID, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                   // ProjectUtils.showLong(mContext, msg);

                    try {
                        publicWallDTO = new Gson().fromJson(response.getJSONObject("data").toString(), PublicWallDTO.class);
                        setData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }


}
