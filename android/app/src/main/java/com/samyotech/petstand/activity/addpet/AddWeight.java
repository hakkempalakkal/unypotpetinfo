package com.samyotech.petstand.activity.addpet;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;
import com.samyotech.petstand.R;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

public class AddWeight extends Fragment implements View.OnClickListener {
    public View rootView;
    public CustomTextView ctvWeight, ctvHeight, ctvTxtH, ctvTxtW;
    //  private CircleImageView civWeight, civHeight;
    public String num, height;
    public RulerValuePicker heightPicker, weightPicker;
    public AddPetSlides addPetSlides;
    String name = "";
    String heightPet = "";
    String weightPet = "";
    String txt = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_weight_new, null, false);


        ctvWeight = (CustomTextView) rootView.findViewById(R.id.ctvWeight);
        ctvHeight = (CustomTextView) rootView.findViewById(R.id.ctvHeight);
        ctvTxtH = (CustomTextView) rootView.findViewById(R.id.ctvTxtH);
        ctvTxtW = (CustomTextView) rootView.findViewById(R.id.ctvTxtW);


//        civWeight = (CircleImageView) rootView.findViewById(R.id.civWeight);
//        civHeight = (CircleImageView) rootView.findViewById(R.id.civHeight);
        heightPicker = rootView.findViewById(R.id.height_ruler_picker);
        weightPicker = rootView.findViewById(R.id.weight_ruler_picker);
//        civWeight.setOnClickListener(this);
//        civHeight.setOnClickListener(this);

        //heightPicker.selectValue(10);

        heightPicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                ctvHeight.setText(selectedValue + "");
                if (selectedValue > 80){
                    heightPicker.setIndicatorColor(getResources().getColor(R.color.red));
                }else {
                    heightPicker.setIndicatorColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                ctvHeight.setText(selectedValue + "");
                if (selectedValue > 80){
                    heightPicker.setIndicatorColor(getResources().getColor(R.color.red));
                }else {
                    heightPicker.setIndicatorColor(getResources().getColor(R.color.gray));
                }
            }
        });

        //Set the weight picker


       // weightPicker.selectValue(25);
        weightPicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                ctvWeight.setText(selectedValue + "");
                if (selectedValue > 80){
                    weightPicker.setIndicatorColor(getResources().getColor(R.color.red));
                }else {
                    weightPicker.setIndicatorColor(getResources().getColor(R.color.gray));
                }

            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                ctvWeight.setText(selectedValue + "");
                if (selectedValue > 80){
                    weightPicker.setIndicatorColor(getResources().getColor(R.color.red));
                }else {
                    weightPicker.setIndicatorColor(getResources().getColor(R.color.gray));
                }

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
      /*  v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.click_event));
        if (v.equals(civWeight)) {
            final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getActivity())
                    .minValue(1)
                    .maxValue(50)
                    .defaultValue(10)
                    .separatorColor(Color.WHITE)
                    .textColor(Color.CYAN)
                    .textSize(25)
                    .build();

            new AlertDialog.Builder(getActivity())
                    .setView(numberPicker)
                    .setNegativeButton(getString(android.R.string.cancel), null)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            numberPicker.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_dialog));
                            num = String.valueOf(numberPicker.getValue());
                            ctvWeight.setText(num);
                        }
                    })
                    .show();
        } else if (v.equals(civHeight)) {
            final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getActivity())
                    .minValue(20)
                    .maxValue(220)
                    .defaultValue(52)
                    .separatorColor(Color.WHITE)
                    .textColor(Color.CYAN)
                    .textSize(25)
                    .build();

            new AlertDialog.Builder(getActivity())
                    .setView(numberPicker)
                    .setNegativeButton(getString(android.R.string.cancel), null)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            numberPicker.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_dialog));
                            height = String.valueOf(numberPicker.getValue());
                            ctvHeight.setText(height);
                        }
                    })
                    .show();
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addPetSlides = (AddPetSlides) activity;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (addPetSlides.addBreednew.breed_dto != null) {
                name = addPetSlides.addBreednew.breed_dto.getBreed_name();

                if (addPetSlides.addGender.gender.equalsIgnoreCase("Male")){
                    weightPet = addPetSlides.addBreednew.breed_dto.getWeight_male();
                    heightPet = addPetSlides.addBreednew.breed_dto.getHeight_male();
                    int h = 0;
                    int w = 0;
                    h = ProjectUtils.getIntValue(heightPet);
                    w = ProjectUtils.getIntValue(weightPet);

                    heightPicker.selectValue(h);
                    weightPicker.selectValue(w);
                    txt = "for male.";

                }else {
                    weightPet = addPetSlides.addBreednew.breed_dto.getWeight_female();
                    heightPet = addPetSlides.addBreednew.breed_dto.getHeight_female();
                    txt = "for female.";
                    int h = 0;
                    int w = 0;
                    h = ProjectUtils.getIntValue(heightPet);
                    w = ProjectUtils.getIntValue(weightPet);

                    heightPicker.selectValue(h);
                    weightPicker.selectValue(w);
                }


                ctvTxtH.setText(name+" "+"(Standard)'s normal height range is : "+heightPet+" "+txt);
                ctvTxtW.setText(name+" "+"(Standard)'s normal weight range is : "+weightPet+" "+txt);

            }

        }
    }
}