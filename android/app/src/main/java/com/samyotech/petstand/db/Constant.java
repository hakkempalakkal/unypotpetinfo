package com.samyotech.petstand.db;

import android.app.Activity;

import java.io.File;
import java.util.ArrayList;


public class Constant {
	/** 跳转时的标识 */
	public static final String SENDDATA = "send_data";
	public static final String SHARED_PREF_FILE_NAME = "vivitar";
	public static final String USER_ID = "userId";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	/** 服务器返回值判断 */
	public static final String RESULT_STATUS = "status";// 1成功2失败
	public static final String RESULT_DATA = "bodys";// 1成功2失败
	public static final boolean LOG_IS_DISPLAY = true;
	public static final String REGISTER_EMAIL = "email";
	
	public static ArrayList<Activity> activityList = new ArrayList<Activity>();

	
	public static final long LOGO_TIME = 2000;//logo显示时间
	/** 超时时间 */
	public static final int TIMEOUT = 20 * 1000;
	/** 推出时间 */
	public static final int EXITTIMESPAN = 3000;
	
	public static String IMAGE_PATH =android.os.Environment.getExternalStorageDirectory().getPath() + File.separator
			+ "vivitar";
	public static int IMAGE_USER_WIDTH =50;// 头像高度
	public static String IMAGE_PATH_USER =IMAGE_PATH + File.separator+"user_image1.jpg";
	public static final String DATABASE_NAME = "Marktest.db";
	public static final int DATABASE_VERSION = 1;
}
