package com.samyotech.petstand.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.samyotech.petstand.R;
import com.samyotech.petstand.models.DummyFilterDTO;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.samyotech.petstand.activity.vat.PlaceDetailsActivity.PHONE_PERMISSION_CONSTANT;

/**
 * Created by Advantal on 21-Mar-16.
 */
public class ProjectUtils {

    public static final String TAG = "ProjectUtility";
    private static AlertDialog dialog;
    private static Toast toast;
    private static ProgressDialog mProgressDialog;
    private static final String VERSION_UNAVAILABLE = "N/A";
    public static HashMap<Integer, ArrayList<DummyFilterDTO>> map = new HashMap<>();
    public static HashMap<Integer, ArrayList<DummyFilterDTO>> mapPetMarket = new HashMap<>();
    public static ArrayList<String> countryList = new ArrayList<>();
    public static final Calendar refCalender = Calendar.getInstance();

    static Dialog dialogbox;

    public static void changeStatusBarColor(Activity activity) {

        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.orange));
        }
    }

    //Set status bar gradiant.
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradiant_bg2);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public static void changeStatusBarColorNew(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }


    //For Progress Dialog
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getResources().getString(R.string.please_wait));
        return progressDialog;
    }

    //For Long Period Toast Message
    public static void showLong(Context context, String message) {
        try {
            if (message == null) {
                return;
            }
            if (toast == null && context != null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            }
            if (toast != null) {
                toast.setText(message);
                toast.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // For Alert Dialog in App
    public static Dialog createDialog(Context context, int titleId, int messageId,
                                      DialogInterface.OnClickListener positiveButtonListener,
                                      DialogInterface.OnClickListener negativeButtonListener) {
        Builder builder = new Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.ok, positiveButtonListener);
        builder.setNegativeButton(R.string.cancel, negativeButtonListener);

        return builder.create();
    }


    // For Alert Dialog on Custom View in App
    public static Dialog createDialog(Context context, int titleId, int messageId, View view,
                                      DialogInterface.OnClickListener positiveClickListener,
                                      DialogInterface.OnClickListener negativeClickListener) {

        Builder builder = new Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, positiveClickListener);
        builder.setNegativeButton(R.string.cancel, negativeClickListener);

        return builder.create();
    }

    public static void datePicker(final Calendar calendar, final Context context, final TextView textView) {

        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calendar.set(Calendar.YEAR, y);
                calendar.set(Calendar.MONTH, m);
                calendar.set(Calendar.DAY_OF_MONTH, d);

                if (calendar.getTimeInMillis() >= refCalender.getTimeInMillis()) {
                    String myFormat = "dd MMM yyyy"; //In which you need put here 2018-02-08
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    textView.setText(sdf.format(calendar.getTime()));
                } else {
                    ProjectUtils.showToast(context, "Please select correct date.");
                }


            }


        }, year, monthOfYear, dayOfMonth);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    public static void timePicker(final Calendar calendar, final Context context, final TextView editText) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);

                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                String currentTime = sdf1.format(new Date());
                editText.setText(sdf1.format(calendar.getTime()));
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public static void Fullscreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public static void showDialog(Context context, String title, String msg,
                                  DialogInterface.OnClickListener OK, boolean isCancelable) {

        if (title == null)
            title = context.getResources().getString(R.string.app_name);

        if (OK == null)
            OK = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    hideDialog();
                }
            };

        if (dialog == null) {
            Builder builder = new Builder(context);
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", OK);
            dialog = builder.create();
            dialog.setCancelable(isCancelable);
        }

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Static method to show the dialog with custom message on it
     *
     * @param context      Context of the activity where to show the dialog
     * @param title        Title to be shown either custom or application name
     * @param msg          Custom message to be shown on dialog
     * @param OK           Overridden click listener for OK button in dialog
     * @param cancel       Overridden click listener for cancel button in dialog
     * @param isCancelable : Sets whether this dialog is cancelable with the BACK key.
     */
    public static void showDialog(Context context, String title, String msg,
                                  DialogInterface.OnClickListener OK,
                                  DialogInterface.OnClickListener cancel, boolean isCancelable) {

        if (title == null)
            title = context.getResources().getString(R.string.app_name);

        if (OK == null)
            OK = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    hideDialog();
                }
            };

        if (cancel == null)
            cancel = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    hideDialog();
                }
            };

        if (dialog == null) {
            Builder builder = new Builder(context);
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", OK);
            builder.setNegativeButton("Cancel", cancel);
            dialog = builder.create();
            dialog.setCancelable(isCancelable);
        }

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Static method to show the progress dialog.
     *
     * @param context      : Context of the activity where to show the dialog
     * @param isCancelable : Sets whether this dialog is cancelable with the BACK key.
     * @param message      : Message to be shwon on the progress dialog.
     * @return Object of progress dialog.
     */
  /*  public static Dialog showProgressDialog(Context context,
                                            boolean isCancelable, String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        mProgressDialog.setCancelable(isCancelable);
        return mProgressDialog;
    }


    public static void pauseProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }*/
    public static void showProgressDialog(Context context,
                                          boolean isCancelable, String message) {
        dialogbox = new Dialog(context);
        dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox.setContentView(R.layout.progressxml);

        //avi = dialogbox.findViewById(R.id.avi);
        // avi.show();
        dialogbox.show();
    }

    public static void pauseProgressDialog() {
        try {
            if (dialogbox != null) {
                // avi.hide();
                dialogbox.dismiss();
            }
        } catch (Exception e) {

        }
    }


    public static String parseToddMMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
        return bd.floatValue();
    }

    /**
     * Static method to cancel the Dialog.
     */
    public static void cancelDialog() {

        try {
            if (dialog != null) {
                dialog.cancel();
                dialog.dismiss();
                dialog = null;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Static method to hide the dialog if visible
     */
    public static void hideDialog() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
    }

    /**
     * This method will create alert dialog
     *
     * @param context  Context of calling class
     * @param title    Title of the dialog to be shown
     * @param msg      Msg of the dialog to be shown
     * @param btnText  array of button texts
     * @param listener
     */
    public static void showAlertDialog(Context context, String title,
                                       String msg, String btnText,
                                       DialogInterface.OnClickListener listener) {

        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                    paramDialogInterface.dismiss();
                }
            };

        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btnText, listener);
        dialog = builder.create();
        dialog.setCancelable(false);
        try {
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    public static void showToast(Context context, String message) {
        if (message == null) {
            return;
        }
        if (toast == null && context != null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (toast != null) {
            toast.setText(message);
            toast.show();
        }
    }


    public static void showAlertDialogWithCancel(Context context, String title,
                                                 String msg, String btnText,
                                                 DialogInterface.OnClickListener listener, String cancelTxt, DialogInterface.OnClickListener cancelListenr) {

        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        if (cancelListenr == null) {
            cancelListenr = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
        builder.setPositiveButton(btnText, listener);
        builder.setNegativeButton(cancelTxt, cancelListenr);
        dialog = builder.create();
        dialog.setCancelable(false);
        try {
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void showAlertDialog3Button(Context context, String title,
                                              String msg, String one,
                                              DialogInterface.OnClickListener onelistener, String two, DialogInterface.OnClickListener twolistener, String three, DialogInterface.OnClickListener threelistener) {

        if (onelistener == null)
            onelistener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        if (twolistener == null) {
            twolistener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }
        if (threelistener == null) {
            threelistener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(context.getResources().getDrawable(R.drawable.app_icon));
        builder.setPositiveButton(one, onelistener);
        builder.setNegativeButton(two, twolistener);
        builder.setNeutralButton(three, threelistener);
        dialog = builder.create();
        dialog.setCancelable(false);
        try {
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static AlertDialog showCustomtDialog(Context context,
                                                String title, String msg, String[] btnText, int layout_id,
                                                DialogInterface.OnClickListener listener) {
        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(layout_id,
                null);
        Builder builder = new Builder(context);
        builder.setTitle(title);
        // builder.setMessage(msg);
        // builder.setView(mEmail_forgot);

        builder.setPositiveButton(btnText[0], listener);
        if (btnText.length != 1) {
            builder.setNegativeButton(btnText[1], listener);
        }
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(textEntryView, 10, 10, 10, 10);
        dialog.show();
        return dialog;

    }

    /**
     * Get  EditText value.
     */
    public static String getEditTextValue(EditText text) {
        return text.getText().toString().trim();
    }

    /**
     * Checks the validation of email address.
     * Takes pattern and checks the text entered is valid email address or not.
     *
     * @param email : email in string format
     * @return True if email address is correct.
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else if (email.equals("")) {
            return false;
        }
        return false;
    }


    /**
     * Method checks if the given phone number is valid or not.
     *
     * @param number : Phone number is to be checked.
     * @return True if the number is valid.
     * False if number is not valid.
     */
    public static boolean isPhoneNumberValid(String number) {

        //String regexStr = "^([0-9\\(\\)\\/\\+ \\-]*)$";
        String regexStr = "^((0)|(91)|(00)|[7-9]){1}[0-9]{3,14}$";

        if (number.length() < 6 || number.length() > 13 || number.matches(regexStr) == false) {
            //	Log.d("tag", "Number is not valid");
            return false;
        }

        return true;
    }


    public static boolean isPasswordValid(String number) {

        //String regexStr = "^([0-9\\(\\)\\/\\+ \\-]*)$";
        String regexStr = " (?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,20})$";

        if (number.length() < 6 || number.length() > 13 /*|| number.matches(regexStr) == false*/) {
            //	Log.d("tag", "Number is not valid");
            return false;
        }

        return true;
    }

    /**
     * Checks if any text box is null or not.
     *
     * @param text : Text view for which validation is to be checked.
     * @return True if not null.
     */
    public static boolean isEditTextFilled(EditText text) {
        if (text.getText() != null && text.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTextFilled(TextView text) {
        if (text.getText() != null && text.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPasswordLengthCorrect(EditText text) {
        if (text.getText() != null && text.getText().toString().trim().length() >= 8) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public static void InternetAlertDialog(Context mContext) {
        Builder alertDialog = new Builder(mContext);

        //Setting Dialog Title
        alertDialog.setTitle("Error Connecting");

        //Setting Dialog Message
        alertDialog.setMessage("No Internet Connection");

        //On Pressing Setting button
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getAppVersion(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float getDpi(Activity activity) {
        float dp = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.density == 3.0) {
            dp = 1;
        }
        return dp;
    }

    public static void putBitmapInDiskCache(int blobId, Bitmap avatar, Context mContext) {
        // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
        // thumbnails
        File cacheDir = new File(mContext.getCacheDir(), "thumbnails-nudgebuddies");
        if (!cacheDir.exists())
            cacheDir.mkdir();
        // Create a path in that dir for a file, named by the default hash of the url

        File cacheFile = new File(cacheDir, "" + blobId);
        try {
            // Create a file at the file path, and open it for writing obtaining the output stream
            cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            avatar.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // Flush and close the output stream
            fos.flush();
            fos.close();
            avatar.recycle();
        } catch (Exception e) {
            // Log anything that might go wrong with IO to file
            Log.e("IMAGE CACHE", "Error when saving image to cache. ", e);
        }
    }

    public static Bitmap getBitmapFromDiskCache(int blobId, Context mContext) {
        // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
        // thumbnails
        Bitmap avatar = null;
        File cacheDir = new File(mContext.getCacheDir(), "thumbnails-nudgebuddies");
        // Create a path in that dir for a file, named by the default hash of the url

        File cacheFile = new File(cacheDir, "" + blobId);
        try {
            if (cacheFile.exists()) {

                FileInputStream fis = new FileInputStream(cacheFile);
                // Read a bitmap from the file (which presumable contains bitmap in PNG format, since
                // that's how files are created)
                avatar = BitmapFactory.decodeStream(fis);
                // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            }
            // Create a file at the file path, and open it for writing obtaining the output stream
            // Flush and close the output stream
        } catch (Exception e) {
            // Log anything that might go wrong with IO to file
            Log.e("IMAGE CACHE", "Error when saving image to cache. ", e);
        }
        return avatar;
    }

    public static void saveImage(Bitmap finalBitmap, int fileName) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/nudgebuddies/avatar");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = fileName + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromExternalStorage(int fileName) {

        Bitmap bitmap = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File f = new File(root + "/nudgebuddies/avatar/" + fileName + ".jpg");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String getImagePathExternalStorage(String fileName) {


       /* String fileName= listFile[i];
        fileName = fileName.replace(':', '/');
        fileName = fileName.replace('/', '_');*/

        String bitmapPath = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File f = new File(root + "/nudgebuddies/avatar/" + fileName + ".jpg");
        String loadURL = "file://" + Environment.getExternalStorageDirectory() + "/nudgebuddies/avatar/" + fileName;
        bitmapPath = f.getAbsolutePath();
        return loadURL;
    }

    public static String getInitials(String str) {
        String userInitials = "";
        if (str.length() > 1) {
            String[] array = str.split(" ");
            if (array.length == 1) {
                String firstLatter = String.valueOf(array[0].charAt(0)).toUpperCase();
                String secLatter = String.valueOf(array[0].charAt(1)).toUpperCase();
                userInitials = firstLatter + secLatter;
            } else if (array.length == 2) {
                String firstLatter = String.valueOf(array[0].charAt(0)).toUpperCase();
                String secLatter = String.valueOf(array[1].charAt(0)).toUpperCase();
                userInitials = firstLatter + secLatter;
            }
        } else if (str.length() == 1) {
            userInitials = str;
        }
        return userInitials;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }


            Log.v(TAG, "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static void soundPlayer(Context mContext, int resourceId) {
        MediaPlayer player = MediaPlayer.create(mContext, resourceId);
        if (player.isPlaying()) {
            player.stop();
        } else {
            player.start();
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static String getVersionName(Context context) {
        // Get app version
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = VERSION_UNAVAILABLE;
        }
        return versionName;
    }

    public static String getAgeInMonth(String strDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        try {
            long timeInMillis = Long.parseLong(strDate);
            Date birthDate = new Date(timeInMillis);
            //create calendar object for birth day
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTimeInMillis(birthDate.getTime());
            //create calendar object for current day
            long currentTime = System.currentTimeMillis();
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(currentTime);
            //Get difference between years
            years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
            int currMonth = now.get(Calendar.MONTH) + 1;
            int birthMonth = birthDay.get(Calendar.MONTH) + 1;
            //Get difference between months
            months = currMonth - birthMonth;
            //if month difference is in negative then reduce years by one and calculate the number of months.
            if (months < 0) {
                years--;
                months = 12 - birthMonth + currMonth;
                if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                    months--;
            } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                years--;
                months = 11;
            }
            //Calculate the days
            if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
                days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
            else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                int today = now.get(Calendar.DAY_OF_MONTH);
                now.add(Calendar.MONTH, -1);
                days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
            } else {
                days = 0;
                if (months == 12) {
                    years++;
                    months = 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        int month = (years * 12) + months;
        //Create new Age object
//        return years + " Y " + months + " M " + days + " days";
        return month + "";
    }

    public static String calculateAge(String strDate) {


        int years = 0;
        int months = 0;
        int days = 0;

        try {
            long timeInMillis = Long.parseLong(strDate);
            Date birthDate = new Date(timeInMillis);


            //create calendar object for birth day
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTimeInMillis(birthDate.getTime());
            //create calendar object for current day
            long currentTime = System.currentTimeMillis();
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(currentTime);
            //Get difference between years
            years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
            int currMonth = now.get(Calendar.MONTH) + 1;
            int birthMonth = birthDay.get(Calendar.MONTH) + 1;
            //Get difference between months
            months = currMonth - birthMonth;
            //if month difference is in negative then reduce years by one and calculate the number of months.
            if (months < 0) {
                years--;
                months = 12 - birthMonth + currMonth;
                if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                    months--;
            } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                years--;
                months = 11;
            }
            //Calculate the days
            if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
                days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
            else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                int today = now.get(Calendar.DAY_OF_MONTH);
                now.add(Calendar.MONTH, -1);
                days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
            } else {
                days = 0;
                if (months == 12) {
                    years++;
                    months = 0;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //Create new Age object
        return years + " years " + months + " months " + days + " days";
    }


    public static boolean hasPermissionInManifest(Activity activity, int requestCode, String permissionName) {
        if (ContextCompat.checkSelfPermission(activity,
                permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionName},
                    requestCode);
        } else {
            return true;
        }
        return false;
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public static String getTimeStamp(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return "" + calendar.getTimeInMillis();
    }

    public static long getTimeStampInLong(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();
    }


    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDate(long timestamp) {
        Timestamp tStamp = new Timestamp(timestamp);
        SimpleDateFormat simpleDateFormat;

        //simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return simpleDateFormat.format(tStamp);

    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDateMin(long timestamp) {
        Timestamp tStamp = new Timestamp(timestamp);
        SimpleDateFormat simpleDateFormat;

        //simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy KK:mm a");
        return simpleDateFormat.format(tStamp);

    }

    @SuppressLint("SimpleDateFormat")
    public static String convertTimestampToDate(long timestamp) {
        Timestamp tStamp = new Timestamp(timestamp);
        SimpleDateFormat simpleDateFormat;
        if (DateUtils.isToday(timestamp)) {
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            return "Today " + simpleDateFormat.format(tStamp);
        } else {
            //simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            return simpleDateFormat.format(tStamp);
        }
    }

    public static long correctTimestamp(long timestampInMessage) {
        long correctedTimestamp = timestampInMessage;
        try {

            if (String.valueOf(timestampInMessage).length() < 13) {
                int difference = 13 - String.valueOf(timestampInMessage).length(), i;
                String differenceValue = "1";
                for (i = 0; i < difference; i++) {
                    differenceValue += "0";
                }
                correctedTimestamp = (timestampInMessage * Integer.parseInt(differenceValue))
                        + (System.currentTimeMillis() % (Integer.parseInt(differenceValue)));
            }
        } catch (Exception e) {

        }
        return correctedTimestamp;
    }


    public static ArrayList<String> getPetTypeList() {
        ArrayList<String> petTypes = new ArrayList<String>();
        petTypes.add("Indoor");
        petTypes.add("Outdoor");
        petTypes.add("Both");

        return petTypes;
    }

    public static ArrayList<String> getPetTrainedTypesList() {
        ArrayList<String> trainedTypes = new ArrayList<String>();
        trainedTypes.add("Basic");
        trainedTypes.add("Obedience");
        trainedTypes.add("Professional");
        trainedTypes.add("None");

        return trainedTypes;
    }

    public static ArrayList<String> getOptionList() {
        ArrayList<String> optionAdapter = new ArrayList<String>();
        optionAdapter.add("6-lb bag");
        optionAdapter.add("12-lb bag");
        optionAdapter.add("24-lb bag");
        optionAdapter.add("40-lb bag");

        return optionAdapter;
    }

    public static ArrayList<String> getPetLifeStyleList() {
        ArrayList<String> lifeStyleTypes = new ArrayList<String>();
        lifeStyleTypes.add("Very Active");
        lifeStyleTypes.add("Active");
        lifeStyleTypes.add("Moderately Active");
        lifeStyleTypes.add("Sedentary");
        lifeStyleTypes.add("Lazy");

        return lifeStyleTypes;
    }

    public static ArrayList<String> getPetWeightList() {
        ArrayList<String> weightUnits = new ArrayList<String>();
        weightUnits = new ArrayList<String>();
        weightUnits.add("KG");
        return weightUnits;
    }


    public static ArrayList<String> getPetHeightList() {
        ArrayList<String> heightUnits = new ArrayList<String>();
        heightUnits = new ArrayList<String>();
        heightUnits.add("CM");

        return heightUnits;
    }

    public static int getItemPos(List<String> arrayList, String category) {

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equalsIgnoreCase(category)) {
                return i;
            }
        }
        return 0;
    }


    public static String getFirstLetterCapital(String input) {
        String val = "";
        try {
            val = Character.toUpperCase(input.charAt(0)) + input.substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }


    public static String getReminderDateTimeFormat(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm a");
        return sdf.format(cal.getTime());
    }

    public static String getDifferenceInTwoDate(String date) {
        long elapsedDays = 0;
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");
            Date date1 = simpleDateFormat.parse(date);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            // Set the date for both of the calendar instance
            cal1.setTimeInMillis(System.currentTimeMillis());
            cal2.setTime(date1);
            // Get the represented date in milliseconds
            long millis1 = cal1.getTimeInMillis();
            long millis2 = cal2.getTimeInMillis();
            // Calculate difference in milliseconds
            long diff = millis2 - millis1;
            // Calculate difference in seconds
            long diffSeconds = diff / 1000;
            // Calculate difference in minutes
            long diffMinutes = diff / (60 * 1000);
            // Calculate difference in hours
            long diffHours = diff / (60 * 60 * 1000);
            // Calculate difference in days
            elapsedDays = diff / (24 * 60 * 60 * 1000);
            DialogUtility.showLOG(elapsedDays + "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return elapsedDays + "";
    }

    public static String getRemind(String count) {
        if (count.equals("30"))
            return "30 mins";
        if (count.equals("60"))
            return "1 hour in advance";
        if (count.equals("120"))
            return "2 hour in advance";
        if (count.equals("1440"))
            return "1 day in advance";
        if (count.equals("2880"))
            return "2 days in advance";
        if (count.equals("4320"))
            return "3 days in advance";
        if (count.equals("5760"))
            return "4 days in advance";
        if (count.equals("7200"))
            return "5 days in advance";
        if (count.equals("10080"))
            return "1 week";
        return "No";
    }

    public static String getRepeat(String count) {
        if (count.equals("1"))
            return "1days";
        if (count.equals("2"))
            return "2days";
        if (count.equals("3"))
            return "3days";
        if (count.equals("4"))
            return "4days";
        if (count.equals("5"))
            return "5days";
        if (count.equals("6"))
            return "6days";
        if (count.equals("7"))
            return "7days";
        if (count.equals("8"))
            return "8days";
        if (count.equals("9"))
            return "9days";
        return "None";
    }


    public static ArrayList<String> getPetCategory() {
        ArrayList<String> petCat = new ArrayList<String>();
        petCat.add("Dog");
        petCat.add("Cat");
        petCat.add("Bird");
        petCat.add("Small Pet");
        petCat.add("Fish");

        return petCat;
    }


    public static int getIntValue(String value) {
        int result = 0;
        try {
            result = ((Number) NumberFormat.getInstance().parse(value)).intValue();

        } catch (ParseException e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    public static String convertTimestampDateToTime(long timestamp) {
        Timestamp tStamp = new Timestamp(timestamp);
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
        return simpleDateFormat.format(tStamp);
    }

    public static String getDisplayableDay(long delta) {
        long difference = 0;
        Long mDate = java.lang.System.currentTimeMillis();

        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            } else if (DateUtils.isToday(delta)) {
                return "TODAY";
            }/* else if (seconds < 120) {
                return "TODAY";
            } else if (seconds < 2700) // 45 * 60
            {
                return "TODAY";
            } else if (seconds < 5400) // 90 * 60
            {
                return "TODAY";
            } else if (seconds < 86400) // 24 * 60 * 60
            {
                return "TODAY";
            }*/ else if (seconds < 172800) // 48 * 60 * 60
            {
                return "YESTERDAY";
            } else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " days ago";
            } else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return null;
    }

    public static String convertTimestampToTime(long timestamp) {
        Timestamp tStamp = new Timestamp(timestamp);
        SimpleDateFormat simpleDateFormat;
        if (DateUtils.isToday(timestamp)) {
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            return simpleDateFormat.format(tStamp);
        } else {
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            return simpleDateFormat.format(tStamp);
        }
    }


    public static String compressImage(String imageUri, Context mContext) {

        String filePath = getRealPathFromURI(imageUri, mContext);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private static String getRealPathFromURI(String contentURI, Context mContext) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static void makecall(String phone, Context mContext) {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, PHONE_PERMISSION_CONSTANT);
        } else {
            try {
                Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
                my_callIntent.setData(Uri.parse("tel:" + phone));
                mContext.startActivity(my_callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String removeHtmlFromText(String html) {
        return Jsoup.parse(html).text();
    }

    public static String getCalculatedDate(String date, String dateFormat, int days) {

        SimpleDateFormat s = new SimpleDateFormat(dateFormat);

        try {
            Date myDate = s.parse(date);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(myDate);
            cal1.add(Calendar.DAY_OF_YEAR, 7);
            Date previousDate = cal1.getTime();
            return s.format(previousDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
    }

}