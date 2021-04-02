package com.samyotech.petstand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.filter.FilterPetMarketActivity;
import com.samyotech.petstand.utils.CustomTextViewBold;

import java.util.ArrayList;

/**
 * Created by varunverma on 25/9/17.
 */

public class AdapterFilterPetMarketCategory extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> categoryList;
    private FilterPetMarketActivity filterActivity;
    public int selectedposition = 0;

    public AdapterFilterPetMarketCategory(Context mContext, ArrayList<String> categoryList, FilterPetMarketActivity filterActivity) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.filterActivity = filterActivity;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.adapter_filter_category, null);
        holder.tvCategory = (CustomTextViewBold) itemView.findViewById(R.id.tvCategory);
        holder.llClick = (RelativeLayout) itemView.findViewById(R.id.llClick);
        holder.ivArrow = (ImageView) itemView.findViewById(R.id.ivArrow);

        holder.tvCategory.setText(categoryList.get(position));

        if (selectedposition == position) {
            holder.llClick.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.ivArrow.setVisibility(View.VISIBLE);

        } else {
            holder.llClick.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            holder.ivArrow.setVisibility(View.GONE);

        }
        holder.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                filterActivity.showData(position);
                selectedposition = position;
                notifyDataSetChanged();
            }
        });

        return itemView;
    }


    static class ViewHolder {
        public CustomTextViewBold tvCategory;
        public RelativeLayout llClick;
        public ImageView ivArrow;
    }

}
