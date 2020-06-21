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
import android.widget.AdapterView;
import android.widget.Spinner;
import java.util.ArrayList;
import android.view.KeyEvent;
import java.util.HashSet;
import android.widget.TextView.BufferType;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;

import android.preference.PreferenceManager;
import android.content.SharedPreferences;

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
	private Spinner mModeSpinner;

	// 変数
	private int mCharNum = 1;
    private ArrayList mListData;
	private int mCurrentMode = 0;

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
		mModeSpinner = (Spinner) findViewById(R.id.Spinner01);

		// リストをセット
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.regist_mode_list_str,
				R.layout.my_simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mModeSpinner.setAdapter(adapter);

		//// イベントリスナーの生成 /////
		// EditTxt
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
		// NextButton
		mNextBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				NextBtnClick();
			}
		});
		// PrevButton
		mPrevBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				PrevBtnClick();
			}
		});
		// ListView
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
					onLocusListItemClick(parent, view, position, l);
				}
			});
		// mModeSpinner
		mModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				onModeSpinerItemSelected( parent, view, position, id);
            }
			@Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

		// 初期表示の設定
		mCharNum = getIntFromDB(makeSQL_getStartCharNum());
		if(mCharNum==0) mCurrentMode = 1;
		mModeSpinner.setSelection(mCurrentMode);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean("LocusRegist", true).commit();
	}

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean("LocusRegist", false).commit();
	}

	//==============================================================================
	// Widgetのイベント処理
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

	private void onLocusListItemClick(AdapterView<?> parent, View view, int position, long l){
		// EditTextのテキストを取得
		String liststr = mEditTxt.getText().toString();
		// 選択されたListViewの文字列をTextEditに表示
		String item = (String)parent.getItemAtPosition(position);
		mEditTxt.setText(item, BufferType.NORMAL);
		// LocusListから対象のitemを削除
		mListData.remove(position);
		// LocusListにEditTextの文字列を追加
		if(liststr.length() > 0) mListData.add(liststr);
		// ListViewの更新
		updateLocusListView();
	}

	private void onModeSpinerItemSelected(AdapterView<?> parent, View view, int position, long id){
		mCurrentMode = position;
		if(mCurrentMode==0){
			mCharNum = getIntFromDB(makeSQL_getStartCharNum());
		}else{
			mCharNum = getIntFromDB(makeSQL_getStartMoveNum());
			if(mCharNum==0) mCharNum = 1;
		}
		setLocusEditMode();
	}

	//==============================================================================
	// private method
	//==============================================================================

	// 軌道登録のモードを表示
	private void setLocusEditMode(){
		ShowEditChar();
		SetNextLocusList();
	}

	//現在のLocusListをDBに格納
	private void putCurrentLocusList(){
		if(mCurrentMode==0){
			// DBから該当のLucusListを削除
			exeNonQuery(makeSQL_deleteTargetList());
			if(mListData.size() > 0){
				// DBに現在のLocusListを追加
				exeNonQuery(makeSQL_insertLocusList());
			}
		}else{
			// DBから該当のLucusListを削除
			exeNonQuery(makeSQL_deleteTargetList_Move());
			if(mListData.size() > 0){
				// DBに現在のLocusListを追加
				exeNonQuery(makeSQL_insertLocusList_Move());
			}
		}
	}

	// メインテキストビューの表示
	private void ShowEditChar(){
		mCharNumTxtView.setText(String.valueOf(mCharNum));
		mMainCharView.setText(getMainChar());
		if(mCurrentMode==0){
			mRomaCharView.setText(getRomaChar());
		}else{
			mRomaCharView.setText("");
		}
	}

	private void SetNextLocusList(){
		// 登録された軌道の取得
		String sqlstr;
		if(mCurrentMode==0){
			sqlstr = makeSQL_getLocusList();
		}else{
			sqlstr = makeSQL_getMoveList();
		}
		mListData = getStringListFromDB(sqlstr);
		// ListViewの更新
		updateLocusListView();
		// EditTextのテキストをクリア
		mEditTxt.getEditableText().clear();
	}

	private String getMainChar(){
		String sqlstr;
		if(mCurrentMode==0){
			sqlstr = makeSQL_getMainChar();
		}else{
			sqlstr = makeSQL_getMainMove();
		}
		return getStringFromDB(sqlstr);
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
		while(c.moveToNext()){
			rtn.add(c.getString(0));
		}
		mydb.close();
		return rtn;
	}

	private int getIntFromDB(String SQLstr){
		int rtn = 0;
		mydb = hlpr.getWritableDatabase();
		SQLiteCursor c = (SQLiteCursor)mydb.rawQuery(SQLstr, null);
		c.moveToFirst();
		if(c.getCount()>0) rtn = c.getInt(0);
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

	private String makeSQL_getMainMove(){
		String rtn =  "SELECT mov FROM mov_master_data WHERE mov_no='";
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

	private String makeSQL_getMoveList(){
		String rtn = "SELECT locul_string FROM mov_data_table WHERE mov_no='";
		rtn += String.valueOf(mCharNum) + "';";
		return rtn;
	}

	private String makeSQL_deleteTargetList(){
		String rtn = "DELETE FROM char_data_table WHERE char_no='";
		rtn += String.valueOf(mCharNum) + "';";
		return rtn;
	}

	private String makeSQL_deleteTargetList_Move(){
		String rtn = "DELETE FROM mov_data_table WHERE mov_no='";
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

	private String makeSQL_insertLocusList_Move(){
		int i, cnt;
		String rtn, item;
		
		rtn = "INSERT INTO mov_data_table(locul_string, mov_no, stl_len) VALUES ";
		cnt = mListData.size();

		for(i=0;i<cnt;i++){
			if(i>0) rtn += ",";
			item = (String)mListData.get(i);
			rtn += "('" + item + "', " + String.valueOf(mCharNum) + " , ";
			rtn +=  String.valueOf(item.length()) + " )";
		}
		
		return rtn + ";";
	}

	private String makeSQL_getStartCharNum(){
		String rtn = "";
		rtn += "SELECT A.char_no FROM ";
		rtn += "(SELECT char_no FROM hira_master_table) AS A ";
		rtn += "LEFT JOIN ";
		rtn += "(SELECT char_no FROM char_data_table) AS B ";
		rtn += "ON A.char_no = B.char_no ";
		rtn += "WHERE B.char_no IS NULL ORDER BY A.char_no; ";
		return rtn;
	}

	private String makeSQL_getStartMoveNum(){
		String rtn = "";
		rtn += "SELECT A.mov_no FROM ";
		rtn += "(SELECT mov_no FROM mov_master_data) AS A ";
		rtn += "LEFT JOIN ";
		rtn += "(SELECT mov_no FROM mov_data_table) AS B ";
		rtn += "ON A.mov_no = B.mov_no ";
		rtn += "WHERE B.mov_no IS NULL ORDER BY A.mov_no; ";
		return rtn;
	}

}
