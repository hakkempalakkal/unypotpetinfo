package com.samyotech.petstand.https;


import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.samyotech.petstand.SysApplication;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.models.BaseDTO;
import com.samyotech.petstand.models.GalleryDTO;
import com.samyotech.petstand.utils.Consts;

import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UploadFileToServer extends AsyncTask<String, Void, Boolean> {
    String match;
    ContentValues contentValuesForParam;
    ContentValues contentValuesForByte;
    Context ctx;
    String readableMsg = "";
    ArrayList<String> urls = new ArrayList<String>();
    HttpClient client;
    GalleryDTO galleryDTO;
    BaseDTO baseDTO;
    SysApplication sysApplication;
    String response = null;
    JSONParser jsonParser;

    public UploadFileToServer(String match, ContentValues contentValuesForParam, ContentValues contentValuesForByte, Context ctx) {
        this.match = match;
        this.contentValuesForParam = contentValuesForParam;
        this.ctx = ctx;
        this.contentValuesForByte = contentValuesForByte;
        sysApplication = SysApplication.getInstance();
    }

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(boolean output, String message, JSONObject response);
        void processFinish(boolean output, String message);
    }

    public AsyncResponse delegate = null;

    public void setOnTaskFinishedEvent(AsyncResponse delegate) {
        if (delegate != null) {
            this.delegate = delegate;
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... urlsServer) {

        try {

            client = new HttpClient(Consts.BASE_URL + match);
            client.connectForMultipart();
            addParam();
            addByte();

            client.finishMultipart();
            response = client.getResponse();

            jsonParser = new JSONParser(ctx, response);
            if (jsonParser != null)
            {
                if (match.equals(Consts.ADD_PET) || match.equals(Consts.UPDATE_PET)) {
                    readableMsg = jsonParser.MESSAGE;
                    return jsonParser.RESULT;
                }

                else if (match.equals(Consts.UPDATE_USER_PROFILE)) {
                    readableMsg = jsonParser.MESSAGE;
//                    UpdateProfile updateProfile = new Gson().fromJson(response.toString(), UpdateProfile.class);
//                    sysApplication.getUser().setUserDetail(updateProfile.getUserdetail());
//                    DialogUtility.showLOG("hahaha");
//                  //  jsonParser.userUpdate();
                    return jsonParser.RESULT;
                }



                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    protected void onPostExecute(Boolean result)
    {
        if (delegate != null && (match.equals(Consts.ADD_PET) || match.equals(Consts.UPDATE_PET))) {
            this.delegate.processFinish(result, readableMsg, jsonParser.jObj);
        }
        else if (delegate != null && (match.equals(Consts.UPDATE_USER_PROFILE))) {
            this.delegate.processFinish(result, readableMsg, jsonParser.jObj);
        }
//        else if (delegate != null && (match.equals(Consts.ADD_VACCINATION_RECORD))) {
//            this.delegate.processFinish(result, readableMsg, baseDTO);
//        }
    }

    private void addParam() throws UnsupportedEncodingException {
        try {
            Set<Map.Entry<String, Object>> s = contentValuesForParam.valueSet();
            Iterator itr = s.iterator();
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next();
                String key = me.getKey().toString();
                String value = (String) me.getValue();

                // client.addFormPart(URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
                client.addFormPart(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addByte() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            Set<Map.Entry<String, Object>> s = contentValuesForByte.valueSet();
            Iterator itr = s.iterator();
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next();
                String key = me.getKey().toString();
                byte[] value = (byte[]) me.getValue();
                client.addFilePart(key, "fileName.png", value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}