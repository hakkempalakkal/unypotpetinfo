package com.samyotech.petstand.activity.editPet;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.samyotech.petstand.R;
import com.samyotech.petstand.SysApplication;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;

/**
 * Created by mayank on 8/2/18.
 */

public class LifeFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    SysApplication sysApplication;
    CustomEditText medicinesET;
    Spinner trainedSpinner, petTypeSpinner, lifeStyleSpinner;
    ArrayAdapter<String> trainedAdapter;
    ArrayAdapter<String> lifeStyleAdapter;
    ArrayAdapter<String> petTypesAdapter;
    private EditPet editPet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_lifestyle, null, false);
        sysApplication = SysApplication.getInstance();
        initView();
        return rootView;
    }

    private void initView() {
        medicinesET = (CustomEditText) rootView.findViewById(R.id.medicinesET);
        trainedSpinner = (Spinner) rootView.findViewById(R.id.trainedSpinner);
        petTypeSpinner = (Spinner) rootView.findViewById(R.id.petTypeSpinner);
        lifeStyleSpinner = (Spinner) rootView.findViewById(R.id.lifeStyleSpinner);

        addTextChangeListeners();

        trainedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetTrainedTypesList());
        trainedAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        trainedSpinner.setAdapter(trainedAdapter);

        lifeStyleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetLifeStyleList());
        lifeStyleAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        lifeStyleSpinner.setAdapter(lifeStyleAdapter);

        petTypesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetTypeList());
        petTypesAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        petTypeSpinner.setAdapter(petTypesAdapter);

        showData();
    }

    @Override
    public void onClick(View view) {
    }


    public void addTextChangeListeners() {
        medicinesET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editPet.values.put(Consts.MEDICINES, s.toString());
            }
        });
    }

    public void showData() {
        medicinesET.setText(editPet.petListDTO.getMedicines());
        petTypeSpinner.setSelection(ProjectUtils.getItemPos(ProjectUtils.getPetTypeList(), editPet.petListDTO.getActive_area()));
        trainedSpinner.setSelection(ProjectUtils.getItemPos(ProjectUtils.getPetTrainedTypesList(), editPet.petListDTO.getTrained()));
        lifeStyleSpinner.setSelection(ProjectUtils.getItemPos(ProjectUtils.getPetLifeStyleList(), editPet.petListDTO.getLifestyle()));

        editPet.values.put(Consts.LIFESTYL, lifeStyleSpinner.getSelectedItem().toString());
        editPet.values.put(Consts.ACTIVE_AREA, petTypeSpinner.getSelectedItem().toString());
        editPet.values.put(Consts.TRAIND,trainedSpinner.getSelectedItem().toString());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        editPet = (EditPet) activity;

    }

}
