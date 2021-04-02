package com.samyotech.petstand.activity.UserProfile;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ImageCompression;
import com.samyotech.petstand.utils.MainFragment;
import com.samyotech.petstand.utils.ProjectUtils;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout back;
    private ImageView IVprofile, ivChange, ivEditProfile;
    private CustomEditText cetName, cetMobile, cetEmail, cetAddress;
    private CardView cvSave;
    private Context mContext;
    private SharedPrefrence sharedPrefrence;
    private int flag;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    File file;
    DisplayImageOptions options;
    Bitmap bitmap = null;
    private String lastName, firstName;
    private LoginDTO loginDTO;
    private String TAG = UserProfileActivity.class.getSimpleName();
    private HashMap<String, String> parms = new HashMap<>();
    String possibleEmail = "";
    Account[] accounts;
    int flag_login;
    Double latitude, longitude;
    private int PLACE_PICKER_REQUEST = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(UserProfileActivity.this);
        setContentView(R.layout.activity_user_profile);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Places.initialize(getApplicationContext(), "AIzaSyAheuzu6EwJfzrZZ71Ys55eZ71s3vrMdJU");

        mContext = UserProfileActivity.this;

        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);

        if (getIntent().hasExtra("FlagLogin")) {
            flag_login = getIntent().getIntExtra("FlagLogin", 0);
        }
        init();

        showData();


    }

    public void getEmailId() {

        try {
            possibleEmail += "************* Get Registered Gmail Account*************\n\n";
            accounts =
                    AccountManager.get(this).getAccountsByType("com.google");
            if (accounts.length > 0) {
                cetEmail.setText(accounts[0].name);
            }
//            for (Account account : accounts) {
//
//                possibleEmail += " --> " + account.name + " : " + account.type + " , \n";
//                possibleEmail += " \n\n";
//
//            }
            Log.e("Exception", "mails:" + accounts[0].name);
        } catch (Exception e) {
            Log.e("Exception", "Exception:" + e);
        }

    }

    protected void init() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        IVprofile = findViewById(R.id.IVprofile);
        ivEditProfile = findViewById(R.id.ivEditProfile);
        ivChange = findViewById(R.id.ivChange);
        cvSave = findViewById(R.id.cvSave);
        cetMobile = findViewById(R.id.cetMobile);
        cetEmail = findViewById(R.id.cetEmail);
        cetName = findViewById(R.id.cetName);
        cetAddress = findViewById(R.id.cetAddress);
        //IVprofile.setOnClickListener(this);
        ivChange.setOnClickListener(this);
        ivEditProfile.setOnClickListener(this);
        cvSave.setOnClickListener(this);

        builder = new BottomSheet.Builder(UserProfileActivity.this).sheet(R.menu.menu_cards);
        builder.title("Select Image From");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(UserProfileActivity.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(UserProfileActivity.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
                                        picUri = FileProvider.getUriForFile(UserProfileActivity.this.getApplicationContext(), UserProfileActivity.this.getApplicationContext().getPackageName() + ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }

                                    sharedPrefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(UserProfileActivity.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(UserProfileActivity.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
        getEmailId();
        if (loginDTO.getAddress().equalsIgnoreCase("")) {
            editAll();
        }
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
        switch (view.getId()) {
            case R.id.back: {
                loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
                if (!loginDTO.getAddress().equalsIgnoreCase("")) {
                    if (flag_login == 1) {
                        Intent intent = new Intent(mContext, BaseActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }

                } else {
                    clickDone();
                }
                break;
            }
            case R.id.ivEditProfile:
                editAll();
                break;
            case R.id.ivChange:
                builder.show();

                break;
            case R.id.cetAddress:
                ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
                showPlacePicker();
                break;
            case R.id.cvSave: {
                if (!validateName()) {
                    return;
                } else if (!validateMobile()) {
                    return;
                } else if (!validateAddress()) {
                    return;
                } else if (!validateEmail()) {
                    return;
                } else {
                    view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    updateProfile();
                }
                break;
            }
        }
    }


    public boolean validateMobile() {
        if (!ProjectUtils.isEditTextFilled(cetMobile)) {
            cetMobile.setError("Please enter your mobile.");
            cetMobile.requestFocus();
            return false;
        } else {
            cetMobile.setError(null);
            cetMobile.clearFocus();
            return true;
        }
    }

    public boolean validateName() {
        if (!ProjectUtils.isEditTextFilled(cetName)) {
            cetName.setError("Please enter your name");
            cetName.requestFocus();
            return false;
        } else {
            cetName.setError(null);
            cetName.clearFocus();
            return true;
        }
    }

    public boolean validateEmail() {
        if (!ProjectUtils.isEmailValid(cetEmail.getText().toString().trim())) {
            cetEmail.setError("Please enter correct email.");
            cetEmail.requestFocus();
            return false;
        } else {
            cetEmail.setError(null);
            cetEmail.clearFocus();
            return true;
        }
    }

    public boolean validateAddress() {
        if (!ProjectUtils.isEditTextFilled(cetAddress)) {
            cetAddress.setError("Please enter your Address.");
            cetAddress.requestFocus();
            return false;
        } else {
            cetAddress.setError(null);
            cetAddress.clearFocus();
            return true;
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

            imageName = Consts.PET_CARE + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }

    public void editAll() {

        cetName.setEnabled(true);
        cetName.setSelection(cetName.getText().length());
        cetMobile.setEnabled(true);
        cetMobile.setSelection(cetMobile.getText().length());

       /* cetEmail.setEnabled(true);
        cetEmail.setSelection(cetEmail.getText().length());*/

        cetAddress.setEnabled(true);
        cetAddress.setOnClickListener(this);
        cvSave.setVisibility(View.VISIBLE);
        ivChange.setVisibility(View.VISIBLE);
        ivEditProfile.setVisibility(View.GONE);
    }

    private void showData() {
        cetMobile.setText(loginDTO.getMobile_no());
        cetEmail.setText(loginDTO.getEmail());
        cetAddress.setText(loginDTO.getAddress());
        if (!loginDTO.getFirst_name().equalsIgnoreCase("")) {
            cetName.setText(loginDTO.getFirst_name() + " " + loginDTO.getLast_name());

        }
        Glide.with(mContext)
                .load(loginDTO.getProfile_pic())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(IVprofile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProjectUtils.pauseProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    ProjectUtils.pauseProgressDialog();

                    latitude = data.getDoubleExtra(LATITUDE, 0.0);
                    longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                    Log.d("LONGITUDE****", longitude.toString());

                    String postalcode = data.getStringExtra(ZIPCODE);

                    getAddress(latitude, longitude);


                } catch (Exception e) {

                }
            }
        }
        if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(mContext);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(mContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(IVprofile);
                            editAll();
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);
                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
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
                try {
                    bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(mContext);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(mContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(IVprofile);
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);
                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(sharedPrefrence.getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(sharedPrefrence
                        .getValue(Consts.IMAGE_URI_CAMERA));
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

        if (requestCode == 102 && resultCode == RESULT_OK) {
            try {
                ArrayList<File> Files = (ArrayList<File>) data.getSerializableExtra("");

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(mContext, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

    private void showPlacePicker() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                //.withLocation(41.4036299, 2.1743558)
                .build(mContext);

        startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST);
    }


    public void findPlace() {
        /*try {
            List<com.google.android.libraries.places.api.model.Place.Field> placeFields = new ArrayList<>(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.values()));
            List<TypeFilter> typeFilters = new ArrayList<>(Arrays.asList(TypeFilter.values()));
// Create a RectangularBounds object.
            RectangularBounds bounds = RectangularBounds.newInstance(
                    new LatLng(-33.880490, 151.184363),
                    new LatLng(-33.858754, 151.229596));
            Intent autocompleteIntent =
                    new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields)
                            .setLocationBias(bounds)
                            .setTypeFilter(typeFilters.get(0))
                            .build(this);
            startActivityForResult(autocompleteIntent, 1001);

        } catch (Exception e) {
            // TODO: Handle the error.
        }*/
    }


    public void updateProfile() {
//        String[] names = cetName.getText().toString().split("\\s", 0);
//        firstName = names[0];
//        lastName = names[1];
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.MOBILE_NO, ProjectUtils.getEditTextValue(cetMobile));
        parms.put(Consts.FIRSTNAME, cetName.getText().toString().trim());
        parms.put(Consts.LASTNAME, "");
        parms.put(Consts.EMAIL, cetEmail.getText().toString());
        parms.put(Consts.ADDRESS, cetAddress.getText().toString());

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.UPDATEPROFILEUSER, parms, getparmFile(), mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    try {
                        loginDTO = new Gson().fromJson(response.getJSONObject("data").toString(), LoginDTO.class);
                        sharedPrefrence.setParentUser(loginDTO, Consts.LOGINDTO);
                        cvSave.setVisibility(View.GONE);
                        ivChange.setVisibility(View.GONE);
                        ivEditProfile.setVisibility(View.VISIBLE);
                        cetName.setEnabled(false);
                        cetMobile.setEnabled(false);
                        cetEmail.setEnabled(false);
                        cetAddress.setEnabled(false);
                        cetName.clearAnimation();
                        cetMobile.clearAnimation();
                        cetAddress.clearAnimation();
                        cetEmail.clearAnimation();
                        Intent intent = new Intent(mContext, BaseActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public HashMap<String, File> getparmFile() {
        HashMap<String, File> parms = new HashMap<>();
        parms.put(Consts.USER_PROFILE_PIC, file);

        return parms;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        if (!loginDTO.getAddress().equalsIgnoreCase("")) {
            if (flag_login == 1) {
                Intent intent = new Intent(mContext, BaseActivity.class);
                startActivity(intent);
                finish();
            } else {
                finish();
            }

        } else {
            clickDone();
        }
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);

            cetAddress.setText(obj.getAddressLine(0));

            parms.put(Consts.LATI, String.valueOf(lat));
            parms.put(Consts.LONGI, String.valueOf(lng));
            if (!String.valueOf(obj.getLocality()).equals("null")) {
                parms.put(Consts.CITY, String.valueOf(obj.getLocality()));
            }
            if (!String.valueOf(obj.getAdminArea()).equals("null")) {
                parms.put(Consts.STATE, String.valueOf(obj.getAdminArea()));
            }
            if (!String.valueOf(obj.getPostalCode()).equals("null")) {
                parms.put(Consts.POSTCODE, String.valueOf(obj.getPostalCode()));

            }
            if (!String.valueOf(obj.getCountryName()).equals("null")) {
                parms.put(Consts.COUNTRY, String.valueOf(obj.getCountryName()));
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*E/Place: country--India,
     postalCode--452010,
      state--Madhya Pradesh,
       city--Indore,
       stAddress1--1st Floor Pramukh Plaza, Near Sajan Prabha Garden, Vijay Nagar,
       stAddress2--Sheetal Nagar,
       latitude1--22.749753,
       longitude1--75.89973599999999*/


    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.walk_icon)
                .setTitle(R.string.app_name)
                .setMessage(getResources().getString(R.string.close_msg_profile))
                .setNegativeButton("Close PetStand", new DialogInterface.OnClickListener() {
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
                .setPositiveButton("Yes, Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}