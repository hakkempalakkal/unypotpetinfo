package com.samyotech.petstand;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.ly.ble_library.BleService;
import com.ly.ble_library.Constant;
import com.ly.ble_library.GattServices;
import com.samyotech.petstand.models.BreedDTO;
import com.samyotech.petstand.models.ReminderAdvance;
import com.samyotech.petstand.models.ReminderRepeat;
import com.samyotech.petstand.models.User;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SysApplication extends Application {
    private static Context context;
    public static final String TAG = "SysApplication";
    private static SysApplication instance;
    public static BleService mBluetoothLeService = null;
    public static GattServices gattServices;
    private BreedDTO breedDTO = null;
    private String imagePath;


    private String fileName;
    private User user;
    private int currentIndex = 0;
    int weight =0 , height = 0;
    public HashMap<String, Integer> reminderMapping = new HashMap<>();

    public static SysApplication getInstance() {
        return instance;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }




    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            try {
                mBluetoothLeService = ((BleService.LocalBinder) service)
                        .getService();
                Constant.bleService = ((BleService.LocalBinder) service)
                        .getService();
                // private JSONObject object,object1,jsonobject;

                gattServices = new GattServices(mBluetoothLeService);
                Constant.mgattServices = gattServices;
                Log.e("ly", "onServiceConnected");
                if (!mBluetoothLeService.initialize()) {
                    Log.e("", "Unable to initialize Bluetooth");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // mBluetoothLeService = null;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        SysApplication.context = getApplicationContext();
        Constant.appcontext = getApplicationContext();
        Intent gattServiceIntent = new Intent(this, BleService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        if (instance == null) {
            instance = this;
        }
        addReminderCateImg();
    }





    public List<ReminderAdvance> getRemindList() {
        List<ReminderAdvance> reminderAdvances = new ArrayList<>();
        reminderAdvances.add(new ReminderAdvance("No", 0));
        reminderAdvances.add(new ReminderAdvance("30 mins", 30));
        reminderAdvances.add(new ReminderAdvance("1 hour in advance", 60));
        reminderAdvances.add(new ReminderAdvance("2 hour in advance", 120));
        reminderAdvances.add(new ReminderAdvance("1 day in advance", 1440));
        reminderAdvances.add(new ReminderAdvance("2 day in advance", 2880));
        reminderAdvances.add(new ReminderAdvance("3 day in advance", 4320));
        reminderAdvances.add(new ReminderAdvance("4 day in advance", 5760));
        reminderAdvances.add(new ReminderAdvance("5 day in advance", 7200));
        reminderAdvances.add(new ReminderAdvance("1 week in advance", 10080));
        return reminderAdvances;
    }

    public List<ReminderRepeat> getRepeatList() {
        List<ReminderRepeat> reminderRepeats = new ArrayList<>();
        reminderRepeats.add(new ReminderRepeat("None", 0));
        reminderRepeats.add(new ReminderRepeat("1days", 1));
        reminderRepeats.add(new ReminderRepeat("2days", 2));
        reminderRepeats.add(new ReminderRepeat("3days", 3));
        reminderRepeats.add(new ReminderRepeat("4days", 4));
        reminderRepeats.add(new ReminderRepeat("5days", 5));
        reminderRepeats.add(new ReminderRepeat("6days", 6));
        reminderRepeats.add(new ReminderRepeat("7days", 7));
        reminderRepeats.add(new ReminderRepeat("8days", 8));
        reminderRepeats.add(new ReminderRepeat("9days", 9));
        return reminderRepeats;
    }

    public void addReminderCateImg() {
        reminderMapping.put("Fun Walk", R.drawable.walk_icon);
        reminderMapping.put("Brush Teeth", R.drawable.tooth_icon);
        reminderMapping.put("Clean Ears", R.drawable.ears_icon);
        reminderMapping.put("Take Bath", R.drawable.bath_icon);
        reminderMapping.put("Clip Nails", R.drawable.clipper_icon);
        reminderMapping.put("Vaccination Appointment", R.drawable.shield_con);
        reminderMapping.put("Worm Care", R.drawable.worm_icon);
    }
}