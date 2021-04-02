package com.samyotech.petstand.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.samyotech.petstand.R;
import com.samyotech.petstand.fragment.AddPet.PetList;
import com.samyotech.petstand.fragment.Home.HomeFragment;
import com.samyotech.petstand.fragment.MoreFragment;
import com.samyotech.petstand.fragment.NearBy.NearByFrag;
import com.samyotech.petstand.fragment.duties.DutiesFragment;
import com.samyotech.petstand.fragment.foodDelivery.Catagory;
import com.samyotech.petstand.fragment.wall.WallFragment;
import com.samyotech.petstand.models.DeviceInfo;
import com.samyotech.petstand.models.User;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.BuilderManager;
import com.samyotech.petstand.utils.GPSTracker;
import com.samyotech.petstand.utils.ProjectUtils;

public class BaseActivity extends AppCompatActivity {

    public BoomMenuButton bmb1;
    private Context mContext;
    SharedPrefrence preference;
    public User user;
    public Catagory catagory = new Catagory();
    public MoreFragment moreFragment = new MoreFragment();
    public NearByFrag nearByFrag = new NearByFrag();
    public DutiesFragment dutiesFragment = new DutiesFragment();
    public PetList petList = new PetList();
    public HomeFragment homeFragment = new HomeFragment();
    public WallFragment wallFragment = new WallFragment();
    public FragmentManager fm;
    private DeviceInfo deviceInfo;
    public Fragment deviceList;
    public static String TAG_HOME = "home";
    public static String TAG_WALL = "wall";
    public static String TAG_NEAR_BY = "near_by";
    public static String TAG_REMINDER = "reminder";
    public static String TAG_FOOD = "food";
    public static String TAG_MORE = "more";
    public GPSTracker gps;
    double latitude, longitude;
    int ind = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(BaseActivity.this);
        setContentView(R.layout.activity_base);
        preference = SharedPrefrence.getInstance(this);
        mContext = BaseActivity.this;
        fm = getSupportFragmentManager();
        gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.e("Loction", "Lat" + latitude + "long" + longitude);
        } else {

            gps.showSettingsAlert();
        }

        Fragment fragment = homeFragment;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, TAG_HOME);
        fragmentTransaction.commitAllowingStateLoss();

        bmb1 = (BoomMenuButton) findViewById(R.id.bmb1);
        bmb1.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb1.setPiecePlaceEnum(PiecePlaceEnum.DOT_6_1);
        bmb1.setButtonPlaceEnum(ButtonPlaceEnum.SC_6_1);
        for (int i = 0; i < bmb1.getPiecePlaceEnum().pieceNumber(); i++) {
            bmb1.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilderWithDifferentPieceColor());

        }

        bmb1.setOnBoomListener(new OnBoomListenerAdapter() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                super.onClicked(index, boomButton);
                ind = index;
                changeBoomButton();
            }
        });
    }

    private void changeBoomButton() {
        if (ind == 0) {
            Fragment fragment = homeFragment;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG_HOME);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (ind == 1) {
            Fragment fragment = catagory;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG_FOOD);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (ind == 2) {
            Fragment fragment = nearByFrag;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG_NEAR_BY);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (ind == 3) {
            Fragment fragment = dutiesFragment;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG_REMINDER);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (ind == 4) {
            Fragment fragment = wallFragment;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG_WALL);
            fragmentTransaction.commitAllowingStateLoss();

        } else if (ind == 5) {
            Fragment fragment = moreFragment;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG_MORE);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        if (ind == 0) {
            clickDone();
        } else {
            Fragment fragment = homeFragment;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG_HOME);
            fragmentTransaction.commitAllowingStateLoss();
            ind = 0;
        }
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.walk_icon)
                .setTitle(R.string.app_name)
                .setMessage("Are you sure want to close PetStand?")
                .setPositiveButton("Yes, Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
