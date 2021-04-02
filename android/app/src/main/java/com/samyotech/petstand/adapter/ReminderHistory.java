package com.samyotech.petstand.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.duties.HistoryReminder;
import com.samyotech.petstand.models.Reminder;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.List;

/**
 * Created by welcome pc on 05-01-2018.
 */

public class ReminderHistory extends ArrayAdapter<Reminder> {

    List<Reminder> objects;
    Context context;
    HistoryReminder historyReminder;


    public ReminderHistory(HistoryReminder historyReminder, int textViewResourceId,
                           List<Reminder> objects) {
        super(historyReminder, textViewResourceId, objects);
        this.objects = objects;
        this.context = historyReminder;
        this.historyReminder = historyReminder;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflator = LayoutInflater.from(getContext());
        convertView = layoutInflator.inflate(R.layout.duties_item,
                null);
        CustomTextView reminder_name = convertView.findViewById(R.id.reminder_name);
        CustomTextView tvDog = convertView.findViewById(R.id.tvDog);
        CustomTextView tvDate1 = convertView.findViewById(R.id.tvDate1);
        CustomTextView tvDay = convertView.findViewById(R.id.tvDay);
        ImageView indicate = convertView.findViewById(R.id.indicate);
        CardView cardone = convertView.findViewById(R.id.cardone);
        indicate.setVisibility(View.GONE);

        ImageView ivOne = convertView.findViewById(R.id.ivOne);
        ivOne.setImageResource(historyReminder.sysApplication.reminderMapping.get(objects.get(position).getCategory()));
        reminder_name.setText(objects.get(position).getCategory());
        tvDog.setText(objects.get(position).getPet_name());
        tvDate1.setText(objects.get(position).getDate_string());

        long currentTime = System.currentTimeMillis() / 1000L;
        long itemTime = Long.valueOf(objects.get(position).getAppointment_timestamp());
        if (currentTime > itemTime) {
            //tvDay.setTextColor(Color.RED);
            tvDay.setText(ProjectUtils.getDifferenceInTwoDate(objects.get(position).getAppointment_date()) + " Days Overdue");
        } else {
            //tvDay.setTextColor(Color.parseColor("#b3b3b3"));
            tvDay.setText(ProjectUtils.getDifferenceInTwoDate(objects.get(position).getAppointment_date()) + " Days Left");

        }
        cardone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objects.get(position).getStatus() == 0)
                    historyReminder.clickOnItems(position);
            }
        });
//3 means completed
//        if(objects.get(position).getStatus() == 3)
//        {
//            cardone.setBackgroundColor(Color.parseColor("#00C416"));
//        }//2 means missed
//        else  if(objects.get(position).getStatus() == 2)
//        {
//            cardone.setBackgroundColor(Color.parseColor("#F71800"));
//        }//0 means active
//        else  if(objects.get(position).getStatus() == 0)
//        {
//            cardone.setBackgroundColor(Color.parseColor("#FF6600"));
//        }

        return convertView;
    }

}