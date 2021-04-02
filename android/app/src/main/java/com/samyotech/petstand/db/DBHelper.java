/**
 * 工程名: Susee
 * 文件名: DBHelper.java
 * 包名: com.susee.db
 * 日期: 2015-5-4上午9:13:26
 * Copyright (c) 2015,深圳市奇沃智联有限公司 All Rights Reserved.
 */
 
package com.samyotech.petstand.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * 类名: DBHelper <br/>
 * 功能: TODO 一句话描述这个类的作用. <br/>
 * 日期: 2015-5-4-上午9:13:26 <br/>
 * 
 * @author zhanghang
 * @version 1.1
 */
public class DBHelper extends CommonDB {

	private static DBHelper instance;
	public static DBHelper getInstance(Context mContext) {
		if (null == instance) {
			instance = new DBHelper(mContext);
		}
		return instance;
	}
	/** 皮肤数据表 */
	public final static String TB_JUMP = "sleepdata";
	public final static String TB_JUMP1 = "sleepdaydata";
	public DBHelper(Context context) {
		super(context, Constant.DATABASE_NAME,Constant.DATABASE_VERSION);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Skin.newCreateTableString());
		db.execSQL(Skin.newCreateTableString1());
	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            default:// 其他版本，直接删除
            	db.execSQL(Skin.newDeleteTableString());
                break;
            case 12:
            	onCreate(db);// 创建所有表
                break;
        }
//        onCreate(db);// 创建所有表
    }
	/**
	 * 类名: Member <br/>
	 * 功能: TODO 一句话描述这个类的作用. <br/>
	 * 日期: 2015-5-4-上午9:15:37 <br/>
	 * 
	 * @author zhanghang
	 * @version 1.1
	 */
	public static class Skin{
		/** id */
		public final static String SKIN_ID = "memberId";
		/** 脸部部位 */
		public final static String SKIN_TYPE = "type";
		/** 时间 */
		public final static String SKIN_TIME = "time";
		/** 水分 */
		public final static String SKIN_WATER = "water";
		/** 油分 */
		public final static String SKIN_OIL = "oil";
		/**  */
		public final static String SKIN_OTHER = "other";
		
		public static String newCreateTableString() {
			StringBuffer buffer = new StringBuffer(512);
			buffer.append("create table ")
				.append(TB_JUMP).append(" (")
			      .append("id").append(" ").append("integer primary key autoincrement").append(",")
		          .append("movecounts").append(" ").append("integer").append(",")
		          .append("isupdata").append(" ").append("integer").append(",")
		          .append("marktime").append(" ").append("varchar").append(",")
		          .append("createTime").append(" ").append("integer").append(")");
			
		            return buffer.toString();
		}
		
		public static String newCreateTableString1() {
			StringBuffer buffer = new StringBuffer(512);
			buffer.append("create table ")
				.append(TB_JUMP1).append(" (")
			      .append("id").append(" ").append("integer primary key autoincrement").append(",")
		          .append("starttime").append(" ").append("varchar").append(",")
		          .append("endtime").append(" ").append("integer").append(",")
		          .append("totaltime").append(" ").append("integer").append(",")
		          .append("deeptime").append(" ").append("integer").append(",")
		          .append("shallowtime").append(" ").append("integer").append(",")
		          .append("sobertime").append(" ").append("integer").append(",")
		          .append("sleepquality").append(" ").append("integer").append(",")
		          .append("marktime").append(" ").append("integer").append(",")
		          .append("record").append(" ").append("varchar").append(")");
			
		            return buffer.toString();
		}
		
	
        
        
             
         
		
		
		
		
		
		public static String newDeleteTableString() {
			StringBuffer buffer = new StringBuffer(64);
			buffer.append("DROP TABLE IF EXISTS ")
				.append(TB_JUMP);
			return buffer.toString();
		}
	}
	
	
	
	
	
}