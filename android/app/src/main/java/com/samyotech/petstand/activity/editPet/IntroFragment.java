package com.samyotech.petstand.activity.editPet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.DialogUtility;
import com.samyotech.petstand.utils.ProjectUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mayank on 8/2/18.
 */

public class IntroFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    CustomEditText petNameET, weightET, heightET, dobAdoptionDateET, dobET;
    CustomTextView tv_dogs_temperament, tv_children_temperament, tv_cats_temperament, tv_people_temperament;
    ImageView iv_weight_plus, iv_height_plus;
    LinearLayout ll_height_minus, ll_weight_minus;
    MaterialBetterSpinner weightSpinner, heightSpinner;
    private EditPet editPet;

    public long dob_timeStamp = 0;
    public long adop_timeStamp = 0;
    private Calendar myCalendar = Calendar.getInstance();
    private Calendar myCalendarAdop = Calendar.getInstance();
    private Calendar refCalender = Calendar.getInstance();
    private DatePickerDialog datePickerDialog, datePickerDialogADp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_introduction, null, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        return rootView;
    }

    protected void initView() {
        petNameET = (CustomEditText) rootView.findViewById(R.id.petNameET);
        weightET = (CustomEditText) rootView.findViewById(R.id.weightET);
        heightET = (CustomEditText) rootView.findViewById(R.id.heightET);
        tv_dogs_temperament = (CustomTextView) rootView.findViewById(R.id.tv_dogs_temperament);
        tv_children_temperament = (CustomTextView) rootView.findViewById(R.id.tv_children_temperament);
        tv_cats_temperament = (CustomTextView) rootView.findViewById(R.id.tv_cats_temperament);
        tv_people_temperament = (CustomTextView) rootView.findViewById(R.id.tv_people_temperament);
        iv_weight_plus = (ImageView) rootView.findViewById(R.id.iv_weight_plus);
        iv_height_plus = (ImageView) rootView.findViewById(R.id.iv_height_plus);
        ll_height_minus = (LinearLayout) rootView.findViewById(R.id.ll_height_minus);
        weightSpinner = rootView.findViewById(R.id.weightSpinner);
        dobET = (CustomEditText) rootView.findViewById(R.id.dobET);
        dobAdoptionDateET = (CustomEditText) rootView.findViewById(R.id.dobAdoptionDateET);
        ll_weight_minus = (LinearLayout) rootView.findViewById(R.id.ll_weight_minus);
        heightSpinner = rootView.findViewById(R.id.heightSpinner);

        addTextChangeListeners();

        tv_dogs_temperament.setOnClickListener(this);
        tv_children_temperament.setOnClickListener(this);
        tv_cats_temperament.setOnClickListener(this);
        tv_people_temperament.setOnClickListener(this);
        iv_weight_plus.setOnClickListener(this);
        iv_height_plus.setOnClickListener(this);
        ll_height_minus.setOnClickListener(this);
        dobET.setOnClickListener(this);
        dobAdoptionDateET.setOnClickListener(this);
        ll_weight_minus.setOnClickListener(this);

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetWeightList());
        weightAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        weightSpinner.setAdapter(weightAdapter);



        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("KG")) {
                } else if (item.equalsIgnoreCase("LBS")) {
                } else {
//                  for stone   1 Stone = 6.35029 KGs]
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> heightAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ProjectUtils.getPetHeightList());
        heightAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        heightSpinner.setAdapter(heightAdapter);

        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("CM")) {
                } else {
//                  for FEET
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        showData();
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.click_event));
        switch (view.getId()) {
            case R.id.dobET: {
                openDatePickerDOB();
                break;
            }
            case R.id.dobAdoptionDateET: {
                openDatePickerAdop();
                break;
            }
            case R.id.ll_weight_minus: {
                if (DialogUtility.isEditTextFilled(weightET)) {
                    int weight = Integer.parseInt(weightET.getText().toString());
                    if (weight >= 1) {
                        weight = weight - 1;
                        weightET.setText("" + weight);
                    }
                }
                break;
            }
            case R.id.ll_height_minus: {
                if (DialogUtility.isEditTextFilled(heightET)) {
                    int height = Integer.parseInt(heightET.getText().toString());
                    if (height >= 1) {
                        height = height - 1;
                        heightET.setText("" + height);
                    }
                }
                break;
            }
            case R.id.iv_weight_plus: {
                if (DialogUtility.isEditTextFilled(weightET)) {
                    int weight = Integer.parseInt(weightET.getText().toString());
                    weight = weight + 1;
                    weightET.setText("" + weight);
                }
                break;
            }
            case R.id.iv_height_plus: {
                if (DialogUtility.isEditTextFilled(heightET)) {
                    int height = Integer.parseInt(heightET.getText().toString());
                    height = height + 1;
                    heightET.setText("" + height);
                }
                break;
            }

        }

    }


    public void addTextChangeListeners() {
        weightET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editPet.values.put(Consts.CURRENT_WEIGHT, s.toString());
            }
        });
        heightET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editPet.values.put(Consts.CURRENT_HEIGHT, s.toString());
            }
        });
        petNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editPet.values.put(Consts.PET_NAME, s.toString());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        editPet = (EditPet) activity;

    }

    public void showData() {
        petNameET.setText(editPet.petListDTO.getPetName());
        weightET.setText(editPet.petListDTO.getCurrent_weight());
        heightET.setText(editPet.petListDTO.getCurrent_height());
        dobET.setText(ProjectUtils.getFormattedDate(Long.parseLong(editPet.petListDTO.getBirth_date())));
        dobAdoptionDateET.setText(ProjectUtils.getFormattedDate(Long.parseLong(editPet.petListDTO.getAdoption_date())));

        dob_timeStamp = Long.parseLong(editPet.petListDTO.getBirth_date());
        adop_timeStamp = Long.parseLong(editPet.petListDTO.getAdoption_date());

        try {
            weightSpinner.setText(ProjectUtils.getPetWeightList().get(0));
        } catch (Exception e) {

        }

        try {
            heightSpinner.setText(ProjectUtils.getPetHeightList().get(0));
        } catch (Exception e) {

        }

    }

    public void openDatePickerDOB() {

        int year = myCalendar.get(Calendar.YEAR);
        int monthOfYear = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendar.set(Calendar.YEAR, y);
                myCalendar.set(Calendar.MONTH, m);
                myCalendar.set(Calendar.DAY_OF_MONTH, d);
                if (myCalendar.getTimeInMillis() <= refCalender.getTimeInMillis()) {
                    String myFormat = "dd-MMM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    dobET.setText(sdf.format(myCalendar.getTime()));
                    dob_timeStamp = myCalendar.getTimeInMillis();
                    editPet.values.put(Consts.BIRTH_DATE, dob_timeStamp + "");

                } else {
                    ProjectUtils.showLong(getActivity(), "Cannot select future date");
                }

            }


        }, year, monthOfYear, dayOfMonth);
        datePickerDialog.setTitle("Select Date");

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void openDatePickerAdop() {

        int year = myCalendar.get(Calendar.YEAR);
        int monthOfYear = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialogADp = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendarAdop.set(Calendar.YEAR, y);
                myCalendarAdop.set(Calendar.MONTH, m);
                myCalendarAdop.set(Calendar.DAY_OF_MONTH, d);
                if (myCalendarAdop.getTimeInMillis() <= refCalender.getTimeInMillis()) {
                    String myFormat = "dd-MMM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    dobAdoptionDateET.setText(sdf.format(myCalendarAdop.getTime()));
                    adop_timeStamp = myCalendarAdop.getTimeInMillis();
                    editPet.values.put(Consts.ADOPTION_DATE, adop_timeStamp + "");
                } else {
                    ProjectUtils.showLong(getActivity(), "Cannot select future date");
                }

            }


        }, year, monthOfYear, dayOfMonth);
        datePickerDialogADp.setTitle("Select Date");

        datePickerDialogADp.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialogADp.show();
    }
}
