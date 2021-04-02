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
import com.samyotech.petstand.models.GetViewDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterShowPetVote extends RecyclerView.Adapter<AdapterShowPetVote.ItemViewHolder> {

    Context mContext;
    private ArrayList<GetViewDTO> getViewDTOlist = new ArrayList<>();


    @Override
    public AdapterShowPetVote.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_show_pet_view, parent, false);
        return new ItemViewHolder(itemView);

    }

    public AdapterShowPetVote(Context mContext, ArrayList<GetViewDTO> getViewDTOlist) {
        this.mContext = mContext;
        this.getViewDTOlist = getViewDTOlist;
    }

    @Override
    public void onBindViewHolder(final AdapterShowPetVote.ItemViewHolder holder, final int position) {

        try {


            holder.tvTitle.setText(getViewDTOlist.get(position).getUserName());
            holder.tvDate.setText(ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(getViewDTOlist.get(position).getCreated()))));

            Glide.with(mContext)
                    .load(getViewDTOlist.get(position).getProfile_pic())
                    .placeholder(R.drawable.dummyuser)
                    .dontAnimate() // will load image
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.IVprofile);

            holder.cardClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
                    Intent intent = new Intent(mContext, UserPetProfileForWallActivity.class);
                    intent.putExtra(Consts.USER_ID, getViewDTOlist.get(position).getUser_id());
                    mContext.startActivity(intent);
                }
            });

        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return getViewDTOlist.size();
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