package com.samyotech.petstand.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.FoodDetail;
import com.samyotech.petstand.models.SingleFoodListDTO;

import java.util.ArrayList;

/**
 * Created by mayank on 16/2/18.
 */

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.MyViewHolder> {

    private ArrayList<SingleFoodListDTO.ProjectDetails> petCatList;
    private FoodDetail context;
    public static ArrayList<String> manulist;
    int selectedposition = 0;

    public ProductSizeAdapter(FoodDetail context, ArrayList<SingleFoodListDTO.ProjectDetails> petCatList) {
        this.petCatList = petCatList;
        this.context = context;
        manulist = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.productvaluxml, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.chValue.setText(petCatList.get(position).getSize());


        if (selectedposition == position) {
            holder.chValue.setChecked(true);
        } else {
            holder.chValue.setChecked(false);

        }

        holder.chValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    manulist = new ArrayList<>();
                    notifyDataSetChanged();
                    selectedposition = position;
                    manulist.add(petCatList.get(position).getSize());
                    context.setPrice(position);
                    notifyDataSetChanged();
                } else {
                    manulist.remove(petCatList.get(position));

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return petCatList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox chValue;


        public MyViewHolder(View item) {
            super(item);
            chValue = (CheckBox) item.findViewById(R.id.chValue);

        }
    }


}