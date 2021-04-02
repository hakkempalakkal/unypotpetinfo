package com.samyotech.petstand.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.wall.Comment;
import com.samyotech.petstand.activity.wall.EditPost;
import com.samyotech.petstand.activity.wall.PetVoteActivity;
import com.samyotech.petstand.activity.wall.ViewPost;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PublicWallDTO;
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

public class AdapterMyPostNew extends BaseAdapter {
    private ArrayList<PublicWallDTO> publicWallDTOList;
    private Context mContext;
    private static final int MAX_LINES = 2;
    private String TAG = AdapterMyPostNew.class.getSimpleName();
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    private PublicWallDTO publicWallDTO;
    int lastPosition = -1;
    private Dialog dialog;
    private CustomTextView tvYes, tvNo;
    private CustomEditText etReason;
    String media = "";
    private LayoutInflater inflater;

    public AdapterMyPostNew(ArrayList<PublicWallDTO> publicWallDTOList, Context mContext, LayoutInflater inflater) {
        this.mContext = mContext;
        this.publicWallDTOList = publicWallDTOList;
        this.inflater = inflater;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
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
        holder.RLprofile = itemView.findViewById(R.id.RLprofile);

        holder.llLike = (LinearLayout) itemView.findViewById(R.id.llLike);
        holder.llComment = (LinearLayout) itemView.findViewById(R.id.llComment);
        holder.llShare = (LinearLayout) itemView.findViewById(R.id.llShare);
        holder.post_image = (SquareImageView) itemView.findViewById(R.id.post_image);

        holder.tvCommentCount = itemView.findViewById(R.id.tvCommentCount);


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
        Glide.with(mContext)
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


        return itemView;
    }


    static class ViewHolder {
        public CircleImageView ivUser;
        public CustomTextView tvCommentCount,tvName, tvPost, tvComment, tvLikes, tvLikeCol, tvTitle, tvCategory;
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
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
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
                    ProjectUtils.showLong(mContext, msg);
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
        inflater.inflate(R.menu.menu_my_post, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.ic_edit_post:
                    Intent intent = new Intent(mContext, EditPost.class);
                    intent.putExtra(Consts.PUBLIC_WALL_DTO, publicWallDTO);
                    mContext.startActivity(intent);
                    return true;
                case R.id.ic_delete_post:
                    deletecardDialog();
                    return true;

                default:
            }
            return false;
        }


    }

    public void deletecardDialog() {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
        alertbox.setMessage("Are you sure want to delete?");
        alertbox.setTitle("Remove Post");
        alertbox.setIcon(R.drawable.cards_cancel);

        alertbox.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                        delete();

                    }
                });
        alertbox.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                    }
                });


        alertbox.show();
    }

    public void delete() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait...");
        new HttpsRequest(Consts.DELETE_POST_API, deleteParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    publicWallDTOList.remove(lastPosition);
                    notifyDataSetChanged();

                } else {
                    ProjectUtils.showLong(mContext, msg);

                }
            }
        });
    }

    public Map<String, String> deleteParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.POSTID, publicWallDTO.getPostID());
        Log.e(TAG + " --- Params", params.toString());
        return params;
    }

    public void dislikes(final ViewHolder holder) {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait...");
        new HttpsRequest(Consts.DISLIKE_API, likeParams(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
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
}
