package com.samyotech.petstand.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.petmarket.CommentPetMarket;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.CommentPetMarketDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by varunverma on 23/1/18.
 */

public class AdapterCommentPetMarket extends RecyclerView.Adapter<AdapterCommentPetMarket.ItemViewHolder> {
    private CommentPetMarket mContext;
    private ArrayList<CommentPetMarketDTO> commentDTOList;
    EmojiconEditText edittextMessage;
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    Dialog dialogbox;
    int lastPosition = -1;

    public AdapterCommentPetMarket(CommentPetMarket mContext, ArrayList<CommentPetMarketDTO> commentDTOList) {
        this.mContext = mContext;
        this.commentDTOList = commentDTOList;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment, parent, false);
        AdapterCommentPetMarket.ItemViewHolder holder = new AdapterCommentPetMarket.ItemViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        holder.tvComment.setText(commentDTOList.get(position).getComment());
        holder.tvName.setText(commentDTOList.get(position).getFirst_name());
        holder.tvDate.setText(ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(commentDTOList.get(position).getCreated_at()))));

        Glide.with(mContext)
                .load(commentDTOList.get(position).getProfile_pic())
                .placeholder(R.drawable.dummy_user)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivUser);

        if (commentDTOList.get(position).getUser_id().equals(loginDTO.getId())) {
            holder.tvedit.setVisibility(View.VISIBLE);
            holder.tvdelete.setVisibility(View.VISIBLE);
        } else {
            holder.tvedit.setVisibility(View.GONE);
            holder.tvdelete.setVisibility(View.GONE);
        }


        holder.tvedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editComment(position);
            }
        });
        holder.tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletecardDialog(position, v);
            }
        });
    }


    @Override
    public int getItemCount() {
        return commentDTOList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView ivUser;
        public CustomTextView tvName, tvDate, tvComment, tvdelete, tvedit;
        public ImageView edit_comment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivUser = (CircleImageView) itemView.findViewById(R.id.ivUser);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvDate = (CustomTextView) itemView.findViewById(R.id.tvDate);
            tvComment = (CustomTextView) itemView.findViewById(R.id.tvComment);
            edit_comment = itemView.findViewById(R.id.edit_comment);
            tvdelete = itemView.findViewById(R.id.tvdelete);
            tvedit = itemView.findViewById(R.id.tvedit);

        }
    }

    public void editComment(final int i) {
        dialogbox = new Dialog(mContext);
        //dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.comment_edit_dialog);
        RelativeLayout relative = dialogbox.findViewById(R.id.relative);
        ImageView ivSendMsg = dialogbox.findViewById(R.id.ivSendMsg);
        ImageView ivEmoji = dialogbox.findViewById(R.id.ivEmoji);
        dialogbox.show();

        ivSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCommentAPI(i);
            }
        });

        edittextMessage = (EmojiconEditText) dialogbox.findViewById(R.id.edittextMessage);
        edittextMessage.setText(commentDTOList.get(i).getComment());

        EmojIconActions emojIcon = new EmojIconActions(mContext, relative, edittextMessage, ivEmoji, "#495C66", "#DCE1E2", "#E6EBEF");
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
    }


    public void editCommentAPI(int i) {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.EDIT_COMMENT, doCommentParams(i), mContext).stringPost("Adapter Comment", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    dialogbox.dismiss();
                    mContext.getComment();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }

        });
    }

    public Map<String, String> doCommentParams(int i) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.COMMENT_ID, commentDTOList.get(i).getId());
        params.put(Consts.COMMNET, edittextMessage.getText().toString().trim());
        params.put(Consts.USER_ID, loginDTO.getId());
        return params;
    }

    public void deletecardDialog(final int i, View view) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
        alertbox.setMessage("Are you sure want to delete?");
        alertbox.setTitle("Remove Comment");
        alertbox.setIcon(R.drawable.cards_cancel);

        alertbox.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                        lastPosition = i;
                        deleteCommentAPI(i);

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

    public void deleteCommentAPI(int i) {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.REMOVE_COMMENT_PET, CommentParams(i), mContext).stringPost("Adapter Comment", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    commentDTOList.remove(lastPosition);
                    notifyDataSetChanged();

                    ProjectUtils.showLong(mContext, msg);
                    // dialogbox.dismiss();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }


            }

        });
    }

    public Map<String, String> CommentParams(int i) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.COMMENT_ID, commentDTOList.get(i).getId());
        params.put(Consts.PET_ID, commentDTOList.get(i).getPet_id());
        params.put(Consts.USER_ID, loginDTO.getId());
        return params;
    }

}
