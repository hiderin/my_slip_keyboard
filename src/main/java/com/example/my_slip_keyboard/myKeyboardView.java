package com.example.my_slip_keyboard;

import android.content.Context;
import android.util.AttributeSet;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myKeyboardView extends KeyboardView {

    private static final int NOT_A_KEY = -1;
	private int mOldThroughKey=NOT_A_KEY;
    private OnKeyboardActionListener mKeyboardActionListener;
    private Key[] mKeys;

    /**
     * Listener for virtual keyboard events.
     */
    public interface OnKeyboardActionListener {
        void onKeyThrough(int primaryCode);

        //キーを押した時
        void onKey(int primaryCode, int[] keyCodes);

        void onPress(int primaryCode);

        void onRelease(int primaryCode);

        void onText(CharSequence text);

        void swipeLeft();

        void swipeRight();

        void swipeDown();

        void swipeUp();
    }

	//コンストラクタ
    public myKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public myKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	@Override
    public void setKeyboard(Keyboard keyboard) {
		super.setKeyboard(keyboard);
        List<Key> keys = keyboard.getKeys();
        mKeys = keys.toArray(new Key[keys.size()]);
    }

    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
		super.setOnKeyboardActionListener((KeyboardView.OnKeyboardActionListener) listener);
        mKeyboardActionListener = listener;
    }

//	//イベント
//	@Override
//    void onKey(int primaryCode, int[] keyCodes){
//		super.onKey(primaryCode, keyCodes);
//	}

	private void send_ThroughKey(int index){
        if (index < mKeys.length) {
            final Key key = mKeys[index];
			int code = key.codes[0];
			if(mOldThroughKey!=code && code!=NOT_A_KEY){
				mKeyboardActionListener.onKeyThrough(code);
			}
			mOldThroughKey = code;
		}
	}

}
