package com.samyotech.petstand.adapter;

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
import com.samyotech.petstand.models.PetRatingDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by varunverma on 23/1/18.
 */

public class AdapterPetRating extends RecyclerView.Adapter<AdapterPetRating.ItemViewHolder> {
    private Context mContext;
    private ArrayList<PetRatingDTO> ratingDTOList;

    public AdapterPetRating(Context mContext, ArrayList<PetRatingDTO> ratingDTOList) {
        this.mContext = mContext;
        this.ratingDTOList = ratingDTOList;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pet_rating, parent, false);
        AdapterPetRating.ItemViewHolder holder = new AdapterPetRating.ItemViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        holder.tvName.setText(ratingDTOList.get(position).getUser_name());
        holder.tvDate.setText(ProjectUtils.parseDateToddMMyyyy(ratingDTOList.get(position).getCreated()));
        holder.ratingBar.setRating(ratingDTOList.get(position).getRating());
        Glide
                .with(mContext)
                .load(ratingDTOList.get(position).getUser_img())
                .placeholder(R.drawable.dummy_user)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivUser);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
                Intent intent = new Intent(mContext, UserPetProfileForWallActivity.class);
                intent.putExtra(Consts.USER_ID, ratingDTOList.get(position).getUser_id());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return ratingDTOList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView ivUser;
        public CustomTextView tvName, tvDate;
        public MaterialRatingBar ratingBar;
        CardView cardview;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivUser = (CircleImageView) itemView.findViewById(R.id.ivUser);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvDate = (CustomTextView) itemView.findViewById(R.id.tvDate);
            ratingBar = (MaterialRatingBar) itemView.findViewById(R.id.ratingBar);
            cardview = itemView.findViewById(R.id.cardview);

        }
    }

}
