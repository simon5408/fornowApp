/*****************************************************************************
 *
 *                      FORNOW PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to ForNow
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from ForNow.
 *
 *            Copyright (c) 2014 by ForNow.  All rights reserved.
 *
 *****************************************************************************/
package com.fornow.app.ui.mine;

import com.fornow.app.R;
import com.fornow.app.util.CheckMobileAndEmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class EditGrxxActivity extends Activity {
	private EditText editText;
	private TextView editTitle;
	private String editType;
	private Handler mHandler;
	private static final int PHONE_NUM_ERROR = 0x00, EMAIL_NUM_ERROR = 0x01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_gerenxinxi);
		editText = (EditText) findViewById(R.id.edit_grxx_text);
		editTitle = (TextView) findViewById(R.id.edit_grxx_title);
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			if (intent.getExtras().get("title") != null) {
				editTitle.setText(intent.getExtras().get("title").toString());
				editType = intent.getExtras().get("title").toString();
			}
			if (intent.getExtras().get("data") != null) {
				editText.setText(intent.getExtras().get("data").toString());
			}
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				Toast toast = new Toast(EditGrxxActivity.this);
				switch (msg.what) {
				case PHONE_NUM_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_phone_no_error));
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				case EMAIL_NUM_ERROR:
					toastText.setText(getResources().getString(
							R.string.str_email_error));
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void clearStr(View v) {
		editText.setText("");
	}

	public void confirm(View v) {
		if (editText.getText() != null
				&& !editText.getText().toString().equals("")) {
			String text = editText.getText().toString();
			Intent intent = new Intent(EditGrxxActivity.this,
					GrxxActivity.class);
			if (editType.equals(getResources().getString(R.string.str_sjhm))) {
				if (CheckMobileAndEmail.isMobileNO(text)) {
					intent.putExtra("data", text);
					setResult(RESULT_OK, intent);
					this.finish();
				} else {
					Message updateViewMsg = mHandler
							.obtainMessage(PHONE_NUM_ERROR);
					mHandler.sendMessage(updateViewMsg);
				}
			} else if (editType.equals(getResources().getString(
					R.string.str_dzyx))) {
				if (CheckMobileAndEmail.checkEmail(text)) {
					intent.putExtra("data", text);
					setResult(RESULT_OK, intent);
					this.finish();
				} else {
					Message updateViewMsg = mHandler
							.obtainMessage(EMAIL_NUM_ERROR);
					mHandler.sendMessage(updateViewMsg);
				}
			}

		}

	}

	public void softBack(View v) {
		this.finish();
	}
}
