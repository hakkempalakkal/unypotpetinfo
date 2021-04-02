package com.samyotech.petstand;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.activity.UserProfile.UserProfileActivity;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;
import com.victor.loading.rotate.RotateLoading;

public class Splash extends AppCompatActivity {
    private Handler handler = new Handler();
    private Context mContext;
    SharedPrefrence share;
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.VIBRATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE};
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    int count = 0;
    RotateLoading rotateLoading;
    private LoginDTO loginDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(Splash.this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        share = SharedPrefrence.getInstance(this);
        mContext = Splash.this;
        rotateLoading.start();


        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            handler.postDelayed(mTask, 5000);
        }
    }

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (share.getBooleanValue(SharedPrefrence.IS_LOGIN)) {
                loginDTO = share.getParentUser(Consts.LOGINDTO);

                if (!loginDTO.getAddress().equalsIgnoreCase("")) {
                    Intent intent = new Intent(mContext, BaseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                } else {
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("FlagLogin", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }

            } else {
                startActivity(new Intent(mContext, AppIntro.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }

        }
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            // DialogUtility.showToast(this, grantResults.length + "");
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equals(Manifest.permission.CAMERA)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--accounts granted");
                            count++;
                        } else {
                            //DialogUtility.showToast(this, "Permission is required. ");
                        }
                    } else if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--storage granted");
                            count++;
                        } else {
                           // DialogUtility.showToast(this, "Permission is required...");
                        }
                    } else if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--location granted");
                            count++;
                        } else {
                            //DialogUtility.showToast(this, "Permission is required...");
                        }
                    } else if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--location granted");
                            count++;
                        } else {
                          //  DialogUtility.showToast(this, "Permission is required...");
                        }
                    } else if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--phone granted");
                            count++;
                        } else {
                           // DialogUtility.showToast(this, "Permission is required...");
                        }
                    } else if (permissions[i].equals(Manifest.permission.GET_ACCOUNTS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--phone granted");
                            count++;
                        } else {
                            //DialogUtility.showToast(this, "Permission is required...");
                        }
                    } else if (permissions[i].equals(Manifest.permission.READ_SMS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--phone granted");
                            count++;
                        } else {
                           // DialogUtility.showToast(this, "Permission is required...");
                        }
                    } else if (permissions[i].equals(Manifest.permission.RECEIVE_SMS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "<--phone granted");
                            count++;
                        } else {
                           // DialogUtility.showToast(this, "Permission is required...");
                        }
                    }

                }
                if (count == 8) {
                    handler.postDelayed(mTask, 3000);
                }
            } else {
                handler.postDelayed(mTask, 3000);
            }
            handler.postDelayed(mTask, 3000);
        }
    }
}
