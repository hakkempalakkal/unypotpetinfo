package com.samyotech.petstand.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.petprofile.UserPetProfileForWallActivity;
import com.samyotech.petstand.activity.wall.Comment;
import com.samyotech.petstand.activity.wall.PetVoteActivity;
import com.samyotech.petstand.activity.wall.ViewPost;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PublicWallDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.utils.ResizableCustomView;
import com.samyotech.petstand.utils.SquareImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by varunverma on 25/1/18.
 */

public class AdapterPublicWallNew extends BaseAdapter {
    private ArrayList<PublicWallDTO> publicWallDTOList;
    private Context mContext;
    private static final int MAX_LINES = 3;
    private String TAG = AdapterPublicWallNew.class.getSimpleName();
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    private PublicWallDTO publicWallDTO;
    int lastPosition = -1;
    private Dialog dialog;
    private CustomTextView tvYes, tvNo;
    private CustomEditText etReason;
    String media = "";
    private LayoutInflater inflater;

    public AdapterPublicWallNew(ArrayList<PublicWallDTO> publicWallDTOList, Context mContext, LayoutInflater inflater) {
        this.mContext = mContext;
        this.publicWallDTOList = publicWallDTOList;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return publicWallDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return publicWallDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        View itemView = inflater.inflate(R.layout.adapter_public_wal, null, false);

        holder.ivUser = (CircleImageView) itemView.findViewById(R.id.ivUser);
        holder.ivMenu = (ImageView) itemView.findViewById(R.id.ivMenu);
        holder.ivVedioThumb = (ImageView) itemView.findViewById(R.id.ivVedioThumb);
        holder.ivLikeCol = (ImageView) itemView.findViewById(R.id.ivLikeCol);
        holder.rlVedio = (RelativeLayout) itemView.findViewById(R.id.rlVedio);
        holder.tvTitle = (CustomTextView) itemView.findViewById(R.id.tvTitle);
        holder.tvCategory = (CustomTextView) itemView.findViewById(R.id.tvCategory);
        holder.tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
        holder.tvPost = (CustomTextView) itemView.findViewById(R.id.tvPost);
        holder.tvComment = (CustomTextView) itemView.findViewById(R.id.tvComment);
        holder.tvLikes = (CustomTextView) itemView.findViewById(R.id.tvLikes);
        holder.tvLikeCol = (CustomTextView) itemView.findViewById(R.id.tvLikeCol);

        holder.llLike = (LinearLayout) itemView.findViewById(R.id.llLike);
        holder.tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
        holder.llComment = (LinearLayout) itemView.findViewById(R.id.llComment);
        holder.llShare = (LinearLayout) itemView.findViewById(R.id.llShare);
        holder.post_image = (SquareImageView) itemView.findViewById(R.id.post_image);
        holder.RLprofile = itemView.findViewById(R.id.RLprofile);


        holder.tvComment.setText(publicWallDTOList.get(position).getContent());
        holder.tvName.setText(publicWallDTOList.get(position).getUser_name());
        holder.tvTitle.setText(publicWallDTOList.get(position).getTitle());
        holder.tvPost.setText(ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(publicWallDTOList.get(position).getCreateAt()))));
        holder.tvLikes.setText(publicWallDTOList.get(position).getLikes() + " Likes");
        holder.tvCommentCount.setText(publicWallDTOList.get(position).getComments() + " comments");
        if (publicWallDTOList.get(position).isIs_like()) {
            holder.tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.gradiant));
            holder.ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like_selected));

        } else {
            holder.tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like));
        }
        Glide
                .with(mContext)
                .load(publicWallDTOList.get(position).getUser_image())
                .placeholder(R.drawable.dummy_user)
                .override(80, 80)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivUser);

        //added textview , Max Lines(you want to show at normal),and view more label true so that you can expand
        ResizableCustomView.doResizeTextView(holder.tvComment, MAX_LINES, "View More", true);
        if (publicWallDTOList.get(position).getPostType().equalsIgnoreCase("video")) {
            holder.rlVedio.setVisibility(View.VISIBLE);

            Glide
                    .with(mContext)
                    .load(publicWallDTOList.get(position).getThumb_image())
                    .placeholder(R.drawable.thumb)
                    //.override(800, 400)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.post_image);


        } else if (publicWallDTOList.get(position).getPostType().equalsIgnoreCase("image")) {
            holder.rlVedio.setVisibility(View.VISIBLE);
            Glide
                    .with(mContext)
                    .load(publicWallDTOList.get(position).getMedia())
                    .placeholder(R.drawable.app_logo)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.post_image);

        } else if (publicWallDTOList.get(position).getPostType().equalsIgnoreCase("text")) {
            holder.rlVedio.setVisibility(View.GONE);
        }

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicWallDTO = publicWallDTOList.get(position);
                lastPosition = position;
                if (!publicWallDTO.getUser_id().equals(loginDTO.getId())) {
                    if (!publicWallDTO.isIs_like()) {
                        likes(holder);
                    } else {
                        dislikes(holder);
                    }
                } else {
                    ProjectUtils.showLong(mContext, "This is your post");
                }
            }
        });
        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, Comment.class);
                in.putExtra(Consts.POSTID, publicWallDTOList.get(position).getPostID());
                in.putExtra(Consts.USER_ID, publicWallDTOList.get(position).getUser_id());
                mContext.startActivity(in);
                // mContext.finish();
            }
        });

        holder.tvLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, PetVoteActivity.class);
                in.putExtra(Consts.POSTID, publicWallDTOList.get(position).getPostID());
                mContext.startActivity(in);
                // mContext.finish();
            }
        });
        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicWallDTO = publicWallDTOList.get(position);
                lastPosition = position;
                showPopupMenu(v);
            }
        });

        holder.rlVedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicWallDTO = publicWallDTOList.get(position);
                Intent intent = new Intent(mContext, ViewPost.class);
                intent.putExtra(Consts.PUBLIC_WALL_DTO, publicWallDTO);
                intent.putExtra(Consts.POSTID, publicWallDTO.getPostID());
                mContext.startActivity(intent);
            }
        });

        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicWallDTO = publicWallDTOList.get(position);
                share();
            }
        });

        holder.RLprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserPetProfileForWallActivity.class);
                intent.putExtra(Consts.USER_ID, publicWallDTOList.get(position).getUser_id());

                mContext.startActivity(intent);
            }
        });
        return itemView;
    }


    static class ViewHolder {
        public CircleImageView ivUser;
        public CustomTextView tvCommentCount, tvName, tvPost, tvComment, tvLikes, tvLikeCol, tvTitle, tvCategory;
        public ImageView ivMenu, ivVedioThumb, ivLikeCol;
        public LinearLayout llLike, llComment, llShare;
        public RelativeLayout rlVedio, RLprofile;
        public SquareImageView post_image;
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

    public void likes(final ViewHolder holder) {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait...");
        new HttpsRequest(Consts.LIKE_API, likeParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                 //   ProjectUtils.showLong(mContext, msg);
                    publicWallDTO.setLikes(publicWallDTO.getLikes() + 1);
                    publicWallDTO.setIs_like(true);
                    holder.tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.gradiant));
                    holder.ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like_selected));

                    publicWallDTOList.set(lastPosition, publicWallDTO);
                    notifyDataSetChanged();
                } else {
                    ProjectUtils.showLong(mContext, msg);


                }

            }
        });
    }

    public void dislikes(final ViewHolder holder) {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait...");
        new HttpsRequest(Consts.DISLIKE_API, likeParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                   // ProjectUtils.showLong(mContext, msg);
                    publicWallDTO.setLikes(publicWallDTO.getLikes() - 1);
                    publicWallDTO.setIs_like(false);
                    holder.tvLikeCol.setTextColor(mContext.getResources().getColor(R.color.black));
                    holder.ivLikeCol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_like));
                    publicWallDTOList.set(lastPosition, publicWallDTO);
                    notifyDataSetChanged();
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


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_post, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.ic_abuse:
                    if (!publicWallDTO.isIs_abuse()) {
                        dialogshow();

                    } else {
                        ProjectUtils.showLong(mContext, mContext.getResources().getString(R.string.already_abuse));
                    }

                    return true;

                default:
            }
            return false;
        }


    }

    public void abuse() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait...");
        new HttpsRequest(Consts.ABUSE_POST_API, abuseParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    dialog.dismiss();
                } else {
                    ProjectUtils.showLong(mContext, msg);

                }
            }
        });
    }

    public Map<String, String> abuseParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.POSTID, publicWallDTO.getPostID());
        params.put(Consts.REASON, ProjectUtils.getEditTextValue(etReason));
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }

    public boolean validateReason() {
        if (!ProjectUtils.isEditTextFilled(etReason)) {
            etReason.setError(mContext.getResources().getString(R.string.validate_reason));
            etReason.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    public void dialogshow() {
        dialog = new Dialog(mContext, android.R.style.Theme_Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_abuse);


        tvYes = (CustomTextView) dialog.findViewById(R.id.tvYes);
        tvNo = (CustomTextView) dialog.findViewById(R.id.tvNo);
        etReason = (CustomEditText) dialog.findViewById(R.id.etReason);
        dialog.show();
        dialog.setCancelable(false);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvYes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitForm();


                    }
                });

    }

    public void submitForm() {
        if (!validateReason()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                abuse();

            } else {
                ProjectUtils.showLong(mContext, mContext.getResources().getString(R.string.internet_concation));
            }
        }
    }

}
