package com.example.my_slip_keyboard;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;

class Hira2Kata{

	private SQLiteDatabase mydb;
	private LocusSQLiteOpenHelper hlpr;
	private Context mContext;
	private final String dbname = "mydict.db";

	// コンストラクタ
	public Hira2Kata(Context context, SQLiteDatabase db){
		mContext = context;
		mydb = db;
	}

	private String makeSQLgetKata(boolean isHan, String hira){
		String mfield = isHan ? "hankata":"kata";
		return "select "+ mfield +" from KanaTable where hira=\""+hira+"\"";
	}

	private String getKata(String hira,boolean isHan){
		hlpr = new LocusSQLiteOpenHelper(mContext,dbname);
		mydb = hlpr.getWritableDatabase();
		Cursor c = mydb.rawQuery(makeSQLgetKata(isHan,hira),null);
		c.moveToFirst();
		if(c.getCount()>0){
			return c.getString(0);
		}else{
			return "";
		}
	}

	public String getKataText(String hiraText,boolean isHan){
		int cntN, sp, cN, cNmax;
		String cmtText,subText;

		cmtText="";
		subText="";
		cntN = hiraText.length();

		for(sp=0;sp<cntN;sp++){
			subText=hiraText.substring(sp,sp+1);
			subText=getKata(subText,isHan);
			mydb.close();
			if(!subText.isEmpty()){
				cmtText=cmtText+subText;
			}else{
				cmtText=cmtText+hiraText.substring(sp,sp+1);
			}
		}
		return cmtText;
	}

}
