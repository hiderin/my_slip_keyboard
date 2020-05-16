package com.example.my_slip_keyboard;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

class Hira2Word{

	private SQLiteDatabase mydb;

	// コンストラクタ
	public Hira2Word(SQLiteDatabase db){
		mydb = db;
	}

	private String makeSQLgetWord(String hira, String okuri){
		return "select WordTable.word from YomiTable, Hira2Word, WordTable"
			+" where YomiTable='"+hira+"' heving instr(Hira2Word.okuri,'"+okuri+"')";
	}

	public String getWord(String hira, String okuri){
		Cursor c = mydb.rawQuery(makeSQLgetWord(hira,okuri),null);
		c.moveToFirst();
		if(c.getCount()>0){
			return c.getString(0);
		}else{
			return "";
		}
	}

}
