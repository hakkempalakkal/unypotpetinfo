package com.samyotech.petstand.activity.addpet;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.samyotech.petstand.R;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;


public class AdditionalInfo extends Fragment implements View.OnClickListener {
    public AddPetSlides addPetSlides;
    public View rootView;
    public Spinner lifeStyleSpinner, trainedSpinner;
    public Spinner petTypeSpinner;
    CustomEditText cetMedicines;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.additionalinfo, null, false);
        petTypeSpinner = (Spinner) rootView.findViewById(R.id.petTypeSpinner);
        lifeStyleSpinner = (Spinner) rootView.findViewById(R.id.lifeStyleSpinner);
        trainedSpinner = (Spinner) rootView.findViewById(R.id.trainedSpinner);
        cetMedicines = (CustomEditText) rootView.findViewById(R.id.cetMedicines);

        ArrayAdapter<String> trainedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetTrainedTypesList());
        trainedAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        trainedSpinner.setAdapter(trainedAdapter);
        trainedSpinner.setSelection(0);

        ArrayAdapter<String> lifeStyleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetLifeStyleList());
        lifeStyleAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        lifeStyleSpinner.setAdapter(lifeStyleAdapter);
        lifeStyleSpinner.setSelection(0);

        ArrayAdapter<String> petTypesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetTypeList());
        petTypesAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        petTypeSpinner.setAdapter(petTypesAdapter);
        petTypeSpinner.setSelection(0);
        cetMedicines.getText().toString();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.petType: {
                break;
            }
            case R.id.lifeStyle: {
                break;
            }
            case R.id.trained: {
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