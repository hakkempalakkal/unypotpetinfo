package com.samyotech.petstand.activity.home;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.databinding.ActivityBridInfoBinding;
import com.samyotech.petstand.models.HomeDTO;
import com.samyotech.petstand.utils.Consts;

public class BreedInfoHome extends AppCompatActivity implements View.OnClickListener {

    ActivityBridInfoBinding binding;
    Context mContext;
    HomeDTO.Breed breed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_brid_info);
        mContext = BreedInfoHome.this;

        breed = (HomeDTO.Breed) getIntent().getSerializableExtra(Consts.HOME_BREED);
        binding.back.setOnClickListener(this);
        setData();

    }

    private void setData() {

        Glide.with(mContext)
                .load(breed.getImage())
                .placeholder(R.drawable.app_logo)
                .into(binding.ivBreed);

        binding.tvHeadingText.setText(breed.getBreed_name());
        binding.tvDiscription.setText(breed.getB_description());
        binding.tvTitel.setText(breed.getBreed_name());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }

    }
}
