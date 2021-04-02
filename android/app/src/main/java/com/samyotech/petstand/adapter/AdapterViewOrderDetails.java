package com.samyotech.petstand.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.MyOrder.vieworder.ViewOrderActivity;
import com.samyotech.petstand.models.MyOrderDetails;
import com.samyotech.petstand.utils.CustomTextView;

import java.util.ArrayList;

/**
 * Created by manakbarve on 23/1/18.
 */

public class AdapterViewOrderDetails extends RecyclerView.Adapter<AdapterViewOrderDetails.ItemViewHolder> {
    private ViewOrderActivity viewOrderActivity;
    private ArrayList<MyOrderDetails> myOrderDtoItemList;
    private LayoutInflater inflater;

    public AdapterViewOrderDetails(ViewOrderActivity viewOrderActivity, ArrayList<MyOrderDetails> myOrderDtoItemList, LayoutInflater inflater) {
        this.viewOrderActivity = viewOrderActivity;
        this.myOrderDtoItemList = myOrderDtoItemList;
        this.inflater = inflater;

    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_order_view, parent, false);
        AdapterViewOrderDetails.ItemViewHolder holder = new AdapterViewOrderDetails.ItemViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.tvProductName.setText(myOrderDtoItemList.get(position).getP_name());

        if (!myOrderDtoItemList.get(position).getColor().equals("")) {
            holder.tvProductQuantity.setText("Color - " + myOrderDtoItemList.get(position).getColor() + " Size - " + myOrderDtoItemList.get(position).getSize());
        } else if (!myOrderDtoItemList.get(position).getWeight().equals("")) {
            holder.tvProductQuantity.setText("Weight - " + myOrderDtoItemList.get(position).getWeight());
        }


        holder.tvTotal.setText(myOrderDtoItemList.get(position).getCurrency_type() + " " + myOrderDtoItemList.get(position).getTotal_price());
        holder.tvPrice.setText(myOrderDtoItemList.get(position).getCurrency_type() + " " + myOrderDtoItemList.get(position).getPrice_discount());
        holder.tvQuntity.setText(" x " + myOrderDtoItemList.get(position).getQuantity());

        Glide.with(viewOrderActivity)
                .load(myOrderDtoItemList.get(position).getImg_path())
                .placeholder(R.drawable.app_logo)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivProcuct);
    }


    @Override
    public int getItemCount() {
        return myOrderDtoItemList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvProductQuantity, tvProductName, tvTotal, tvQuntity, tvPrice;
        ImageView ivProcuct;


        public ItemViewHolder(View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvQuntity = itemView.findViewById(R.id.tvQuntity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivProcuct = itemView.findViewById(R.id.ivProcuct);

        }
    }


}
