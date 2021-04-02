package com.samyotech.petstand.activity.addpet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.https.UploadFileToServer;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class AddPetSlides extends AppCompatActivity {

    private ViewPager viewPager;
    private PageAdapter mAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private SharedPrefrence share;
    private Context mContext;

    public AddBreedNew addBreednew = new AddBreedNew();
    private AddDOB addDOB = new AddDOB();
    public AddGender addGender = new AddGender();
    private AddImage addImage = new AddImage();
    private AddWeight addWeight = new AddWeight();

    private AdditionalInfo additionalInfo = new AdditionalInfo();
    private String TAG = AddPetSlides.class.getSimpleName();
    private LoginDTO loginDTO;


    private UploadFileToServer uploadFileToServer;
    public String type = "";
    public CustomTextView tvHeader;
    private LinearLayout llBack;
    private StepperIndicator stepper_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(AddPetSlides.this);
        setContentView(R.layout.add_pet_viewer);
        mContext = AddPetSlides.this;
        share = SharedPrefrence.getInstance(this);
        loginDTO = share.getParentUser(Consts.LOGINDTO);

        tvHeader = (CustomTextView) findViewById(R.id.tvHeader);
        stepper_indicator = findViewById(R.id.stepper_indicator);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        llBack = (LinearLayout) findViewById(R.id.llBack);
        layouts = new int[]{
                R.layout.add_pet_picture,
                R.layout.add_pet_picture,
                R.layout.add_pet_picture,
                R.layout.add_pet_picture,
                R.layout.add_pet_picture,
                R.layout.add_pet_picture};
        addBottomDots(0);
        mAdapter = new PageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(6);


        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.beginFakeDrag();
        stepper_indicator.setViewPager(viewPager, true);

        stepper_indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                //viewPager.setCurrentItem(step, true);
            }
        });

        btnSkip.setVisibility(View.GONE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    if (current == 1) {

                        if (addImage.petList.get(addImage.TypeSpinner.getSelectedItemPosition()).getPet_name().equalsIgnoreCase("-- SELECT PET TYPE --")) {
                            ProjectUtils.showLong(mContext, "Please select pet type.");
                        } else if (!ProjectUtils.isEditTextFilled(addImage.petName)) {
                            ProjectUtils.showLong(AddPetSlides.this, "Please enter Pet name.");
                        } else if (addImage.file == null) {
                            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Image.");
                        } else {
                            type = addImage.petList.get(addImage.TypeSpinner.getSelectedItemPosition()).getId();
                            viewPager.setCurrentItem(current);
                        }
                    } else if (current == 2) {
                        if (addBreednew.breedId.equalsIgnoreCase("")) {
                            ProjectUtils.showLong(mContext, "Please select breed type.");
                        } else {
                            viewPager.setCurrentItem(current);
                        }
                    } else if (current == 3) {
                        if (addDOB.dob_timeStamp == 0) {
                            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Date of birth.");
                        } else if (addDOB.adop_timeStamp == 0) {
                            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Adoption date.");
                        } else if (addDOB.dob_timeStamp > System.currentTimeMillis()) {
                            ProjectUtils.showLong(AddPetSlides.this, "Please check date of birth.");
                        } else if (addDOB.adop_timeStamp > System.currentTimeMillis()) {
                            ProjectUtils.showLong(AddPetSlides.this, "Please check adoption date.");
                        } else if (addDOB.dob_timeStamp > addDOB.adop_timeStamp) {
                            ProjectUtils.showLong(AddPetSlides.this, "Adoption date should be greater then date of birth.");
                        } else {
                            viewPager.setCurrentItem(current);
                        }
                    } else if (current == 4) {
                        if (addGender.gender == null) {
                            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Gender.");
                        } else {
                            viewPager.setCurrentItem(current);
                        }
                    } else {
                        viewPager.setCurrentItem(current);

                    }
                } else {
                    clickForSubmit();
                }
                if (btnNext.getText().toString().equals("SUBMIT")) {
                    clickForSubmit();
                }
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItemPre(1);
                viewPager.setCurrentItem(current);
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    clickDone();

                }
            }
        });

    }

    public void clickForSubmit() {
        if (!ProjectUtils.isEditTextFilled(addImage.petName)) {
            ProjectUtils.showLong(AddPetSlides.this, "Please enter Pet name.");
        } else if (addImage.file == null) {
            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Image.");
//        } else if (addBreednew.breedId == null) {
//            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Breed.");
        } else if (addDOB.dob_timeStamp == 0) {
            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Date of birth.");
        } else if (addDOB.adop_timeStamp == 0) {
            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Adoption date.");
        } else if (addGender.gender == null) {
            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Gender.");
        } else if (addWeight.ctvWeight.equals("0")) {
            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Weight.");
//        } else if (!ProjectUtils.isEditTextFilled(additionalInfo.microId)) {
//            ProjectUtils.showLong(AddPetSlides.this, "Please enter Pet MicroID.");
        } else if (addWeight.ctvHeight.equals("0")) {
            ProjectUtils.showLong(AddPetSlides.this, "Please select Pet Height.");
//        } else if (!ProjectUtils.isEditTextFilled(additionalInfo.microId)) {
//            ProjectUtils.showLong(AddPetSlides.this, "Please enter Pet MicroID.");
        } else if (addDOB.dob_timeStamp > System.currentTimeMillis()) {
            ProjectUtils.showLong(AddPetSlides.this, "Please check date of birth.");
        } else if (addDOB.adop_timeStamp > System.currentTimeMillis()) {
            ProjectUtils.showLong(AddPetSlides.this, "Please check adoption date.");
        } else if (addDOB.dob_timeStamp > addDOB.adop_timeStamp) {
            ProjectUtils.showLong(AddPetSlides.this, "Adoption date should be bigger then date of birth.");
        } else {
            AddPet();
            //ProjectUtils.showLong(AddPetSlides.this, "call API.");
        }


    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private int getItemPre(int i) {
        return viewPager.getCurrentItem() - i;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
            }
            if (position == 0) {
                llBack.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.GONE);

            } else {
                llBack.setVisibility(View.GONE);
                btnSkip.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    public class PageAdapter extends FragmentStatePagerAdapter {
        // private Fragment fragment;
        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    tvHeader.setText("Pet Picture");
                    return addImage;
                case 1:
                    tvHeader.setText("Breed");
                    return addBreednew;
                case 2:
                    tvHeader.setText("Important Dates");
                    return addDOB;
                case 3:
                    tvHeader.setText("Gender");
                    return addGender;
                case 4:
                    tvHeader.setText("Pet Info");
                    return addWeight;
                case 5:
                    tvHeader.setText("Additional Information");
                    return additionalInfo;
                default:
                    tvHeader.setText("Pet Picture");
                    return addImage;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }

    }

    public void AddPet() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ADDPET, getparm(), getparmFile(), mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());

        parms.put(Consts.PET_NAME, addImage.petName.getText().toString());
        parms.put(Consts.BREEDID, addBreednew.breedId);
        parms.put(Consts.BIRTH_DATE, addDOB.dob_timeStamp + "");
        parms.put(Consts.ADOPTION_DATE, addDOB.adop_timeStamp + "");
        parms.put(Consts.SEX, addGender.gender);
        parms.put(Consts.NEUTERED, addGender.isNeutred);
        parms.put(Consts.CURRENT_WEIGHT, addWeight.ctvWeight.getText().toString());
        parms.put(Consts.CURRENT_HEIGHT, addWeight.ctvHeight.getText().toString());
        parms.put(Consts.PET_TYPE, type);
        parms.put(Consts.LIFESTYL, additionalInfo.lifeStyleSpinner.getSelectedItem().toString());
        parms.put(Consts.ACTIVE_AREA, additionalInfo.petTypeSpinner.getSelectedItem().toString());
        parms.put(Consts.TRAIND, additionalInfo.trainedSpinner.getSelectedItem().toString());

        parms.put(Consts.MEDICINES, additionalInfo.cetMedicines.getText().toString());

        Log.e("parms", parms.toString());
        return parms;
    }

    public HashMap<String, File> getparmFile() {
        HashMap<String, File> parms = new HashMap<>();
        parms.put(Consts.PET_IMG_PATH, addImage.file);

        return parms;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            clickDone();

        }
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.app_name)
                .setMessage("Opps if seems that you don't want to add pet now?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Do it later!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();


                    }
                })
                .show();
    }

}