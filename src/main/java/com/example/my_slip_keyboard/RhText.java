package com.example.my_slip_keyboard;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

//original text class
public class RhText {

	//original char class
	public StringBuilder hira;
	public StringBuilder rm;

	// DataBase Access
	private LocusSQLiteOpenHelper hlpr;
	private SQLiteDatabase mydb;
	private Context mContext;
	private final String dbname = "mydict.db";

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
	// source kind
	private final int src_rh	=0x0100;	// RhText
	private final int src_const	=0x0200;	// Const Text
	private final int src_locus	=0x0400;	// Locus Text
	// attrs mask

	private RhText nextChar;

	private int attrs=0;

	// constractor
	public RhText(Context context){
		mContext = context;
		hira= new StringBuilder();
		rm= new StringBuilder();
		nextChar=null;
		hlpr = new LocusSQLiteOpenHelper(context,dbname);
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
			nextChar = new RhText(mContext);
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

	public RhText getHiraWord(int start, int end){
		RhText rtn = new RhText(mContext);
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

	public boolean isHira(){
		return isOn(rh_hira);
	}
}

