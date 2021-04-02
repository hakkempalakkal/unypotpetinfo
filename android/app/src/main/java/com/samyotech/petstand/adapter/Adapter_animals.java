package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.SubCategoryList;
import com.samyotech.petstand.databinding.AdapterAnimalBinding;
import com.samyotech.petstand.models.AnimalsDTO;
import com.samyotech.petstand.utils.Consts;

import java.util.ArrayList;


public class Adapter_animals extends RecyclerView.Adapter<Adapter_animals.MyViewHolder> {
    private Context mcontext;
    private ArrayList<AnimalsDTO> animalsDTOArrayList;
    private AdapterAnimalBinding binding;
    private LayoutInflater layoutInflater;


    public Adapter_animals(Context mcontext, ArrayList<AnimalsDTO> animalsDTOArrayList) {
        this.mcontext = mcontext;
        this.animalsDTOArrayList = animalsDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_animal, viewGroup, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        Glide
                .with(mcontext)
                .load(animalsDTOArrayList.get(position).getPet_image())
                .placeholder(R.drawable.app_logo)
                .into(myViewHolder.binding.image);
        myViewHolder.binding.name.setText(animalsDTOArrayList.get(position).getPet_name());

        myViewHolder.binding.llAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.click_event));
                Intent in = new Intent(mcontext, SubCategoryList.class);
                in.putExtra(Consts.P_PET_TYPE, animalsDTOArrayList.get(position).getId());
                in.putExtra(Consts.P_PET_TYPE_NAME, animalsDTOArrayList.get(position).getPet_name());
                mcontext.startActivity(in);

            }
        });
    }

    @Override
    public int getItemCount() {
        return animalsDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AdapterAnimalBinding binding;


        public MyViewHolder(@NonNull AdapterAnimalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
