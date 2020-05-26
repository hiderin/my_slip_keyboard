/* 軌道登録用設定画面のActivity */

package com.example.my_slip_keyboard;

import android.app.Activity;
//import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//public class LocusRegistEdit extends AppCompatActivity{
public class LocusRegistEdit extends Activity{

    /** Widgets which constitute this screen of activity */
//    private EditText mEditTextHira;
//    private EditText mEditTextLocus;
    private Button mRegistBtn;
    private Button mCancelBtn;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locus_regist_edit);

        /* get widgets */
//		mEditTextHira = (EditText)findViewById(R.id.LRegEditHira);
//		mEditTextLocus = (EditText)findViewById(R.id.LRegEditLocus);
        mRegistBtn = (Button)findViewById(R.id.registButton);
        mCancelBtn = (Button)findViewById(R.id.cancelButton);

        /* set the listener */
//        mRegistBtn.setOnClickListener(this);
//        mCancelBtn.setOnClickListener(this);

    }

    /** @see android.view.View.OnClickListener */
    public void onClick(View v) {

//        mEntryButton.setEnabled(false);
//        mCancelButton.setEnabled(false);

        switch (v.getId()) {
            case R.id.registButton:
                /* save the word */
                RegistBtnClick();
                break;
 
            case R.id.cancelButton:
                /* cancel the edit */
                CancelBtnClick();
                break;

//            default:
//                Log.e("OpenWnn", "onClick: Get Invalid ButtonID. ID=" + v.getId());
//                finish();
//                return;
        }
    }
        private void RegistBtnClick() {
                // 保存
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//                sp.edit().putString("SaveString", mEditTextLocus.getText().toString()).commit();
        }

        private void CancelBtnClick() {
			finish();
                // 読み込み
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//                mEditTextHira.setText(sp.getString("SaveString", null), BufferType.NORMAL);
        }
}
