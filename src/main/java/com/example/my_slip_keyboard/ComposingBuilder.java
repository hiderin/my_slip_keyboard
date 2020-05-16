package com.example.my_slip_keyboard;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ComposingBuilder {

	// DataBase Access
	private LocusSQLiteOpenHelper hlpr;
	private SQLiteDatabase mydb;
	private Context mContext;
	private final String dbname = "mydict.db";

	// Return Strings
	private RhText masterText;
//	private StringBuilder masterbuilder;
	private StringBuilder mHiraText;
	private StringBuilder mKataText;
	private StringBuilder mHanKataText;
	private ArrayList<String> mCandidateList;
	//private Roma2Hira r2h;
	private Hira2Kata h2k;

	// attrs flg
	// is set
	private final int rh_none	=0x0000;	// yet commit
	private final int rh_cmt	=0x0001;	// commit
	// char kind
	private final int rh_hira	=0x0002;	// commit hiragana
	private final int rh_kata	=0x0004;	// commit katakana
	private final int rh_han	=0x0008;	// commit hankakuKatakana
	private final int rh_alph	=0x0010;	// commit alphabet
	// char condition
	private final int rh_hdkt	=0x0020;	// commit handakuten
	private final int rh_nn		=0x0040;	// commit "ん"
	private final int rh_next	=0x0080;	// have created nextChar
	// attrs mask

	private int ListEndPoint=0;

	//original char class
	public class RhChar{
		public StringBuilder hira;
		public StringBuilder rm;
		//constractor
		public RhChar(){
			hira= new StringBuilder();
			rm= new StringBuilder();
		}
	}

	//original text class
	private class RhText extends RhChar{

		private RhText nextChar;

		private int attrs=0;

		// constractor
		public RhText(){
			super();
			nextChar=null;
		}

		// sub function
		private String makeSQLgetHira(String roma){
			return "select hira from Roma2Hira where roma=\""+roma+"\"";
		}

		private boolean isHandakuten(String txt){
			boolean rtn=false;
			if(
				txt.equals("kk")||
				txt.equals("ss")||
				txt.equals("tt")||
				txt.equals("hh")||
				txt.equals("mm")||
				txt.equals("yy")||
				txt.equals("rr")||
				txt.equals("ww")||
				txt.equals("gg")||
				txt.equals("jj")||
				txt.equals("zz")||
				txt.equals("dd")||
				txt.equals("bb")||
				txt.equals("cc")||
				txt.equals("pp")
			){
					rtn=true;
			}
			return rtn;
		}

		private boolean isOn(int atr){
			return (attrs&atr)==atr;
		}

		private void setOff(int atr){
			attrs &= ~atr;
		}

		private void make_nextChar(){
			if(!isOn(rh_next)){
				nextChar = new RhText();
				attrs |= rh_next;
			}
		}

		private void NextAppend(char str){
			make_nextChar();
			nextChar.append(str);
		}

		private void NextAppend(char[] str){
			make_nextChar();
			nextChar.append(str);
		}

		private void NextAppend(CharSequence str){
			make_nextChar();
			nextChar.append(str);
		}

		private void NextAppend(StringBuilder str){
			make_nextChar();
			nextChar.append(str);
		}

		private void copyRhText(RhText rhtext){
			rm = rhtext.rm;
			hira = rhtext.hira;
			nextChar = rhtext.nextChar;
			attrs = rhtext.attrs;
		}

		// main method
		public void append(char str){
			// if committed
			if(isOn(rh_cmt)){
				NextAppend(str);
				return;
			}
			rm.append(str);
			BaseAppend();
		}

		public void append(char[] str){
			// if committed
			if(isOn(rh_cmt)){
				NextAppend(str);
				return;
			}
			rm.append(str);
			BaseAppend();
		}

		public void append(CharSequence str){
			// if committed
			if(isOn(rh_cmt)){
				NextAppend(str);
				return;
			}
			rm.append(str);
			BaseAppend();
		}

		public void append(StringBuilder str){
			// if committed
			if(isOn(rh_cmt)){
				NextAppend(str);
				return;
			}
			rm.append(str);
			BaseAppend();
		}

		public void BaseAppend(){

			// change to hira from database
			mydb = hlpr.getWritableDatabase();
			SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(makeSQLgetHira(rm.toString()),null);
			c.moveToFirst();
			if(c.getCount()>0){
				hira.append(c.getString(0));
				attrs |= (rh_cmt|rh_hira);
				if(isHandakuten(rm.toString())){
					attrs |= rh_hdkt;
					NextAppend(rm.substring(0,1));
					rm.setLength(1);
				}
				else if(rm.length()>3){
					NextAppend(rm.substring(1,rm.length()));
					rm.setLength(1);
					attrs |=(rh_cmt|rh_alph);
				}
			}
		}

		public void NextDelete(){
			if(isOn(rh_next)){
				nextChar.NextDelete();
			}
			nextChar = null;
			setOff(rh_next);
		}

		public void setLength(int newLength){
			if(newLength>0&&isOn(rh_next)){
				nextChar.setLength(newLength -1);
				return;
			}else{
				NextDelete();
				rm.setLength(0);
				hira.setLength(0);
				setOff(rh_cmt);
				setOff(rh_hira);
			}
		}

		public char charAt(int index){
			int mysize = rm.length();
			if(mysize>index){
				return rm.charAt(index);
			}else{
				return nextChar.charAt(index - mysize);
			}
		}

		public String subString(int start, int end){

			boolean isnext = isOn(rh_next);

			if(isnext&&start>0){
				return nextChar.subString(start-1,end-1);
			}else if(isnext&&end>1){
				StringBuilder rtn = new StringBuilder();
				rtn.append(hira);
				return rtn.append(nextChar.subString(start-1,end-1)).toString();
			}else if(end>0){
				return hira.toString();
			}else{
				return "";
			}
		}

		public StringBuilder toStringBuilder(){
			StringBuilder rtn = new StringBuilder();
			if(isOn(rh_hira)){
				rtn.append(hira);
			}else{
				rtn.append(rm);
			}
			if(isOn(rh_next)){
				return rtn.append(nextChar.toStringBuilder());
			}else{
				return rtn;
			}
		}

		public StringBuilder toRomaStringBuilder(){
			StringBuilder rtn = new StringBuilder();
			rtn.append(rm);
			if(isOn(rh_next)){
				return rtn.append(nextChar.toRomaStringBuilder());
			}else{
				return rtn;
			}
		}

		public String toString(){
			return this.toStringBuilder().toString();
		}

		public String getOkuri(int index){
			if(index>0){
				if(isOn(rh_next)){
					return nextChar.getOkuri(index-1);
				}else{
					return "";
				}
			}else{
				return rm.substring(0,1);
			}
		}

		public RhChar getHiraWord(int start, int end){
			RhChar rtn = new RhChar();
			rtn.hira.append(this.subString(start, end));
			if(end>start){
				rtn.rm.append(this.getOkuri(end));
			}
			return rtn;
		}

		public int size(){
			if(isOn(rh_next)){
				return nextChar.size() +1;
			}else{
				if(rm.length()>0){
					return 1;
				}else{
					return 0;
				}
			}
		}

		public RhText IndexText(int index){
			if(index>0){
				if(isOn(rh_next)){
					return nextChar.IndexText(index-1);
				}else{
					return null;
				}
			}else{
				return this;
			}
		}

		public void delete(int start, int end){
			if(start>1){
				nextChar.delete(start-1,end-1);
			}else if(start==1){
				nextChar = this.IndexText(end);
			}else{
				copyRhText(this.IndexText(end));
			}
			if(nextChar==null){
				setOff(rh_next);
			}
		}

		public boolean backspace(){
			boolean rtn=false;
			if(isOn(rh_next)){
				rtn = nextChar.backspace();
				if(rtn) setOff(rh_next);
				return false;
			}
			if(isOn(rh_hira)){
				hira.setLength(0);
				rm.setLength(0);
				setOff(rh_cmt);
				setOff(rh_hira);
				return true;
			}
			if(rm.length()>0){
				rm.setLength(rm.length()-1);
                return rm.length() == 0;
            }
			return true;
		}

	}

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
			mydb.close();
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
			RhChar rcv1 = new RhChar();
			RhChar rcv2 = new RhChar();
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
							if(masterText.IndexText(ep-1).isOn(rh_hira)){
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
			mydb.close();
			wordlist.add(0,getFirstString());
		}

		private String getFirstString(){
			int sp,ep,textsize;
			RhChar rcv1 = new RhChar();
			RhChar rcv2 = new RhChar();
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
							if(masterText.IndexText(ep-1).isOn(rh_hira)){
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
			mydb.close();
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
//		mydb = hlpr.getWritableDatabase();

//		masterbuilder = new StringBuilder();
		masterText = new RhText();
		mHiraText = new StringBuilder();
		mKataText = new StringBuilder();
		mHanKataText = new StringBuilder();
		mCandidateList = new ArrayList<String>();
		//r2h = new Roma2Hira(mydb);
		h2k = new Hira2Kata(mContext, mydb);
	}

	// StringBuilder method
	public void setLength(int newLength){
//		masterbuilder.setLength(newLength);
		masterText.setLength(newLength);
	}

	public int length(){
//		return masterbuilder.length();
		return masterText.size();
	}

	public char charAt(int index){
//		return masterbuilder.charAt(index);
		return masterText.charAt(index);
	}

	public StringBuilder delete(int start, int end){
		masterText.delete(start, end);
		return masterText.toStringBuilder();
//		return masterbuilder.delete(start, end);
	}

	public void append(char c){
		masterText.append(c);
		ListEndPoint=0;
//		return masterbuilder.append(c);
	}

	public void append(char[] str){
		masterText.append(str);
		ListEndPoint=0;
//		return masterbuilder.append(str);
	}

	public void append(CharSequence s){
		masterText.append(s);
		ListEndPoint=0;
//		return masterbuilder.append(s);
	}

	public void append(StringBuilder sb){
		masterText.append(sb);
		ListEndPoint=0;
//		return masterbuilder.append(sb);
	}

	public String toString(){
//		return masterbuilder.toString();
		return masterText.toString();
	}

	// output function
	public StringBuilder me(){
//		return masterbuilder;
		return masterText.toStringBuilder();
	}

	public StringBuilder hira(){
//		mHiraText.setLength(0);
//		mHiraText.append(r2h.getHiraText(masterbuilder.toString()));
//		return mHiraText;
		return masterText.toStringBuilder();
	}

	public StringBuilder roma(){
//		mHiraText.setLength(0);
//		mHiraText.append(r2h.getHiraText(masterbuilder.toString()));
//		return mHiraText;
		return masterText.toRomaStringBuilder();
	}

	public StringBuilder kata(){
		mKataText.setLength(0);
//		mKataText.append(h2k.getKataText(mHiraText.toString(),false));
		mKataText.append(h2k.getKataText(masterText.toString(),false));
		return mKataText;
	}

	public StringBuilder han(){
		mHanKataText.setLength(0);
//		mHanKataText.append(h2k.getKataText(mHiraText.toString(),true));
		mHanKataText.append(h2k.getKataText(masterText.toString(),true));
		return mHanKataText;
	}

	public ArrayList<String> getCandidateList(){
		mCandidateList.clear();
		if(masterText.size()>0){
			RhWordList wList = new RhWordList(masterText);
			wList.MakeList();
			mCandidateList = wList.getCandidateList();
	//		mCandidateList.add(masterbuilder.toString());
			mCandidateList.add(masterText.toString());
			mCandidateList.add(this.kata().toString());
			mCandidateList.add(this.han().toString());
		}
		return mCandidateList;
	}

	public void close(){
		mydb.close();
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
