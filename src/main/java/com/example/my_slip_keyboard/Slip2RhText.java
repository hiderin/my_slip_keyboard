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

	////////////////////////////////////////////////////////////////////////////////
	// private method

	// sliptext$B$N@hF,$NJ8;z$rJ8;z%3!<%I$KJQ49(B
	private int getHeadChar2Ascii(String sliptxt){
		return (int)(sliptxt.toCharArray()[0]);
	}

	// sliptext$B$NF,J8;z$+$i(BDB$B$K3:Ev$9$kJ8;zNsD9$5$N%j%9%H$r:n@.(B
	private ArrayList<int> getLengthList(String sliptxt, int idb){
		ArrayList<int> rtn = new ArrayList<int>();
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

	// sliptext$B$+$i:F5"E*$K(BRomaText$B$r:n@.(B
	private ArrayList<String> makeRomaTextList(String sliptxt, int idb){
		int iLenN, i, j, k;
		ArrayList<int> iLenList = new ArrayList<int>();
		String sqltxt, basetext, resttext;
		ArrayList<String> rtn = new ArrayList<String>();

		// $B%k!<%W$N=`Hw(B
		iLenList = getLengthList(sliptxt, idb);
		iLenN = iLenList.size();

		// $B:F5"E*=hM}(B
		for(i=0;i<iLenN;i++){
			if(iLenList(i) > sliptxt.length()) continue;
			// $B%j%9%H$N@8@.(B
			ArrayList<String> baseStrList = new ArrayList<String>();
			ArrayList<String> restStrLisr = new ArrayList<String>();
			// sliptxt$B$NJ,3d(B
			basetext = sliptxt.substring(0, iLenList(i));
			resttext = sliptxt.supstring(iLenList(i) - 1, sliptxt.length());
			// baseStrList$B$N<hF@(B
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
			// restStrList$B$N<hF@(B($B<+?H$N4X?t$K(Btext$B$rEjF~(B)
			restStrList = makeRomaTextList(resttext, idb==CHAR_DATA ? MOVE_DATA : CHAR_DATA);

			// baseStrList$B$H(BrestStrLisr$B$NAH9g$;(B
			int baseN, restN;
			baseN = baseStrList.size();
			restN = restStrList.size();
			if(idb == MOVE_DATA) baseN = 1;
			for(j=0;j<baseN;j++){
				for(k=0;k<restN;k++){
					String addText =  baseStrList.size() > 0 ? baseStrList(j) : "";
					String addText += restStrList.size() > 0 ? restStrList(k) : "";
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
		mRhText.setLengrh(0);
	}

	public ArrayList<String> getRomaTextList(){
		return makeRomaTextList(mSlipText, CHAR_DATA);
	}
}

