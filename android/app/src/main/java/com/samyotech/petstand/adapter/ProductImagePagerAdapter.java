package com.samyotech.petstand.adapter;

/**
 * Created by pushpraj on 20/2/18.
 */

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.SingleFoodListDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ImageZoomActivity;


import java.util.ArrayList;

public class ProductImagePagerAdapter extends PagerAdapter {
    Context context;

    LayoutInflater layoutInflater;
    ArrayList<SingleFoodListDTO.Image> imageArrayList = new ArrayList<>();

    public ProductImagePagerAdapter(Context context, ArrayList<SingleFoodListDTO.Image> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.imagepageradapter, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);

        Glide.with(context)
                .load(imageArrayList.get(position).getImage())
                .dontAnimate() // will load image
                .into(imageView);


        container.addView(itemView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn = new Intent(context, ImageZoomActivity.class);
                inn.putExtra(Consts.IMAGE, imageArrayList.get(position).getImage());
                context.startActivity(inn);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}


