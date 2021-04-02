package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.ProductByHomeList;
import com.samyotech.petstand.databinding.AdapterBrandsOfPetFoodBinding;
import com.samyotech.petstand.models.BrandsDTO;
import com.samyotech.petstand.utils.Consts;

import java.util.ArrayList;


public class AdapterBrand extends RecyclerView.Adapter<AdapterBrand.MyViewHolder> {

    private Context context;
    private ArrayList<BrandsDTO> brandsDTOArrayList;
    private LayoutInflater layoutInflater;
    private AdapterBrandsOfPetFoodBinding binding;

    public AdapterBrand(Context context, ArrayList<BrandsDTO> brandsDTOArrayList) {
        this.context = context;
        this.brandsDTOArrayList = brandsDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_brands_of_pet_food, viewGroup, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        Glide
                .with(context)
                .load(brandsDTOArrayList.get(position).getC_img_path())
                .placeholder(R.drawable.app_logo)
                .into(myViewHolder.binding.image);

        myViewHolder.binding.llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, ProductByHomeList.class);
                in.putExtra(Consts.BRAND_ID, brandsDTOArrayList.get(position).getC_id());
                in.putExtra(Consts.BRAND_NAME, brandsDTOArrayList.get(position).getC_name());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandsDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterBrandsOfPetFoodBinding binding;

        public MyViewHolder(@NonNull AdapterBrandsOfPetFoodBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
