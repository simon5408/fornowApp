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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.LoginData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.captcha.CheckAction;
import com.fornow.app.ui.captcha.CheckGetUtil;
import com.fornow.app.ui.captcha.CheckView;
import com.fornow.app.ui.captcha.ConmentConfig;
import com.fornow.app.util.MD5Utils;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class RegisterActivity extends Activity {
	private EditText userNameView, passwordView, againPasswordView,
			yanchengmaView;
	private Handler mHandler;
	private static final int REGISTER_COMPLETE = 0x00, NET_ERROR = 0x01,
			UPDATA_CHECKNUM = 0x02;
	private Dialog dialog;
	private CheckAction mCheckView;
	private String username, password;
	int[] checkNum = { 0, 0, 0, 0 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		userNameView = (EditText) findViewById(R.id.register_user_name);
		passwordView = (EditText) findViewById(R.id.register_user_password);
		againPasswordView = (EditText) findViewById(R.id.register_user_password_again);
		yanchengmaView = (EditText) findViewById(R.id.register_user_yzm);
		dialog = new LoadingAnim(RegisterActivity.this, R.style.my_dialog);
		mCheckView = (CheckView) findViewById(R.id.checkView);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case REGISTER_COMPLETE:
					dialog.dismiss();
					Intent intent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					intent.putExtra("username", username);
					intent.putExtra("password", password);
					setResult(RESULT_OK, intent);
					RegisterActivity.this.finish();
					break;
				case NET_ERROR:
					dialog.dismiss();
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(RegisterActivity.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
					break;
				case UPDATA_CHECKNUM:
					mCheckView.invaliChenkNum();
					break;
				default:
					break;
				}
			}
		};
		initCheckNum();
	}

	public void initCheckNum() {
		checkNum = CheckGetUtil.getCheckNum();
		mCheckView.setCheckNum(checkNum);
		mCheckView.invaliChenkNum();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		new Thread(new myThread()).start();
		super.onResume();
	}

	public void clearUserName(View v) {
		userNameView.setText("");
	}

	public void clearPasswordAgain(View v) {
		againPasswordView.setText("");
	}

	public void clearPassword(View v) {
		passwordView.setText("");
	}

	public void clearYanzhengma(View v) {
		yanchengmaView.setText("");
	}

	public void replaceCheckImg(View v) {
		initCheckNum();
	}

	public void register(View v) {
		if (userNameView.getText().toString() != null
				&& !userNameView.getText().toString().equals("")
				&& passwordView.getText().toString() != null
				&& !passwordView.getText().toString().equals("")
				&& againPasswordView.getText().toString() != null
				&& !againPasswordView.getText().toString().equals("")) {
			if (passwordView.getText().toString()
					.equals(againPasswordView.getText().toString())) {
				String checkCode = yanchengmaView.getText().toString();
				if (CheckGetUtil.checkNum(checkCode, checkNum)) {
					dialog.show();
					username = userNameView.getText().toString();
					password = passwordView.getText().toString();
					final String passMd5 = MD5Utils.getInstance()
							.getStringHash(password);
					LoginData loginData = new LoginData();
					loginData.setUsername(username);
					loginData.setPassword(passMd5);
					ControllerManager.getInstance().getLoginController()
							.unRegisterAll();
					ControllerManager.getInstance().getLoginController()
							.registerNotification(new ViewListener() {

								@Override
								public void updateView(ViewUpdateObj obj) {
									if (obj.getCode() == 200) {
										Message updateViewMsg = mHandler
												.obtainMessage(REGISTER_COMPLETE);
										mHandler.sendMessage(updateViewMsg);
									} else {
										Message updateViewMsg = mHandler
												.obtainMessage(NET_ERROR);
										mHandler.sendMessage(updateViewMsg);
									}
								}
							});
					ControllerManager.getInstance().getLoginController()
							.register(loginData);
				} else {
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_qzqsryzm));
					Toast toast = new Toast(RegisterActivity.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(view);
					toast.show();
				}

			} else {
				// 两次输入密码不一样
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				toastText.setText(getResources().getString(
						R.string.str_register_password_nomatch));
				Toast toast = new Toast(RegisterActivity.this);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(view);
				toast.show();
			}
		} else {
			// 某一栏为空
			View view = getLayoutInflater().inflate(R.layout.my_toast, null);
			TextView toastText = (TextView) view.findViewById(R.id.toast_text);
			toastText.setText(getResources()
					.getString(R.string.str_login_empty));
			Toast toast = new Toast(RegisterActivity.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(view);
			toast.show();
		}

	}

	public void softBack(View v) {
		this.finish();
	}

	class myThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Message updateViewMsg = mHandler.obtainMessage(UPDATA_CHECKNUM);
				mHandler.sendMessage(updateViewMsg);
				try {
					Thread.sleep(ConmentConfig.PTEDE_TIME);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
