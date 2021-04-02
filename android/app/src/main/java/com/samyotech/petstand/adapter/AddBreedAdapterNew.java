package com.samyotech.petstand.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.addpet.AddBreedNew;
import com.samyotech.petstand.models.BreedDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.CustomTextView;

import java.util.ArrayList;
import java.util.Locale;


public class AddBreedAdapterNew extends RecyclerView.Adapter<AddBreedAdapterNew.ViewHolder> {

    private ArrayList<BreedDTO> objects;
    private ArrayList<BreedDTO> breeds =null;
    private ArrayList<Integer> mSectionPositions;
    private DisplayImageOptions options;
    String charText = "";
    private SharedPrefrence prefrence;
    private Context context;
    public static String breedId;
    AddBreedNew addBreedNew;


    private static SparseBooleanArray sSelectedItems;
    private static UpdateDataClickListener sClickListener;
    private static int sPosition;

    public AddBreedAdapterNew(AddBreedNew addBreedNew, Context context, ArrayList<BreedDTO> breeds) {
        this.addBreedNew = addBreedNew;
        this.breeds = breeds;
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
        objects = new ArrayList<>();
        objects.addAll(breeds);

        sSelectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_breed_item, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvBreedNameCollapsed.setText(breeds.get(position).getBreed_name().trim());
        holder.tv_origin_header.setText(breeds.get(position).getOrigin().trim());
        Glide.with(context)
                .load(breeds.get(position).getImage_path())
                .into(holder.ivPetBreedCollapsed);


     /*   if (breeds.get(position).isSelected())
        {
            holder.checkBox.setChecked(true);
        } else
            {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBreedNew.updateList(position);

            }
        });*/
        if (sSelectedItems.get(position)) {
            holder.cvBreed.setCardBackgroundColor(addBreedNew.getResources().getColor(R.color.blue2));
            holder.tvBreedNameCollapsed.setTextColor(addBreedNew.getResources().getColor(R.color.white));
            holder.tv_origin_header.setTextColor(addBreedNew.getResources().getColor(R.color.white));
        } else {
            holder.cvBreed.setCardBackgroundColor(addBreedNew.getResources().getColor(R.color.white));
            holder.tvBreedNameCollapsed.setTextColor(addBreedNew.getResources().getColor(R.color.inset));
            holder.tv_origin_header.setTextColor(addBreedNew.getResources().getColor(R.color.text_color_gray));
        }
        holder.llBreedInfo.setSelected(sSelectedItems.get(position, false));


    }

    @Override
    public int getItemCount() {
        return breeds.size();
    }


    public void setOnItemClickListener(UpdateDataClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        CustomTextView tvBreedNameCollapsed, tv_origin_header;
        ImageView ivPetBreedCollapsed;
        RelativeLayout rlBreedHeaderViewCollapsed;
        CheckBox checkBox;
        CardView cvBreed;
        LinearLayout llBreedInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            cvBreed = (CardView) itemView.findViewById(R.id.cvBreed);
            llBreedInfo = (LinearLayout) itemView.findViewById(R.id.llBreedInfo);
            ivPetBreedCollapsed = (ImageView) itemView.findViewById(R.id.ivPetBreedCollapsed);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            tvBreedNameCollapsed = itemView.findViewById(R.id.tvBreedNameCollapsed);
            tv_origin_header = itemView.findViewById(R.id.tv_origin_header);
            rlBreedHeaderViewCollapsed = (RelativeLayout) itemView.findViewById(R.id.rlBreedHeaderViewCollapsed);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (sSelectedItems.get(getAdapterPosition(), false)) {
                sSelectedItems.delete(getAdapterPosition());
                llBreedInfo.setSelected(false);
                cvBreed.setCardBackgroundColor(addBreedNew.getResources().getColor(R.color.white));
                tvBreedNameCollapsed.setTextColor(addBreedNew.getResources().getColor(R.color.inset));
                tv_origin_header.setTextColor(addBreedNew.getResources().getColor(R.color.text_color_gray));
            } else {
                sSelectedItems.put(sPosition, false);
                sSelectedItems.put(getAdapterPosition(), true);
                llBreedInfo.setSelected(true);
                cvBreed.setCardBackgroundColor(addBreedNew.getResources().getColor(R.color.blue2));
                tvBreedNameCollapsed.setTextColor(addBreedNew.getResources().getColor(R.color.white));
                tv_origin_header.setTextColor(addBreedNew.getResources().getColor(R.color.white));
            }
            sClickListener.onItemClick(getAdapterPosition());
        }

    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        breeds.clear();
        if (charText.length() == 0) {
            breeds.addAll(objects);
        } else {
            for (BreedDTO breedDTO : objects) {
                if (breedDTO.getBreed_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    breeds.add(breedDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void selected(int position) {
        sPosition = position;
        notifyDataSetChanged();
    }



    public interface UpdateDataClickListener {
        void onItemClick(int position);
    }
}