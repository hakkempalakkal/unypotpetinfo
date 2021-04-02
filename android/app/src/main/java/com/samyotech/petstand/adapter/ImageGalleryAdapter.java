package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.Memories.SpacePhotoActivity;
import com.samyotech.petstand.models.GalleryDTO;
import com.samyotech.petstand.utils.Consts;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<GalleryDTO> galleryDTOList;

    public ImageGalleryAdapter(Context mContext, ArrayList<GalleryDTO> galleryDTOList) {
        this.mContext = mContext;
        this.galleryDTOList = galleryDTOList;
    }

    @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ImageGalleryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

        ImageView imageView = holder.mPhotoImageView;

        Glide.with(mContext)
                .load(galleryDTOList.get(position).getPet_img_path())
                .placeholder(R.drawable.dog_shape)
                .skipMemoryCache(true)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return galleryDTOList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)galleryDTOList);
                intent.putExtra(Consts.EXTRA_SPACE_PHOTO,args);
                intent.putExtra("pos",position);
                mContext.startActivity(intent);
            }
        }
    }

}
