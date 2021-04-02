package com.samyotech.petstand.adapter;

import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.activity.food.BrandList;
import com.samyotech.petstand.activity.food.FoodList;
import com.samyotech.petstand.models.BrandDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mayank on 16/2/18.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {

    private ArrayList<BrandDTO> brandDTOList;
    private ArrayList<BrandDTO> newBrandDTOList;
    private SharedPrefrence prefrence;
    public BaseActivity baseActivity;
    private BrandList context;
    String pet_type_id = "";

    public BrandAdapter(BrandList context, ArrayList<BrandDTO> brandDTOList, String pet_type_id) {
        this.brandDTOList = brandDTOList;
        this.context = context;
        this.pet_type_id = pet_type_id;
        prefrence = SharedPrefrence.getInstance(context);
        newBrandDTOList = new ArrayList<>();
        newBrandDTOList.addAll(brandDTOList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_adapter, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.ctvbType.setText(brandDTOList.get(position).getC_name());
        Glide.with(context)
                .load(brandDTOList.get(position).getC_img_path())
                .placeholder(R.drawable.hero_bulldog)
                .into(holder.ivType);


        holder.cvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, FoodList.class);
                in.putExtra(Consts.P_PET_TYPE, pet_type_id);
                in.putExtra(Consts.P_SUB_PET_TYPE, context.pet_sub_type_id);
                in.putExtra(Consts.P_PET_TYPE_NAME, brandDTOList.get(position).getC_name());
                in.putExtra(Consts.BRAND_ID, brandDTOList.get(position).getC_id());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandDTOList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold ctvbType;
        ImageView ivType;
        CardView cvType;

        public MyViewHolder(View item) {
            super(item);
            ctvbType = (CustomTextViewBold) item.findViewById(R.id.ctvbType);
            ivType = (ImageView) item.findViewById(R.id.ivType);
            cvType = (CardView) item.findViewById(R.id.cvType);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        brandDTOList.clear();
        if (charText.length() == 0) {
            brandDTOList.addAll(newBrandDTOList);
        } else {
            for (BrandDTO brandDTO : newBrandDTOList) {
                if (brandDTO.getC_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    brandDTOList.add(brandDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

}