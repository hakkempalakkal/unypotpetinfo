package com.samyotech.petstand.adapter;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.BannerDTO;
import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {
    private ArrayList<BannerDTO> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImage_Adapter(Context context, ArrayList<BannerDTO> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View imageLayout = inflater.inflate(R.layout.fragment_blank, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);


        Glide
                .with(context)
                .load(imageModelArrayList.get(position).getB_image())
                .placeholder(R.drawable.app_logo)
                .into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
