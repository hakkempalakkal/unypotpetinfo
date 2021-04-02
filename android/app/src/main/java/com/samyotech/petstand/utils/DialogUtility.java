package com.samyotech.petstand.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.samyotech.petstand.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DialogUtility {

    public static final String TAG = "DialogUtility";
    private static AlertDialog dialog;
    private static ProgressDialog mProgressDialog;

    static Dialog dialogbox;
   // static AVLoadingIndicatorView avi;

    /**
     * Static method to show the dialog with custom message on it
     *
     * @param context      Context of the activity where to show the dialog
     * @param title        Title to be shown either custom or application name
     * @param msg          Custom message to be shown on dialog
     * @param OK           Overridden click listener for OK button in dialog
     * @param isCancelable : Sets whether this dialog is cancelable with the BACK key.
     */
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
                                  DialogInterface.OnClickListener cancel, boolean isCancelable, String positiveBtnTxt, String negativeBtnTxt) {

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
            builder.setPositiveButton(positiveBtnTxt, OK);
            builder.setNegativeButton(negativeBtnTxt, cancel);
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
        mProgressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        mProgressDialog.setMessage(message);

        DoubleBounce doubleBounce = new DoubleBounce();
        mProgressDialog.setIndeterminateDrawable(doubleBounce);

        mProgressDialog.show();
        mProgressDialog.setCancelable(isCancelable);

        return mProgressDialog;
    }

    *//**
     * Static method to pause the progress dialog.
     *//*
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
            return true;
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

        if (number.length() < 10 || number.length() > 13 || number.matches(regexStr) == false) {
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
        if (text.getText() != null && text.getText().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkInternetConn(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getActiveNetworkInfo();
        if (wifi == null) {
            return false;
        } else {
            return true;
        }
        // NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //NetworkInfo roaming = connMgr.getNetworkInfo(ConnectivityManager.ty)
//        if (mobile.isConnected() || wifi.isConnected()) {
//            return true;
//        } else {
//            return false;
//        }
    }

    public static boolean isValidVehicleNumber(String number) {
      //  String regexStr = "^[a-zA-Z]{2}[0-9]{2}[a-zA-Z]{1,2}[0-9]{4}$";
        // String regexStr2 = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$";
        String regexStr = "^[A-Za-z]{2}[0-9]{2}[A-Za-z]{0,2}[0-9]{4}$";

        if (number.matches(regexStr) == false) {


            return false;


            //	Log.d("tag", "Number is not valid");
        }
        return true;

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public static void showToast(Context context, String msg){
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showLOG(String msg)
    {
        Log.d(TAG, msg);
    }
}
