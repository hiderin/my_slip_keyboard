package com.example.my_slip_keyboad;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.content.SharedPreferences;
import android.content.Context;

public class NewKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard keyboard;
	//private SharedPreferences prefs;

	private boolean mDoubleKey = false;

    /** Instance of this service */
    private static NewKeyboard mSelf = null;

    /**
     * Constructor
     */
	public NewKeyboard(){
		super();
		mSelf = this;
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

        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.keyboard_en);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        keyboardView.setPreviewEnabled(false);
        return keyboardView;
    }

    //キーボードが表示されるたびに呼ばれるメソッド
    @Override
    public void onStartInputView(EditorInfo editorInfo, boolean restarting) {
		super.onStartInputView(editorInfo, restarting);
		//prefs = getSharedPreferences("NewKeyboardData", MODE_MULTI_PROCESS);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mDoubleKey = prefs.getBoolean("doubleKey", false);

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
        switch (primaryCode) {
            case KeyEvent.KEYCODE_1:
                ic.commitText("1", 1);
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
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case KeyEvent.KEYCODE_ENTER:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
				handleCharacter(primaryCode, keyCodes);
                break;
        }
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {

		if (mDoubleKey) {
			primaryCode = Character.toUpperCase(primaryCode);
        }
		getCurrentInputConnection().commitText(
				String.valueOf((char) primaryCode), 1);
    }

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

}
