package com.example.my_slip_keyboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

public class LocusSQLiteOpenHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;

	public LocusSQLiteOpenHelper(Context context, String dbname) {
			super(context, dbname, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
