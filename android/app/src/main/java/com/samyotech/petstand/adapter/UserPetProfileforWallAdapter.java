package com.samyotech.petstand.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.EventDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayank on 16/2/18.
 */

public class UserPetProfileforWallAdapter extends RecyclerView.Adapter<UserPetProfileforWallAdapter.MyViewHolder> {


    private ArrayList<EventDTO> eventDTOlist;
    private SharedPrefrence prefrence;
    private Context context;

    public UserPetProfileforWallAdapter(Context context, ArrayList<EventDTO> eventDTOlist) {
        this.eventDTOlist = eventDTOlist;
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_pet_profile, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvPetName.setText(ProjectUtils.getFirstLetterCapital(eventDTOlist.get(position).getEvent_name()));
        holder.tvBreedName.setText(eventDTOlist.get(position).getAddress());
        Glide.with(context)
                .load(eventDTOlist.get(position).getImage())
                .placeholder(R.drawable.default_error)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPet);

        holder.rlEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent inn = new Intent(context, MyEventDescriptionActivity.class);
                inn.putExtra(Consts.EVENT_DTO, eventDTOlist.get(position));
                context.startActivity(inn);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventDTOlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlEventList;
        CustomTextViewBold tvPetName;
        CustomTextView tvBreedName;
        CircleImageView ivPet;

        public MyViewHolder(View item) {
            super(item);
            rlEventList = item.findViewById(R.id.rlEventList);
            tvPetName = item.findViewById(R.id.tvPetName);
            tvBreedName = item.findViewById(R.id.tvBreedName);
            ivPet = item.findViewById(R.id.ivPet);

        }
    }

}