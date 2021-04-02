package com.samyotech.petstand.adapter;

/**
 * Created by varun on 31/10/17.
 */

import android.content.DialogInterface;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.chat.ChatActivity;
import com.samyotech.petstand.activity.chat.OneTwoOneChatDemo;
import com.samyotech.petstand.models.ChatListDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private String TAG = ChatListAdapter.class.getSimpleName();
    private HashMap<String, String> params;
    private SharedPrefrence prefrence;
    private DialogInterface dialog_delete;
    private LoginDTO loginDTO;
    ChatActivity chatActivity;
    ArrayList<ChatListDTO> chatDTOlist;

    public ChatListAdapter(ChatActivity chatActivity, ArrayList<ChatListDTO> chatDTOlist) {
        this.chatActivity = chatActivity;
        this.chatDTOlist = chatDTOlist;
        prefrence = SharedPrefrence.getInstance(chatActivity);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvTitle.setText(chatDTOlist.get(position).getUserName());
        holder.tvMsg.setText(chatDTOlist.get(position).getMessage());
        try {
            holder.tvDay.setText(ProjectUtils.getDisplayableDay(ProjectUtils.correctTimestamp(Long.parseLong(chatDTOlist.get(position).getDate()))));
            holder.tvDate.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(chatDTOlist.get(position).getDate()))));

        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(chatActivity).
                load(chatDTOlist.get(position).getUserImage())
                .placeholder(R.drawable.dummy_user)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.IVprofile);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(chatActivity, OneTwoOneChatDemo.class);
                in.putExtra(Consts.USER_ID, chatDTOlist.get(position).getUser_id_receiver());
                in.putExtra(Consts.NAME, chatDTOlist.get(position).getUserName());
                chatActivity.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {

        return chatDTOlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold tvTitle;
        public CustomTextView tvDay, tvDate, tvMsg;
        public CircleImageView IVprofile;
        public CardView cardClick;

        public MyViewHolder(View view) {
            super(view);

            cardClick = view.findViewById(R.id.cardClick);
            IVprofile = view.findViewById(R.id.IVprofile);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDay = view.findViewById(R.id.tvDay);
            tvDate = view.findViewById(R.id.tvDate);
            tvMsg = view.findViewById(R.id.tvMsg);

        }
    }




}