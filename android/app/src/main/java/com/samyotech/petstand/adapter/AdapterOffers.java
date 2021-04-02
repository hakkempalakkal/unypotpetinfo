package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.FoodDetail;
import com.samyotech.petstand.databinding.AdapterTopOffersBinding;
import com.samyotech.petstand.models.OffersDTO;
import com.samyotech.petstand.utils.Consts;

import java.util.ArrayList;

public class AdapterOffers extends RecyclerView.Adapter<AdapterOffers.MyViewHolder> {

    private Context mcontext;
    private ArrayList<OffersDTO> offersDTOArrayList;
    private AdapterTopOffersBinding binding;
    private LayoutInflater layoutInflater;

    public AdapterOffers(Context mcontext, ArrayList<OffersDTO> offersDTOArrayList) {
        this.mcontext = mcontext;
        this.offersDTOArrayList = offersDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_top_offers, viewGroup, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        Glide
                .with(mcontext)
                .load(offersDTOArrayList.get(position).getImage())

                .placeholder(R.drawable.app_logo)
                .into(myViewHolder.binding.image);

        myViewHolder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn = new Intent(mcontext, FoodDetail.class);
                inn.putExtra(Consts.PRODUCT_DETAILS,offersDTOArrayList.get(position));
                inn.putExtra(Consts.FLAG,1);
                mcontext.startActivity(inn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offersDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AdapterTopOffersBinding binding;

        public MyViewHolder(@NonNull AdapterTopOffersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
