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
package com.fornow.app.ui.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.CacheData;
import com.fornow.app.model.LoginData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.main.MainActivity;
import com.fornow.app.utils.MD5Utils;

/**
 * @author Simon Lv 2013-11-2
 */
public class LoginActivity extends Activity {
	private boolean boolAutoLogin = false;
	private EditText userNameView, passwordView;
	private TextView userNameErrorMsg, passWordErrorMsg;
	private Handler mHandler;
	private Dialog dialog;
	private Context mContext;
	private static final int LOGIN_COMPLETE = 0x00, NET_ERROR = 0x01,
			USER_NOT_EXISTS = 0x02, PASSWORD_ERROR = 0x03;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mContext = this.getApplicationContext();
		dialog = new LoadingAnim(LoginActivity.this, R.style.my_dialog);
		userNameView = (EditText) findViewById(R.id.login_user_name);
		//userNameView.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		passwordView = (EditText) findViewById(R.id.login_user_password);
		userNameErrorMsg = (TextView) findViewById(R.id.username_error_msg);
		passWordErrorMsg = (TextView) findViewById(R.id.password_error_msg);
		userNameView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				userNameErrorMsg.setVisibility(View.GONE);
			}
		});
		passwordView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				passWordErrorMsg.setVisibility(View.GONE);
			}
		});
		userNameView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					checkPhone();
					return true;
				}
				return false;
			}
		});
		if (CacheData.getInstance().getLoginName() != null) {
			userNameView.setText(CacheData.getInstance().getLoginName());
			Spannable spanText = (Spannable) userNameView.getText();
			Selection.setSelection(spanText, userNameView.getText().length());
		}
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onStart() {
		
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
				case USER_NOT_EXISTS:
					dialog.dismiss();
					userNameErrorMsg.setText(mContext.getResources().getString(
							R.string.str_phone_not_exists));
					userNameErrorMsg.setVisibility(View.VISIBLE);
					break;
				case PASSWORD_ERROR:
					dialog.dismiss();
					passWordErrorMsg.setText(mContext.getResources().getString(
							R.string.str_pass_error));
					passWordErrorMsg.setVisibility(View.VISIBLE);
					break;
				case NET_ERROR:
					dialog.dismiss();
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
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

	public void checkPhone() {
//		if (!CheckMobileAndEmailAndPost.isMobileNO(userNameView.getText() + "")) {
//			userNameErrorMsg.setText(mContext.getResources().getString(
//					R.string.str_wrong_phone));
//			userNameErrorMsg.setVisibility(View.VISIBLE);
//		} else {
//			userNameErrorMsg.setVisibility(View.GONE);
//		}
	}

	public void checkPass() {
//		if (passwordView.getText().toString() == null
//				|| passwordView.getText().toString().equals("")) {
//			passWordErrorMsg.setText(mContext.getResources().getString(
//					R.string.str_pass_empty));
//			passWordErrorMsg.setVisibility(View.VISIBLE);
//		} else {
//			passWordErrorMsg.setVisibility(View.GONE);
//		}
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	public void clearUserName(View v) {
		userNameView.setText("");
		userNameErrorMsg.setVisibility(View.GONE);
	}

	public void clearPassword(View v) {
		passwordView.setText("");
		passWordErrorMsg.setVisibility(View.GONE);
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
		checkPhone();
		checkPass();
		if (userNameErrorMsg.getVisibility() == View.GONE
				&& passWordErrorMsg.getVisibility() == View.GONE) {
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
							
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								CacheData.getInstance().setAutologin(
										boolAutoLogin);
								CacheData.getInstance().setLoginName(userName);
								CacheData.getInstance().setLoginPass(passMd5);
								updateViewMsg = mHandler
										.obtainMessage(LOGIN_COMPLETE);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 403:
								updateViewMsg = mHandler
										.obtainMessage(USER_NOT_EXISTS);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 401:
								updateViewMsg = mHandler
										.obtainMessage(PASSWORD_ERROR);
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
					.login(loginData);
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
