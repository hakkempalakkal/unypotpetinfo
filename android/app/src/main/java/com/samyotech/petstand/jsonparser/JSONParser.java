package com.samyotech.petstand.jsonparser;

import android.content.Context;
import android.content.Intent;


import com.samyotech.petstand.activity.register.LoginSignupactivity;
import com.samyotech.petstand.db.DatabaseHandler;
import com.samyotech.petstand.models.FoodPlan;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;


public class JSONParser {
    String jsonObjResponse;
    public String ERROR = "";
    public String MESSAGE = "";
    public boolean RESULT = false;
    public Context context;
    public static String PIC_KEY = "profilepic";
    private SharedPrefrence sharedPrefrence;
    public JSONObject jObj;
    DatabaseHandler db;


    public static String TAG_ERROR = "status";
    public static String TAG_MESSAGE = "message";


    public JSONParser(Context context, String response) {
        this.context = context;
        this.jsonObjResponse = response;
        sharedPrefrence = SharedPrefrence.getInstance(context);
        db = new DatabaseHandler(context);
        try {
            jObj = new JSONObject(response);
            ERROR = getJsonString(jObj, TAG_ERROR);

            MESSAGE = html2text(getJsonString(jObj, TAG_MESSAGE));
            if (ERROR.equals("true"))
                RESULT = false;
            else
                RESULT = true;

        } catch (JSONException e) {
            jObj = null;
            e.printStackTrace();
        }
    }

    public JSONParser(Context context, JSONObject response) {
        try {
            this.context = context;
            jObj = response;
            ERROR = getJsonString(jObj, TAG_ERROR);
            MESSAGE = html2text(getJsonString(jObj, TAG_MESSAGE));
            sharedPrefrence = SharedPrefrence.getInstance(context);
            if (ERROR.equals("0")) {
                RESULT = false;
            } else if (ERROR.equals("1")) {
                RESULT = true;
            } else if (ERROR.equals("3")) {
                sharedPrefrence.clearPreferences(Consts.LOGINDTO);
                sharedPrefrence.clearPreferences(SharedPrefrence.IS_LOGIN);
                Intent intent = new Intent(context, LoginSignupactivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

        } catch (Exception e) {
            jObj = null;
            e.printStackTrace();
        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean getBoolean(String val) {
        if (val.equals("true"))
            return true;
        else
            return false;
    }

    public static JSONObject getJsonObject(JSONObject obj, String parameter) {
        try {
            return obj.getJSONObject(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

    }

    public static String getJsonString(JSONObject obj, String parameter) {
        try {
            String val = obj.getString(parameter);
            if (val != null) {
                if (val.equalsIgnoreCase("null") || val.equalsIgnoreCase(""))
                    return "";
                else
                    return val;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static JSONArray getJsonArray(JSONObject obj, String parameter) {
        try {
            return obj.getJSONArray(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }

    }

    public void getCalculatedThreshold() {
        try {
            JSONObject ins = getJsonObject(jObj, "recommend_food");

            String threshold = getJsonString(ins, "threshold");
            String petid = getJsonString(ins, "petid");

            FoodPlan foodPlan = db.getFood(petid);
            foodPlan.setQuantity(threshold);
            db.addFood(foodPlan);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}