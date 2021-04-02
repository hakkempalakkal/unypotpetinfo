package com.samyotech.petstand.db;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.samyotech.petstand.models.RealCount;
import com.samyotech.petstand.models.StepCount;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.DialogUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.List;

/**
 * Created by welcome pc on 07-01-2018.
 */

public class CopyDataIntoDB extends AsyncTask<String, Void, Boolean> {
    String mDeviceAddress;
    Context ctx;
    DatabaseHandler db;
    String petId;
    String emailId;

    public CopyDataIntoDB(String mDeviceAddress, Context ctx, String petId, String emailId) {
        this.mDeviceAddress = mDeviceAddress;
        this.ctx = ctx;
        db = new DatabaseHandler(ctx);
        this.petId = petId;
        this.emailId = emailId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... urlsServer) {

        try {

            List<RealCount> lst = readFileData();
//            for (int i = 0; i < lst.size(); i++) {
//
//                StepCount stepCount = new StepCount();
//                stepCount.setDate(lst.get(i).getSteptime());
//                stepCount.setDeviceId(mDeviceAddress);
//                stepCount.setPetID(petId);
//                stepCount.setUserEmailId(emailId);
//                stepCount.setIsSynced("no");
//                stepCount.setSteps(lst.get(i).getStepdata()+"");
//                stepCount.setTotalSteps("0");
//
//
//                db.addHourlyData(stepCount);
//            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //ArrayList<getterSetterclass> list =  read(context);
    public List<RealCount> readFileData() {

        ObjectInputStream input = null;
        List<RealCount> ReturnClass = null;

        File directory = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + Consts.POOCH_PLAY + File.separator + Consts.POOCH_PLAY_TRACKER_DATA);
        try {

            File childFile[] = directory.listFiles();
            for (int i = 0; i < childFile.length; i++) {

                String filePat = directory
                        + File.separator + childFile[i].getName();
                input = new ObjectInputStream(new FileInputStream(filePat));
                ReturnClass = (List<RealCount>) input.readObject();

                DialogUtility.showLOG("================Open ===============================");
                DialogUtility.showLOG("===List number : " + i);
                Log.i("data", "===total count：" + ReturnClass.size());
                input.close();
                // List<RealCount> lst = readFileData();
                for (int ij = 0; ij < ReturnClass.size(); ij++) {

                    StepCount stepCount = new StepCount();
                    stepCount.setDate(ReturnClass.get(ij).getSteptime());
                    stepCount.setDeviceId(mDeviceAddress);
                    stepCount.setPetID(petId);
                    stepCount.setUserEmailId(emailId);
                    stepCount.setIsSynced("no");
                    stepCount.setSteps(ReturnClass.get(ij).getStepdata() + "");
                    stepCount.setTotalSteps("0");


                    db.addHourlyData(stepCount);
                }
                File file = new File(filePat);
                boolean deleted = file.delete();
                DialogUtility.showLOG("==== file path ==="+filePat);
                DialogUtility.showLOG("==== close and delete ==="+deleted);
            }

        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ReturnClass;
    }


    @Override
    protected void onPostExecute(Boolean result) {
    }
}