
/**
 * 工程名: Susee
 * 文件名: CommentDBHelper.java
 * 包名: com.susee.db
 * 日期: 2015-5-15下午10:51:29
 * Copyright (c) 2015,深圳市奇沃智联有限公司 All Rights Reserved.
 */
 
package com.samyotech.petstand.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ly.ble_library.SleepData;


/**
 * 类名: CommentDBHelper 
 * 功能: 用户操作. 
 * 日期: 2015-5-15-下午10:51:29 
 * 
 * @author zhanghang
 * @version 1.1
 */

public class CommentDBHelper {

	private static CommentDBHelper instance;
	public static CommentDBHelper getInstance(Context mContext) {
		if (null == instance) {
			instance = new CommentDBHelper(mContext);
		}
		return instance;
	}
	
	private DBHelper dbbase = null;
	private static SQLiteDatabase db;
	
    public CommentDBHelper(Context context){
    	dbbase = DBHelper.getInstance(context);
    }
    
    /**
	 * Open DB
	 */
	public CommentDBHelper openWrite() throws SQLException {
		if (db != null) db.close();
		db = dbbase.getWritableDatabase();
		return this;
	}

	public CommentDBHelper openRead() throws SQLException {
		if (db != null) 
			db.close();
		db = dbbase.getReadableDatabase();
		return this;
	}
	
	/**
	 * 支持事务,启动一个事物
	 */
	public void setTransactionBegin() {
		if(db != null)
			db.beginTransaction();
	}
	/**
	 * 结束事务
	 */
	public void setTransactionSucess() {
		db.setTransactionSuccessful();
		if(db != null && db.inTransaction())
			db.endTransaction();
	}
	/**
	 * 回滚事务
	 */
	public void setTransactionFailure() {
		if(db != null && db.inTransaction())
			db.endTransaction();
	}

	/**
	 * close DB
	 */
	public void close() {
		if  (db!=null) {
			db.close();
		}
		if  (dbbase!=null) 
			dbbase.close();
	}
	public void deleteDBAll() {
		dbbase.deleteAll(DBHelper.TB_JUMP);
	}
	
	/**
	 * getComments:(获取肌肤数据) 
	 * (根据时间戳获取数据) 
	 * @param lvId
	 * @return
	 * @return ArrayList<DBHelper.Skin>
	 * @author zhanghang
	 * @since 1.1
	 */
//	public ArrayList<JumpEntity> getSkinListData(int min,int max)
// {
//		ArrayList<JumpEntity> jumpList = null;
//		StringBuilder sql = new StringBuilder("select *");
//		sql.append(" from " + DBHelper.TB_JUMP);
//		if(min>0&&max>0){
//			sql.append(" where  " + "biaoshi" + ">=" + min+" and "+"biaoshi"+"<"+max);
//		}
//
//
////		sql.append(" order by ");
//		LogUtils.e(sql.toString());
//		if (db != null) {
//			Cursor cur = db.rawQuery(sql.toString(), null);
//			sql = null;
//			if (null == cur || cur.getCount() <1) {
//				if (cur != null)
//					cur.close();
//
//				return jumpList;
//			}
//			int jump_id = cur.getColumnIndex("id");
//			int jump_counts = cur.getColumnIndex("counts");
//			int jump_timeConsuming = cur.getColumnIndex("timeConsuming");
//			int jump_dataType = cur.getColumnIndex("dataType");
//			int jump_calorie = cur.getColumnIndex("calorie");
//			int jump_startTime = cur.getColumnIndex("startTime");
//			int jump_endTime = cur.getColumnIndex("endTime");
//			int jump_lowestRate = cur.getColumnIndex("lowestRate");
//			int jump_lowestRateS = cur.getColumnIndex("lowestRateS");
//			int jump_highestRate = cur.getColumnIndex("highestRate");
//			int jump_highestRateS = cur.getColumnIndex("highestRateS");
//			int jump_avgRate = cur.getColumnIndex("avgRate");
//			int jump_avgRateS = cur.getColumnIndex("avgRateS");
//			int jump_biaoshi = cur.getColumnIndex("biaoshi");
//
//
//			jumpList = new ArrayList<JumpEntity>();
//			while(cur.moveToNext()) {
//				JumpEntity je = new JumpEntity();
//				je.id = cur.getInt(jump_id);
//				je.counts= cur.getInt(jump_counts);
//				je.timeConsuming= cur.getInt(jump_timeConsuming);
//				je.dataType = cur.getInt(jump_dataType);
//				je.calorie = cur.getInt(jump_calorie);
//				je.startTime = cur.getString(jump_startTime);
//				je.endTime = cur.getString(jump_endTime);
//				je.lowestRate = cur.getInt(jump_lowestRate);
//				je.lowestRateS = cur.getInt(jump_lowestRateS);
//				je.highestRate = cur.getInt(jump_highestRate);
//				je.highestRateS = cur.getInt(jump_highestRateS);
//				je.avgRate = cur.getInt(jump_avgRate);
//				je.avgRateS = cur.getInt(jump_avgRateS);
//				je.biaoshi = cur.getInt(jump_biaoshi);
//				jumpList.add(je);
//			}
//			cur.close();
//		}
//		return jumpList;
//	}
	
	/**
	 * insertSkinData:(插入跳绳数据) 
	 * (这里描述这个方法适用条件 – 可选)
	 * @param
	 * @return
	 * @return long
	 * @author zhanghang
	 * @since 1.1
	 */
	public long insertSkinData(SleepData sd) {
		ContentValues cv = new ContentValues();
		cv.put("movecounts", sd.sleepdata);
//		cv.put("isupdata", sd.isupdate);
		cv.put("createTime", sd.sleeptime);
		cv.put("marktime", sd.marktime);
		if (db != null) {
			return db.insert(DBHelper.TB_JUMP, null, cv);
		}
		return -1;
	}
//	/**
//	 * 向表格插入一条记录
//	 * @param table  表名
//	 * @param values 值
//	 */
//	public long insert(String table,ContentValues values){
//		SQLiteDatabase db = getWritableDatabase();
//		long number = db.insert(table, null, values);
//		db.close();
//		return number;
//	}
	
	

	
	public int deleteData(int id) {
		String[] args = {String.valueOf(id)};
		if(db!=null){
	     return db.delete(DBHelper.TB_JUMP, "id=?", args);
		}
		return -1;
	}
	
	/**
	 * insertSkinData:(插入皮肤数据) 
	 * (批量插入) 
	 * @param sb
	 * @return
	 * @return long
	 * @author zhanghang
	 * @since 1.1
	 */
//	public void insertSkinData(ArrayList<SkinBean> skinList) {
//		if (null == skinList || skinList.size()<1)
//			return;
//		ContentValues cv = new ContentValues();
//		for (SkinBean sb : skinList) {
//			cv.put(DBHelper.Skin.SKIN_ID, sb.skin_id);
//			cv.put(DBHelper.Skin.SKIN_TYPE, sb.skin_type);
//			cv.put(DBHelper.Skin.SKIN_OIL, sb.skin_oil);
//			cv.put(DBHelper.Skin.SKIN_WATER, sb.skin_water);
//			cv.put(DBHelper.Skin.SKIN_TIME, sb.skin_time);
//			cv.put(DBHelper.Skin.SKIN_OTHER, sb.skin_other);
//			if (db != null) {
//				db.insert(DBHelper.TB_SKIN, null, cv);
//			}
//		}                                        
//	}
}
