package com.samyotech.petstand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.duties.AddReminder;
import com.samyotech.petstand.models.ReminderCategory;
import com.samyotech.petstand.utils.CustomTextView;

import java.util.List;

/**
 * Created by welcome pc on 26-12-2017.
 */

public class ReminderAdapter extends BaseAdapter {

    List<ReminderCategory> objects;
    Context context;
    AddReminder addReminder;


    public ReminderAdapter(AddReminder addReminder,
                           List<ReminderCategory> objects) {
        super();
        this.objects = objects;
        this.context = addReminder;
        this.addReminder = addReminder;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflator = LayoutInflater.from(context);
        convertView = layoutInflator.inflate(R.layout.add_reminder_items,null);
        CustomTextView category = convertView.findViewById(R.id.tvOne3);
        ImageView cate_img = convertView.findViewById(R.id.cate_img);
        category.setText(objects.get(position).getName());
        cate_img.setImageResource(objects.get(position).getImgSource());
        return convertView;
    }

}