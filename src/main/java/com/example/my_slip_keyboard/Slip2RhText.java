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
	private StringBuilder mSlipText;

	// constractor
	public Slip2RhText(Context context){
		mContext = context;
		hlpr = new LocusSQLiteOpenHelper(context,dbname);

		mSlipText = new StringBuilder();
	}

	////////////////////////////////////////////////////////////////////////////////
	// private method

	// sliptextの先頭の文字を文字コードに変換
	private int getHeadChar2Ascii(String sliptxt){
		return (int)(sliptxt.toCharArray()[0]);
	}

	// sliptextの頭文字からDBに該当する文字列長さのリストを作成
	private ArrayList<Integer> getLengthList(String sliptxt, int idb){
		ArrayList<Integer> rtn = new ArrayList<Integer>();
		String sqltxt, stable;
		int ihcode;

		stable = idb == CHAR_DATA ? "slip_char_table" : "move_char_table";
		ihcode = getHeadChar2Ascii(sliptxt);
		sqltxt = "SELECT DISTINCT str_len FROM " + stable + " WHERE head_code = ";
		sqltxt += Integer.toString(ihcode) + " ORDER BY str_len DESC;";

		mydb = hlpr.getWritableDatabase();
		SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(sqltxt,null);
		while(c.moveToNext()){
			rtn.add(c.getInt(0));
		}
		if(mydb != null) mydb.close();

		return rtn;
	}

	// sliptextから再帰的にRomaTextを作成
	private ArrayList<String> makeRomaTextList(String sliptxt, int idb){
		int iLenN, i, j, k;
		ArrayList<Integer> iLenList = new ArrayList<Integer>();
		String sqltxt, basetext, resttext;
		ArrayList<String> rtn = new ArrayList<String>();

		// ループの準備
		iLenList = getLengthList(sliptxt, idb);
		iLenN = iLenList.size();

		// 再帰的処理
		for(i=0;i<iLenN;i++){
			if(iLenList.get(i) > sliptxt.length()) continue;
			// リストの生成
			ArrayList<String> baseStrList = new ArrayList<String>();
			ArrayList<String> restStrList = new ArrayList<String>();
			// sliptxtの分割
			basetext = sliptxt.substring(0, iLenList.get(i));
			resttext = sliptxt.substring(iLenList.get(i) - 1, sliptxt.length());
			// baseStrListの取得
			if(idb==CHAR_DATA){
				sqltxt = "SELECT B.roma FROM (SELECT char_no FROM slip_char_table ";
				sqltxt += "WHERE locus_string = '" + basetext + "') AS A ";
				sqltxt += "LEFT JOIN (SELECT char_no, roma FROM hira_master_table) AS B ";
				sqltxt += "ON A.char_no = B.char_no; ";
				mydb = hlpr.getWritableDatabase();
				SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(sqltxt,null);
				while(c.moveToNext()){
					baseStrList.add(c.getString(0));
				}
				if(mydb != null) mydb.close();
			}
			// restStrListの取得(自身の関数にtextを投入)
			restStrList = makeRomaTextList(resttext, idb==CHAR_DATA ? MOVE_DATA : CHAR_DATA);

			// baseStrListとrestStrLisrの組合せ
			int baseN, restN;
			baseN = baseStrList.size();
			restN = restStrList.size();
			String addText;
			if(idb == MOVE_DATA) baseN = 1;
			for(j=0;j<baseN;j++){
				for(k=0;k<restN;k++){
					addText =  baseStrList.size() > 0 ? baseStrList.get(j) : "";
					addText += restStrList.size() > 0 ? restStrList.get(k) : "";
					if(!addText.equals("")) rtn.add(addText);
				}
			}
		}
		return rtn;
	}

	////////////////////////////////////////////////////////////////////////////////
	// public method
	public void append(char str){
		mSlipText.append(str);
	}

	public void clear(){
		mSlipText.setLength(0);
	}

	public ArrayList<String> getRomaTextList(){
		return makeRomaTextList(mSlipText.toString(), CHAR_DATA);
	}
}

