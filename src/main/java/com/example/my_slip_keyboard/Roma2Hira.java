package com.example.my_slip_keyboard;

//import android.database.sqlite.SQLiteDatabase;
//import android.database.Cursor;

class Roma2Hira{

	//private SQLiteDatabase mydb;

	// コンストラクタ
//	public Roma2Hira(SQLiteDatabase db){
//		mydb = db;
//	}

//	private String makeSQLgetHira(String roma){
//		return "select hira from Roma2Hira where roma=\""+roma+"\"";
//	}

//	private String getHira(String roma){
//		Cursor c = mydb.rawQuery(makeSQLgetHira(roma),null);
//		c.moveToFirst();
//		if(c.getCount()>0){
//			return c.getString(0);
//		}else{
//			return "";
//		}
//	}

	private String getHira(String roma){
		String ctxt1, ctxt2, ctxt3;
		ctxt1 = (roma.length() > 0) ? roma.substring(0,1) : "";
		ctxt2 = (roma.length() > 1) ? roma.substring(1,2) : "";
		ctxt3 = (roma.length() > 2) ? roma.substring(2,3) : "";

		if(ctxt1.equals("t")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ちゃ";}
				else if(ctxt3.equals("i")){	return "ちぃ";}
				else if(ctxt3.equals("u")){	return "ちゅ";}
				else if(ctxt3.equals("e")){	return "ちぇ";}
				else if(ctxt3.equals("o")){	return "ちょ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "とぁ";}
				else if(ctxt3.equals("i")){	return "とぃ";}
				else if(ctxt3.equals("u")){	return "とぅ";}
				else if(ctxt3.equals("e")){	return "とぇ";}
				else if(ctxt3.equals("o")){	return "とぉ";}}
			else if(ctxt2.equals("s")){	if(ctxt3.equals("a")){	return "つぁ";}
				else if(ctxt3.equals("i")){	return "つぃ";}
				else if(ctxt3.equals("u")){	return "つ";}
				else if(ctxt3.equals("e")){	return "つぇ";}
				else if(ctxt3.equals("o")){	return "つぉ";}}
			else if(ctxt2.equals("h")){	if(ctxt3.equals("a")){	return "てゃ";}
				else if(ctxt3.equals("i")){	return "てぃ";}
				else if(ctxt3.equals("u")){	return "てゅ";}
				else if(ctxt3.equals("e")){	return "てぇ";}
				else if(ctxt3.equals("o")){	return "てょ";}}
			else if(ctxt2.equals("u")){		return "つ";}
			else if(ctxt2.equals("o")){		return "と";}
			else if(ctxt2.equals("i")){		return "ち";}
			else if(ctxt2.equals("e")){		return "て";}
			else if(ctxt2.equals("a")){		return "た";}}
		else if(ctxt1.equals("s")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "しゃ";}
				else if(ctxt3.equals("i")){	return "しぃ";}
				else if(ctxt3.equals("u")){	return "しゅ";}
				else if(ctxt3.equals("e")){	return "しぇ";}
				else if(ctxt3.equals("o")){	return "しょ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "すぁ";}
				else if(ctxt3.equals("i")){	return "すぃ";}
				else if(ctxt3.equals("u")){	return "すぅ";}
				else if(ctxt3.equals("e")){	return "すぇ";}
				else if(ctxt3.equals("o")){	return "すぉ";}}
			else if(ctxt2.equals("h")){	if(ctxt3.equals("a")){	return "しゃ";}
				else if(ctxt3.equals("i")){	return "し";}
				else if(ctxt3.equals("u")){	return "しゅ";}
				else if(ctxt3.equals("e")){	return "しぇ";}
				else if(ctxt3.equals("o")){	return "しょ";}}
			else if(ctxt2.equals("u")){		return "す";}
			else if(ctxt2.equals("o")){		return "そ";}
			else if(ctxt2.equals("i")){		return "し";}
			else if(ctxt2.equals("e")){		return "せ";}
			else if(ctxt2.equals("a")){		return "さ";}}
		else if(ctxt1.equals("x")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ゃ";}
				else if(ctxt3.equals("i")){	return "ぃ";}
				else if(ctxt3.equals("u")){	return "ゅ";}
				else if(ctxt3.equals("e")){	return "ぇ";}
				else if(ctxt3.equals("o")){	return "ょ";}}
			else if(ctxt2.equals("k")){	if(ctxt3.equals("a")){	return "ヵ";}
				else if(ctxt3.equals("e")){	return "ヶ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "ゎ";}}
			else if(ctxt2.equals("u")){		return "ぅ";}
			else if(ctxt2.equals("t")){	if(ctxt3.equals("u")){	return "っ";}}
			else if(ctxt2.equals("o")){		return "ぉ";}
			else if(ctxt2.equals("n")){		return "ん";}
			else if(ctxt2.equals("i")){		return "ぃ";}
			else if(ctxt2.equals("e")){		return "ぇ";}
			else if(ctxt2.equals("a")){		return "ぁ";}}
		else if(ctxt1.equals("q")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "くゃ";}
				else if(ctxt3.equals("i")){	return "くぃ";}
				else if(ctxt3.equals("u")){	return "くゅ";}
				else if(ctxt3.equals("e")){	return "くぇ";}
				else if(ctxt3.equals("o")){	return "くょ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "くぁ";}
				else if(ctxt3.equals("i")){	return "くぃ";}
				else if(ctxt3.equals("u")){	return "くぅ";}
				else if(ctxt3.equals("e")){	return "くぇ";}
				else if(ctxt3.equals("o")){	return "くぉ";}}
			else if(ctxt2.equals("u")){		return "く";}
			else if(ctxt2.equals("o")){		return "くぉ";}
			else if(ctxt2.equals("i")){		return "くぃ";}
			else if(ctxt2.equals("e")){		return "くぇ";}
			else if(ctxt2.equals("a")){		return "くぁ";}}
		else if(ctxt1.equals("g")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ぎゃ";}
				else if(ctxt3.equals("i")){	return "ぎぃ";}
				else if(ctxt3.equals("u")){	return "ぎゅ";}
				else if(ctxt3.equals("e")){	return "ぎぇ";}
				else if(ctxt3.equals("o")){	return "ぎょ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "ぐぁ";}
				else if(ctxt3.equals("i")){	return "ぐぃ";}
				else if(ctxt3.equals("u")){	return "ぐぅ";}
				else if(ctxt3.equals("e")){	return "ぐぇ";}
				else if(ctxt3.equals("o")){	return "ぐぉ";}}
			else if(ctxt2.equals("u")){		return "ぐ";}
			else if(ctxt2.equals("o")){		return "ご";}
			else if(ctxt2.equals("i")){		return "ぎ";}
			else if(ctxt2.equals("e")){		return "げ";}
			else if(ctxt2.equals("a")){		return "が";}}
		else if(ctxt1.equals("f")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ふゃ";}
				else if(ctxt3.equals("i")){	return "ふぃ";}
				else if(ctxt3.equals("u")){	return "ふゅ";}
				else if(ctxt3.equals("e")){	return "ふぇ";}
				else if(ctxt3.equals("o")){	return "ふょ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "ふぁ";}
				else if(ctxt3.equals("i")){	return "ふぃ";}
				else if(ctxt3.equals("u")){	return "ふぅ";}
				else if(ctxt3.equals("e")){	return "ふぇ";}
				else if(ctxt3.equals("o")){	return "ふぉ";}}
			else if(ctxt2.equals("u")){		return "ふ";}
			else if(ctxt2.equals("o")){		return "ふぉ";}
			else if(ctxt2.equals("i")){		return "ふぃ";}
			else if(ctxt2.equals("e")){		return "ふぇ";}
			else if(ctxt2.equals("a")){		return "ふぁ";}}
		else if(ctxt1.equals("l")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ゃ";}
				else if(ctxt3.equals("i")){	return "ぃ";}
				else if(ctxt3.equals("u")){	return "ゅ";}
				else if(ctxt3.equals("e")){	return "ぇ";}
				else if(ctxt3.equals("o")){	return "ょ";}}
			else if(ctxt2.equals("k")){	if(ctxt3.equals("a")){	return "ヵ";}
				else if(ctxt3.equals("e")){	return "ヶ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "ゎ";}}
			else if(ctxt2.equals("u")){		return "ぅ";}
			else if(ctxt2.equals("t")){	if(ctxt3.equals("u")){	return "っ";}}
			else if(ctxt2.equals("o")){		return "ぉ";}
			else if(ctxt2.equals("i")){		return "ぃ";}
			else if(ctxt2.equals("e")){		return "ぇ";}
			else if(ctxt2.equals("a")){		return "ぁ";}}
		else if(ctxt1.equals("n")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "にゃ";}
				else if(ctxt3.equals("i")){	return "にぃ";}
				else if(ctxt3.equals("u")){	return "にゅ";}
				else if(ctxt3.equals("e")){	return "にぇ";}
				else if(ctxt3.equals("o")){	return "にょ";}}
			else if(ctxt2.equals("u")){		return "ぬ";}
			else if(ctxt2.equals("o")){		return "の";}
			else if(ctxt2.equals("n")){		return "ん";}
			else if(ctxt2.equals("i")){		return "に";}
			else if(ctxt2.equals("e")){		return "ね";}
			else if(ctxt2.equals("a")){		return "な";}}
		else if(ctxt1.equals("k")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "きゃ";}
				else if(ctxt3.equals("i")){	return "きぃ";}
				else if(ctxt3.equals("u")){	return "きゅ";}
				else if(ctxt3.equals("e")){	return "きぇ";}
				else if(ctxt3.equals("o")){	return "きょ";}}
			else if(ctxt2.equals("w")){	if(ctxt3.equals("a")){	return "くぁ";}}
			else if(ctxt2.equals("u")){		return "く";}
			else if(ctxt2.equals("o")){		return "こ";}
			else if(ctxt2.equals("i")){		return "き";}
			else if(ctxt2.equals("e")){		return "け";}
			else if(ctxt2.equals("a")){		return "か";}}
		else if(ctxt1.equals("v")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ヴゃ";}
				else if(ctxt3.equals("i")){	return "ヴぃ";}
				else if(ctxt3.equals("u")){	return "ヴゅ";}
				else if(ctxt3.equals("e")){	return "ヴぇ";}
				else if(ctxt3.equals("o")){	return "ヴょ";}}
			else if(ctxt2.equals("u")){		return "ヴ";}
			else if(ctxt2.equals("o")){		return "ヴぉ";}
			else if(ctxt2.equals("i")){		return "ヴぃ";}
			else if(ctxt2.equals("e")){		return "ヴぇ";}
			else if(ctxt2.equals("a")){		return "ヴぁ";}}
		else if(ctxt1.equals("r")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "りゃ";}
				else if(ctxt3.equals("i")){	return "りぃ";}
				else if(ctxt3.equals("u")){	return "りゅ";}
				else if(ctxt3.equals("e")){	return "りぇ";}
				else if(ctxt3.equals("o")){	return "りょ";}}
			else if(ctxt2.equals("u")){		return "る";}
			else if(ctxt2.equals("o")){		return "ろ";}
			else if(ctxt2.equals("i")){		return "り";}
			else if(ctxt2.equals("e")){		return "れ";}
			else if(ctxt2.equals("a")){		return "ら";}}
		else if(ctxt1.equals("p")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ぴゃ";}
				else if(ctxt3.equals("i")){	return "ぴぃ";}
				else if(ctxt3.equals("u")){	return "ぴゅ";}
				else if(ctxt3.equals("e")){	return "ぴぇ";}
				else if(ctxt3.equals("o")){	return "ぴょ";}}
			else if(ctxt2.equals("u")){		return "ぷ";}
			else if(ctxt2.equals("o")){		return "ぽ";}
			else if(ctxt2.equals("i")){		return "ぴ";}
			else if(ctxt2.equals("e")){		return "ぺ";}
			else if(ctxt2.equals("a")){		return "ぱ";}}
		else if(ctxt1.equals("m")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "みゃ";}
				else if(ctxt3.equals("i")){	return "みぃ";}
				else if(ctxt3.equals("u")){	return "みゅ";}
				else if(ctxt3.equals("e")){	return "みぇ";}
				else if(ctxt3.equals("o")){	return "みょ";}}
			else if(ctxt2.equals("u")){		return "む";}
			else if(ctxt2.equals("o")){		return "も";}
			else if(ctxt2.equals("i")){		return "み";}
			else if(ctxt2.equals("e")){		return "め";}
			else if(ctxt2.equals("a")){		return "ま";}}
		else if(ctxt1.equals("j")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "じゃ";}
				else if(ctxt3.equals("i")){	return "じぃ";}
				else if(ctxt3.equals("u")){	return "じゅ";}
				else if(ctxt3.equals("e")){	return "じぇ";}
				else if(ctxt3.equals("o")){	return "じょ";}}
			else if(ctxt2.equals("u")){		return "じゅ";}
			else if(ctxt2.equals("o")){		return "じょ";}
			else if(ctxt2.equals("i")){		return "じ";}
			else if(ctxt2.equals("e")){		return "じぇ";}
			else if(ctxt2.equals("a")){		return "じゃ";}}
		else if(ctxt1.equals("h")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "ひゃ";}
				else if(ctxt3.equals("i")){	return "ひぃ";}
				else if(ctxt3.equals("u")){	return "ひゅ";}
				else if(ctxt3.equals("e")){	return "ひぇ";}
				else if(ctxt3.equals("o")){	return "ひょ";}}
			else if(ctxt2.equals("u")){		return "ふ";}
			else if(ctxt2.equals("o")){		return "ほ";}
			else if(ctxt2.equals("i")){		return "ひ";}
			else if(ctxt2.equals("e")){		return "へ";}
			else if(ctxt2.equals("a")){		return "は";}}
		else if(ctxt1.equals("d")){	if(ctxt2.equals("h")){	if(ctxt3.equals("a")){	return "でゃ";}
				else if(ctxt3.equals("i")){	return "でぃ";}
				else if(ctxt3.equals("u")){	return "でゅ";}
				else if(ctxt3.equals("e")){	return "でぇ";}
				else if(ctxt3.equals("o")){	return "でょ";}}
			else if(ctxt2.equals("u")){		return "づ";}
			else if(ctxt2.equals("o")){		return "ど";}
			else if(ctxt2.equals("i")){		return "ぢ";}
			else if(ctxt2.equals("e")){		return "で";}
			else if(ctxt2.equals("a")){		return "だ";}}
		else if(ctxt1.equals("c")){	if(ctxt2.equals("h")){	if(ctxt3.equals("a")){	return "ちゃ";}
				else if(ctxt3.equals("i")){	return "ち";}
				else if(ctxt3.equals("u")){	return "ちゅ";}
				else if(ctxt3.equals("e")){	return "ちぇ";}
				else if(ctxt3.equals("o")){	return "ちょ";}}
			else if(ctxt2.equals("u")){		return "く";}
			else if(ctxt2.equals("o")){		return "こ";}
			else if(ctxt2.equals("i")){		return "し";}
			else if(ctxt2.equals("e")){		return "せ";}
			else if(ctxt2.equals("a")){		return "か";}}
		else if(ctxt1.equals("b")){	if(ctxt2.equals("y")){	if(ctxt3.equals("a")){	return "びゃ";}
				else if(ctxt3.equals("i")){	return "びぃ";}
				else if(ctxt3.equals("u")){	return "びゅ";}
				else if(ctxt3.equals("e")){	return "びぇ";}
				else if(ctxt3.equals("o")){	return "びょ";}}
			else if(ctxt2.equals("u")){		return "ぶ";}
			else if(ctxt2.equals("o")){		return "ぼ";}
			else if(ctxt2.equals("i")){		return "び";}
			else if(ctxt2.equals("e")){		return "べ";}
			else if(ctxt2.equals("a")){		return "ば";}}
		else if(ctxt1.equals("z")){	if(ctxt2.equals("u")){		return "ず";}
			else if(ctxt2.equals("o")){		return "ぞ";}
			else if(ctxt2.equals("i")){		return "じ";}
			else if(ctxt2.equals("e")){		return "ぜ";}
			else if(ctxt2.equals("a")){		return "ざ";}}
		else if(ctxt1.equals("y")){	if(ctxt2.equals("u")){		return "ゆ";}
			else if(ctxt2.equals("o")){		return "よ";}
			else if(ctxt2.equals("i")){		return "い";}
			else if(ctxt2.equals("e")){		return "いぇ";}
			else if(ctxt2.equals("a")){		return "や";}}
		else if(ctxt1.equals("w")){	if(ctxt2.equals("u")){		return "う";}
			else if(ctxt2.equals("o")){		return "を";}
			else if(ctxt2.equals("i")){		return "ゐ";}
			else if(ctxt2.equals("e")){		return "ゑ";}
			else if(ctxt2.equals("a")){		return "わ";}}
		else if(ctxt1.equals("u")){			return "う";}
		else if(ctxt1.equals("o")){			return "お";}
		else if(ctxt1.equals("i")){			return "い";}
		else if(ctxt1.equals("e")){			return "え";}
		else if(ctxt1.equals("a")){			return "あ";}
		else if(ctxt1.equals(",")){			return "、";}
		else if(ctxt1.equals(".")){			return "。";}

		return "";
	}

	public String getHiraText(String romaText){
		int cntN, sp, cN, cNmax;
		int mIsHandakuten=0;
		String cmtText,subText;

		cmtText="";
		subText="";
		cntN = romaText.length();

		for(sp=0;sp<cntN;sp++){
			cNmax = Math.min(3,cntN-sp);
			for(cN=0;cN<cNmax;cN++){
				subText=romaText.substring(sp,sp+cN+1);
				mIsHandakuten=isHandakuten(subText);
				if(mIsHandakuten==1){
					subText="っ";
				}
				else{
					subText=getHira(subText);
				}
				if(!subText.isEmpty()) break;
			}
			if(cN<cNmax){
				cmtText=cmtText+subText;
			}else{
				cN=0;
				//cmtText=cmtText+romaText.substring(sp,sp+1);
			}
			sp+=cN-mIsHandakuten;
		}
		return isHandakuten(romaText) == 1 ? "" : cmtText;
	}

	private int isHandakuten(String txt){
		int rtn=0;
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
			txt.equals("zz")||
			txt.equals("dd")||
			txt.equals("bb")||
			txt.equals("cc")||
			txt.equals("pp")
		){
				rtn=1;
		}
		return rtn;
	}
}
