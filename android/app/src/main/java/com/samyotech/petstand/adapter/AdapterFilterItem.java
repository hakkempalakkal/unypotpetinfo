package com.samyotech.petstand.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.models.DummyFilterDTO;

import java.util.ArrayList;

/**
 * Created by varunverma on 25/9/17.
 */

public class AdapterFilterItem extends BaseAdapter {
    private Context mContext;
    private ArrayList<DummyFilterDTO> dummyFilterList;
    private Typeface font;
    public AdapterFilterItem(Context mContext, ArrayList<DummyFilterDTO> dummyFilterList) {
        this.mContext = mContext;
        this.dummyFilterList = dummyFilterList;
    }

    @Override
    public int getCount() {
        return dummyFilterList.size();
    }

    @Override
    public Object getItem(int position) {
        return dummyFilterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.adapter_filter_item, null);
        holder.cbFilter = (CheckBox) itemView.findViewById(R.id.cbFilter);
        holder.llClick = (LinearLayout) itemView.findViewById(R.id.llClick);
        font = Typeface.createFromAsset(
                mContext.getAssets(),
                "Ubuntu-Medium.ttf");

        holder.cbFilter.setText(dummyFilterList.get(position).getName());
        holder.cbFilter.setTypeface(font);
        if (dummyFilterList.get(position).isChecked()) {
            holder.cbFilter.setChecked(true);
        } else {
            holder.cbFilter.setChecked(false);
        }

        holder.cbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.cbFilter.getText().toString().trim().equalsIgnoreCase("Both")){

                    if (isChecked) {
                        dummyFilterList.get(position).setChecked(false);

                    } else {
                        dummyFilterList.get(position).setChecked(true);
                    }
                }else {
                    if (isChecked) {
                        dummyFilterList.get(position).setChecked(true);

                    } else {
                        dummyFilterList.get(position).setChecked(false);
                    }
                }


            }
        });
        return itemView;
    }

    static class ViewHolder {
        public CheckBox cbFilter;
        public LinearLayout llClick;
    }
}
