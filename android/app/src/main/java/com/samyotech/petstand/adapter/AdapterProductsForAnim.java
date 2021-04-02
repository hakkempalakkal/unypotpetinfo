package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.FoodDetail;
import com.samyotech.petstand.databinding.AdapterProductsForAnimBinding;
import com.samyotech.petstand.models.FoodListDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

public class AdapterProductsForAnim extends RecyclerView.Adapter<AdapterProductsForAnim.MyViewHolder> {
    private Context mcontext;
    private ArrayList<FoodListDTO> productsForAnimArrayList;
    private AdapterProductsForAnimBinding binding;
    private LayoutInflater layoutInflater;


    public AdapterProductsForAnim(Context mcontext, ArrayList<FoodListDTO> productsForAnimArrayList) {
        this.mcontext = mcontext;
        this.productsForAnimArrayList = productsForAnimArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_products_for_anim, viewGroup, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {


        if (!productsForAnimArrayList.get(i).getDiscount().equals("0")) {
                myViewHolder.binding.ctvbOld.setText(productsForAnimArrayList.get(i).getCurrency_type() + " " + ProjectUtils.round(productsForAnimArrayList.get(i).getP_rate_sale(), 2) + "");
                myViewHolder.binding.ctvbOld.setPaintFlags(myViewHolder.binding.ctvbOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                myViewHolder.binding.ctvbDiscount.setText(productsForAnimArrayList.get(i).getDiscount() + "% off");
                myViewHolder.binding.ctvbOld.setVisibility(View.VISIBLE);
                myViewHolder.binding.ctvbDiscount.setVisibility(View.VISIBLE);
                myViewHolder.binding.llOff.setBackgroundResource(R.drawable.circleshap);

            } else {
                //myViewHolder.binding.ctvbOld.setVisibility(View.GONE);
                myViewHolder.binding.ctvbDiscount.setText("");
                //myViewHolder.binding.llOff.setVisibility(View.GONE);
                myViewHolder.binding.llOff.setBackgroundResource(R.drawable.circletransshap);
            }

            Glide.with(mcontext)
                    .load(productsForAnimArrayList.get(i).getImg_path())
                    .placeholder(R.drawable.app_logo)
                    .into(myViewHolder.binding.ivFoodPic);




            myViewHolder.binding.ctvbFoodTitle.setText(productsForAnimArrayList.get(i).getP_name());
            myViewHolder.binding.ctvFoodDesc.setText(productsForAnimArrayList.get(i).getP_description());
            myViewHolder.binding.ctvbPrice1.setText(productsForAnimArrayList.get(i).getCurrency_type() + " " + productsForAnimArrayList.get(i).getFinal_amount());

            myViewHolder.binding.llProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.click_event));
                    Intent in = new Intent(mcontext, FoodDetail.class);

                    in.putExtra(Consts.P_PET_TYPE, productsForAnimArrayList.get(i).getP_pet_type());
                    in.putExtra(Consts.P_TYPE, productsForAnimArrayList.get(i).getP_type());
                    in.putExtra(Consts.PRODUCT_DETAILS, productsForAnimArrayList.get(i));
                    mcontext.startActivity(in);
                }
            });
        }

    @Override
    public int getItemCount() {
        return productsForAnimArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AdapterProductsForAnimBinding binding;

        public MyViewHolder(@NonNull AdapterProductsForAnimBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
