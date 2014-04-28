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
package com.fornow.app.ui.setting;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.customdialog.LoginDialog;
import com.fornow.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Simon Lv 2013-11-5
 */
public class YjfkActivity extends Activity {
	private EditText suggestion;
	private static final int UUID_TIMEOUT = 0x00, NET_ERROR = 0x01,
			SUGGEST_SUCCEST = 0x02;
	private Dialog dialog;
	private Context mContext;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			View view = getLayoutInflater().inflate(R.layout.my_toast, null);
			TextView toastText = (TextView) view.findViewById(R.id.toast_text);
			Toast toast = new Toast(YjfkActivity.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(view);
			switch (msg.what) {
			case SUGGEST_SUCCEST:
				dialog.dismiss();
				suggestion.setText("");
				toastText.setText(getResources().getString(
						R.string.str_suggest_success));
				toast.show();
				break;
			case NET_ERROR:
				dialog.dismiss();
				toastText.setText(getResources().getString(
						R.string.str_net_error));
				toast.show();
				break;
			case UUID_TIMEOUT:
				dialog.dismiss();
				LoginDialog loginDialog = new LoginDialog(YjfkActivity.this,
						mContext, mContext.getResources().getString(
								R.string.str_tishi), mContext.getResources()
								.getString(R.string.str_uuid_timeout), 0);
				loginDialog.build();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yjfk);
		mContext = this.getApplication();
		suggestion = (EditText) findViewById(R.id.suggestion);
		dialog = new LoadingAnim(YjfkActivity.this, R.style.my_dialog);
	}

	public void softBack(View v) {
		this.finish();
	}

	public void submit(View v) {
		String text = suggestion.getText().toString();
		if (text != null && !text.equals("")) {
			ControllerManager.getInstance().getLoginController()
					.unRegisterAll();
			ControllerManager.getInstance().getLoginController()
					.registerNotification(new ViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								updateViewMsg = mHandler
										.obtainMessage(SUGGEST_SUCCEST);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 408:
								updateViewMsg = mHandler
										.obtainMessage(UUID_TIMEOUT);
								mHandler.sendMessage(updateViewMsg);
								break;
							default:
								updateViewMsg = mHandler
										.obtainMessage(NET_ERROR);
								mHandler.sendMessage(updateViewMsg);
								break;
							}
						}
					});
			ControllerManager.getInstance().getLoginController()
					.sendSuggestion(text);
		}
	}
}
