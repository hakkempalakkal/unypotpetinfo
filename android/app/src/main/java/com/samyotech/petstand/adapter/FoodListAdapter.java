package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.FoodDetail;
import com.samyotech.petstand.models.FoodListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by mayank on 16/2/18.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder> {

    private ArrayList<FoodListDTO> list;
    private ArrayList<FoodListDTO> foodList = null;
    private SharedPrefrence prefrence;
    private String P_pet_type = "";
    private String p_type = "";
    private Context context;

    public FoodListAdapter(Context context, ArrayList<FoodListDTO> foodList, String p_type, String P_pet_type) {
        this.foodList = foodList;
        this.context = context;
        this.p_type = p_type;
        this.P_pet_type = P_pet_type;
        prefrence = SharedPrefrence.getInstance(context);
        this.list = new ArrayList<FoodListDTO>();
        this.list.addAll(foodList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (foodList.get(position).getQuantity().equals("0")) {
            holder.out_of_stock.setVisibility(View.VISIBLE);

        } else {
            holder.out_of_stock.setVisibility(View.GONE);
        }

        holder.ctvbFoodTitle.setText(foodList.get(position).getP_name());
        holder.ctvFoodDesc.setText(foodList.get(position).getP_description());
        holder.ctvbPrice1.setText(foodList.get(position).getCurrency_type() + " " + ProjectUtils.round(Float.parseFloat(foodList.get(position).getFinal_amount()), 2) + "");
        if (!foodList.get(position).getDiscount().equals("0")) {
            holder.ctvbOld.setText(foodList.get(position).getCurrency_type() + " " + ProjectUtils.round(foodList.get(position).getP_rate_sale(), 2) + "");
            holder.ctvbOld.setPaintFlags(holder.ctvbOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.ctvbDiscount.setText(foodList.get(position).getDiscount() + "% off");
            // holder.ctvbOld.setVisibility(View.VISIBLE);
            holder.ctvbDiscount.setVisibility(View.VISIBLE);
            holder.llOff.setBackgroundResource(R.drawable.circleshap);

        } else {
            //holder.ctvbOld.setVisibility(View.GONE);
            holder.ctvbDiscount.setText("");
            holder.llOff.setVisibility(View.GONE);
            holder.llOff.setBackgroundResource(R.drawable.circletransshap);
        }
        holder.ratingBar.setRating(Float.parseFloat(foodList.get(position).getProduct_rating()));
        try {
            Glide.with(context)
                    .load(foodList.get(position).getProImage().get(0).getImage())
                    .placeholder(R.drawable.app_logo)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivFoodPic);
        } catch (Exception e) {

        }

        try {
            if (foodList.get(position).getSize().size() > 0) {
                holder.ctvSize.setText("Size : " + foodList.get(position).getSize().toString().trim().substring(1, foodList.get(position).getSize().toString().length() - 1));
            } else {
                holder.ctvSize.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
        try {
            if (!foodList.get(position).getWeight().equals("0")) {
                holder.ctvWeight.setText("Weight : " + foodList.get(position).getWeight());
            } else {
                holder.ctvWeight.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }


        try {

            if (foodList.get(position).getColor().size() > 0) {
                holder.ctvColor.setText("Color : " + foodList.get(position).getColor().toString().trim().substring(1, foodList.get(position).getColor().toString().length() - 1));
            } else {
                holder.ctvColor.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }


        //  holder.ctvbDiscount.setText(context.getResources().getString(R.string.Rs) + " " + ProjectUtils.round(foodList.get(position).getP_rate() - foodList.get(position).getP_rate_sale(), 1) + " \n SAVE");

        holder.rlFoodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, FoodDetail.class);
                in.putExtra(Consts.PRODUCT_DETAILS, foodList.get(position));
                in.putExtra(Consts.P_PET_TYPE, foodList.get(position).getP_pet_type());
                in.putExtra(Consts.P_TYPE, foodList.get(position).getP_type());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold ctvbFoodTitle, ctvbPrice1, ctvbOld, ctvbDiscount;
        CustomTextView ctvFoodDesc, ctvColor, ctvSize, ctvWeight;
        ImageView ivFoodPic, out_of_stock;
        RelativeLayout rlFoodList;
        MaterialRatingBar ratingBar;
        LinearLayout llOff;

        public MyViewHolder(View item) {
            super(item);
            ctvbFoodTitle = (CustomTextViewBold) item.findViewById(R.id.ctvbFoodTitle);
            ctvbPrice1 = (CustomTextViewBold) item.findViewById(R.id.ctvbPrice1);
            ctvbDiscount = (CustomTextViewBold) item.findViewById(R.id.ctvbDiscount);
            ctvbOld = (CustomTextViewBold) item.findViewById(R.id.ctvbOld);
            ctvFoodDesc = (CustomTextView) item.findViewById(R.id.ctvFoodDesc);
            ctvColor = (CustomTextView) item.findViewById(R.id.ctvColor);
            ctvSize = (CustomTextView) item.findViewById(R.id.ctvSize);
            ctvWeight = (CustomTextView) item.findViewById(R.id.ctvWeight);

            ivFoodPic = (ImageView) item.findViewById(R.id.ivFoodPic);
            out_of_stock = (ImageView) item.findViewById(R.id.stiv);
            rlFoodList = (RelativeLayout) item.findViewById(R.id.rlFoodList);
            ratingBar = (MaterialRatingBar) item.findViewById(R.id.ratingBar);
            llOff = item.findViewById(R.id.llOff);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        foodList.clear();
        if (charText.length() == 0) {
            foodList.addAll(list);
        } else {
            for (FoodListDTO foodListDTO : list) {
                if (foodListDTO.getP_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    foodList.add(foodListDTO);
                }
            }
        }
        notifyDataSetChanged();
    }
}