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

public class myKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener{

    public interface OnKeyboardActionListener {

        void onPress(int primaryCode);

        void onRelease(int primaryCode);

        void onKey(int primaryCode, int[] keyCodes);

        void onText(CharSequence text);

        void swipeLeft();

        void swipeRight();

        void swipeDown();

        void swipeUp();
    }

    private OnKeyboardActionListener mKeyboardActionListener;

	//コンストラクタ
    public myKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public myKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public myKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//	}

    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
        mKeyboardActionListener = listener;
    }

	//OnKeyboardActionListenerのOverride
    @Override
	public void onPress(int primaryCode){
		mKeyboardActionListener.onPress(primaryCode);
	}

    @Override
	public void onRelease(int primaryCode){
		mKeyboardActionListener.onRelease(primaryCode);
	}

    @Override
	public void onKey(int primaryCode, int[] keyCodes){
		mKeyboardActionListener.onKey(primaryCode, keyCodes);
	}

    @Override
	public void onText(CharSequence text){
		mKeyboardActionListener.onText(text);
	}

    @Override
	public void swipeLeft(){
		mKeyboardActionListener.swipeLeft();
	}

    @Override
	public void swipeRight(){
		mKeyboardActionListener.swipeRight();
	}

    @Override
	public void swipeDown(){
		mKeyboardActionListener.swipeDown();
	}

    @Override
	public void swipeUp(){
		mKeyboardActionListener.swipeUp();
	}

}
