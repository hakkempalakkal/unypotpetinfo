package com.samyotech.petstand.activity.event;

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
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.EventDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetCatList;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ImageCompression;
import com.samyotech.petstand.utils.MainFragment;
import com.samyotech.petstand.utils.ProjectUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EventCreate extends AppCompatActivity implements View.OnClickListener {

    ImageView ivEvent;
    LinearLayout LLaddimage;
    CustomEditText CETeventtitle, CETdate, CETtime, CETaddress, CETdisciption;
    CustomButton CBsubmit;
    Context mContext;
    private Calendar calendar = Calendar.getInstance();
    private int PLACE_PICKER_REQUEST = 6;
    private String lati;
    private String longi;
    private SharedPrefrence sharedPrefrence;
    private LoginDTO loginDTO;
    CustomTextView tvHeader;

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
    String petType = "";
    private String TAG = EventCreate.class.getSimpleName();
    private ArrayList<PetCatList> petList;
    MaterialBetterSpinner spinnerCatagory;
    LinearLayout back;
    EventDTO eventDTO;
    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(EventCreate.this);
        setContentView(R.layout.activity_event_create);
        mContext = EventCreate.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);

        findUI();
    }

    private void findUI() {
        ivEvent = findViewById(R.id.ivEvent);
        LLaddimage = findViewById(R.id.LLaddimage);
        CETeventtitle = findViewById(R.id.CETeventtitle);
        CETdate = findViewById(R.id.CETdate);
        CETtime = findViewById(R.id.CETtime);
        CETaddress = findViewById(R.id.CETaddress);
        CETdisciption = findViewById(R.id.CETdisciption);
        CBsubmit = findViewById(R.id.CBsubmit);
        spinnerCatagory = findViewById(R.id.spinnerCatagory);
        back = findViewById(R.id.back);
        tvHeader = findViewById(R.id.tvHeader);

        CETdate.setOnClickListener(this);
        CETtime.setOnClickListener(this);
        CETaddress.setOnClickListener(this);
        CBsubmit.setOnClickListener(this);
        LLaddimage.setOnClickListener(this);
        back.setOnClickListener(this);

        if (getIntent().hasExtra(Consts.FLAG)) {
            flag = getIntent().getIntExtra(Consts.FLAG, 0);
            if (flag == 1) {
                eventDTO = (EventDTO) getIntent().getSerializableExtra(Consts.EVENT_DTO);
                setdata();
                tvHeader.setText("Edit Event");
            }
        }

        spinnerCatagory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                petType = petList.get(position).getId();
            }
        });

        builder = new BottomSheet.Builder(this).sheet(R.menu.menu_cards);
        builder.title("Select Image From");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(EventCreate.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EventCreate.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        picUri = FileProvider.getUriForFile(EventCreate.this.getApplicationContext(), EventCreate.this.getApplicationContext().getPackageName() + ".fileprovider", file);
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
                        if (ProjectUtils.hasPermissionInManifest(EventCreate.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EventCreate.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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

    private void setdata() {

        CETeventtitle.setText(eventDTO.getEvent_name());
        CETdate.setText(eventDTO.getEvent_date());
        CETaddress.setText(eventDTO.getAddress());
        CETdisciption.setText(eventDTO.getEvent_desc());
        CETtime.setText(eventDTO.getEvent_time());
        spinnerCatagory.setText(eventDTO.getPet_type());

        lati = eventDTO.getLatitude();
        longi = eventDTO.getLongitude();
        petType = eventDTO.getPet_type_id();

        Glide.with(mContext)
                .load(eventDTO.getImage())
                .placeholder(R.drawable.default_error)
                .dontAnimate() // will load image
                .into(ivEvent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPetType();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CETdate:
                ProjectUtils.datePicker(calendar, mContext, CETdate);
                break;
            case R.id.CETtime:
                ProjectUtils.timePicker(calendar, mContext, CETtime);
                break;
            case R.id.CETaddress:
                ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
                getAddress();
                break;
            case R.id.CBsubmit:
                if (flag == 1) {
                    saveEdit();
                } else {
                    save();
                }
                break;
            case R.id.LLaddimage:
                builder.show();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void save() {
        if (!validateName()) {
            return;
        } else if (!validatedate()) {
            return;
        } else if (!validateTime()) {
            return;
        } else if (!validaddress()) {
            return;
        } else if (!validType()) {
            return;
        } else if (!validatdescription()) {
            return;
        } else if (!validatefile()) {
            return;
        } else {
            createEvent();
        }

    }

    private void saveEdit() {
        if (!validateName()) {
            return;
        } else if (!validatedate()) {
            return;
        } else if (!validateTime()) {
            return;
        } else if (!validaddress()) {
            return;
        } else if (!validType()) {
            return;
        } else if (!validatdescription()) {
            return;
        } else {
            editEvent();
        }

    }


    public boolean validateName() {
        if (!ProjectUtils.isEditTextFilled(CETeventtitle)) {
            CETeventtitle.setError("Please enter name");
            CETeventtitle.requestFocus();
            return false;
        } else {
            CETeventtitle.setError(null);
            CETeventtitle.clearFocus();
            return true;
        }
    }

    public boolean validateTime() {
        if (!ProjectUtils.isEditTextFilled(CETtime)) {
            CETtime.setError("Please enter time");
            CETtime.requestFocus();
            return false;
        } else {
            CETtime.setError(null);
            CETtime.clearFocus();
            return true;
        }
    }

    public boolean validatefile() {
        if (file.length() < 1) {
            ProjectUtils.showToast(mContext, "Please select Image");
            return false;
        } else {
            return true;
        }
    }

    public boolean validatdescription() {
        if (!ProjectUtils.isEditTextFilled(CETdisciption)) {
            CETdisciption.setError("Please enter description");
            CETdisciption.requestFocus();
            return false;
        } else {
            CETdisciption.setError(null);
            CETdisciption.clearFocus();
            return true;
        }
    }

    public boolean validType() {
        if (!ProjectUtils.isEditTextFilled(spinnerCatagory)) {
            spinnerCatagory.setError("Please enter type");
            spinnerCatagory.requestFocus();
            return false;
        } else {
            spinnerCatagory.setError(null);
            spinnerCatagory.clearFocus();
            return true;
        }
    }

    public boolean validaddress() {
        if (!ProjectUtils.isEditTextFilled(CETaddress)) {
            CETaddress.setError("Please enter address");
            CETaddress.requestFocus();
            return false;
        } else {
            CETaddress.setError(null);
            CETaddress.clearFocus();
            return true;
        }
    }

    public boolean validatedate() {
        if (!ProjectUtils.isEditTextFilled(CETdate)) {
            CETdate.setError("Please select date");
            CETdate.requestFocus();
            return false;
        } else {
            CETdate.setError(null);
            CETdate.clearFocus();
            return true;
        }
    }

    public void getAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            ProjectUtils.pauseProgressDialog();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            //  ProjectUtils.showToast(mContext, "Google Play Services is not available");
            ProjectUtils.pauseProgressDialog();
            ProjectUtils.showDialog(this, "Google Paly Service", getString(R.string.googleplayservice), new DialogInterface.OnClickListener() {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    ProjectUtils.pauseProgressDialog();
                    Place place = PlacePicker.getPlace(data, mContext);

                    String address = String.format("%s", place.getAddress());

                    lati = String.valueOf(place.getLatLng().latitude);
                    longi = String.valueOf(place.getLatLng().longitude);
                    CETaddress.setText(address);

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
                                    .into(ivEvent);
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
                                    .into(ivEvent);
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
    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(mContext, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
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


    public void createEvent() {

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ADD_EVENT, getparm(), getparmFile(), mContext).imagePost(TAG, new Helper() {
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

    public HashMap<String, File> getparmFile() {
        HashMap<String, File> parms = new HashMap<>();
        if (file.length() > 1) {
            parms.put(Consts.IMG_PATH, file);
        }
        return parms;
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();

        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.EVENT_NAME, CETeventtitle.getText().toString().trim());
        parms.put(Consts.EVENT_DATE, CETdate.getText().toString().trim());
        parms.put(Consts.EVENT_DESC, CETdisciption.getText().toString());
        parms.put(Consts.EVENT_TIME, CETtime.getText().toString());
        parms.put(Consts.LATITUDE, lati);
        parms.put(Consts.LONGITUDE, longi);
        parms.put(Consts.PET_TYPE, petType);
        parms.put(Consts.ADDRESS, CETaddress.getText().toString());

        return parms;
    }

    public void editEvent() {

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.EDIT_EVENT, getparmEdit(), getparmEditFile(), mContext).imagePost(TAG, new Helper() {
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

    public HashMap<String, File> getparmEditFile() {
        HashMap<String, File> parms = new HashMap<>();
        if (file.length() > 1) {
            parms.put(Consts.IMG_PATH, file);
        }

        return parms;
    }

    public HashMap<String, String> getparmEdit() {
        HashMap<String, String> parms = new HashMap<>();

        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.EVENT_NAME, CETeventtitle.getText().toString().trim());
        parms.put(Consts.EVENT_DATE, CETdate.getText().toString().trim());
        parms.put(Consts.EVENT_DESC, CETdisciption.getText().toString());
        parms.put(Consts.EVENT_TIME, CETtime.getText().toString());
        parms.put(Consts.LATITUDE, lati);
        parms.put(Consts.LONGITUDE, longi);
        parms.put(Consts.PET_TYPE, petType);
        parms.put(Consts.ADDRESS, CETaddress.getText().toString());
        parms.put(Consts.EVENT_ID, eventDTO.getId());

        return parms;
    }


    public void getPetType() {
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.GETALLPETTYPE, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    //ProjectUtils.showLong(getActivity(), msg);
                    Type listType = new TypeToken<List<PetCatList>>() {
                    }.getType();
                    try {
                        petList = new ArrayList<>();
                        petList = (ArrayList<PetCatList>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        ArrayAdapter<PetCatList> adapterthick = new ArrayAdapter<PetCatList>(mContext, android.R.layout.simple_list_item_1, petList);
                        adapterthick.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCatagory.setAdapter(adapterthick);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

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


}
