package com.samyotech.petstand.adapter;


import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.nearbypetlist.PetProfileNearByUser;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by varunverma on 22/11/17.
 */

public class ContactPetListAdapter extends RecyclerView.Adapter<ContactPetListAdapter.petListDTOListHolder> {
    private Context mContext;
    ArrayList<PetListDTO> list;
    private ArrayList<PetListDTO> petListDTOList = null;
    LayoutInflater inflater;

    public ContactPetListAdapter(Context mContext, ArrayList<PetListDTO> petListDTOList, LayoutInflater inflater) {
        this.mContext = mContext;
        this.petListDTOList = petListDTOList;
        this.inflater = inflater;
        this.list = new ArrayList<PetListDTO>();
        this.list.addAll(petListDTOList);
    }

    @Override
    public petListDTOListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_pet_adapter, parent, false);

        return new petListDTOListHolder(itemView);
    }


    @Override
    public void onBindViewHolder(petListDTOListHolder mViewHolder, final int position) {
        try {

            mViewHolder.ctvbPetTitle.setText(petListDTOList.get(position).getPetName());
            mViewHolder.ctvbPetType.setText(petListDTOList.get(position).getPet_type_name());
            mViewHolder.ctvAge.setText(ProjectUtils.calculateAge(petListDTOList.get(position).getBirth_date()));
            mViewHolder.ctvBreed.setText(petListDTOList.get(position).getBreed_name());
            mViewHolder.ctvCity.setText(petListDTOList.get(position).getCity() + ", " + petListDTOList.get(position).getCountry());
            mViewHolder.ctvMessage.setText("Message : "+ProjectUtils.getFirstLetterCapital(petListDTOList.get(position).getMsg()));

            if (petListDTOList.get(position).getSex().equalsIgnoreCase("Male")) {
                mViewHolder.ivGender.setImageDrawable(mContext.getResources().getDrawable(R.drawable.male));

            } else {
                mViewHolder.ivGender.setImageDrawable(mContext.getResources().getDrawable(R.drawable.female_a));
            }
            Glide
                    .with(mContext)
                    .load(petListDTOList.get(position).getPet_img_path())
                    .placeholder(R.drawable.ears_icon)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mViewHolder.ivPetPic);

        } catch (Exception e) {
            e.printStackTrace();
            ProjectUtils.showLong(mContext, "Something went wrong...");
        }
        mViewHolder.cvFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
                Intent in = new Intent(mContext, PetProfileNearByUser.class);
                in.putExtra(Consts.PET_PROFILE, petListDTOList.get(position));
                mContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petListDTOList.size();
    }

    public class petListDTOListHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold ctvbPetTitle, ctvbPetType;
        CircleImageView ivPetPic;
        ImageView ivGender;
        CustomTextView ctvAge, ctvBreed, ctvCity, ctvMessage;
        CardView cvFriendList;


        public petListDTOListHolder(View item) {
            super(item);
            ctvbPetTitle = (CustomTextViewBold) item.findViewById(R.id.ctvbPetTitle);
            ctvbPetType = (CustomTextViewBold) item.findViewById(R.id.ctvbPetType);
            ctvBreed = (CustomTextView) item.findViewById(R.id.ctvBreed);
            ctvAge = (CustomTextView) item.findViewById(R.id.ctvAge);
            ctvCity = (CustomTextView) item.findViewById(R.id.ctvCity);
            ctvMessage = (CustomTextView) item.findViewById(R.id.ctvMessage);
            cvFriendList = (CardView) item.findViewById(R.id.cvFriendList);
            ivPetPic = (CircleImageView) item.findViewById(R.id.ivPetPic);
            ivGender = (ImageView) item.findViewById(R.id.ivGender);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        petListDTOList.clear();
        if (charText.length() == 0) {
            petListDTOList.addAll(list);
        } else {
            for (PetListDTO petListDTO : list) {
                if (petListDTO.getBreed_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    petListDTOList.add(petListDTO);
                }
            }
        }
        notifyDataSetChanged();
    }


}
