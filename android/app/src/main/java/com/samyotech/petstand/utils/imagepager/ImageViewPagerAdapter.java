package com.samyotech.petstand.utils.imagepager;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.utils.TouchImageView;


import java.util.ArrayList;

/**
 * Created by mayank on 21/11/17.
 */

public class ImageViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<PetMarketDTO.PetImage> newsImageLIST = new ArrayList<>();

    String functionid;

    ImageviewpagerActivity imageviewpagerActivity;

    //private String[] mResources;
    int lastPosition = -1;
    //mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    public ImageViewPagerAdapter(ImageviewpagerActivity imageviewpagerActivity, Context mContext, ArrayList<PetMarketDTO.PetImage> newsImageLIST) {
        this.mContext = mContext;
        this.imageviewpagerActivity = imageviewpagerActivity;
        this.newsImageLIST = newsImageLIST;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_bar_view, container, false);

        TouchImageView iv_bottom_foster = (TouchImageView) itemView.findViewById(R.id.iv_bottom_foster);

        Glide.with(mContext).load(newsImageLIST.get(position).getImage()).placeholder(R.drawable.default_error).into(iv_bottom_foster);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return newsImageLIST.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}