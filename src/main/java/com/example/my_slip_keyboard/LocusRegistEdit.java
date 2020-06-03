/* 軌道登録用設定画面のActivity */

package com.example.my_slip_keyboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.view.KeyEvent;
import java.util.HashSet;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;

//public class LocusRegistEdit extends AppCompatActivity{
public class LocusRegistEdit extends Activity{

	// DataBase Access
	private LocusSQLiteOpenHelper hlpr;
	private SQLiteDatabase mydb;
	private final String dbname = "mydict.db";

    /** Widgets which constitute this screen of activity */
    private Button mNextBtn;
    private Button mPrevBtn;
	private EditText mEditTxt;
	private TextView mListNum;
    private ListView mListView;
	private TextView mCharNumTxtView;
	private TextView mMainCharView;
	private TextView mRomaCharView;

	// 変数
	private int mCharNum = 1;
    private ArrayList mListData;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locus_regist_edit);

		// 変数の初期化
		mListData = new ArrayList<>();

		// データベース
		hlpr = new LocusSQLiteOpenHelper(this,dbname);

        /* get widgets */
        mNextBtn = (Button)findViewById(R.id.nextButton);
        mPrevBtn = (Button)findViewById(R.id.prevButton);
		mListView = (ListView)findViewById(R.id.listView);
		mEditTxt = (EditText)findViewById(R.id.LRegEditHira);
		mListNum = (TextView)findViewById(R.id.list_num);
		mCharNumTxtView = (TextView)findViewById(R.id.char_num);
		mMainCharView = (TextView)findViewById(R.id.main_char);
		mRomaCharView = (TextView)findViewById(R.id.roma_char);

		// イベントリスナーの生成
		mEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					if(event.getAction() == KeyEvent.ACTION_DOWN) {
						EditTextOnEnter();
					}
					return true;
				}
				return false;
			}
		});
		mNextBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				NextBtnClick();
			}
		});
		mPrevBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				PrevBtnClick();
			}
		});

    }

	//==============================================================================
	// イベントの処理
	//==============================================================================

	private void NextBtnClick() {
		putCurrentLocusList();
		mCharNum++;
		ShowEditChar();
		SetNextLocusList();
	}

	private void PrevBtnClick() {
		putCurrentLocusList();
		if(mCharNum > 1){
			mCharNum--;
			ShowEditChar();
			SetNextLocusList();
		}
	}

	private void EditTextOnEnter(){

		// EditTextのテキストを取得
		String liststr = mEditTxt.getText().toString();
		if(liststr.length() == 0) return;

		// ListViewにテキストを追加
		mListData.add(liststr);

		// ListViewの更新
		updateLocusListView();

		// EditTextのテキストをクリア
		mEditTxt.getEditableText().clear();

	}

	//==============================================================================
	// private method
	//==============================================================================

	//現在のLocusListをDBに格納
	private void putCurrentLocusList(){
		// DBから該当のLucusListを削除
		exeNonQuery(makeSQL_deleteTargetList());
		// DBに現在のLocusListを追加
		exeNonQuery(makeSQL_insertLocusList());
	}

	// メインテキストビューの表示
	private void ShowEditChar(){
		mCharNumTxtView.setText(String.valueOf(mCharNum));
		mMainCharView.setText(getMainChar());
		mRomaCharView.setText(getRomaChar());
	}

	private void SetNextLocusList(){
		// 登録された軌道の取得
		mListData = getStringListFromDB(makeSQL_getLocusList());
		// ListViewの更新
		updateLocusListView();
		// EditTextのテキストをクリア
		mEditTxt.getEditableText().clear();
	}

	private String getMainChar(){
		return getStringFromDB(makeSQL_getMainChar());
	}

	private String getRomaChar(){
		return getStringFromDB(makeSQL_getRomaChar());
	}

	private void updateLocusListView(){
		//リストの重複を回避
		mListData = new ArrayList<>(new HashSet<>(mListData));

		// リスト項目とListViewを対応付けるArrayAdapterを用意する
		ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListData);

		// ListViewにArrayAdapterを設定する
		mListView.setAdapter(adapter);

		// 軌道数の表示
		mListNum.setText(String.valueOf(adapter.getCount()) + "個");
	}

	//==============================================================================
	// SQLite関係
	//==============================================================================

	//------------------------------------------------------------------------------
	// DB操作BASE

	private String getStringFromDB(String SQLstr){
		String rtn = "";
		mydb = hlpr.getWritableDatabase();
		SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(SQLstr, null);
		c.moveToFirst();
		if(c.getCount()>0) rtn = c.getString(0);
		mydb.close();
		return rtn;
	}

	private ArrayList getStringListFromDB(String SQLstr){
		ArrayList rtn;
		rtn = new ArrayList<>();
		mydb = hlpr.getWritableDatabase();
		SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(SQLstr, null);
		c.moveToFirst();
		while(c.moveToNext()){
			rtn.add(c.getString(0));
		}
		mydb.close();
		return rtn;
	}

	private void exeNonQuery(String SQLstr){
		mydb = hlpr.getWritableDatabase();
		mydb.execSQL(SQLstr);
		mydb.close();
	}

	//------------------------------------------------------------------------------
	// SQL文字列の作成

	private String makeSQL_getMainChar(){
		String rtn =  "SELECT hira FROM hira_master_table WHERE char_no='";
		rtn += String.valueOf(mCharNum) + "';";
		return rtn;
	}

	private String makeSQL_getRomaChar(){
		String rtn =  "SELECT roma FROM hira_master_table WHERE char_no='";
		rtn += String.valueOf(mCharNum) + "';";
		return rtn;
	}

	private String makeSQL_getLocusList(){
		String rtn = "SELECT locus_string FROM char_data_table WHERE char_no='";
		rtn += String.valueOf(mCharNum) + "';";
		return rtn;
	}

	private String makeSQL_deleteTargetList(){
		String rtn = "DELETE FROM char_data_table WHERE char_no='";
		rtn += String.valueOf(mCharNum) + "';";
		return rtn;
	}

	private String makeSQL_insertLocusList(){
		int i, cnt;
		String rtn, item;
		
		rtn = "INSERT INTO char_data_table(locus_string, char_no, str_len) VALUES ";
		cnt = mListData.size();

		for(i=0;i<cnt;i++){
			if(i>0) rtn += ",";
			item = (String)mListData.get(i);
			rtn += "('" + item + "', " + String.valueOf(mCharNum) + " , ";
			rtn +=  String.valueOf(item.length()) + " )";
		}
		
		return rtn + ";";
	}
}
