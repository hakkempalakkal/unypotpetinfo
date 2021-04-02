package com.samyotech.petstand.activity.addpet;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.samyotech.petstand.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddGender extends Fragment implements View.OnClickListener {

    public View rootView;
    CircleImageView male, female;
    ImageView ivYes, ivNo;
    public String gender = null, isNeutred = "yes";
    public AddPetSlides addPetSlides;

    private ImageView dogIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_gender, null, false);
        male = (CircleImageView) rootView.findViewById(R.id.male);
        female = (CircleImageView) rootView.findViewById(R.id.female);
        ivYes = (ImageView) rootView.findViewById(R.id.ivYes);
        ivNo = (ImageView) rootView.findViewById(R.id.ivNo);
        dogIV = (ImageView) rootView.findViewById(R.id.dogIV);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        ivYes.setOnClickListener(this);
        ivNo.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.click_event));
        switch (v.getId()) {
            case R.id.male: {
                gender = "Male";
                male.setImageDrawable(getResources().getDrawable(R.drawable.male));
                female.setImageDrawable(getResources().getDrawable(R.drawable.female_d));
                break;
            }
            case R.id.female: {
                gender = "Female";
                female.setImageDrawable(getResources().getDrawable(R.drawable.female_a));
                male.setImageDrawable(getResources().getDrawable(R.drawable.male_d));
                break;
            }
            case R.id.ivYes: {
                isNeutred = "1";
                ivYes.setImageDrawable(getResources().getDrawable(R.drawable.yes_a));
                ivNo.setImageDrawable(getResources().getDrawable(R.drawable.no_d));
                dogIV.setImageDrawable(getResources().getDrawable(R.drawable.dog_happy));
                break;
            }
            case R.id.ivNo: {
                isNeutred = "0";
                ivNo.setImageDrawable(getResources().getDrawable(R.drawable.no_a));
                ivYes.setImageDrawable(getResources().getDrawable(R.drawable.yes_d));
                dogIV.setImageDrawable(getResources().getDrawable(R.drawable.sad_dog));
                break;
            }

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addPetSlides = (AddPetSlides) activity;
    }
}