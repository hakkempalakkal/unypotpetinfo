package com.samyotech.petstand.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.duties.CreateReminder;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.utils.CustomTextView;

import java.util.ArrayList;


public class PetPagerAdapter extends PagerAdapter {
    private Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<PetListDTO> myListy;
    //private String[] mResources;
    DisplayImageOptions options;
    CreateReminder createReminder;

    public PetPagerAdapter(CreateReminder createReminder, ArrayList<PetListDTO> myList /*String[] resources*/) {
        this.createReminder = createReminder;
        mContext = createReminder;
        myListy = myList;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_pet, container, false);
        ImageView iv_bottom_foster = (ImageView) itemView.findViewById(R.id.iv_bottom_foster);
        CustomTextView tvPetName = (CustomTextView) itemView.findViewById(R.id.tvPetName);
        Glide
                .with(mContext)
                .load(myListy.get(position).getPet_img_path())
                .placeholder(R.drawable.betaal)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_bottom_foster);
        tvPetName.setText(myListy.get(position).getPetName()+"-"+createReminder.reminderCategory.getName());

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return myListy.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}