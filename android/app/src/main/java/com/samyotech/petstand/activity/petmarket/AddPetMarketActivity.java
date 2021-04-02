package com.samyotech.petstand.activity.petmarket;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetCatList;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.multipleimagepicker.MultiImageSelector;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddPetMarketActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    CardView CVtakeimage, CVCountry, saveBTN;
    MaterialBetterSpinner spinnerCatagory;
    CustomEditText cetTitle, cetDescription, cetPrice, mobileET;
    LinearLayout LLprice, LLfree, back;
    CountryCodePicker ccp;
    private String TAG = AddPetMarketActivity.class.getSimpleName();
    private ArrayList<PetCatList> petList;
    String strSpinnerCategory = "";

    public static ArrayList<String> mSelectedImagesList = new ArrayList<>();

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;
    private final int REQUEST_IMAGE = 301;
    HashMap<String, String> parms = new HashMap<>();


    private MultiImageSelector mMultiImageSelector;
    private int PLACE_PICKER_REQUEST = 6;
    private String lati;
    private String longi;

    CustomEditText CTVAddress;
    PetMarketDTO petMarketDTO;

    HashMap<String, File> Imagelist = new HashMap<>();

    File file;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    String country = "", city = "", areacode = "", state = "", address = "";
    String saleType = "1";
    int flag = 0;
    RelativeLayout RLsave;
    CustomEditText cetCountry, cetCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(AddPetMarketActivity.this);
        setContentView(R.layout.activity_add_pet_market);
        mContext = AddPetMarketActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        mMultiImageSelector = MultiImageSelector.create();

        findUI();
        if (getIntent().hasExtra(Consts.FLAG)) {
            flag = getIntent().getIntExtra(Consts.FLAG, 0);
            if (flag == 1) {
                petMarketDTO = (PetMarketDTO) getIntent().getSerializableExtra(Consts.PET_DTO);
                setdata();
            }
        }

    }

    private void setdata() {
        spinnerCatagory.setText(petMarketDTO.getPet_name());
        cetTitle.setText(petMarketDTO.getTitle());
        cetDescription.setText(petMarketDTO.getDescription());
        mobileET.setText(petMarketDTO.getMobile_no());
        CTVAddress.setText(petMarketDTO.getAddress());
        ccp.setCountryForNameCode(petMarketDTO.getCountry_code());
        saleType = petMarketDTO.getSale_type();
        city = petMarketDTO.getCity();
        country = petMarketDTO.getCountry();
        lati = petMarketDTO.getLatitude();
        longi = petMarketDTO.getLongitude();
        strSpinnerCategory = petMarketDTO.getType_id();

        if (petMarketDTO.getSale_type().equals("0")) {
            free();
        } else {
            price();
            cetPrice.setText(petMarketDTO.getPrice());
        }


    }

    private void findUI() {
        CVtakeimage = findViewById(R.id.CVtakeimage);
        CVCountry = findViewById(R.id.CVCountry);
        RLsave = findViewById(R.id.RLsave);
        spinnerCatagory = findViewById(R.id.spinnerCatagory);
        cetTitle = findViewById(R.id.cetTitle);
        cetDescription = findViewById(R.id.cetDescription);
        mobileET = findViewById(R.id.mobileET);
        cetPrice = findViewById(R.id.cetPrice);
        LLprice = findViewById(R.id.LLprice);
        LLfree = findViewById(R.id.LLfree);
        ccp = findViewById(R.id.ccp);
        back = findViewById(R.id.back);
        CTVAddress = findViewById(R.id.CTVAddress);
        cetCountry = findViewById(R.id.cetCountry);
        cetCity = findViewById(R.id.cetCity);

        CVtakeimage.setOnClickListener(this);
        CTVAddress.setOnClickListener(this);
        RLsave.setOnClickListener(this);
        LLprice.setOnClickListener(this);
        LLfree.setOnClickListener(this);
        back.setOnClickListener(this);

        spinnerCatagory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strSpinnerCategory = petList.get(position).getId();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategoryies();
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_event));
        switch (v.getId()) {
            case R.id.CVtakeimage:
                addImages();
                break;
            case R.id.CTVAddress:
                ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
                getAddress();
                break;
            case R.id.RLsave:
                if (saleType.equals("0")) {
                    saveFree();
                } else {
                    savePrice();
                }
                break;
            case R.id.LLprice:
                price();
                break;
            case R.id.LLfree:
                free();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void free() {
        LLprice.setBackgroundColor(getResources().getColor(R.color.white));
        LLfree.setBackgroundColor(getResources().getColor(R.color.pink));
        saleType = "0";
        cetPrice.setEnabled(false);
        cetPrice.setText("0");
    }

    private void price() {
        LLprice.setBackgroundColor(getResources().getColor(R.color.pink));
        LLfree.setBackgroundColor(getResources().getColor(R.color.white));
        saleType = "1";
        cetPrice.setEnabled(true);
        // cetPrice.setText("");
    }

    private void savePrice() {
        if (!validateType()) {
            return;
        } else if (!validateTitle()) {
            return;
        } else if (!validateDiscription()) {
            return;
        } else if (!validatePrice()) {
            return;
        } else if (!validateAddress()) {
            return;
        } else if (!validateCountry()) {
            return;
        } else if (!validateCity()) {
            return;
        } else if (!validatePhone()) {
            return;
        } else {
            if (flag == 1) {
                editPetMarket();
            } else {
                addPetMarket();
            }
        }
    }

    private void saveFree() {
        if (!validateType()) {
            return;
        } else if (!validateTitle()) {
            return;
        } else if (!validateDiscription()) {
            return;
        } else if (!validateAddress()) {
            return;
        } else if (!validateCountry()) {
            return;
        } else if (!validateCity()) {
            return;
        } else if (!validatePhone()) {
            return;
        } else {
            if (flag == 1) {
                editPetMarket();
            } else {
                addPetMarket();
            }
        }

    }

    public boolean validateType() {
        if (!ProjectUtils.isEditTextFilled(spinnerCatagory)) {
            spinnerCatagory.setError("Please Select Type");
            spinnerCatagory.requestFocus();
            return false;
        } else {
            spinnerCatagory.setError(null);
            spinnerCatagory.clearFocus();
            return true;
        }
    }

    public boolean validateTitle() {
        if (!ProjectUtils.isEditTextFilled(cetTitle)) {
            cetTitle.setError("Please Enter Title");
            cetTitle.requestFocus();
            return false;
        } else {
            cetTitle.setError(null);
            cetTitle.clearFocus();
            return true;
        }
    }

    public boolean validateDiscription() {
        if (!ProjectUtils.isEditTextFilled(cetDescription)) {
            cetDescription.setError("Please Enter Discription");
            cetDescription.requestFocus();
            return false;
        } else {
            cetDescription.setError(null);
            cetDescription.clearFocus();
            return true;
        }
    }

    public boolean validatePrice() {
        if (!ProjectUtils.isEditTextFilled(cetPrice)) {
            cetPrice.setError("Please Enter Price");
            cetPrice.requestFocus();
            return false;
        } else {
            cetPrice.setError(null);
            cetPrice.clearFocus();
            return true;
        }
    }


    public boolean validateAddress() {
        if (!ProjectUtils.isTextFilled(CTVAddress)) {
            CTVAddress.setError("Please Select Address");
            CTVAddress.requestFocus();
            return false;
        } else {
            CTVAddress.setError(null);
            CTVAddress.clearFocus();
            return true;
        }
    }

    public boolean validateCity() {
        if (!ProjectUtils.isTextFilled(cetCity)) {
            cetCity.setError("Please Select City");
            cetCity.requestFocus();
            return false;
        } else {
            cetCity.setError(null);
            cetCity.clearFocus();
            return true;
        }
    }

    public boolean validateCountry() {
        if (!ProjectUtils.isTextFilled(cetCountry)) {
            cetCountry.setError("Please Select Country");
            cetCountry.requestFocus();
            return false;
        } else {
            cetCountry.setError(null);
            cetCountry.clearFocus();
            return true;
        }
    }

    public boolean validatePhone() {
        if (mobileET.getText().toString().trim().equalsIgnoreCase("")) {
            mobileET.setError("Please enter mobile number");
            mobileET.requestFocus();
            return false;
        } else {
            mobileET.setError(null);
            mobileET.clearFocus();
            return true;
            /*if (!ProjectUtils.isPhoneNumberValid(mobileET.getText().toString().trim())) {
                mobileET.setError("Please enter valid mobile number");
                mobileET.requestFocus();
                return false;
            } else {
                mobileET.setError(null);
                mobileET.clearFocus();
                return true;
            }*/
        }
    }

    public void getAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddPetMarketActivity.this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            ProjectUtils.showDialog(mContext, "Google Play Service", getString(R.string.googleplayservice), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    //Copy App URL from Google Play Store.
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms"));
                    startActivity(intent);
                }
            }, true);
        }
    }

    private void addImages() {
        try {
            if (checkAndRequestPermissions()) {
                mMultiImageSelector.showCamera(true);
                mMultiImageSelector.count(3);
                mMultiImageSelector.multi();
                mMultiImageSelector.origin(mSelectedImagesList);
                mMultiImageSelector.start(AddPetMarketActivity.this, REQUEST_IMAGE);
            }
        } catch (Exception e) {

        }
    }


    public void getCategoryies() {
        new HttpsRequest(Consts.GETALLPETTYPE, getparms(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<PetCatList>>() {
                    }.getType();
                    try {
                        petList = new ArrayList<>();
                        petList = (ArrayList<PetCatList>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        ArrayAdapter<PetCatList> adapter = new ArrayAdapter<PetCatList>(mContext, android.R.layout.simple_list_item_1, petList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCatagory.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);

                }
            }
        });

    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }


    private boolean checkAndRequestPermissions() {
        int externalStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AddPetMarketActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    ProjectUtils.pauseProgressDialog();
                    Place place = PlacePicker.getPlace(data, mContext);

                    address = String.format("%s", place.getAddress());
                    /*//String strcountry = address.substring(address.lastIndexOf(",") + 1);
                    String add = address.substring(0, address.lastIndexOf(","));
                    String[] spitestr = address.split(",");*/

                    lati = String.valueOf(place.getLatLng().latitude);
                    longi = String.valueOf(place.getLatLng().longitude);
                    CTVAddress.setText(address);
                    getAddressCity(place.getLatLng().latitude, place.getLatLng().longitude);


                } catch (Exception e) {

                }
            }
        }

        if (requestCode == REQUEST_IMAGE) {
            try {
                Imagelist = new HashMap<>();
                mSelectedImagesList = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                for (int j = 0; j < mSelectedImagesList.size(); j++) {

                    String imagePath = ProjectUtils.compressImage(mSelectedImagesList.get(j), mContext);

                    file = new File(imagePath);
                    Imagelist.put("files[" + String.valueOf(j) + "]", file);

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void addPetMarket() {
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.TYPE_ID, strSpinnerCategory);
        parms.put(Consts.TITLE, cetTitle.getText().toString().trim());
        parms.put(Consts.DESCRIPTION, cetDescription.getText().toString().trim());
        parms.put(Consts.PRICE, cetPrice.getText().toString().trim());
        parms.put(Consts.SALE_TYPE, saleType);
        parms.put(Consts.MOBILE_NO, mobileET.getText().toString().trim());
        parms.put(Consts.COUNTRY_CODE, ccp.getSelectedCountryCode() + "");
        parms.put(Consts.CITY, cetCity.getText().toString());
        parms.put(Consts.COUNTRY, cetCountry.getText().toString());
        parms.put(Consts.LATITUDE, lati);
        parms.put(Consts.LONGITUDE, longi);
        parms.put(Consts.ADDRESS, CTVAddress.getText().toString().trim());


        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ADD_PET_MARKET, parms, Imagelist, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);

                    try {
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void editPetMarket() {
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.PET_ID, petMarketDTO.getId());
        parms.put(Consts.TYPE_ID, strSpinnerCategory);
        parms.put(Consts.TITLE, cetTitle.getText().toString().trim());
        parms.put(Consts.DESCRIPTION, cetDescription.getText().toString().trim());
        parms.put(Consts.PRICE, cetPrice.getText().toString().trim());
        parms.put(Consts.SALE_TYPE, saleType);
        parms.put(Consts.MOBILE_NO, mobileET.getText().toString().trim());
        parms.put(Consts.COUNTRY_CODE, ccp.getSelectedCountryCode() + "");
        parms.put(Consts.CITY, city);
        parms.put(Consts.COUNTRY, country);
        parms.put(Consts.LATITUDE, lati);
        parms.put(Consts.LONGITUDE, longi);
        parms.put(Consts.ADDRESS, CTVAddress.getText().toString().trim());


        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.EDIT_PET_MARKET, parms, Imagelist, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);

                    try {
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void getAddressCity(double lat, double lng) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
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
            country = obj.getCountryName();
            city = obj.getSubAdminArea();

            if (!country.equals("") || country != null) {
                cetCountry.setText(country);
            }
            if (!city.equals("") || city != null) {
                cetCity.setText(city);
            }

            areacode = obj.getPostalCode();
            state = obj.getAdminArea();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
