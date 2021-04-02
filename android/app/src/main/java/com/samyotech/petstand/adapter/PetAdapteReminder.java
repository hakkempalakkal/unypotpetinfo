package com.samyotech.petstand.adapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.duties.CreateReminder;
import com.samyotech.petstand.models.PetListDTO;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by varun on 21/3/18.
 */

public class PetAdapteReminder extends RecyclerView.Adapter<PetAdapteReminder.UpperScrollHolder> {

    private CreateReminder createReminder;
    private ArrayList<PetListDTO> petListDTOList;
    LayoutInflater inflater;
    public int selectedposition = -1;
    public PetAdapteReminder(CreateReminder createReminder, ArrayList<PetListDTO> petListDTOList, LayoutInflater inflater) {
        this.createReminder = createReminder;
        this.petListDTOList = petListDTOList;
        this.inflater = inflater;
    }

    @Override
    public UpperScrollHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.adapter_pet_reminder, parent, false);
        UpperScrollHolder holder = new UpperScrollHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(UpperScrollHolder holder, final int position) {

        Glide
                .with(createReminder)
                .load(petListDTOList.get(position).getPet_img_path())
                .placeholder(R.drawable.betaal)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.upperScrollCIV);
        if (selectedposition == position) {
            holder.parentLayout.setBackground(createReminder.getResources().getDrawable(R.drawable.pet_select));


        } else {
            holder.parentLayout.setBackgroundColor(Color.TRANSPARENT);
        }


        holder.llone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedposition);
                selectedposition = position;
                notifyItemChanged(selectedposition);
                createReminder.SELECTED_PET = petListDTOList.get(position).getId();
            }
        });

    }

    @Override
    public int getItemCount() {
        return petListDTOList.size();
    }

    public class UpperScrollHolder extends RecyclerView.ViewHolder {
        public CircleImageView upperScrollCIV;
        public LinearLayout llone, parentLayout;

        public UpperScrollHolder(View itemView) {
            super(itemView);
            upperScrollCIV = (CircleImageView) itemView.findViewById(R.id.upperScrollCIV);
            llone = (LinearLayout) itemView.findViewById(R.id.llone);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);
        }
    }
}
