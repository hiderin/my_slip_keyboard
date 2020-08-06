package com.example.my_slip_keyboard;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ComposingBuilder {

	// flag int
	private boolean isSlip;

	// DataBase Access
	private LocusSQLiteOpenHelper hlpr;
	private SQLiteDatabase mydb;
	private Context mContext;
	private final String dbname = "mydict.db";

	// Return Strings
	private RhText masterText;
	private Slip2RhText masterSlipText;
	private ArrayList<RhText> rhSlipTextList;
	//private StringBuilder masterString;
	private StringBuilder mKataText;
	private StringBuilder mHanKataText;
	private ArrayList<String> mCandidateList;
	//private Roma2Hira r2h;
	private Hira2Kata h2k;

	private int ListEndPoint=0;

	// make Chanese wordlist class
	private class RhWordList{
		private ArrayList<String> wordlist;
		private RhText masterText;
		
		//constructor
		public RhWordList(RhText rhtxt){
			masterText = rhtxt;
			wordlist = new ArrayList<String>();
		}

		// DatabaseTableの選択
		public String getsubTableName(String str){
			char headchar = str.charAt(0);
			int isize = str.length();
			if(isize>1){
				if(headchar=='#'){
					return "sharp_";
				}else if(headchar=='>'){
					return "frow_";
				}
			}
			return null;
		}

		public StringBuilder getTableName(String str){
			StringBuilder rtn = new StringBuilder("WordTable_");
			String subname = getsubTableName(str);
			if(subname!=null) rtn.append(subname);
			hlpr = new LocusSQLiteOpenHelper(mContext,dbname);
			mydb = hlpr.getWritableDatabase();
			StringBuilder sqltext =
				new StringBuilder("select roma from Roma2Hira where hira='");
			int idx = subname==null?0:1;
			sqltext.append(str, idx, idx+1);
			sqltext.append("' order by roma limit 1");
			SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(sqltext.toString(),null);
			if(c.getCount()>0){
				c.moveToFirst();
				String getstr = c.getString(0);
				char compchar = getstr.charAt(0);
				if(compchar<'a'||compchar>'z'){
					rtn.append("ather");
				}else if(getstr.compareTo("nn")==0){
					rtn.append("ather");
				}else{
					rtn.append(getstr);
				}
			}else{
				rtn.append("ather");
			}
			if(mydb != null) mydb.close();
			return rtn;
		}

		private String makeSQLgetWord(StringBuilder hira1,StringBuilder hira2, StringBuilder okuri){
			StringBuilder rtn = new StringBuilder();
			rtn.append("select word, okuri from ");
			rtn.append(getTableName(hira1.toString()));
			rtn.append(" where (yomi='");
			rtn.append(hira1);
			rtn.append("' and okuri like '%0%')");
			if(okuri.length()>0){
				rtn.append(" or (yomi='");
				rtn.append(hira2);
				rtn.append("' and okuri like '%");
				rtn.append(okuri);
				rtn.append("%')");
			}
			return rtn.toString();
		}

		private String makeSQLgetWordLimit(StringBuilder hira1,StringBuilder hira2, StringBuilder okuri){
			StringBuilder rtn = new StringBuilder();
			rtn.append(makeSQLgetWord(hira1,hira2,okuri));
			rtn.append(" limit 1");
			return rtn.toString();
		}

		public void MakeList(){

			int i,sp,ep,textsize,endpoint;
			RhText rcv1 = new RhText(mContext);
			RhText rcv2 = new RhText(mContext);
			boolean isFind = false;
			StringBuilder sql = new StringBuilder();

			textsize = masterText.size();

			// 一度変換済の場合はListEndPointまでを変換対象にする
			if(ListEndPoint>0){
				endpoint = ListEndPoint;
			}else{
				endpoint = textsize;
			}

			for(sp=0;sp<textsize;sp++){
				for(ep=endpoint;ep>sp;ep--){
					rcv1 = masterText.getHiraWord(sp,ep);
					if(rcv1.hira.length()==0) continue;
					if(ep>1){
						rcv2 = masterText.getHiraWord(sp,ep-1);
					}
					// 単語検索
					sql.setLength(0);
					sql.append(makeSQLgetWord( rcv1.hira,rcv2.hira, rcv2.rm));
					mydb = hlpr.getWritableDatabase();
					SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(sql.toString(),null);
					while(c.moveToNext()){
						StringBuilder ilist = new StringBuilder(c.getString(0));
						if(rcv2.rm.length()>0 && 
								c.getString(1).indexOf(rcv2.rm.toString())>-1){
							int isize = rcv1.hira.length();
							if(masterText.IndexText(ep-1).isHira()){
								ilist.append(rcv1.hira.substring(isize-1,isize));
							}
						}
						wordlist.add(ilist.toString());
					}
					if(wordlist.size()>0 || ListEndPoint>0){
					   	if(ListEndPoint==0){
							ListEndPoint = ep;
						}
					   	break;
					}
				}
				if(wordlist.size()>0 || ListEndPoint>0) break;
			}
			if(mydb != null) mydb.close();
			wordlist.add(0,getFirstString());
		}

		private String getFirstString(){
			int sp,ep,textsize;
			RhText rcv1 = new RhText(mContext);
			RhText rcv2 = new RhText(mContext);
			StringBuilder rtn = new StringBuilder();
			StringBuilder sql = new StringBuilder();

			textsize = masterText.size();
			for(sp=0;sp<textsize;sp++){
				if(sp==0){
					ep = ListEndPoint;
				}else{
					ep = textsize;
				}
				for(;ep>sp;ep--){
					StringBuilder ilist = new StringBuilder();
					rcv1 = masterText.getHiraWord(sp,ep);
					if(rcv1.hira.length()==0) continue;
					if(ep>1){
						rcv2 = masterText.getHiraWord(sp,ep-1);
					}
					// 単語検索
					sql.setLength(0);
					sql.append(makeSQLgetWordLimit(rcv1.hira, rcv2.hira, rcv2.rm));
					mydb = hlpr.getWritableDatabase();
					SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(sql.toString(),null);
					if(c.moveToNext()){
						ilist.setLength(0);
						ilist.append(c.getString(0));
						if(rcv2.rm.length()>0 && 
								c.getString(1).indexOf(rcv2.rm.toString())>-1){
							int isize = rcv1.hira.length();
							if(masterText.IndexText(ep-1).isHira()){
								ilist.append(rcv1.hira.substring(isize-1,isize));
							}
						}
						rtn.append(ilist);
					}
					if(ilist.length()>0){
						sp=ep-1;
					   	break;
					}else if(ep-sp==1){
						rtn.append(masterText.subString(sp,ep));
					}
				}
			}
			if(mydb != null) mydb.close();
			return rtn.toString();
		}

		public ArrayList<String> getCandidateList(){
			return wordlist;
		}
	}

	// Constructor
	public ComposingBuilder(Context context){
		mContext = context;
		hlpr = new LocusSQLiteOpenHelper(mContext,dbname);

		masterText = new RhText(context);
		masterSlipText = new Slip2RhText(context);
		rhSlipTextList = new ArrayList<RhText>();
		//masterString = new StringBuilder();
		mKataText = new StringBuilder();
		mHanKataText = new StringBuilder();
		mCandidateList = new ArrayList<String>();
		h2k = new Hira2Kata(mContext, mydb);
		isSlip = false;
	}

	// StringBuilder method
	public void setLength(int newLength){
		masterText.setLength(newLength);
	}

	public int length(){
		return masterText.size();
	}

	public char charAt(int index){
		return masterText.charAt(index);
	}

	public StringBuilder delete(int start, int end){
		masterText.delete(start, end);
		return masterText.toStringBuilder();
	}

	public void append(char c){
		masterText.append(c);
		ListEndPoint=0;
	}

	public void append(char[] str){
		masterText.append(str);
		ListEndPoint=0;
	}

	public void append(CharSequence s){
		masterText.append(s);
		ListEndPoint=0;
	}

	public void append(StringBuilder sb){
		masterText.append(sb);
		ListEndPoint=0;
	}

	public String toString(){
		return masterText.toString();
	}

	// slip function
	public void slip_append(char c){
		masterSlipText.append(c);
		isSlip = true;
	}

	private void strlist_to_rhlist(){
		ArrayList<String> romaTextList = masterSlipText.getRomaTextList();
		int listN = romaTextList.size();
		int i;

		for(i=0;i<listN;i++){
			RhText rhtxt = new RhText(mContext);
			rhSlipTextList.add(rhtxt.AppendString(romaTextList.get(i)));
		}
	}

	public void slip_off(){
		isSlip = false;
		rhSlipTextList.clear();
	}

	// output function
	public StringBuilder me(){
		return masterText.toStringBuilder();
	}

	public StringBuilder hira(){
		return masterText.toStringBuilder();
	}

	public StringBuilder roma(){
		return masterText.toRomaStringBuilder();
	}

	public StringBuilder kata(){
		mKataText.setLength(0);
		mKataText.append(h2k.getKataText(masterText.toString(),false));
		return mKataText;
	}

	public StringBuilder han(){
		mHanKataText.setLength(0);
		mHanKataText.append(h2k.getKataText(masterText.toString(),true));
		return mHanKataText;
	}

	public ArrayList<String> getCandidateList(){
		int i, listN;

		mCandidateList.clear();
		if(!isSlip){
			rhSlipTextList.add(masterText);
		}
		else{
			strlist_to_rhlist();
		}

		listN = rhSlipTextList.size();
		
		for(i=0;i<listN;i++){
			if(rhSlipTextList.get(i).size()>0){
				RhWordList wList = new RhWordList(rhSlipTextList.get(i));
				wList.MakeList();
				mCandidateList = wList.getCandidateList();
				mCandidateList.add(masterText.toString());
				mCandidateList.add(this.kata().toString());
				mCandidateList.add(this.han().toString());
			}
		}
		return mCandidateList;
	}

	public void close(){
		if(mydb != null) mydb.close();
	}

	public void RebuildForIndex(int index){
		if(index==0){
			this.setLength(0);
		}else if(index>0 && index< mCandidateList.size()-3){
			if(masterText.size()==ListEndPoint){
				this.setLength(0);
			}else{
				masterText = masterText.IndexText(ListEndPoint);
			}
		}else{
			this.setLength(0);
		}
		ListEndPoint=0;
	}

	public void backspace(){
		masterText.backspace();
	}

	public int CursorLeft(){
		if(ListEndPoint>0){
			ListEndPoint--;
		}
		return ListEndPoint;
	}

	public int CursorRight(){
		if(ListEndPoint<this.length()){
			ListEndPoint++;
		}
		return ListEndPoint;
	}

}
