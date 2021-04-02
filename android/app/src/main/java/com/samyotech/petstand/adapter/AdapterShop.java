package com.samyotech.petstand.adapter;


import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.vat.PlaceDetailsActivity;
import com.samyotech.petstand.databinding.AdapterShopBinding;
import com.samyotech.petstand.models.NearByDTO;
import com.samyotech.petstand.utils.Consts;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mayank on 20/05/19.
 */

public class AdapterShop extends RecyclerView.Adapter<AdapterShop.MyViewHolder> {
    private Context mContext;
    ArrayList<NearByDTO> list;
    ArrayList<NearByDTO> newlist;
    private LayoutInflater layoutInflater;
    AdapterShopBinding binding;

    public AdapterShop(Context mContext, ArrayList<NearByDTO> list) {
        this.mContext = mContext;
        this.list = list;
        newlist = new ArrayList<>();
        newlist.addAll(list);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_shop, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(MyViewHolder mViewHolder, final int position) {

        mViewHolder.binding.ctvbTitle.setText(list.get(position).getName());
        Glide.with(mContext)
                .load(list.get(position).getImage_path())
                .placeholder(R.drawable.app_logo)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mViewHolder.binding.ivPic);


        mViewHolder.binding.cvFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlaceDetailsActivity.class);
                intent.putExtra(Consts.NEAR_BY, list.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AdapterShopBinding binding;

        public MyViewHolder(AdapterShopBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(newlist);
        } else {
            for (NearByDTO nearByDTO : newlist) {
                if (nearByDTO.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(nearByDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

}
