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
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.ShopifyFoodDetail;
import com.samyotech.petstand.models.ProductSopifyDTO;
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

public class ShopifyFoodListAdapter extends RecyclerView.Adapter<ShopifyFoodListAdapter.MyViewHolder> {

    private ArrayList<ProductSopifyDTO> list;
    private ArrayList<ProductSopifyDTO> foodList = null;
    private SharedPrefrence prefrence;
    private Context context;

    public ShopifyFoodListAdapter(Context context, ArrayList<ProductSopifyDTO> foodList) {
        this.foodList = foodList;
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
        this.list = new ArrayList<ProductSopifyDTO>();
        this.list.addAll(foodList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.ctvbFoodTitle.setText(foodList.get(position).getTitle());
        holder.ctvFoodDesc.setText(ProjectUtils.removeHtmlFromText(foodList.get(position).getBody_html()));
        holder.ctvFoodSKU.setText(foodList.get(position).getVariants().get(0).getSku());
        holder.ctvbPrice1.setText("$ " + foodList.get(position).getVariants().get(0).getPrice()/* + " " + ProjectUtils.round(foodList.get(position).getP_rate_sale(), 1) + ""*/);
        holder.ctvbOld.setText(/*foodList.get(position).getCurrency_type() + " " +*/ foodList.get(position).getVariants().get(0).getPrice());
        holder.ctvbOld.setPaintFlags(holder.ctvbOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.ctvbDiscount.setText("$ " + foodList.get(position).getVariants().get(0).getPrice()/*, 1) + "% off"*/);
        // holder.ratingBar.setRating(Float.parseFloat(foodList.get(position).getProduct_rating()));
        Glide.with(context)
                .load(foodList.get(position).getImage().getSrc())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivFoodPic);
       // holder.tvOff.setText(context.getResources().getString(R.string.Rs) + " " + /*ProjectUtils.round(*/foodList.get(position).getVariants().get(0).getPrice() /*- foodList.get(position).getVariants().get(0).getPrice(), 1) + " \n SAVE")*/);

        holder.rlFoodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, ShopifyFoodDetail.class);
                in.putExtra(Consts.PRODUCT_DETAILS, foodList.get(position));
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
        CustomTextView ctvFoodDesc,  ctvFoodSKU;
        ImageView ivFoodPic;
        RelativeLayout rlFoodList;
        MaterialRatingBar ratingBar;

        public MyViewHolder(View item) {
            super(item);
            ctvbFoodTitle = (CustomTextViewBold) item.findViewById(R.id.ctvbFoodTitle);
            ctvbPrice1 = (CustomTextViewBold) item.findViewById(R.id.ctvbPrice1);
            ctvbDiscount = (CustomTextViewBold) item.findViewById(R.id.ctvbDiscount);
            ctvbOld = (CustomTextViewBold) item.findViewById(R.id.ctvbOld);
            ctvFoodDesc = (CustomTextView) item.findViewById(R.id.ctvFoodDesc);
           // tvOff = (CustomTextView) item.findViewById(R.id.tvOff);
            ivFoodPic = (ImageView) item.findViewById(R.id.ivFoodPic);
            rlFoodList = (RelativeLayout) item.findViewById(R.id.rlFoodList);
            ratingBar = (MaterialRatingBar) item.findViewById(R.id.ratingBar);
            ctvFoodSKU = item.findViewById(R.id.ctvFoodSKU);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        foodList.clear();
        if (charText.length() == 0) {
            foodList.addAll(list);
        } else {
            for (ProductSopifyDTO foodListDTO : list) {
                if (foodListDTO.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    foodList.add(foodListDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

}