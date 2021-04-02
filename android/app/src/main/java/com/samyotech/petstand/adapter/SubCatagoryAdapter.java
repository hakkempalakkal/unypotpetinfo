package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.activity.food.BrandList;
import com.samyotech.petstand.models.PetCategoryDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mayank on 16/2/18.
 */

public class SubCatagoryAdapter extends RecyclerView.Adapter<SubCatagoryAdapter.MyViewHolder> {

    private ArrayList<PetCategoryDTO> petCatList;
    private ArrayList<PetCategoryDTO> newPetCatList;
    private SharedPrefrence prefrence;
    public BaseActivity baseActivity;
    private Context context;
    String pet_type_id = "";

    public SubCatagoryAdapter(Context context, ArrayList<PetCategoryDTO> petCatList, String pet_type_id) {
        this.petCatList = petCatList;
        this.context = context;
        this.pet_type_id = pet_type_id;
        prefrence = SharedPrefrence.getInstance(context);
        newPetCatList = new ArrayList<>();
        newPetCatList.addAll(petCatList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_catagory_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.ctvbType.setText(petCatList.get(position).getCat_title());

        Glide.with(context)
                .load(petCatList.get(position).getC_img_path())
                .placeholder(R.drawable.about_us_back)
                .into(holder.ivType);


        holder.cvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, BrandList.class);
                in.putExtra(Consts.P_PET_TYPE, pet_type_id);
                in.putExtra(Consts.P_SUB_PET_TYPE, petCatList.get(position).getId());
                in.putExtra(Consts.P_PET_TYPE_NAME, petCatList.get(position).getCat_title());
                context.startActivity(in);
            }
        });

        if (position % 4 == 0) {
            holder.editnormal.setBackgroundColor(Color.parseColor("#FFA968"));
        } else if (position % 4 == 1) {
            holder.editnormal.setBackgroundColor(Color.parseColor("#FF9874"));

        } else if (position % 4 == 2) {
            holder.editnormal.setBackgroundColor(Color.parseColor("#FE8581"));
        } else if (position % 4 == 3) {
            holder.editnormal.setBackgroundColor(Color.parseColor("#FE6E90"));
        }
    }


    @Override
    public int getItemCount() {
        return petCatList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold ctvbType;
        ImageView ivType;
        CardView cvType;
        LinearLayout editnormal;

        public MyViewHolder(View item) {
            super(item);
            ctvbType = (CustomTextViewBold) item.findViewById(R.id.ctvbType);
            ivType = (ImageView) item.findViewById(R.id.ivType);
            cvType = (CardView) item.findViewById(R.id.cvType);
            editnormal = (LinearLayout) item.findViewById(R.id.ll_bg);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        petCatList.clear();
        if (charText.length() == 0) {
            petCatList.addAll(newPetCatList);
        } else {
            for (PetCategoryDTO petCate : newPetCatList) {
                if (petCate.getCat_title().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    petCatList.add(petCate);
                }
            }
        }
        notifyDataSetChanged();
    }

}