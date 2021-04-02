package com.samyotech.petstand.sharedprefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.models.BodyScoreDTO;
import com.samyotech.petstand.models.DeviceInfo;
import com.samyotech.petstand.models.LoginDTO;

import java.lang.reflect.Type;

public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;
    public static SharedPrefrence myObj;
    public static String FAVOURITE = "favourite_list";
    public static final String USER_EMAIL = "userEmail";
    public static final String PASSWORD = "password";
    public static final String NULL_VALUE = "null";
    public static final String ItemIndex = "0";
    public static final String GET_PET_DETAIL = "getPetDetail";
    public static final String IS_FIRST_RUN = "isFirstRun";
//    public static final String CURRENT_INDEX = "currentIndex";
//    public static final String ADD_PET_INDEX = "addPetIndex";
    public static final String PET_APPOINTMENTS_DETAIL = "petAppointments";
    public static final String USER_DETAIL = "userDetails";
    public static final String BREED_LIST = "breedList";
    public static final String BREED_TARGET = "breedNameWithTarget";
    public static final String CALENDAR = "myCalendar";
    public static final String NOTIFIOCATION_ENABLE = "notificationAlert";
    public static final String URI = "mediaUri";
    // public static final String DEVICE_INFO = "deviceInfo";

    public static final String GET_PET_GALLERY = "getPetGallery";
    public static final String GET_REJECTION_LIST = "getRejectionList";

    public static final String IS_LOGIN = "isLogin";
    public static final String BODY_SCORE = "bodyScore";

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";


    public static final String INSURANCE = "insurance";
    public static final String VACCINATION_RECORD = "vaccinationRecord";
    public static final String NOTIFICATION_COUNT = "notificationCount";

    public static final String FORGOT_PASS_EMAIL = "forgotPassEmail";
    public static final String IS_FORGOT_PASS_OK = "forgotPassResult";
    public static final String WEIGHT_LAST_RECORD = "weightManagement";
    private SharedPrefrence() {

    }

    public long getLongValue(String Tag) {

        if (Tag.equals(CALENDAR))
            return myPrefs.getLong(Tag, 0L);
        return myPrefs.getLong(Tag, 0L);

    }

    public void setLongValue(String Tag, long token) {
        prefsEditor.putLong(Tag, token);
        prefsEditor.commit();
    }

    public boolean getBooleanValue(String Tag) {

        if (Tag.equals(IS_LOGIN))
            return myPrefs.getBoolean(Tag, false);
        if (Tag.equals(NOTIFIOCATION_ENABLE))
            return myPrefs.getBoolean(Tag, true);
        return myPrefs.getBoolean(Tag, false);

    }

    public void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }


    public int getIntValue(String Tag) {

        if (Tag.equals(NOTIFICATION_COUNT))
            return myPrefs.getInt(Tag, 0);
        return myPrefs.getInt(Tag, 0);

    }

    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.commit();
    }


    public String getValue(String Tag) {

        if (Tag.equals(IS_FIRST_RUN))
            return myPrefs.getString(Tag, "true");
        else if (Tag.equals(USER_EMAIL))
            return myPrefs.getString(Tag, "");
        else if (Tag.equals(FORGOT_PASS_EMAIL))
            return myPrefs.getString(Tag, "");
        else if (Tag.equals(PASSWORD))
            return myPrefs.getString(Tag, "");
        else if (Tag.equals(IS_FORGOT_PASS_OK))
            return myPrefs.getString(Tag, "");
        else if (Tag.equals(URI))
            return myPrefs.getString(Tag, "");
        return myPrefs.getString(Tag, "default");
    }

    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }

    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence();
            myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }

    public void setParentUser(LoginDTO loginDTO, String tag) {

        //convert to string using gson
        Gson gson = new Gson();
        String hashMapString = gson.toJson(loginDTO);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }


    public LoginDTO getParentUser(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new LoginDTO();
        } else {

            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<LoginDTO>() {
            }.getType();
            LoginDTO testHashMap = gson.fromJson(storedHashMapString, type);

            return testHashMap;
        }
    }



    public void setDeviceInfo(DeviceInfo deviceInfo, String tag) {

        //convert to string using gson
        Gson gson = new Gson();
        String hashMapString = gson.toJson(deviceInfo);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public DeviceInfo getDeviceInfo(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new DeviceInfo();
        } else {

            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<DeviceInfo>() {
            }.getType();
            DeviceInfo deviceInfo = gson.fromJson(storedHashMapString, type);

            return deviceInfo;
        }
    }



    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public void setBodyScore(BodyScoreDTO bodyScoreDTO, String tag) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(bodyScoreDTO);
        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public BodyScoreDTO getBodyScore(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new BodyScoreDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<BodyScoreDTO>() {
            }.getType();
            BodyScoreDTO bodyScoreDTO = gson.fromJson(storedHashMapString, type);
            return bodyScoreDTO;
        }
    }


}