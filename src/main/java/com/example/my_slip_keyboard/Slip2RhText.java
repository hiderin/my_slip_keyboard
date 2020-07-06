package com.example.my_slip_keyboard;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

//original text class
public class Slip2RhText {

	// DataBase Access
	private LocusSQLiteOpenHelper hlpr;
	private SQLiteDatabase mydb;
	private Context mContext;
	private final String dbname = "mydict.db";
	private final int MOVE_DATA = 0;
	private final int CHAR_DATA = 1;

	// main text data
	private ArrayList<RhText> mRhTextList;
	private StringBuilder mSlipText;

	// main text data
	private RhText mRhText;
	private StringBuilder mSlipText;

	// constractor
	public Slip2RhText(Context context){
		mContext = context;
		hlpr = new LocusSQLiteOpenHelper(context,dbname);

		mRTextList = new ArrayList<mRhTextList>();
		mSlipText = new StringBuilder();
	}

	// private method
	private int getHeadChar2Ascii(String sliptxt){
		return (int)(sliptxt.toCharArray()[0]);
	}

	private ArrayList<int> getLengthList(String sliptxt, int idb){
		ArrayList<int> rtn = new ArrayList<int>();
		String sqltxt, stable;
		int ihcode;

		stable = idb == CHAR_DATA ? "slip_char_table" : "move_char_table";
		ihcode = getHeadChar2Ascii(sliptxt);
		sqltxt = "SELECT DISTINCT str_len FROM " + stable + " WHERE head_code = ";
		sqltxt += Integer.toString(ihcode) + " ;";

		mydb = hlpr.getWritableDatabase();
		SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(sqltxt,null);
		while(c.moveToNext()){
			StringBuilder ilist = new StringBuilder(c.getString(0));
			rtn.add(c.getInt(0));
		}
		if(mydb != null) mydb.close();

		return rtn;
	}

	private ArrayList<RhText> makeRhTextList(String sliptxt){
	}

	// public method
	public void append(char str){
		mSlipText.append(str);
	}

	public void clear(){
		mSlipText.setLength(0);
		mRhText.setLengrh(0);
	}

}

