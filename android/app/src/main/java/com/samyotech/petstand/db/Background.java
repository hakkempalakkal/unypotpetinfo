package com.samyotech.petstand.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ly.ble_library.Array;
import com.ly.ble_library.StepData;
import com.samyotech.petstand.models.BaseDTO;
import com.samyotech.petstand.models.StepCount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Background extends AsyncTask<String, Void, Boolean> {
    String mDeviceAddress;
    Context ctx;
    BaseDTO baseDTO;
    DatabaseHandler db;
    List<StepData> dataLst;
    String petId;
    String emailId;
    SimpleDateFormat df;

    public Background(String mDeviceAddress, Context ctx, String petId, String emailId) {
        this.mDeviceAddress = mDeviceAddress;
        this.ctx = ctx;
        db = new DatabaseHandler(ctx);
        //  dataLst = Array.liststep;
        dataLst = new ArrayList<StepData>(Array.liststep);
        Array.liststep.clear();
        this.petId = petId;
        this.emailId = emailId;
        df = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... urlsServer) {

        try {

            Log.e("amit", "background data count " + dataLst.size());
            for (int i = 0; i < dataLst.size(); i++)
            {
                if (dataLst.get(i).getStepdata() > 0)
                {

                    StepCount stepCount = new StepCount();
                    stepCount.setDeviceId(mDeviceAddress);
                    stepCount.setDate(dataLst.get(i).getSteptime());
                    stepCount.setSteps("" + dataLst.get(i).getStepdata());
                    stepCount.setTotalSteps("0");
                    stepCount.setIsSynced("no");
                    stepCount.setUserEmailId(emailId);
                    stepCount.setPetID(petId);
                    db.addHourlyData(stepCount);
                    Log.e("PoochPlay", "Step : " + dataLst.get(i).getStepdata() + " - " + "Calorie:" + dataLst.get(i).getCaloriedata()
                            + " - " + "Time?" + dataLst.get(i).getSteptime());
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public int getTotalCountVal(String date) {
//        int count = 0;
//        Calendar cal = Calendar.getInstance();
//        String currentDate = df.format(cal.getTime());
//        try {
//            String dateArry[] = date.split(" ");
//
//            if (dateArry[0].equals(currentDate)) {
//                count = Array.RtCtrlData.totalSteps;
//            } else {
//                count = 0;
//            }
//            return count;
//        } catch (Exception e) {
//            return count;
//        }
//    }


    @Override
    protected void onPostExecute(Boolean result) {
//        if (delegate != null && (match.equals(Consts.ADD_PET) || match.equals(Consts.UPDATE_PET))) {
//            this.delegate.processFinish(result, readableMsg, addPetDTO);
//        } else if (delegate != null && (match.equals(Consts.ADD_TO_GALLERY))) {
//            this.delegate.processFinish(result, readableMsg, galleryDTO);
//        } else if (delegate != null && (match.equals(Consts.UPDATE_USER_PROFILE))) {
//            this.delegate.processFinish(result, readableMsg);
//        } else if (delegate != null && (match.equals(Consts.ADD_INSURANCE))) {
//            this.delegate.processFinish(result, readableMsg, insurance);
//        } else if (delegate != null && (match.equals(Consts.ADD_VACCINATION_RECORD))) {
//            this.delegate.processFinish(result, readableMsg, baseDTO);
//        }

    }
}