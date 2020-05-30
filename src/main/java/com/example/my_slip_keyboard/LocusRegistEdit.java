/* 軌道登録用設定画面のActivity */

package com.example.my_slip_keyboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;

//public class LocusRegistEdit extends AppCompatActivity{
public class LocusRegistEdit extends Activity{

    /** Widgets which constitute this screen of activity */
    private Button mNextBtn;
    private Button mPrevBtn;
	private EditText mEditTxt;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locus_regist_edit);

        /* get widgets */
        mNextBtn = (Button)findViewById(R.id.nextButton);
        mPrevBtn = (Button)findViewById(R.id.prevButton);

		mEditTxt = (EditText)findViewById(R.id.LRegEditHira);
		mEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					if(event.getAction() == KeyEvent.ACTION_UP) {
						EditTextOnEnter();
					}
					return true;
				}
				return false;
			}
		});

    }

    /** @see android.view.View.OnClickListener */
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nextButton:
                /* save the word */
                NextBtnClick();
                break;
 
            case R.id.prevButton:
                /* cancel the edit */
                PrevBtnClick();
                break;
        }
    }
        private void NextBtnClick() {
                // 保存
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//                sp.edit().putString("SaveString", mEditTextLocus.getText().toString()).commit();
        }

        private void PrevBtnClick() {
			finish();
                // 読み込み
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//                mEditTextHira.setText(sp.getString("SaveString", null), BufferType.NORMAL);
        }

		private void EditTextOnEnter(){
		}
}
