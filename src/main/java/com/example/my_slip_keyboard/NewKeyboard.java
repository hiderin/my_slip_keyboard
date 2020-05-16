package com.example.my_slip_keyboard;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.content.SharedPreferences;
import android.content.Context;

import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import java.util.ArrayList;
import java.util.List;

public class NewKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private CompletionInfo[] mCompletions;
    private boolean mCompletionOn;

	// View
    private CandidateView mCandidateView;
    private KeyboardView kv;
	// keyboard
    private Keyboard en_keyboard;
    private Keyboard m12KeyNumKeyboard;
    private Keyboard mSymbolsKeyboard ;
    private Keyboard mSymbolsShiftedKeyboard ;
    private Keyboard mJpnKeyboard;

	// 変数
    private int mCapsLock = 0;
	//private StringBuilder mComposingTxt;
	private ComposingBuilder mComposingTxt;
	private String mCommitTxt;
	private Roma2Hira r2h;
    private String mTemporaryText;
	private ArrayList<String> mCandidateList;
	private boolean mCandidateOn;

	private boolean mDoubleKey = false;

	// 初回起動判定
    public static final int PREFERENCE_INIT = 0;
    public static final int PREFERENCE_BOOTED = 1;

    //データ保存
    private void setState(int state) {
        // SharedPreferences設定を保存
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putInt("InitState", state).commit();
    }

    //データ読み出し
    private int getState() {
        // 読み込み
        int state;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        state = sp.getInt("InitState", PREFERENCE_INIT);
        return state;
    }

    /** Instance of this service */
    private static NewKeyboard mSelf = null;

    /**
     * Constructor
     */
	public NewKeyboard(){
		super();
		mSelf = this;
		mComposingTxt = new ComposingBuilder(this);
		mCommitTxt = "";
		r2h = new Roma2Hira();
	}
    public NewKeyboard(Context context) {
        this();
        attachBaseContext(context);
	}


    //初回だけ呼ばれる
    @Override
    public void onCreate() {
        super.onCreate();
    }

	public static NewKeyboard getInstance(){
		return mSelf;
	}

    //初回だけ呼ばれる
    @Override
    public View onCreateInputView() {
        super.onCreateInputView();

		// キーボードxmlのインスタンス化
        en_keyboard = new Keyboard(this, R.xml.keyboard_en);
        m12KeyNumKeyboard = new Keyboard(this, R.xml.keyboard_12key_num);
        mSymbolsKeyboard = new Keyboard(this, R.xml.symbols);
        mSymbolsShiftedKeyboard = new Keyboard(this, R.xml.symbols_shift);
        mJpnKeyboard = new Keyboard(this, R.xml.keyboard_jp);

		// KeyboardViewのセット
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        kv.setKeyboard(mJpnKeyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
        return kv;
    }

    /**
     * Called by the framework when your view for showing candidates needs to
     * be generated, like {@link #onCreateInputView}.
     */
    @Override public View onCreateCandidatesView() {
        mCandidateView = new CandidateView(this);
        mCandidateView.setService(this);
        return mCandidateView;
    }


    /**
     * This is the main point where we do our initialization of the input method
     * to begin operating on an application.  At this point we have been
     * bound to the client, and are now receiving all of the detailed information
     * about the target of our edits.
     */
    @Override public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

//        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;
		mCandidateOn = false;
		mTemporaryText = "";

        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        mComposingTxt.setLength(0);
        updateCandidates();

		// 初回起動時の処理
        if(PREFERENCE_INIT == getState() ){
			if(UnzipMydict()){
                setState(PREFERENCE_BOOTED);
			}
        }
    }

	private boolean UnzipMydict(){
		String path = null;
		try {
			AssetManager    am  = getResources().getAssets();
			InputStream     is  = am.open("mydict.zip", AssetManager.ACCESS_STREAMING);
			ZipInputStream  zis = new ZipInputStream(is);
			ZipEntry        ze  = zis.getNextEntry();

			if (ze != null) {

				//// databasesフォルダ作成用ダミー
				//DummyRegister dreg = new DummyRegister(this);
				//dreg.CreateDatabase();

				path = getDatabasePath("mydict.db").toString();
				FileOutputStream fos = new FileOutputStream(path, false);
				byte[] buf = new byte[1024];
				int size = 0;

				while ((size = zis.read(buf, 0, buf.length)) > -1) {
					fos.write(buf, 0, size);
				}
				fos.close();
				zis.closeEntry();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

    /**
     * This tells us about completions that the editor has determined based
     * on the current text in it.  We want to use this in fullscreen mode
     * to show the completions ourself, since the editor can not be seen
     * in that situation.
     */
    @Override public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }

            List<String> stringList = new ArrayList<String>();
            for (int i = 0; i < completions.length; i++) {
                CompletionInfo ci = completions[i];
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);
        }
    }

    //キーボードが表示されるたびに呼ばれるメソッド
    @Override
    public void onStartInputView(EditorInfo editorInfo, boolean restarting) {
		super.onStartInputView(editorInfo, restarting);
		//prefs = getSharedPreferences("NewKeyboardData", MODE_MULTI_PROCESS);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mDoubleKey = prefs.getBoolean("doublekey", false);

    }

    //キーボードが閉じる時に呼ばれるメソッド
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //キーを押した時
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        InputConnection ic = getCurrentInputConnection();
		Keyboard current = kv.getKeyboard();

        switch (primaryCode) {
            case KeyEvent.KEYCODE_1:
                ic.commitText( mDoubleKey ? "11" : "1", 1);
                break;
            case KeyEvent.KEYCODE_2:
                ic.commitText("2", 1);
                break;
            case KeyEvent.KEYCODE_3:
                ic.commitText("3", 1);
                break;
            case KeyEvent.KEYCODE_4:
                ic.commitText("4", 1);
                break;
            case KeyEvent.KEYCODE_5:
                ic.commitText("5", 1);
                break;
            case KeyEvent.KEYCODE_6:
                ic.commitText("6", 1);
                break;
            case KeyEvent.KEYCODE_7:
                ic.commitText("7", 1);
                break;
            case KeyEvent.KEYCODE_8:
                ic.commitText("8", 1);
                break;
            case KeyEvent.KEYCODE_9:
                ic.commitText("9", 1);
                break;
            case KeyEvent.KEYCODE_0:
                ic.commitText("0", 1);
                break;
            case Keyboard.KEYCODE_SHIFT:
				handleShift();
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
				nextKeyboard();
                break;
            case Keyboard.KEYCODE_DELETE:
				handleBackSpace();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
                break;
            case KeyEvent.KEYCODE_ENTER:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case KeyEvent.KEYCODE_SPACE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
				if(primaryCode >= 10000){
					// 定型文の入力
					mTemporaryText = getFixText(primaryCode);
					if(!mTemporaryText.isEmpty()){
						mCandidateList = getFixList(primaryCode);
						ic.setComposingText(mTemporaryText,1);
						updateCandidates(mCandidateList);
						mCandidateOn = true;
					}
				}
				else if(current==mJpnKeyboard){
					mComposingTxt.append(String.valueOf((char) primaryCode));
					mCommitTxt = r2h.getHiraText(mComposingTxt.toString());
					if(mCommitTxt.isEmpty()){
						ic.setComposingText(mComposingTxt.me(), mComposingTxt.length());
					}
					else{
						ic.commitText(mCommitTxt, mCommitTxt.length());
						mComposingTxt.setLength(0);
						mCommitTxt = "";
					}
				}
				else{
					handleCharacter(primaryCode, keyCodes);
					break;
				}
        }
    }

	//------------------------------------------------------------------------------
	// onkeyに伴うメソッド

    private void handleCharacter(int primaryCode, int[] keyCodes) {
		if (kv.isShifted()) {
			primaryCode = Character.toUpperCase(primaryCode);
			if (mCapsLock==1) {
				handleShift(false);
			}
        }
		getCurrentInputConnection().commitText(
				String.valueOf((char) primaryCode), 1);
    }

	private void handleSpace(){
        InputConnection ic = getCurrentInputConnection();
		ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
	}

	private void handleBackSpace(){
        InputConnection ic = getCurrentInputConnection();
		if(mComposingTxt.length()==0){
			ic.deleteSurroundingText(1, 0);
		}
		else{
			mComposingTxt.setLength(mComposingTxt.length()-1);
			ic.setComposingText(mComposingTxt.me(), mComposingTxt.length());
		}
	}

	private void handleShift(boolean swt){
		Keyboard current = kv.getKeyboard();
		if(current==en_keyboard){
			if(mCapsLock==2 || !swt){
				mCapsLock=0;
				kv.setShifted(false);
			}else{
				mCapsLock++;
				kv.setShifted(true);
			}
		}
		else if(current==m12KeyNumKeyboard){
			kv.setKeyboard(mSymbolsKeyboard);
			kv.setShifted(true);
		}
		else if(current==mSymbolsKeyboard){
			kv.setKeyboard(m12KeyNumKeyboard);
			kv.setShifted(false);
		}
		else if(current==mJpnKeyboard){
			kv.setKeyboard(mSymbolsShiftedKeyboard);
			kv.setShifted(true);
		}
		else if(current==mSymbolsShiftedKeyboard){
			kv.setKeyboard(mJpnKeyboard);
			kv.setShifted(false);
		}
	}

	private void handleShift(){ handleShift(true);}

	private void nextKeyboard(){
		Keyboard current = kv.getKeyboard();
		if(current==en_keyboard){
			current = m12KeyNumKeyboard;
		}
		else if(current==m12KeyNumKeyboard){
			current = mJpnKeyboard;
		}
		else{
			current = en_keyboard;
		}
		kv.setKeyboard(current);
		kv.setShifted(false);
		mCapsLock = 0;
	}

	//------------------------------------------------------------------------------

	private String getFixText(int ipCode){
		switch(ipCode){
			case 10000: return "立正佼成会";
			case 10100: return "お役";
			case 10200: return "導師";
			case 10300: return "鐘";
			case 10400: return "木鉦";
			case 10500: return "太鼓";
			case 10600: return "今日は13:00から教会で練習があります。";
			case 10700: return "久美ちゃん";
			case 10800: return "よちゃん";
			case 10900: return "。";
			case 11000: return "今";
			case 11100: return "帰るよ";
			case 11200: return "迎えに行くね！";
			case 11300: return "行くね！";
			case 11400: return "駐車場で待ってるね！";
			case 11500: return "終るよ";
			case 11600: return "もう少しかかるよ。";
			case 11700: return "遅くなる";
			case 11800: return "ありがとうね";
			case 11900: return "すみません";
			case 12000: return "お疲れさま";
			case 12100: return "おはよう";
			case 12200: return "こんばんは";
			case 12300: return "了解";
			case 12400: return "よろしくね";
			case 12500: return "お願いします";
			case 12600: return "です";
			default: return "";
		}
	}

	private ArrayList<String> getFixList(int ipCode){
		ArrayList<String> rtnList = new ArrayList<String>();
		switch(ipCode){
			case 10000:
				rtnList.add("立正佼成会");
				rtnList.add("広島教会");
				break;
			case 10100:
				rtnList.add("お役");
				rtnList.add("導師");
				rtnList.add("鐘");
				rtnList.add("木鉦");
				rtnList.add("太鼓");
				break;
			case 10600:
				rtnList.add("今日は13:00から教会で練習があります。");
				rtnList.add("今日は19:30から五日市道場で練習があります。");
				break;
			case 10700:
				rtnList.add("久美ちゃん");
				rtnList.add("芽生ちゃん");
				rtnList.add("真悠ちゃん");
				break;
			case 10800:
				rtnList.add("よちゃん");
				rtnList.add("ばあばちゃん");
				rtnList.add("じいじ");
				break;
			case 10900:
				rtnList.add("。");
				break;
			case 11000:
				rtnList.add("今");
				rtnList.add("今から");
				break;
			case 11100:
				rtnList.add("帰るよ");
				rtnList.add("帰ったよ");
				break;
			case 11200:
				rtnList.add("迎えに行くね！");
				break;
			case 11300:
				rtnList.add("行くね！");
				break;
			case 11400:
				rtnList.add("駐車場で待ってるね！");
				break;
			case 11500:
				rtnList.add("終るよ");
				rtnList.add("終ったよ");
				break;
			case 11600:
				rtnList.add("もう少しかかるよ。");
				rtnList.add("もう少し");
				rtnList.add("少し");
				break;
			case 11700:
				rtnList.add("遅くなる");
				rtnList.add("遅れます。");
				break;
			case 11800:
				rtnList.add("ありがとうね");
				rtnList.add("ありがとうございます");
				break;
			case 11900:
				rtnList.add("すみません");
				rtnList.add("ごめんね");
				break;
			case 12000:
				rtnList.add("お疲れさま");
				rtnList.add("お疲れ様です");
				break;
			case 12100:
				rtnList.add("おはよう");
				rtnList.add("おはようございます");
				break;
			case 12200:
				rtnList.add("こんばんは");
				break;
			case 12300:
				rtnList.add("了解");
				rtnList.add("了解です");
				break;
			case 12400:
				rtnList.add("よろしくね");
				rtnList.add("よろしくお願いします");
				break;
			case 12500:
				rtnList.add("お願いします");
				break;
		}
		return rtnList;
	}

	//------------------------------------------------------------------------------

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }

	//------------------------------------------------------------------------------
	// Candidate

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void updateCandidates(ArrayList<String> list) {
        if (!mCompletionOn) {
			setSuggestions(list, true, true);
        }
    }

    private void updateCandidates() {
        if (!mCompletionOn) {
            //if (mComposingTxt.length() > 0) {
            if (mTemporaryText.length() > 0) {
//				mCandidateList=mComposing.getCandidateList();
                setSuggestions(mCandidateList, true, true);
				mCandidateOn = true;
            } else {
                setSuggestions(null, false, false);

            }
        }
    }
    public void setSuggestions(List<String> suggestions, boolean completions,
            boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (mCandidateView != null) {
            mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }

    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (mCandidateView != null) {
                mCandidateView.clear();
            }
		} else if(mCandidateOn){
			InputConnection ic = getCurrentInputConnection();
//			int sp = getComposingStartPoint();
			int sp = 0;
			ic.setSelection(sp,sp+mComposingTxt.length());
			ic.commitText(mCandidateList.get(index),1);
//			mComposing.RebuildForIndex(index);
//			int length = mComposing.length();
//			if (length > 0) {
//				ic.setComposingText(mComposing.hira(), 1);
//			} else if (length > 0) {
//				ic.commitText("", 0);
//			} 
			mCandidateOn =false;
			mTemporaryText = "";
        } else if (mComposingTxt.length() > 0) {

            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
//            commitTyped(getCurrentInputConnection(),null);
        }
		updateCandidates();
    }
}
