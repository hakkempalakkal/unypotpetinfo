package com.samyotech.petstand.activity.addpet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import com.samyotech.petstand.R;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddDOB extends Fragment implements View.OnClickListener {
    public View rootView;
    CustomTextView dob, adoption;
    public long dob_timeStamp = 0;
    public long adop_timeStamp = 0;
    public AddPetSlides addPetSlides;
    private Calendar myCalendar = Calendar.getInstance();
    private Calendar myCalendarAdop = Calendar.getInstance();
    public Calendar refCalender = Calendar.getInstance();
     DatePickerDialog datePickerDialog, datePickerDialogADp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_dob, null, false);
        dob = (CustomTextView) rootView.findViewById(R.id.dob);
        adoption = (CustomTextView) rootView.findViewById(R.id.adoption);
        dob.setOnClickListener(this);
        adoption.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.click_event));
        switch (v.getId()) {
            case R.id.dob: {
                openDatePickerDOB();
                break;
            }
            case R.id.adoption: {
                openDatePickerAdop();
                break;
            }

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addPetSlides = (AddPetSlides) activity;
    }

    public void openDatePickerDOB() {

       /* datePickerDialog = new DatePickerDialog(getActivity(), dateDOB, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/

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
                    dob.setText(sdf.format(myCalendar.getTime()));
                    dob_timeStamp= myCalendar.getTimeInMillis();
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
                    adoption.setText(sdf.format(myCalendarAdop.getTime()));
                    adop_timeStamp= myCalendarAdop.getTimeInMillis();
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