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

import android.view.MotionEvent;

public class myKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener{

    public interface OnKeyboardActionListener {

        void onPress(int primaryCode);

        void onRelease(int primaryCode);

        void onKey(int primaryCode, int[] keyCodes);

        void onKeyThrough(int primaryCode);

        void onText(CharSequence text);

        void swipeLeft();

        void swipeRight();

        void swipeDown();

        void swipeUp();
    }

    private OnKeyboardActionListener mKeyboardActionListener;

	private int mPaddingTop;
	private int mPaddingBottom;
	private int mPaddingLeft;
	private int mPaddingRight;

    private int mVerticalCorrection;
    private int mProximityThreshold;

    private Keyboard mKeyboard;
    private Key[] mKeys;
    private static final int NOT_A_KEY = -1;
	private int mOldThroughKey=NOT_A_KEY;

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
		super.setOnKeyboardActionListener(this);
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

    @Override
    public boolean onTouchEvent(MotionEvent me) {
		onModifiedTouchEvent(me);
		return super.onTouchEvent(me);
    }

    public void setKeyboard(Keyboard keyboard) {
		super.setKeyboard(keyboard);
        mKeyboard = keyboard;
        List<Key> keys = mKeyboard.getKeys();
        mKeys = keys.toArray(new Key[keys.size()]);
    }

    private int getKeyIndices(int x, int y) {
        final Key[] keys = mKeys;
        int primaryIndex = NOT_A_KEY;
        int closestKey = NOT_A_KEY;
        int closestKeyDist = mProximityThreshold + 1;
        int [] nearestKeyIndices = mKeyboard.getNearestKeys(x, y);
        final int keyCount = nearestKeyIndices.length;
        for (int i = 0; i < keyCount; i++) {
            final Key key = keys[nearestKeyIndices[i]];
            int dist = 0;
            boolean isInside = key.isInside(x,y);
            if (isInside) {
                primaryIndex = nearestKeyIndices[i];
            }

            if ((((dist = key.squaredDistanceFrom(x, y)) < mProximityThreshold)
                    || isInside)
                    && key.codes[0] > 32) {
                // Find insertion point
                final int nCodes = key.codes.length;
                if (dist < closestKeyDist) {
                    closestKeyDist = dist;
                    closestKey = nearestKeyIndices[i];
                }
            }
        }
        if (primaryIndex == NOT_A_KEY) {
            primaryIndex = closestKey;
        }
        return primaryIndex;
    }

    private void onModifiedTouchEvent(MotionEvent me) {
        int touchX = (int) me.getX() - mPaddingLeft;
        int touchY = (int) me.getY() - mPaddingTop;
        if (touchY >= -mVerticalCorrection)
            touchY += mVerticalCorrection;
        final int action = me.getAction();
        final long eventTime = me.getEventTime();
        int keyIndex = getKeyIndices(touchX, touchY);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
				send_ThroughKey(keyIndex);
                break;
        }
    }

	private void send_ThroughKey(int index){
        if (index != NOT_A_KEY && index < mKeys.length) {
            final Key key = mKeys[index];
			int code = key.codes[0];
			if(mOldThroughKey!=code && code!=NOT_A_KEY){
				mKeyboardActionListener.onKeyThrough(code);
			}
			mOldThroughKey = code;
		}
	}

}
