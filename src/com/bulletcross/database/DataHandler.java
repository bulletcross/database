package com.bulletcross.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {
	public static final String LAT = "lattitude";
	public static final String LON = "longitude";
	public static final String TABLE_NAME = "mytable";
	public static final String DATABASE_NAME = "mydatabase";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_CREATE = "create table mytable(latitude text not null, longitude text not null);";
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public DataHandler(Context ctx1){
		this.ctx = ctx1;
		dbhelper = new DataBaseHelper(ctx1);
	}
	
	static class DataBaseHelper extends SQLiteOpenHelper{

		public DataBaseHelper(Context ctx1) {
			super(ctx1, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
		
		public void onCreate(SQLiteDatabase db){
			try{
			db.execSQL(TABLE_CREATE);
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS mytable");
			onCreate(db);
			
		}
	}
	
	public DataHandler open(){
		db = dbhelper.getWritableDatabase();
		return this;
	}
	public void close(){
		dbhelper.close();
	}
	
	public long insertData(double d, double e){
		ContentValues content = new ContentValues();
		content.put(LAT, d);
		content.put(LON, e);
		return db.insert(TABLE_NAME, null, content);
	}
	
	public Cursor returnData(){
		return db.query(TABLE_NAME,new String[]{LAT,LON},null,null,null,null,null);
	}
}
