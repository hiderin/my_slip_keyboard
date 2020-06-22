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

	// constractor
	public Slip2RhText(Context context){
		mContext = context;
		hlpr = new LocusSQLiteOpenHelper(context,dbname);
	}

}

