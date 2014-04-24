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
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.CacheData;
import com.fornow.app.model.LoginData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.main.MainActivity;
import com.fornow.app.util.MD5Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
public class LoginActivity extends Activity {
	private boolean boolAutoLogin = false;
	private EditText userNameView, passwordView;
	private Handler mHandler;
	private Dialog dialog;
	private static final int LOGIN_COMPLETE = 0x00, NET_ERROR = 0x01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		dialog = new LoadingAnim(LoginActivity.this, R.style.my_dialog);
		userNameView = (EditText) findViewById(R.id.login_user_name);
		passwordView = (EditText) findViewById(R.id.login_user_password);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOGIN_COMPLETE:
					dialog.dismiss();
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					setResult(RESULT_OK, intent);
					LoginActivity.this.finish();
					break;
				case NET_ERROR:
					dialog.dismiss();
					View view = getLayoutInflater().inflate(
							R.layout.my_toast, null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(LoginActivity.this);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void clearUserName(View v) {
		userNameView.setText("");
	}

	public void clearPassword(View v) {
		passwordView.setText("");
	}

	public void boolAutoLogin(View v) {
		if (boolAutoLogin) {
			v.setBackgroundResource(R.drawable.checkbox_no);
			boolAutoLogin = false;
		} else {
			v.setBackgroundResource(R.drawable.checkbox_yes);
			boolAutoLogin = true;
		}
	}

	public void sendLoginRequest(final String userName, String password) {
		if (userName != null && !userName.equals("") && password != null
				&& !password.equals("")) {
			dialog.show();
			final String passMd5 = MD5Utils.getInstance().getStringHash(
					password);
			LoginData loginData = new LoginData();
			loginData.setUsername(userName);
			loginData.setPassword(passMd5);
			ControllerManager.getInstance().getLoginController()
					.unRegisterAll();
			ControllerManager.getInstance().getLoginController()
					.registerNotification(new ViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							// TODO Auto-generated method stub
							if (obj.getCode() == 200) {
								CacheData.getInstance().setAutologin(false);
								CacheData.getInstance().setLoginName(null);
								CacheData.getInstance().setLoginPass(null);
								if (boolAutoLogin) {
									CacheData.getInstance().setAutologin(true);
									CacheData.getInstance().setLoginName(
											userName);
									CacheData.getInstance().setLoginPass(
											passMd5);
								}

								Message updateViewMsg = mHandler
										.obtainMessage(LOGIN_COMPLETE);
								mHandler.sendMessage(updateViewMsg);
							} else {
								Message updateViewMsg = mHandler
										.obtainMessage(NET_ERROR);
								mHandler.sendMessage(updateViewMsg);
							}
						}
					});
			ControllerManager.getInstance().getLoginController()
					.login(loginData);
		}else{
			View view = getLayoutInflater().inflate(
					R.layout.my_toast, null);
			TextView toastText = (TextView) view
					.findViewById(R.id.toast_text);
			toastText.setText(getResources().getString(
					R.string.str_login_empty));
			Toast toast = new Toast(LoginActivity.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(view);
			toast.show();
		}
	}

	public void login(View v) {
		String userName = userNameView.getText().toString();
		String password = passwordView.getText().toString();
		sendLoginRequest(userName, password);
	}

	public void register(View v) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		this.startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (data.getExtras() != null
					&& data.getExtras().get("username") != null
					&& data.getExtras().get("password") != null) {
				String userName = data.getExtras().get("username").toString();
				String password = data.getExtras().get("password").toString();
				userNameView.setText(userName);
				passwordView.setText(password);
				sendLoginRequest(userName, password);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			softBack(null);
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public void softBack(View v) {
		this.finish();
	}

}
