package com.samyotech.petstand.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


import com.samyotech.petstand.models.FoodPlan;
import com.samyotech.petstand.models.ManualActivity;
import com.samyotech.petstand.models.NotificationsDTO;
import com.samyotech.petstand.models.StepCount;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;

import java.io.File;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "PoochPlay.db";

    // Contacts table name
    private static final String TABLE_STEP_COUNT_HOUR = "countHourly";
    // Contacts table name
    // private static final String TABLE_STEP_COUNT_DATE = "countDaily";
    // Contacts Table Columns names
    private static final String DEVICE_ID = "deviceId";
    private static final String CONNECTED_DATE = "connectedDate";
    private static final String STEP_COUNT = "stepCount";
    private static final String TOTAL_COUNT = "totalCount";
    private static final String IS_SYNCED = "isSynced";
    //private static final String CALORIE = "Calorie";

    private static final String PET_ID = "petId";
    private static final String USER_EMAIL = "user_email";

    // Stories table name
    private static final String TABLE_NOTIFICATION = "notifications";
    // Table Column
//    private static final String PET_ID = "petId";
    private static final String NOTIFICATION_ID = "id";
    private static final String NOTIFICATION_TITLE = "title";
    private static final String NOTIFICATION_DESCRIPTION = "description";
    private static final String NOTIFICATION_TIME = "time";
    private static final String NOTIFICATION_CATEGORY = "category";
    private static final String NOTIFICATION_ROOT_CATEGORY = "rootCategory";
    private static final String NOTIFICATION_ITEM_ID = "itemID";
    private static final String NOTIFICATION_IMAGE = "image";

    // Stories table name
    private static final String TABLE_COMPANY = "company";
    // Table Column
    private static final String COM_ID = "c_id";
    private static final String COM_NAME = "c_name";
    private static final String COM_IMAGE = "c_img_path";

    // Stories table name
    private static final String TABLE_PRODUCT = "product";
    // Table Column
    private static final String P_COM_ID = "c_id";
    private static final String P_COM_NAME = "c_name";
    private static final String BREED_CATEGORIES = "breedcategories";
    private static final String P_IMAGE = "img_path";
    private static final String P_IS_ADULT = "isadult";
    private static final String P_ID = "p_id";
    private static final String P_NAME = "p_name";
    private static final String P_THRESHOLD = "threshold";
    private static final String P_UNIT = "unit";


    // Stories table name
    private static final String TABLE_MANUAL_ACTIVITY = "manualActivity";
    // Table Column
    private static final String MA_USER_EMAIL = "userEmail";
    private static final String MA_PET_ID = "petId";
    private static final String MA_LONG = "longDate";
    private static final String MA_HUMAN_DATE = "humanDate";
    private static final String MA_ACTIVITY_VALUE = "activityValue";
    private static final String MA_UNIT = "unit";
    private static final String MA_TARGET = "target";

    // Stories table name
    private static final String TABLE_WEIGHT = "weight";
    // Table Column
    private static final String W_USER_EMAIL = "userEmail";
    private static final String W_PET_ID = "petId";
    private static final String W_LONG = "longDate";
    private static final String W_HUMAN_DATE = "humanDate";
    private static final String W_WEIGHT = "weight";
    private static final String W_UNIT = "unit";


    // Stories table name
    private static final String TABLE_FOOD = "food";
    // Table Column
    private static final String F_PET_ID = "petId";
    private static final String F_PRODUCT_ID = "productId";
    private static final String F_PRODUCT_NAME = "productName";
    private static final String F_COM_ID = "comId";
    private static final String F_COM_NAME = "comName";
    private static final String F_QUANTITY = "quantity";
    private static final String F_UNIT = "unit";


    SharedPrefrence share;
    Context ctx;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, makeDBFilePath(), null, DATABASE_VERSION);
        ctx = context;
        share = SharedPrefrence.getInstance(ctx);

    }

    public static String makeDBFilePath() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), Consts.POOCH_PLAY + File.separator + Consts.DATABASE);
        if (!file.exists()) {
            file.mkdirs();
        }
        String dbPath = (file.getPath() + File.separator + DATABASE_NAME);
        return dbPath;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_STEP_COUNT_HOUR = "CREATE TABLE " + TABLE_STEP_COUNT_HOUR + "("
                + PET_ID + " TEXT,"
                + USER_EMAIL + " TEXT,"
                + DEVICE_ID + " TEXT,"
                + CONNECTED_DATE + " TEXT,"
                + STEP_COUNT + " TEXT,"
                + TOTAL_COUNT + " TEXT,"
                + IS_SYNCED + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_STEP_COUNT_HOUR);

        String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE " + TABLE_NOTIFICATION + "("
                + PET_ID + " TEXT,"
                + NOTIFICATION_ID + " TEXT,"
                + NOTIFICATION_TITLE + " TEXT,"
                + NOTIFICATION_DESCRIPTION + " TEXT,"
                + NOTIFICATION_CATEGORY + " TEXT,"
                + NOTIFICATION_ROOT_CATEGORY + " TEXT,"
                + NOTIFICATION_ITEM_ID + " TEXT,"
                + NOTIFICATION_IMAGE + " TEXT,"
                + NOTIFICATION_TIME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);


        String CREATE_TABLE_COMPANY = "CREATE TABLE " + TABLE_COMPANY + "("
                + COM_ID + " TEXT,"
                + PET_ID + " TEXT,"
                + COM_NAME + " TEXT,"
                + COM_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_COMPANY);


        String CREATE_TABLE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + "("
                + P_COM_ID + " TEXT,"
                + P_COM_NAME + " TEXT,"
                + BREED_CATEGORIES + " TEXT,"
                + P_IMAGE + " TEXT,"
                + P_IS_ADULT + " TEXT,"
                + P_ID + " TEXT,"
                + P_THRESHOLD + " TEXT,"
                + P_UNIT + " TEXT,"
                + P_NAME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_PRODUCT);


        String CREATE_TABLE_MANUAL_ACTIVITY = "CREATE TABLE " + TABLE_MANUAL_ACTIVITY + "("
                + MA_USER_EMAIL + " TEXT,"
                + MA_PET_ID + " TEXT,"
                + MA_LONG + " TEXT,"
                + MA_HUMAN_DATE + " TEXT,"
                + MA_ACTIVITY_VALUE + " TEXT,"
                + MA_TARGET + " TEXT,"
                + MA_UNIT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_MANUAL_ACTIVITY);

        String CREATE_TABLE_FOOD = "CREATE TABLE " + TABLE_FOOD + "("
                + F_PET_ID + " TEXT,"
                + F_PRODUCT_ID + " TEXT,"
                + F_PRODUCT_NAME + " TEXT,"
                + F_COM_ID + " TEXT,"
                + F_COM_NAME + " TEXT,"
                + F_QUANTITY + " TEXT,"
                + F_UNIT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_FOOD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.e("amit", "database version " + oldVersion);
        Log.e("amit", "database new version " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_COUNT_HOUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANUAL_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }

    //*************************************************************************
    //*********Add Hourly Data for manage Graph in Sensor Section**************
    //*************************************************************************

    public void addHourlyData(StepCount stepCount) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            boolean check = checkHourly(stepCount.getDate(), db, stepCount);
            if (check) {
                updateCountsHourly(stepCount, db);
            } else {
                addCountsHourly(stepCount,db);
            }
            db.close(); // Closing database connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //*************************************************************************
    //*********Check if data exist into table**********************************
    //*************************************************************************
    public boolean checkHourly(String date, SQLiteDatabase db, StepCount stepCount) {

        String selectQuery = "SELECT * FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + CONNECTED_DATE + " ='" + date + "' and " + DEVICE_ID + "='" + stepCount.getDeviceId() + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean check = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    check = true;
                    break;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return check;
    }

    //*************************************************************************
    //*********Update count hourly for every steps ****************************
    //*************************************************************************
    public void updateCountsHourly(StepCount stepCount, SQLiteDatabase db) {
        String update = "UPDATE " + TABLE_STEP_COUNT_HOUR + " SET " + STEP_COUNT + "='" + stepCount.getSteps() + "', " + TOTAL_COUNT + "='" + stepCount.getTotalSteps() + "' WHERE " + CONNECTED_DATE + "='" + stepCount.getDate() + "' and " + DEVICE_ID + "='" + stepCount.getDeviceId() + "'";
        db.execSQL(update);
    }

    //*************************************************************************
    //*********Add every count into table for backup on cloud *****************
    //*************************************************************************
    public void addCountsHourly(StepCount stepCount, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(PET_ID, stepCount.getPetID());
        values.put(USER_EMAIL, stepCount.getUserEmailId());
        values.put(DEVICE_ID, stepCount.getDeviceId());
        values.put(CONNECTED_DATE, stepCount.getDate());
        values.put(IS_SYNCED, stepCount.getIsSynced());
        values.put(STEP_COUNT, stepCount.getSteps());
        values.put(TOTAL_COUNT, stepCount.getTotalSteps());
        db.insert(TABLE_STEP_COUNT_HOUR, null, values);
    }


    //*************************************************************************
    //*********Get hourly progress for shoing into graph **********************
    //*************************************************************************

//    public int getMove(String deviceIds, String date) {
//        ArrayList<StepCount> listStepCnt = getRecordOFDate(deviceIds, date);
//        int totalMove = 0;
//        for (int i = 0; i < listStepCnt.size(); i++) {
//
//            int realCount = Integer.parseInt(listStepCnt.get(i).getTotalSteps());
//            if (realCount == 0) {
//                totalMove = totalMove + Integer.parseInt(listStepCnt.get(i).getSteps());
//            } else {
//                totalMove = realCount + totalMove;
//                break;
//            }
//        }
//        return totalMove;
//    }
//    public String getHourlyForProgress(String deviceIds, String date, boolean isDifferDate) {
//        String value = "0";
//        int totalSteps = 0;
//        try {
//            ArrayList<StepCount> recentLst = new ArrayList<StepCount>();
//            String selectQuery = "SELECT  * FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + DEVICE_ID + " ='" + deviceIds + "' and " + CONNECTED_DATE + " LIKE '" + date + "%' order by " + CONNECTED_DATE;
//            SQLiteDatabase db = this.getWritableDatabase();
//            Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    do {
//
//                        StepCount stepCount = new StepCount();
//
//                        String deviceId = cursor.getString(cursor.getColumnIndex(DEVICE_ID));
//                        String dateVal = cursor.getString(cursor.getColumnIndex(CONNECTED_DATE));
//                        String count = cursor.getString(cursor.getColumnIndex(STEP_COUNT));
//                        String total = cursor.getString(cursor.getColumnIndex(TOTAL_COUNT));
//                        String petID = cursor.getString(cursor.getColumnIndex(PET_ID));
//                        String userMail = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
//                        String isSynced = cursor.getString(cursor.getColumnIndex(IS_SYNCED));
//
//                        totalSteps = totalSteps + getInt(count);
//
//                        stepCount.setDate(dateVal);
//                        stepCount.setDeviceId(deviceId);
//                        stepCount.setSteps(count);
//                        stepCount.setTotalSteps(total);
//                        stepCount.setPetID(petID);
//                        stepCount.setUserEmailId(userMail);
//                        stepCount.setIsSynced(isSynced);
//
//                        recentLst.add(stepCount);
//                    } while (cursor.moveToNext());
//                }
//            }
//            cursor.close();
//            db.close();
//            if (recentLst.size() > 0) {
//                StepCount stepCount = recentLst.get(recentLst.size() - 1);
//                value = stepCount.getTotalSteps();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (isDifferDate)
//            return totalSteps + "";
//        else
//            return value + "";
//    }

    //*************************************************************************
    //*********Get Int of Count ***********************************************
    //*************************************************************************
    public int getInt(String count) {
        int value = 0;
        try {
            value = value + Integer.parseInt(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    //*************************************************************************
    //*********Check if data exist into table**********************************
    //*************************************************************************

    public ArrayList<StepCount> getRecordOFDate(String deviceIds, String date) {
        ArrayList<StepCount> recentLst = new ArrayList<StepCount>();

        String first = date + " 00:00";
        String second = date + " 23:59";

        String selectQuery = "SELECT  * FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + DEVICE_ID + " ='" + deviceIds + "' and " + CONNECTED_DATE + " BETWEEN '" + first + "' AND '" + second + "' ORDER BY " + CONNECTED_DATE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    StepCount stepCount = new StepCount();

                    String deviceId = cursor.getString(cursor.getColumnIndex(DEVICE_ID));
                    String dateVal = cursor.getString(cursor.getColumnIndex(CONNECTED_DATE));
                    String count = cursor.getString(cursor.getColumnIndex(STEP_COUNT));
                    String total = cursor.getString(cursor.getColumnIndex(TOTAL_COUNT));
                    String petID = cursor.getString(cursor.getColumnIndex(PET_ID));
                    String userMail = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                    String isSynced = cursor.getString(cursor.getColumnIndex(IS_SYNCED));

                    stepCount.setDate(dateVal);
                    stepCount.setDeviceId(deviceId);
                    stepCount.setSteps(count);
                    stepCount.setTotalSteps(total);
                    stepCount.setPetID(petID);
                    stepCount.setUserEmailId(userMail);
                    stepCount.setIsSynced(isSynced);

                    recentLst.add(stepCount);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return recentLst;
    }


    public ArrayList<StepCount> getHourly(String deviceIds, String date) {
        ArrayList<StepCount> recentLst = new ArrayList<StepCount>();
        String selectQuery = "SELECT  * FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + DEVICE_ID + " ='" + deviceIds + "' and " + CONNECTED_DATE + " LIKE '" + date + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    StepCount stepCount = new StepCount();

                    String deviceId = cursor.getString(cursor.getColumnIndex(DEVICE_ID));
                    String dateVal = cursor.getString(cursor.getColumnIndex(CONNECTED_DATE));
                    String count = cursor.getString(cursor.getColumnIndex(STEP_COUNT));
                    String total = cursor.getString(cursor.getColumnIndex(TOTAL_COUNT));
                    String petID = cursor.getString(cursor.getColumnIndex(PET_ID));
                    String userMail = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                    String isSynced = cursor.getString(cursor.getColumnIndex(IS_SYNCED));

                    stepCount.setDate(dateVal);
                    stepCount.setDeviceId(deviceId);
                    stepCount.setSteps(count);
                    stepCount.setTotalSteps(total);
                    stepCount.setPetID(petID);
                    stepCount.setUserEmailId(userMail);
                    stepCount.setIsSynced(isSynced);

                    recentLst.add(stepCount);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return recentLst;
    }

    //*************************************************************************
    //*********Check if data exist into table**********************************
    //*************************************************************************
    public StepCount getDailyCountFromHourly(String deviceIds, String date) {
        String selectQuery = "SELECT  * FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + DEVICE_ID + " ='" + deviceIds + "' and " + CONNECTED_DATE + " LIKE '" + date + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        StepCount stepCount = new StepCount();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {


                    String deviceId = cursor.getString(cursor.getColumnIndex(DEVICE_ID));
                    String dateVal = cursor.getString(cursor.getColumnIndex(CONNECTED_DATE));
                    String count = cursor.getString(cursor.getColumnIndex(STEP_COUNT));
                    String total = cursor.getString(cursor.getColumnIndex(TOTAL_COUNT));
                    String petID = cursor.getString(cursor.getColumnIndex(PET_ID));
                    String userMail = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                    String isSynced = cursor.getString(cursor.getColumnIndex(IS_SYNCED));

                    stepCount.setDate(dateVal);
                    stepCount.setDeviceId(deviceId);
                    stepCount.setSteps(count);
                    stepCount.setTotalSteps(total);
                    stepCount.setPetID(petID);
                    stepCount.setUserEmailId(userMail);
                    stepCount.setIsSynced(isSynced);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return stepCount;
    }

    //*************************************************************************
    //*********UnSyncFlag******************************************************
    //*************************************************************************
    public void updateUnSyncFlag(String deviceIds, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE " + TABLE_STEP_COUNT_HOUR + " SET " + IS_SYNCED + "='yes' WHERE " + IS_SYNCED + "='no'";
        db.execSQL(update);
        db.close();
    }
    //*************************************************************************
    //*********UnSyncFlag******************************************************
    //*************************************************************************
    public void deleteUnSyncFlag(String deviceIds, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "DELETE FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + DEVICE_ID + " ='" + deviceIds + "' LIMIT 100";
        db.execSQL(selectQuery);
        db.close();
    }

    //*************************************************************************
    //*********Check if data exist into table**********************************
    //*************************************************************************
    public ArrayList<StepCount> getUnSyncedData(String deviceIds, String date) {

        String selectQuery = "SELECT  * FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + DEVICE_ID + " ='" + deviceIds + "'";

    //    String selectQuery = "SELECT  * FROM " + TABLE_STEP_COUNT_HOUR + " WHERE " + IS_SYNCED + " ='no'";
        //Log.e("amit", "" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<StepCount> stepCountLst = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    StepCount stepCount = new StepCount();
                    String deviceId = cursor.getString(cursor.getColumnIndex(DEVICE_ID));
                    String dateVal = cursor.getString(cursor.getColumnIndex(CONNECTED_DATE));
                    String count = cursor.getString(cursor.getColumnIndex(STEP_COUNT));
                    String total = cursor.getString(cursor.getColumnIndex(TOTAL_COUNT));
                    String petID = cursor.getString(cursor.getColumnIndex(PET_ID));
                    String userMail = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                    String isSynced = cursor.getString(cursor.getColumnIndex(IS_SYNCED));

                    stepCount.setDate(dateVal);
                    stepCount.setDeviceId(deviceId);
                    stepCount.setSteps(count);
                    stepCount.setTotalSteps(total);
                    stepCount.setPetID(petID);
                    stepCount.setUserEmailId(userMail);
                    stepCount.setIsSynced(isSynced);

                    stepCountLst.add(stepCount);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return stepCountLst;
    }


//    For notifications


    public void notify(String petId, NotificationsDTO notificationsDTO) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (checkNotificationsData(petId, notificationsDTO, db)) {
            updateNotificationsData(petId, notificationsDTO, db);
        } else {
            addNotifications(notificationsDTO, db);
        }
        db.close();
    }

    public boolean checkNotificationsData(String petId, NotificationsDTO notificationsDTO, SQLiteDatabase db) {

        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION + " WHERE " + NOTIFICATION_ID + " ='" + notificationsDTO.getId() + "' AND " + PET_ID + "='" + petId + "'";
        Log.e("<-- selectQuery", "" + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor count", "" + cursor.getCount());
        boolean check = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    check = true;
                    break;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return check;
    }

    public void updateNotificationsData(String petId, NotificationsDTO notificationsDTO, SQLiteDatabase db) {

        String update = "UPDATE " + TABLE_NOTIFICATION + " SET "
                + NOTIFICATION_TITLE + "='" + notificationsDTO.getTitle() + "',"
                + NOTIFICATION_DESCRIPTION + "='" + notificationsDTO.getDescription() + "',"
                + NOTIFICATION_TIME + "='" + notificationsDTO.getImage() + "'"
                + "WHERE " + NOTIFICATION_ID + "='" + notificationsDTO.getId() + "' AND " + PET_ID + "='" + petId + "'";
        Log.e("<-- selectQuery", "" + update);
        db.execSQL(update);
    }

    public void addNotifications(NotificationsDTO notificationsDTO, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(PET_ID, notificationsDTO.getPetId());
        values.put(NOTIFICATION_ID, notificationsDTO.getId());
        values.put(NOTIFICATION_TITLE, notificationsDTO.getTitle());
        values.put(NOTIFICATION_DESCRIPTION, notificationsDTO.getDescription());
        values.put(NOTIFICATION_TIME, notificationsDTO.getTime());
        values.put(NOTIFICATION_CATEGORY, notificationsDTO.getCategory());
        values.put(NOTIFICATION_ROOT_CATEGORY, notificationsDTO.getRootCategory());
        values.put(NOTIFICATION_ITEM_ID, notificationsDTO.getItemId());
        values.put(NOTIFICATION_IMAGE, notificationsDTO.getImage());

        db.insert(TABLE_NOTIFICATION, null, values);
    }


    public void deleteNotification(String notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_NOTIFICATION + " WHERE " + NOTIFICATION_ID + "='" + notificationId + "'";
        db.execSQL(delete);
        db.close();
    }


    //========================================================================
    public ArrayList<NotificationsDTO> getNotificationsData() {
        ArrayList<NotificationsDTO> recentLst = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    NotificationsDTO notificationsDTO = new NotificationsDTO();

                    notificationsDTO.setId(cursor.getString(cursor.getColumnIndex(NOTIFICATION_ID)));
                    notificationsDTO.setTitle(cursor.getString(cursor.getColumnIndex(NOTIFICATION_TITLE)));
                    notificationsDTO.setDescription(cursor.getString(cursor.getColumnIndex(NOTIFICATION_DESCRIPTION)));
                    notificationsDTO.setTime(cursor.getString(cursor.getColumnIndex(NOTIFICATION_TIME)));
                    notificationsDTO.setCategory(cursor.getString(cursor.getColumnIndex(NOTIFICATION_CATEGORY)));
                    notificationsDTO.setPetId(cursor.getString(cursor.getColumnIndex(PET_ID)));
                    notificationsDTO.setItemId(cursor.getString(cursor.getColumnIndex(NOTIFICATION_ITEM_ID)));
                    notificationsDTO.setImage(cursor.getString(cursor.getColumnIndex(NOTIFICATION_IMAGE)));
                    notificationsDTO.setRootCategory(cursor.getString(cursor.getColumnIndex(NOTIFICATION_ROOT_CATEGORY)));

                    // Adding contact to list
                    recentLst.add(notificationsDTO);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return recentLst;
    }


    public String getPrpperString(String val) {
        //String folder = "ArslanFolder 20/01/2013 ? / '";
        String result = val.replaceAll("[|?*<\":>+\\[\\]/']", "");
        return result;
    }

    public void deleteAllProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCT);
        db.close();
    }

    //==========Add Manual Activity=======
    //===================================
    //===================================
    public void addManualActivity(ManualActivity manualActivity) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            boolean bool = checkManualActivity(db, manualActivity);
            if (bool) {
                updateManualActivity(manualActivity, db);
            } else {
                addManual(manualActivity, db);
            }
            db.close(); // Closing database connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkManualActivity(SQLiteDatabase db, ManualActivity manualActivity) {

        String selectQuery = "SELECT * FROM " + TABLE_MANUAL_ACTIVITY + " WHERE " + MA_PET_ID + " ='" + manualActivity.getPetId() + "' AND " + MA_HUMAN_DATE + " ='" + manualActivity.getHumanDate() + "'";
        //   Log.e("<--activity detail ", "" + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean check = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    check = true;
                    break;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return check;
    }

    public void updateManualActivity(ManualActivity manualActivity, SQLiteDatabase db) {
        String update = "UPDATE " + TABLE_MANUAL_ACTIVITY + " SET " + MA_PET_ID + "='" + manualActivity.getPetId() + "'" +
                "," + MA_HUMAN_DATE + "='" + manualActivity.getHumanDate() + "'" +
                "," + MA_ACTIVITY_VALUE + "='" + manualActivity.getActivityValue() + "'" +
                "," + MA_LONG + "='" + manualActivity.getLongDate() + "'" +
                "," + MA_TARGET + "='" + manualActivity.getTarget() + "'" +
                "," + MA_UNIT + "='" + manualActivity.getUnit() + "'" +
                "," + MA_USER_EMAIL + "='" + manualActivity.getUserEmail() + "' WHERE " + MA_PET_ID + " ='" + manualActivity.getPetId() + "' AND " + MA_HUMAN_DATE + " ='" + manualActivity.getHumanDate() + "'";
        // Log.e("<-- update product", "" + update);
        db.execSQL(update);
    }

    public void addManual(ManualActivity manualActivity, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(MA_PET_ID, manualActivity.getPetId());
        values.put(MA_HUMAN_DATE, manualActivity.getHumanDate());
        values.put(MA_ACTIVITY_VALUE, manualActivity.getActivityValue());
        values.put(MA_LONG, manualActivity.getLongDate());
        values.put(MA_TARGET, manualActivity.getTarget());
        values.put(MA_UNIT, manualActivity.getUnit());
        values.put(MA_USER_EMAIL, manualActivity.getUserEmail());
        db.insert(TABLE_MANUAL_ACTIVITY, null, values);

    }

    public ArrayList<ManualActivity> getManualActivity(String petId, String currentDate) {
        ArrayList<ManualActivity> recentLst = new ArrayList<ManualActivity>();
        String selectQuery = "SELECT  * FROM " + TABLE_MANUAL_ACTIVITY + " WHERE " + MA_PET_ID + "='" + petId + "' AND " + MA_HUMAN_DATE + "='" + currentDate + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ManualActivity manualActivity = new ManualActivity();

                    manualActivity.setPetId(cursor.getString(cursor.getColumnIndex(MA_PET_ID)));
                    manualActivity.setHumanDate(cursor.getString(cursor.getColumnIndex(MA_HUMAN_DATE)));
                    manualActivity.setActivityValue(cursor.getString(cursor.getColumnIndex(MA_ACTIVITY_VALUE)));
                    manualActivity.setLongDate(cursor.getString(cursor.getColumnIndex(MA_LONG)));
                    manualActivity.setTarget(cursor.getString(cursor.getColumnIndex(MA_TARGET)));
                    manualActivity.setUnit(cursor.getString(cursor.getColumnIndex(MA_UNIT)));
                    manualActivity.setUserEmail(cursor.getString(cursor.getColumnIndex(MA_USER_EMAIL)));

                    recentLst.add(manualActivity);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return recentLst;
    }


    //==========Add Food Activity=======
    //===================================
    //===================================
    public void addFood(FoodPlan foodPlan) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            boolean bool = checkFood(db, foodPlan);
            if (bool) {
                updateFood(foodPlan, db);
            } else {
                insertFood(foodPlan, db);
            }
            db.close(); // Closing database connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkFood(SQLiteDatabase db, FoodPlan foodPlan) {

        String selectQuery = "SELECT * FROM " + TABLE_FOOD + " WHERE " + F_PET_ID + " ='"
                + foodPlan.getPetId() + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean check = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    check = true;
                    break;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return check;
    }

    public void updateFood(FoodPlan foodPlan, SQLiteDatabase db) {
        String update = "UPDATE " + TABLE_FOOD + " SET " + F_PET_ID + "='" + foodPlan.getPetId() + "'" +
                "," + F_PRODUCT_ID + "='" + foodPlan.getProductId() + "'" +
                "," + F_PRODUCT_NAME + "='" + foodPlan.getProductName() + "'" +
                "," + F_COM_ID + "='" + foodPlan.getComId() + "'" +
                "," + F_COM_NAME + "='" + foodPlan.getComName() + "'" +
                "," + F_QUANTITY + "='" + foodPlan.getQuantity() + "'" +
                "," + F_UNIT + "='" + foodPlan.getUnit() + "' WHERE " + F_PET_ID + " ='" + foodPlan.getPetId() + "'";
        db.execSQL(update);
    }

    public void insertFood(FoodPlan foodPlan, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(F_PET_ID, foodPlan.getPetId());
        values.put(F_PRODUCT_ID, foodPlan.getProductId());
        values.put(F_PRODUCT_NAME, foodPlan.getProductName());
        values.put(F_COM_ID, foodPlan.getComId());
        values.put(F_COM_NAME, foodPlan.getComName());
        values.put(F_QUANTITY, foodPlan.getQuantity());
        values.put(F_UNIT, foodPlan.getUnit());
        db.insert(TABLE_FOOD, null, values);

    }


    public FoodPlan getFood(String petId) {
        String selectQuery = "SELECT  * FROM " + TABLE_FOOD + " WHERE " + F_PET_ID + "='" + petId + "'";
        FoodPlan foodPlan = new FoodPlan();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {


                    foodPlan.setPetId(cursor.getString(cursor.getColumnIndex(F_PET_ID)));
                    foodPlan.setProductId(cursor.getString(cursor.getColumnIndex(F_PRODUCT_ID)));
                    foodPlan.setProductName(cursor.getString(cursor.getColumnIndex(F_PRODUCT_NAME)));
                    foodPlan.setComId(cursor.getString(cursor.getColumnIndex(F_COM_ID)));
                    foodPlan.setComName(cursor.getString(cursor.getColumnIndex(F_COM_NAME)));
                    foodPlan.setUnit(cursor.getString(cursor.getColumnIndex(F_UNIT)));
                    foodPlan.setQuantity(cursor.getString(cursor.getColumnIndex(F_QUANTITY)));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return foodPlan;
    }

}