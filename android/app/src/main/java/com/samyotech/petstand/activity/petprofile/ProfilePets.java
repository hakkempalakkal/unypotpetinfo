package com.samyotech.petstand.activity.petprofile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.editPet.EditPet;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.utils.ScreenshotUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilePets extends AppCompatActivity implements View.OnClickListener {
    private CustomTextView tvPetbreed, tvBirthdaydate, tvPetname, tvAgedate, tvActivity, tvWeight, tvWeightstatus, tvWeightdate, tvAdoptsince, tvHeight, tvPettype, tvAdoptiondate, tvWeightinfo, ctvActivity;
    private CustomTextViewBold ctvbPublic, ctvbSell;
    private DonutProgress progressbar;
    private LineChart mChart;
    private ImageView ivGender, ivPetprofile, ivShare;
    private SwitchButton switchPublic, switchSell, switchAdopt;
    private Context mContext;
    private LinearLayout back, llShare, llEditProfile;
    private PetListDTO petListDTO;
    private HashMap<String, String> paramsPublic = new HashMap<>();
    private HashMap<String, String> paramsSale = new HashMap<>();
    private HashMap<String, String> paramsAdopt = new HashMap<>();

    private ArrayList<PetListDTO.Chart> chartList;
    private CardView cardview, cardview_manglist, cvDelete;
    private HashMap<String, String> parmsPet = new HashMap<>();
    private HashMap<String, String> parmsSteps = new HashMap<>();
    private HashMap<String, String> parmsDelete = new HashMap<>();
    private Dialog dialog;
    private CustomEditText etSteps;
    private CustomTextView tvCancel, tvAdd, tvTarget;
    private String TAG = ProfilePets.class.getSimpleName();
    private RelativeLayout rlImage;
    HashMap<String, File> parmsFile = new HashMap<>();

    private CustomTextView tvYes, tvNo;
    private DialogInterface dialog_delete;
    private File screenShotFile;
    Bitmap b = null;
    private Dialog dialog_share;
    private LinearLayout llShareMedia, llShareWall, llCancel;
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    CustomTextViewBold tvFollowerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ProfilePets.this);
        setContentView(R.layout.activity_profile_pets);
        mContext = ProfilePets.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
    }

    public void init() {
        if (getIntent().hasExtra(Consts.PET_PROFILE)) {
            petListDTO = (PetListDTO) getIntent().getSerializableExtra(Consts.PET_PROFILE);
            paramsPublic.put(Consts.USER_ID, petListDTO.getUser_id());
            paramsPublic.put(Consts.PET_ID, petListDTO.getId());

            paramsSale.put(Consts.USER_ID, petListDTO.getUser_id());
            paramsSale.put(Consts.PET_ID, petListDTO.getId());

            paramsAdopt.put(Consts.USER_ID, petListDTO.getUser_id());
            paramsAdopt.put(Consts.PET_ID, petListDTO.getId());

            parmsPet.put(Consts.USER_ID, petListDTO.getUser_id());
            parmsPet.put(Consts.PET_ID, petListDTO.getId());

            parmsSteps.put(Consts.USER_ID, petListDTO.getUser_id());
            parmsSteps.put(Consts.PET_ID, petListDTO.getId());
            parmsSteps.put(Consts.UNIT, "steps");
            parmsSteps.put(Consts.TARGET, petListDTO.getBreed_target() + "");

        }
        rlImage = findViewById(R.id.rlImage);
        tvTarget = findViewById(R.id.tvTarget);
        cardview_manglist = findViewById(R.id.cardview_manglist);
        cardview = findViewById(R.id.cardview);
        cvDelete = findViewById(R.id.cvDelete);
        tvFollowerCount = findViewById(R.id.tvFollowerCount);
        back = findViewById(R.id.back);
        llEditProfile = findViewById(R.id.llEditProfile);
        llShare = findViewById(R.id.llShare);
        back.setOnClickListener(this);
        llEditProfile.setOnClickListener(this);
        llShare.setOnClickListener(this);
        cardview_manglist.setOnClickListener(this);
        cvDelete.setOnClickListener(this);
        tvFollowerCount.setOnClickListener(this);

        ctvActivity = findViewById(R.id.ctvActivity);
        ctvActivity.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate_left));
        ivPetprofile = findViewById(R.id.ivPetprofile);
        ivPetprofile.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate_left));

        ivGender = findViewById(R.id.ivGender);
        ivGender.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.fadein));
        tvPetbreed = findViewById(R.id.tvPetbreed);
        tvPetbreed.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate_left));

        tvBirthdaydate = findViewById(R.id.tvBirthdaydate);
        tvBirthdaydate.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate));

        tvPetname = findViewById(R.id.tvPetname);
        tvPetname.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate));

        tvAgedate = findViewById(R.id.tvAgedate);
        tvAgedate.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate_left));

        ctvbSell = findViewById(R.id.ctvbSell);
        ctvbSell.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate_left));

        ctvbPublic = findViewById(R.id.ctvbPublic);
        ctvbPublic.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate));

        tvActivity = findViewById(R.id.tvActivity);
        tvActivity.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate));

        tvWeight = findViewById(R.id.tvWeight);
        tvWeightstatus = findViewById(R.id.tvWeightstatus);
        tvWeightdate = findViewById(R.id.tvWeightdate);
        tvAdoptsince = findViewById(R.id.tvAdoptsince);
        tvHeight = findViewById(R.id.tvHeight);
        tvPettype = findViewById(R.id.tvPettype);
        tvAdoptiondate = findViewById(R.id.tvAdoptiondate);
        tvWeightinfo = findViewById(R.id.tvWeightinfo);

        progressbar = findViewById(R.id.progressbar);

        switchPublic = findViewById(R.id.switchPublic);
        switchAdopt = findViewById(R.id.switchAdopt);
        switchSell = findViewById(R.id.switchSell);
        mChart = findViewById(R.id.lineChart1);

        switchPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isShown()) {
                    if (b == true) {
                        paramsPublic.put(Consts.PUBLIC_PRIVATE, "0");

                        forPublic();
                    } else {
                        paramsPublic.put(Consts.PUBLIC_PRIVATE, "1");

                        forPublic();
                    }

                }


            }
        });

        switchAdopt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isShown()) {
                    if (b == true) {
                        paramsAdopt.put(Consts.ADOPT, "0");

                        forAdopt();
                    } else {
                        paramsAdopt.put(Consts.ADOPT, "1");

                        forAdopt();
                    }

                }


            }
        });


        switchSell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isShown()) {
                    if (b == true) {
                        paramsSale.put(Consts.SEL_NOTSEL, "0");

                        forSale();
                    } else {
                        paramsSale.put(Consts.SEL_NOTSEL, "1");

                        forSale();
                    }
                }


            }
        });


        showData();

    }

    protected LineData generateLineData(ArrayList<PetListDTO.Chart> charts) {


        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();


        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < charts.size(); i++) {

            entries.add(new Entry(i, charts.get(i).getManualactivity()));
        }

        LineDataSet ds1 = new LineDataSet(entries, "Weekly Moves Data");


        ds1.setLineWidth(2f);

        ds1.setDrawCircles(true);
        ds1.setDrawFilled(true);

        ds1.setColor(ColorTemplate.COLORFUL_COLORS[0]);

        sets.add(ds1);

        LineData d = new LineData(sets);
        return d;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cardview_manglist:
                dialogshow();
                break;
            case R.id.llShare:
                takeScreenshot();
                break;
            case R.id.tvFollowerCount:
                goFollowers();
                break;
            case R.id.cvDelete:
                petDelete();
                break;
            case R.id.llEditProfile:
                Intent in = new Intent(mContext, EditPet.class);
                in.putExtra(Consts.PET_PROFILE, petListDTO);
                startActivity(in);
                break;

        }
    }

    private void goFollowers() {
        Intent in = new Intent(mContext, PetFolowersActivity.class);
        in.putExtra(Consts.PET_ID, petListDTO.getId());
        startActivity(in);
    }

    public void showData() {
        tvPetbreed.setText(petListDTO.getBreed_name());
        tvPetname.setText(petListDTO.getPetName());
        tvFollowerCount.setText(petListDTO.getFollowerCount() + " Follower");
        tvBirthdaydate.setText(ProjectUtils.getFormattedDate(Long.parseLong(petListDTO.getBirth_date())));
        tvAgedate.setText(ProjectUtils.calculateAge(petListDTO.getBirth_date()));
        tvWeight.setText(petListDTO.getCurrent_weight() + " KGs");
        tvAdoptsince.setText(ProjectUtils.calculateAge(petListDTO.getAdoption_date()));
        tvHeight.setText(petListDTO.getCurrent_height() + " CMs");
        tvPettype.setText(petListDTO.getActive_area());
        tvAdoptiondate.setText(ProjectUtils.getFormattedDate(Long.parseLong(petListDTO.getAdoption_date())));
        tvWeightinfo.setText(petListDTO.getCurrent_weight() + " KGs");
        tvWeightdate.setText("last update " + ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(petListDTO.getUpdated_stamp()))));
        progressbar.setDonut_progress(petListDTO.getPercent());
        tvTarget.setText("Today's Target : " + petListDTO.getBreed_target());
        Glide
                .with(mContext)
                .load(petListDTO.getPet_img_path())
                .placeholder(R.drawable.dog_shape)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPetprofile);

        if (petListDTO.getSex().equalsIgnoreCase("Male")) {
            ivGender.setImageDrawable(mContext.getResources().getDrawable(R.drawable.male));

        } else {
            ivGender.setImageDrawable(mContext.getResources().getDrawable(R.drawable.female_a));
        }
        if (petListDTO.getSel_notsel().equalsIgnoreCase("0")) {
            switchSell.setChecked(true);
        } else {
            switchSell.setChecked(false);
        }
        if (petListDTO.getPublic_private().equalsIgnoreCase("0")) {
            switchPublic.setChecked(true);

        } else {
            switchPublic.setChecked(false);
        }

        if (petListDTO.getAdopt().equalsIgnoreCase("0")) {
            switchAdopt.setChecked(true);
        } else {
            switchAdopt.setChecked(false);
        }


        chartList = new ArrayList<>();
        chartList = petListDTO.getChart();


        if (chartList.size() > 0) {
            mChart.setData(generateLineData(chartList));

        }
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);

        mChart.animateX(3000);

        Typeface tf = Typeface.createFromAsset(ProfilePets.this.getAssets(), "Ubuntu-Medium.ttf");

        Legend l = mChart.getLegend();
        l.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setAxisMaximum(petListDTO.getBreed_target());
        leftAxis.setAxisMinimum(0);

        XAxis rightAxis = mChart.getXAxis();
        rightAxis.setTypeface(tf);
        rightAxis.setAxisMaximum(6);
        rightAxis.setAxisMinimum(0);

        mChart.getAxisRight().setEnabled(false);
        mChart.setDrawGridBackground(false);
        rightAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    public void forPublic() {
        new HttpsRequest(Consts.UPDATE__PRIVATE, paramsPublic, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void forSale() {
        new HttpsRequest(Consts.UPDATE_SALE_NOTSALE, paramsSale, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void forAdopt() {
        new HttpsRequest(Consts.UPDATE_ADOPT, paramsAdopt, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }


    public void getPet() {

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.GET_SINGLE_PET, parmsPet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        petListDTO = new Gson().fromJson(response.getJSONObject("data").toString(), PetListDTO.class);

                        showData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }


    public void setManual() {
        parmsSteps.put(Consts.TIME_STAMP, System.currentTimeMillis() + "");
        parmsSteps.put(Consts.MANUAL_ACTIVITY, ProjectUtils.getEditTextValue(etSteps));

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.SET_MANUAL_ACTIVITY, parmsSteps, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    dialog.dismiss();
                    try {
                        // petListDTO = new Gson().fromJson(response.getJSONObject("data").toString(), PetListDTO.class);
                        getPet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPet();
    }

    public void dialogshow() {
        dialog = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_add_manual);

        ///dialog.getWindow().setBackgroundDrawableResource(R.color.black);
        etSteps = (CustomEditText) dialog.findViewById(R.id.etSteps);
        tvCancel = (CustomTextView) dialog.findViewById(R.id.tvCancel);
        tvAdd = (CustomTextView) dialog.findViewById(R.id.tvAdd);

        dialog.show();
        dialog.setCancelable(false);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(mContext)) {
                    submitForm();
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
                }
            }
        });

    }

    public void dialogshare_social() {
        dialog = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_share_option);

        ///dialog.getWindow().setBackgroundDrawableResource(R.color.black);
        llShareMedia = (LinearLayout) dialog.findViewById(R.id.llShareMedia);
        llShareWall = (LinearLayout) dialog.findViewById(R.id.llShareWall);
        llCancel = (LinearLayout) dialog.findViewById(R.id.llCancel);

        dialog.show();
        dialog.setCancelable(false);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screenShotFile != null) {
                    screenShotFile.delete();
                }
                dialog.dismiss();
            }
        });
        llShareWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogshare(b);
                dialog.dismiss();
            }
        });
        llShareMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMedia(b);
                dialog.dismiss();
            }
        });

    }

    public void submitForm() {
        if (!validateSteps()) {
            return;
        } else {
            setManual();
        }
    }

    public boolean validateSteps() {
        if (etSteps.getText().toString().trim().equalsIgnoreCase("")) {
            etSteps.setError("Please enter moves");
            etSteps.requestFocus();
            return false;
        } else {
            if (!(Integer.parseInt(ProjectUtils.getEditTextValue(etSteps)) <= petListDTO.getBreed_target())) {
                etSteps.setError("Moves can't be greater than daily target.");
                etSteps.requestFocus();
                return false;
            } else {
                etSteps.setError(null);
                etSteps.clearFocus();
                return true;
            }
        }
    }


    private void takeScreenshot() {


        b = ScreenshotUtils.screenShot(rlImage);

        if (b != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File saveFile = ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            screenShotFile = ScreenshotUtils.store(b, Consts.PET_CARE + timeStamp + ".jpg", saveFile);//save the screenshot to selected path
            parmsFile.put(Consts.MEDIA, screenShotFile);
            dialogshare_social();
        } else {

        }

    }

    public void shareMedia(Bitmap mBitmap) {
        try {
            Bitmap b = mBitmap;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                    b, "Title", null);
            Uri imageUri = Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(share, "Select"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void addPost() {
        ProjectUtils.showProgressDialog(mContext, false, "Please wait..");

        new HttpsRequest(Consts.ADD_POST_API, getParms(), parmsFile, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    dialog_share.dismiss();
                    if (screenShotFile != null) {
                        screenShotFile.delete();
                    }
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }

        });
    }

    public Map<String, String> getParms() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.CONTENT, "wall");
        params.put(Consts.COMUNITY_ID, loginDTO.getComunity_id() + "");
        params.put(Consts.TITLE, "wall");
        params.put(Consts.POSTTYPE, "image");
        return params;
    }

    public void dialogshare(Bitmap b) {
        dialog_share = new Dialog(mContext, android.R.style.Theme_Dialog);
        dialog_share.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_share.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_share.setContentView(R.layout.dailog_share);


        tvYes = (CustomTextView) dialog_share.findViewById(R.id.tvYes);
        tvNo = (CustomTextView) dialog_share.findViewById(R.id.tvNo);
        ivShare = (ImageView) dialog_share.findViewById(R.id.ivShare);
        dialog_share.show();
        dialog_share.setCancelable(false);
        ivShare.setImageBitmap(b);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share.dismiss();

                if (screenShotFile != null) {
                    screenShotFile.delete();
                }
            }
        });
        tvYes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPost();


                    }
                });

    }

    public void petDelete() {
        ProjectUtils.showAlertDialogWithCancel(mContext, "Delete", "Are you sure want to delete " + petListDTO.getPetName() + " ?", "Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog_delete = dialog;
                deletePet();
            }
        }, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    public void deletePet() {
        parmsDelete.put(Consts.USER_ID, petListDTO.getUser_id());
        parmsDelete.put(Consts.PET_ID, petListDTO.getId());

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.DELETE_PET_API, parmsDelete, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    dialog_delete.dismiss();
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
