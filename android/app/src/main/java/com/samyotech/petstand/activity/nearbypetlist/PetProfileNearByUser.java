package com.samyotech.petstand.activity.nearbypetlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.chat.OneTwoOneChatDemo;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetProfileNearByUser extends AppCompatActivity implements View.OnClickListener {

    private CustomTextView tvPetbreed, tvTarget, ctvbSell, ctvbAdopt, tvBirthdaydate, tvPetname, tvAgedate, tvActivity, tvWeight, tvWeightstatus, tvWeightdate, tvAdoptsince, tvHeight, tvPettype, tvAdoptiondate, tvWeightinfo, ctvActivity;
    private DonutProgress progressbar;
    private LineChart mChart;
    private ImageView ivGender,ivVerify, ivPetprofile;
    private Context mContext;
    private LinearLayout back;
    private PetListDTO petListDTO;
    private RelativeLayout rlContact, rlAdopt;
    private ArrayList<PetListDTO.Chart> chartList;
    private CardView cardview;
    public HashMap<String, String> paramsPet = new HashMap<>();
    public HashMap<String, String> paramsRating = new HashMap<>();
    public HashMap<String, String> paramsFollow = new HashMap<>();
    private String TAG = PetProfileNearByUser.class.getSimpleName();
    private CustomTextViewBold tvSubmit;
    private RatingBar ratingBar, RBpet;
    private float myrating;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    CustomTextViewBold tvFollowerCount;
    CustomTextView tvFollow;
    RelativeLayout rlFolow;
    CardView cardviewadopt, cardviewfd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(PetProfileNearByUser.this);
        setContentView(R.layout.activity_pet_profile_near_by_user);
        mContext = PetProfileNearByUser.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
        viewPet();

    }

    public void init() {
        if (getIntent().hasExtra(Consts.PET_PROFILE)) {
            petListDTO = (PetListDTO) getIntent().getSerializableExtra(Consts.PET_PROFILE);
            paramsPet.put(Consts.PET_ID, petListDTO.getId());
            paramsPet.put(Consts.USER_ID, loginDTO.getId());
            paramsRating.put(Consts.PET_ID, petListDTO.getId());
            paramsRating.put(Consts.USER_ID, loginDTO.getId());
        }

        ratingBar = findViewById(R.id.ratingBar);
        RBpet = findViewById(R.id.RBpet);
        tvSubmit = findViewById(R.id.tvSubmit);
        tvTarget = findViewById(R.id.tvTarget);
        rlContact = findViewById(R.id.rlContact);
        cardview = findViewById(R.id.cardview);
        rlAdopt = findViewById(R.id.rlAdopt);
        back = findViewById(R.id.back);
        tvFollowerCount = findViewById(R.id.tvFollowerCount);
        tvFollow = findViewById(R.id.tvFollow);
        ctvbAdopt = findViewById(R.id.ctvbAdopt);
        rlFolow = findViewById(R.id.rlFolow);
        cardviewfd = findViewById(R.id.cardviewfd);
        cardviewadopt = findViewById(R.id.cardviewadopt);
        tvSubmit.setOnClickListener(this);
        back.setOnClickListener(this);
        rlFolow.setOnClickListener(this);
        rlAdopt.setOnClickListener(this);
        rlContact.setOnClickListener(this);
        ctvActivity = findViewById(R.id.ctvActivity);
        ctvActivity.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate_left));
        ivPetprofile = findViewById(R.id.ivPetprofile);
        ivPetprofile.startAnimation((Animation) AnimationUtils.loadAnimation(mContext, R.anim.translate_left));

        ivGender = findViewById(R.id.ivGender);
        ivVerify = findViewById(R.id.ivVerify);
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


        mChart = findViewById(R.id.lineChart1);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myrating = ratingBar.getRating();
                paramsRating.put(Consts.RATING, String.valueOf(myrating));
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
            case R.id.tvSubmit:
                sendRating();
                break;
            case R.id.rlFolow:
                followUnFollow();
                break;
            case R.id.rlAdopt:
                if (!petListDTO.getUser_id().equals(loginDTO.getId())) {
                    goChat();
                } else {
                    ProjectUtils.showToast(mContext, "This is your pet");
                }
                break;
            case R.id.rlContact:
              /*  if (!petListDTO.getUser_id().equals(loginDTO.getId())) {
                    Intent inn = new Intent(mContext, ContactActivity.class);
                    inn.putExtra(Consts.PET_PROFILE, petListDTO);
                    startActivity(inn);
                }*/
                if (!petListDTO.getUser_id().equals(loginDTO.getId())) {
                    goChat();
                } else {
                    ProjectUtils.showToast(mContext, "This is your pet");
                }

                break;
        }
    }

    private void followUnFollow() {
        followPet();
       /* if (petListDTO.getIs_follow().equals("0")) {
            followPet();
        } else {
            tvFollow.setText("Unfollow");
        }*/
    }


    public void goChat() {
        Intent inn = new Intent(mContext, OneTwoOneChatDemo.class);
        inn.putExtra(Consts.USER_ID, petListDTO.getUser_id());
        inn.putExtra(Consts.NAME, petListDTO.getPetName());
        startActivity(inn);
    }

    public void showData() {
        tvPetbreed.setText(petListDTO.getBreed_name());
        tvPetname.setText(petListDTO.getPetName());
        tvFollowerCount.setText(petListDTO.getFollowerCount() + " Followers");

        if (petListDTO.getIs_follow().equals("0")) {
            tvFollow.setText("Follow");
        } else {
            tvFollow.setText("Unfollow");
        }

        try {
            tvBirthdaydate.setText(ProjectUtils.getFormattedDate(Long.parseLong(petListDTO.getBirth_date())));
            tvAgedate.setText(ProjectUtils.calculateAge(petListDTO.getBirth_date()));
            tvAdoptsince.setText(ProjectUtils.calculateAge(petListDTO.getAdoption_date()));
            tvAdoptiondate.setText(ProjectUtils.getFormattedDate(Long.parseLong(petListDTO.getAdoption_date())));
            tvWeightdate.setText("last update " + ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(petListDTO.getUpdated_stamp()))));

        } catch (Exception e) {
            e.printStackTrace();
        }


        tvWeight.setText(petListDTO.getCurrent_weight() + " KGs");
        tvHeight.setText(petListDTO.getCurrent_height() + " CMs");
        tvPettype.setText(petListDTO.getActive_area());

        RBpet.setRating(Float.parseFloat(petListDTO.getRating()));

        tvWeightinfo.setText(petListDTO.getCurrent_weight() + " KGs");
        progressbar.setDonut_progress(petListDTO.getPercent());
        tvTarget.setText("Today's Target : " + petListDTO.getBreed_target());
        Glide.with(mContext)
                .load(petListDTO.getPet_img_path())
                .placeholder(R.drawable.ears_icon)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPetprofile);

        if (petListDTO.getSex().equalsIgnoreCase("Male")) {
            ivGender.setImageDrawable(mContext.getResources().getDrawable(R.drawable.male));

        } else {
            ivGender.setImageDrawable(mContext.getResources().getDrawable(R.drawable.female_a));
        }


        if (petListDTO.getVerified().equalsIgnoreCase("0")) {
            ivVerify.setVisibility(View.GONE);

        } else {
           ivVerify.setVisibility(View.VISIBLE);
        }
        if (petListDTO.getSel_notsel().equalsIgnoreCase("0")) {
            ctvbSell.setText("If you want to buy this pet please contact for more info.");

        } else {
            ctvbSell.setText("If you want to buy this pet please contact for more info.");
            cardviewfd.setVisibility(View.GONE);
        }
        if (petListDTO.getAdopt().equalsIgnoreCase("0")) {
            ctvbAdopt.setText("If you want to adopt this pet please contact for more info.");

        } else {
            ctvbAdopt.setText("If you want to adopt this pet please contact for more info.");
            cardviewadopt.setVisibility(View.GONE);
        }

        chartList = new ArrayList<>();
        chartList = petListDTO.getChart();

        if (chartList.size() > 0) {
            mChart.setData(generateLineData(chartList));

        }


        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);

        mChart.animateX(3000);

        Typeface tf = Typeface.createFromAsset(PetProfileNearByUser.this.getAssets(), "Ubuntu-Medium.ttf");

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

    public void viewPet() {
        new HttpsRequest(Consts.VIEW_PET_PROFILE, paramsPet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

            }
        });
    }

    public void sendRating() {
        new HttpsRequest(Consts.RATING_API, paramsRating, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }//9624267631

    public void followPet() {

        paramsFollow.put(Consts.PET_ID, petListDTO.getId());
        paramsFollow.put(Consts.FOLLOWER_USER_ID, loginDTO.getId());
        paramsFollow.put(Consts.USER_ID, loginDTO.getId());

        new HttpsRequest(Consts.FOLLOW_PET_PROFILE, paramsFollow, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);

                    if (petListDTO.getIs_follow().equals("0")) {
                        petListDTO.setIs_follow("1");
                        tvFollow.setText("Unfollow");

                        tvFollowerCount.setText(String.valueOf(Integer.valueOf(petListDTO.getFollowerCount()) + 1) + " Followers");
                        petListDTO.setFollowerCount(String.valueOf(Integer.valueOf(petListDTO.getFollowerCount()) + 1));
                    } else {
                        petListDTO.setIs_follow("0");
                        tvFollow.setText("Follow");

                        tvFollowerCount.setText(String.valueOf(Integer.valueOf(petListDTO.getFollowerCount()) - 1) + " Followers");
                        petListDTO.setFollowerCount(String.valueOf(Integer.valueOf(petListDTO.getFollowerCount()) - 1));
                    }


                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }
//Want to buy!!!
// for sale == Loving this breed!!! Want ot buy?? Please contact with suport team from More Info.
    // for not sale == If you want to buy this Pet so please contact with support team from More Info.
}
