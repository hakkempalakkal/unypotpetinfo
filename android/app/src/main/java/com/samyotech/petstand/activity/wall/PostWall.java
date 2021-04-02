package com.samyotech.petstand.activity.wall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ImageCompression;
import com.samyotech.petstand.utils.MainFragment;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.videocompression.MediaController;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class PostWall extends Activity implements View.OnClickListener {
    private static final String TAG = PostWall.class.getSimpleName();
    private ImageView ivImg, ivBack;
    private CustomEditText etCommet, etTitle;
    private LinearLayout llPicture, llVedio, llPost;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    BottomSheet.Builder builder;
    BottomSheet.Builder buildervideo;
    String pathOfImage = "", extensionIMG = "";
    Bitmap bm, share_img;
    ImageCompression imageCompression;
    private File image, vedio, share_file;
    private SharedPrefrence prefrence;
    private Context mContext;
    private LoginDTO loginDTO;
    private CardView cardview1;
    ///new
    static final int REQUEST_VIDEO_CAPTURE = 115;
    private String filePath;

    public static final int MEDIA_TYPE_VIDEO = 6;
    public static PostWall ActivityContext = null;
    private VideoView vidPreview;
    HashMap<String, String> paramsForCategory = new HashMap<>();
    HashMap<String, String> params = new HashMap<>();

    boolean compressed = false;
    HashMap<String, File> parmsFile = new HashMap<>();
    int REQUEST_TAKE_GALLERY_VIDEO = 7, REQUEST_TAKE_CAMERA_VIDEO = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(PostWall.this);
        setContentView(R.layout.activity_post_wall);

        params.put(Consts.POSTTYPE, "text");

        mContext = PostWall.this;
        ActivityContext = this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
    }

    public void init() {
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(this);
        cardview1 = (CardView) findViewById(R.id.cardview1);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        etTitle = findViewById(R.id.etTitle);
        etCommet = findViewById(R.id.etCommet);
        ivImg = findViewById(R.id.ivImg);
        llPicture = findViewById(R.id.llPicture);
        llVedio = findViewById(R.id.llVedio);
        llPost = findViewById(R.id.llPost);

        llPicture.setOnClickListener(this);
        llVedio.setOnClickListener(this);
        llPost.setOnClickListener(this);
        builder = new BottomSheet.Builder(PostWall.this).sheet(R.menu.menu_cards);
        builder.title("PublicWall : Take Image From");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        picUri = FileProvider.getUriForFile(PostWall.this.getApplicationContext(), PostWall.this.getApplicationContext().getPackageName() + ".fileprovider", file);
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
                        if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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


        buildervideo = new BottomSheet.Builder(PostWall.this).sheet(R.menu.menu_cards);
        buildervideo.title("Take Video From");
        buildervideo.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {

                                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takeVideoIntent, REQUEST_TAKE_CAMERA_VIDEO);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(PostWall.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                Intent intent = new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);


                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        buildervideo.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        switch (v.getId()) {
            case R.id.llPicture:
                cardview1.setVisibility(View.VISIBLE);
                params.put(Consts.POSTTYPE, "image");
                builder.show();
                break;
            case R.id.llVedio:
                cardview1.setVisibility(View.VISIBLE);
                params.put(Consts.POSTTYPE, "video");
                //dispatchTakeVideoIntent();
                buildervideo.show();
                break;
            case R.id.llPost:
                submitForm();
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    public void submitForm() {
        if (!validateTitle()) {
            return;
        } else if (!validateComment()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                String type = params.get(Consts.POSTTYPE);
                if (type.equalsIgnoreCase("image")) {
                    parmsFile.put(Consts.MEDIA, image);

                    addPost();
                } else if (type.equalsIgnoreCase("video")) {
                    parmsFile.put(Consts.MEDIA, vedio);
                    addPost();
                } else {
                    addPost();

                }
            } else {
                ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
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

        } else if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");

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
                    //bitmap = MediaStore.Images.Media.getBitmap(PostWall.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(PostWall.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(mContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivImg);

                            pathOfImage = imagePath;
                            vidPreview.setVisibility(View.GONE);
                            ivImg.setVisibility(View.VISIBLE);
                            image = new File(imagePath);

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
                    bm = MediaStore.Images.Media.getBitmap(PostWall.this.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(PostWall.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {

                            Glide.with(mContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivImg);
                            image = new File(imagePath);

                            pathOfImage = imagePath;
                            vidPreview.setVisibility(View.GONE);
                            ivImg.setVisibility(View.VISIBLE);

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
                // image = new File(ConvertUriToFilePath.getPathFromURI(PostWall.this, picUri));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));
                // image = new File(ConvertUriToFilePath.getPathFromURI(PostWall.this, picUri));

                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }


        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();

                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    //    image = new File(ConvertUriToFilePath.getPathFromURI(PostWall.this, tempUri));
                    Log.e("image 2", image + "");
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        /*if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            try {
                filePath = getFilePath(PostWall.this, videoUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            File file = new File(filePath);
            ProjectUtils.showProgressDialog(mContext,false,"Please wait..");
            new VideoCompressor().execute();

        }*/

        if (requestCode == REQUEST_TAKE_CAMERA_VIDEO && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            try {
                filePath = getFilePath(PostWall.this, videoUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            File file = new File(filePath);
            ProjectUtils.showProgressDialog(mContext, false, "Please wait..");
            new VideoCompressor().execute();


        }

        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            try {
                filePath = getFilePath(PostWall.this, videoUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            File file = new File(filePath);
            ProjectUtils.showProgressDialog(mContext, false, "Please wait..");
            new VideoCompressor().execute();

        }

    }


    public void startCropping(Uri uri, int requestCode) {

        Intent intent = new Intent(PostWall.this, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        intent.putExtra(Consts.FLAG, 1);
        startActivityForResult(intent, requestCode);
    }


    public boolean validateComment() {
        if (!ProjectUtils.isEditTextFilled(etCommet)) {
            etCommet.setError(getResources().getString(R.string.validation_comment));
            etCommet.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateTitle() {
        if (!ProjectUtils.isEditTextFilled(etTitle)) {
            etTitle.setError(getResources().getString(R.string.validate_title));
            etTitle.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    public void addPost() {
        ProjectUtils.showProgressDialog(mContext, false, "Please wait..");

        new HttpsRequest(Consts.ADD_POST_API, getParms(), parmsFile, mContext).imagePost(TAG, new Helper() {
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


    public Map<String, String> getParms() {

        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.CHAT_TYPE, loginDTO.getId());
        params.put(Consts.CONTENT, ProjectUtils.getEditTextValue(etCommet));
        params.put(Consts.COMUNITY_ID, loginDTO.getComunity_id() + "");
        params.put(Consts.TITLE, ProjectUtils.getEditTextValue(etTitle));

        return params;
    }


    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(filePath);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            ProjectUtils.pauseProgressDialog();
            if (compressed) {
                vidPreview.setVisibility(View.VISIBLE);
                ivImg.setVisibility(View.GONE);
                vidPreview.setVideoPath(MediaController.cachedFile.getPath());
                vidPreview.start();
                vedio = new File(MediaController.cachedFile.getPath());

            } else {
                ProjectUtils.showLong(mContext, "Please record video again.");
            }

        }
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}