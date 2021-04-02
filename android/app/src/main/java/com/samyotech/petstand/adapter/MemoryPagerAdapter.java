package com.samyotech.petstand.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.Memories.SpacePhotoActivity;
import com.samyotech.petstand.models.GalleryDTO;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;


public class MemoryPagerAdapter extends PagerAdapter {
    private Context mContext;
    LayoutInflater mLayoutInflater;
    private SpacePhotoActivity activity;
    private ArrayList<GalleryDTO> galleryDTOList;

    public MemoryPagerAdapter(SpacePhotoActivity activity, Context context, ArrayList<GalleryDTO> galleryDTOList) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.galleryDTOList = galleryDTOList;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        final ImageView image = (ImageView) itemView.findViewById(R.id.image);
        CustomTextView tvDes = itemView.findViewById(R.id.tvDes);
        CustomTextView tvDate = itemView.findViewById(R.id.tvDate);

        tvDes.setText(galleryDTOList.get(position).getDescription());
        tvDate.setText(ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(galleryDTOList.get(position).getCreated()))));

     /*   Glide.with(mContext)
                .load(galleryDTOList.get(position).getPet_img_path())
                .into(image);*/

        Glide.with(mContext)
                .load(galleryDTOList.get(position).getPet_img_path())
                .placeholder(R.drawable.dog_shape)
                .skipMemoryCache(true)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);


/*
        Glide.with(mContext)
                .load(galleryDTOList.get(position).getPet_img_path())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.dummy_img)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        image.setMaxZoom(0);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(image);
*/
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return galleryDTOList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}