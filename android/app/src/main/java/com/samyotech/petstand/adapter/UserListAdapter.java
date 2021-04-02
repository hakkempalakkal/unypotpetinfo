package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.nearbypetlist.FriendPetProfile;
import com.samyotech.petstand.models.NearByUserDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayank on 20/2/18.
 */

public class UserListAdapter extends BaseAdapter {
    ArrayList<NearByUserDTO> list;
    ArrayList<NearByUserDTO> nearByUserDTOList = null;
    LayoutInflater inflater;
    Context context;
    public UserListAdapter(Context context, ArrayList<NearByUserDTO> nearByUserDTOList, LayoutInflater inflater) {
        this.nearByUserDTOList = nearByUserDTOList;
        this.context = context;
        this.inflater = inflater;
        this.list = new ArrayList<NearByUserDTO>();
        this.list.addAll(nearByUserDTOList);
    }

    @Override
    public int getCount() {
        return nearByUserDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return nearByUserDTOList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        UserListAdapter.MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_list_adapter, parent, false);
            mViewHolder = new UserListAdapter.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (UserListAdapter.MyViewHolder) convertView.getTag();
        }
        double d = Double.parseDouble(nearByUserDTOList.get(position).getDistance());
        String bob = Double.toString(d);
        //     String bob = list.get(position).getDistance();
        String[] convert = bob.split("\\.");

        mViewHolder.ctvbUserTitle.setText(nearByUserDTOList.get(position).getFirst_name());
        mViewHolder.tvDistance.setText(convert[0] + " KM away, "+nearByUserDTOList.get(position).getCity()+", "+nearByUserDTOList.get(position).getCountry());

        Glide
                .with(context)
                .load(nearByUserDTOList.get(position).getProfile_pic())
                .placeholder(R.drawable.user_profile)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mViewHolder.ivUserPic);

        mViewHolder.cvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, FriendPetProfile.class);
                in.putExtra(Consts.USER_PROFILE, nearByUserDTOList.get(position));
                context.startActivity(in);
            }
        });

        return convertView;
    }



    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nearByUserDTOList.clear();
        if (charText.length() == 0) {
            nearByUserDTOList.addAll(list);
        } else {
            for (NearByUserDTO nearByUserDTO : list) {
                if (nearByUserDTO.getFirst_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    nearByUserDTOList.add(nearByUserDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class MyViewHolder {
        CustomTextViewBold ctvbUserTitle;
        CircleImageView ivUserPic;
        CardView cvUser;
        CustomTextView tvDistance;

        public MyViewHolder(View item) {
            tvDistance = (CustomTextView) item.findViewById(R.id.tvDistance);
            ctvbUserTitle = (CustomTextViewBold) item.findViewById(R.id.ctvbUserTitle);
            cvUser = (CardView) item.findViewById(R.id.cvUser);
            ivUserPic = (CircleImageView) item.findViewById(R.id.ivUserPic);
        }
    }
}
