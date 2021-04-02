package com.samyotech.petstand.adapter;

/**
 * Created by varun on 28/08/18.
 */

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.petprofile.UserPetProfileForWallActivity;
import com.samyotech.petstand.models.FollowerDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterShowPetFollowers extends RecyclerView.Adapter<AdapterShowPetFollowers.ItemViewHolder> {

    Context mContext;
    private ArrayList<FollowerDTO> getFollowerDTOlist = new ArrayList<>();


    @Override
    public AdapterShowPetFollowers.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_show_pet_view, parent, false);
        return new ItemViewHolder(itemView);

    }

    public AdapterShowPetFollowers(Context mContext, ArrayList<FollowerDTO> getFollowerDTOlist) {
        this.mContext = mContext;
        this.getFollowerDTOlist = getFollowerDTOlist;
    }

    @Override
    public void onBindViewHolder(final AdapterShowPetFollowers.ItemViewHolder holder, final int position) {

        try {


            holder.tvTitle.setText(getFollowerDTOlist.get(position).getUserName());
            holder.tvDate.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(getFollowerDTOlist.get(position).getCreated_at()))));
            // holder.tvDate.setText(getFollowerDTOlist.get(position).getCreated_at());

            Glide.with(mContext)
                    .load(getFollowerDTOlist.get(position).getProfile_pic())
                    .placeholder(R.drawable.dummyuser)
                    .dontAnimate() // will load image
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.IVprofile);

            holder.cardClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
                    Intent intent = new Intent(mContext, UserPetProfileForWallActivity.class);
                    intent.putExtra(Consts.USER_ID, getFollowerDTOlist.get(position).getFollower_user_id());
                    mContext.startActivity(intent);
                }
            });


        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return getFollowerDTOlist.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public CustomTextViewBold tvTitle;
        CustomTextView tvDate;
        CircleImageView IVprofile;
        CardView cardClick;


        public ItemViewHolder(View view) {
            super(view);

            IVprofile = view.findViewById(R.id.IVprofile);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDate = view.findViewById(R.id.tvDate);
            cardClick = view.findViewById(R.id.cardClick);


        }
    }

}