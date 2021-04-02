package com.samyotech.petstand.activity.editPet;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ConvertUriToFilePath;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.DialogUtility;
import com.samyotech.petstand.utils.ImageCompression;
import com.samyotech.petstand.utils.MainFragment;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by welcome pc on 10-12-2017.
 */

public class EditPet extends AppCompatActivity implements View.OnClickListener {
    private String TAG = EditPet.class.getSimpleName();
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    BottomSheet.Builder builder;
    Uri picUri;
    String checkClick = "Profile";
    Calendar calendar;
    ImageView ivMale, ivFemale, IVprofile;
    CustomTextView ctvTitle, tvHeader, ctvType;
    LinearLayout delete, back, llMale, llFemale;
    String gender = "Male";
    ImageCompression imageCompression;
    String pathOfImage = "";
    Bitmap bm;
    HashMap<String, String> values = new HashMap<>();
    HashMap<String, File> valuesFile = new HashMap<>();
    private File file;
    private ViewPager viewPager;
    private PageAdapter mAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnNext;
    private Context mContext;
    private IntroFragment introFragment = new IntroFragment();
    private LifeFragment lifeFragment = new LifeFragment();
    public PetListDTO petListDTO;
    private SharedPrefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(EditPet.this);
        setContentView(R.layout.add_pet_new);
        mContext = EditPet.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        if (getIntent().hasExtra(Consts.PET_PROFILE)) {
            petListDTO = (PetListDTO) getIntent().getSerializableExtra(Consts.PET_PROFILE);

            values.put(Consts.USER_ID, petListDTO.getUser_id());
            values.put(Consts.PET_ID, petListDTO.getId());
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnNext = (Button) findViewById(R.id.btn_next);
        tvHeader = (CustomTextView) findViewById(R.id.tvHeader);
        ctvTitle = (CustomTextView) findViewById(R.id.ctvTitle);
        ctvType = (CustomTextView) findViewById(R.id.ctvType);
        tvHeader.setText("Edit Pet");
        layouts = new int[]{
                R.layout.add_pet_picture,
                R.layout.add_pet_picture};
        addBottomDots(0);
        mAdapter = new PageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    if (current == 1) {
                        if (introFragment.dob_timeStamp == 0) {
                            ProjectUtils.showLong(EditPet.this, "Please select Pet Date of birth.");
                        } else if (introFragment.adop_timeStamp == 0) {
                            ProjectUtils.showLong(EditPet.this, "Please select Pet Adoption date.");
                        } else if (introFragment.dob_timeStamp > System.currentTimeMillis()) {
                            ProjectUtils.showLong(EditPet.this, "Please check date of birth.");
                        } else if (introFragment.adop_timeStamp > System.currentTimeMillis()) {
                            ProjectUtils.showLong(EditPet.this, "Please check adoption date.");
                        } else if (introFragment.dob_timeStamp > introFragment.adop_timeStamp) {
                            ProjectUtils.showLong(EditPet.this, "Adoption date should be greater then date of birth.");
                        } else {
                            viewPager.setCurrentItem(current);
                        }

                    } else {
                        viewPager.setCurrentItem(current);

                    }
                } else {
                    updatePet();
                }
//                if (btnNext.getText().toString().equals("Submit")) {
//                    updatePet();
//                }
            }
        });

        initView();
        calendar = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        initImagePicker();

        showData();
    }

    public void initView() {
        llMale = (LinearLayout) findViewById(R.id.llMale);
        llMale.setOnClickListener(this);

        llFemale = (LinearLayout) findViewById(R.id.llFemale);
        llFemale.setOnClickListener(this);

        ivMale = (ImageView) findViewById(R.id.ivMale);
        ivFemale = (ImageView) findViewById(R.id.ivFemale);
        IVprofile = (ImageView) findViewById(R.id.IVprofile);
        IVprofile.setOnClickListener(this);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        delete = (LinearLayout) findViewById(R.id.delete);
        delete.setOnClickListener(this);
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

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                ctvType.setText("Lifestyle");
                btnNext.setText(getString(R.string.start));
            } else {
                // still pages are left
                ctvType.setText("Introduction");
                btnNext.setText(getString(R.string.next));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private class PageAdapter extends FragmentStatePagerAdapter {
        // private Fragment fragment;
        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return introFragment;
                case 1:
                    return lifeFragment;
                default:
                    return introFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    public void initImagePicker() {
        builder = new BottomSheet.Builder(EditPet.this).sheet(R.menu.menu_cards);
        builder.title("PublicWall : Take Image From");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(EditPet.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EditPet.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
                                        picUri = FileProvider.getUriForFile(EditPet.this.getApplicationContext(), EditPet.this.getApplicationContext().getPackageName() + ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }


                                    prefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(EditPet.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EditPet.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_event));
        switch (v.getId()) {

            case R.id.IVprofile: {
                checkClick = "Profile";
                builder.show();
                break;
            }
            case R.id.llMale: {
                gender = "Male";
                ivMale.setImageDrawable(getResources().getDrawable(R.drawable.male));
                ivFemale.setImageDrawable(getResources().getDrawable(R.drawable.female_d));
                values.put(Consts.SEX, gender);
                break;
            }
            case R.id.llFemale: {
                gender = "Female";
                ivFemale.setImageDrawable(getResources().getDrawable(R.drawable.female_a));
                ivMale.setImageDrawable(getResources().getDrawable(R.drawable.male_d));
                values.put(Consts.SEX, gender);
                break;
            }
            case R.id.back: {
                finish();
                break;
            }
            case R.id.delete: {
//                clickDelete();
                ProjectUtils.showLong(mContext, "In Progess.");
                break;
            }
        }
    }

    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();

        File mediaStorageDir = new File(root, Consts.PET_CARE);

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.PET_CARE + timeStamp + ".png");

        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_CAMERA_IMAGE) {

            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));

                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(EditPet.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(EditPet.this).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(IVprofile);

                            file = new File(ConvertUriToFilePath.getPathFromURI(EditPet.this, Uri.parse("file://" + imagePath)));
                            if (file != null) {
                                valuesFile.put(Consts.PET_IMG_PATH, file);
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        if (requestCode == CROP_GALLERY_IMAGE) {

            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                Log.e("image 1", picUri + "");
                try {
                    bm = MediaStore.Images.Media.getBitmap(EditPet.this.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(EditPet.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {

                            Glide.with(EditPet.this).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(IVprofile);

                            file = new File(ConvertUriToFilePath.getPathFromURI(EditPet.this, Uri.parse("file://" + imagePath)));
                            if (file != null) {
                                valuesFile.put(Consts.PET_IMG_PATH, file);
                            }

                            Log.e("image 2", imagePath);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {

                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));

                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }


        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();

                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {

                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    public void startCropping(Uri uri, int requestCode) {

        Intent intent = new Intent(this, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("cropType", Consts.CROP_4_3);
        startActivityForResult(intent, requestCode);

    }

    public void updatePet() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.UPDATE_PET, values, valuesFile, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }


    public void clickDelete() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.app_icon)
                .setTitle("Delete Pet")
                .setMessage("Are you sure want to delete " + "Betaal" + "?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deletePet();
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

    public void deletePet() {
        DialogUtility.showProgressDialog(this, false, "Please wait...");
        AndroidNetworking.post(Consts.BASE_URL + Consts.DELETE_PET)
                .setTag(Consts.DELETE_PET)
                .setPriority(Priority.HIGH)
                .addBodyParameter(Consts.DELETE_PET_ID, "1")
                .addBodyParameter(Consts.DELETE_VALUE, "true")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DialogUtility.pauseProgressDialog();
//                        deletePetResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        DialogUtility.pauseProgressDialog();
                        ProjectUtils.showLong(getApplicationContext(), Consts.SERVER_ERROR);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    public void showData() {
        Glide
                .with(mContext)
                .load(petListDTO.getPet_img_path())
                .placeholder(R.drawable.ears_icon)
                .into(IVprofile);
        ctvTitle.setText(petListDTO.getPetName());
        if (petListDTO.getSex().equalsIgnoreCase("Male")) {
            ivMale.setImageDrawable(getResources().getDrawable(R.drawable.male));
            ivFemale.setImageDrawable(getResources().getDrawable(R.drawable.female_d));
        } else {
            ivFemale.setImageDrawable(getResources().getDrawable(R.drawable.female_a));
            ivMale.setImageDrawable(getResources().getDrawable(R.drawable.male_d));
        }

    }
}
