package com.samyotech.petstand.adapter;

/**
 * Created by mayank on 31/10/17.
 */

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.petprofile.PetFolowersActivity;
import com.samyotech.petstand.activity.wall.ViewPost;
import com.samyotech.petstand.models.NotificationDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CustomTextView CTVdescription, CTVdate;
        CustomTextViewBold CTVtitel;
        ImageView IVcolorset, IVheart;
        RelativeLayout rlNoti;


        public MyViewHolder(View view) {
            super(view);

            CTVdescription = (CustomTextView) view.findViewById(R.id.CTVdescription);
            CTVdate = (CustomTextView) view.findViewById(R.id.CTVdate);
            CTVtitel = (CustomTextViewBold) view.findViewById(R.id.CTVtitel);
            rlNoti = view.findViewById(R.id.rlNoti);


        }
    }

    public NotificationAdapter(Context mContext, ArrayList<NotificationDTO> notificationDTOS) {
        this.mContext = mContext;
        this.notificationDTOS = notificationDTOS;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapternotificationxml, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            holder.CTVdescription.setText(notificationDTOS.get(position).getMsg());
            holder.CTVtitel.setText(notificationDTOS.get(position).getTitle());

            //String strdate = ProjectUtils.parseDateToddMMyyyy(notificationDTOS.get(position).getCreated_at());

            holder.CTVdate.setText(ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(notificationDTOS.get(position).getCreated_at()))));
            // holder.CTVdate.setText(ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(notificationDTOS.get(position).getCreated_at()))));

            holder.rlNoti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (notificationDTOS.get(position).getType().equals("Comment")) {
                        goViewActivity(notificationDTOS.get(position).getIds());
                    } else if (notificationDTOS.get(position).getType().equals("Like")) {
                        goViewActivity(notificationDTOS.get(position).getIds());
                    } else if (notificationDTOS.get(position).getType().equals("Follow")) {
                        Intent intent = new Intent(mContext, PetFolowersActivity.class);
                        intent.putExtra(Consts.PET_ID, notificationDTOS.get(position).getIds());
                       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });


        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {

        return notificationDTOS.size();
    }

    public void goViewActivity(String id) {
        Intent intent = new Intent(mContext, ViewPost.class);
        intent.putExtra(Consts.POSTID, id);
        mContext.startActivity(intent);
    }
}