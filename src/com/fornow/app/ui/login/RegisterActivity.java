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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.fornow.app.model.DeviceData;
import com.fornow.app.model.RegisterData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.utils.CheckMobileAndEmailAndPost;
import com.fornow.app.utils.GsonTool;
import com.fornow.app.utils.MD5Utils;
import com.fornow.app.utils.captcha.ConmentConfig;

/**
 * @author Simon Lv 2013-11-3
 */
public class RegisterActivity extends Activity {
	private EditText userNameView, passwordView, againPasswordView,
			yanchengmaView;
	private TextView phone_error_msg, pass_empty_msg, pass_error_msg,
			checkcode_error_msg;
	private static final int REGISTER_COMPLETE = 0x00, NET_ERROR = 0x01,
			UPDATA_CHECKNUM = 0x02, ALREADY_REGISTER = 0x03,
			CHECKCODE_TIMEOUT = 0x04, CHECKCODE_ERROR = 0x05,
			PHONE_NUM_ERROR = 0x06;
	private Dialog dialog;
	private Context mContext;
	private String username, password;
	//, checkCode
	int[] checkNum = { 0, 0, 0, 0 };
	private String deviceId;
	private Handler mHandler = new Handler() {
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
			case ALREADY_REGISTER:
				dialog.dismiss();
				phone_error_msg.setText(mContext.getResources().getString(
						R.string.str_phone_already_register));
				phone_error_msg.setVisibility(View.VISIBLE);
				break;
			case CHECKCODE_TIMEOUT:
				dialog.dismiss();
				checkcode_error_msg.setText(mContext.getResources().getString(
						R.string.str_checkcode_timeout));
				checkcode_error_msg.setVisibility(View.VISIBLE);
				break;
			case CHECKCODE_ERROR:
				dialog.dismiss();
				checkcode_error_msg.setText(mContext.getResources().getString(
						R.string.str_checkcode_error));
				checkcode_error_msg.setVisibility(View.VISIBLE);
				break;
			case PHONE_NUM_ERROR:
				dialog.dismiss();
				phone_error_msg.setText(mContext.getResources().getString(
						R.string.str_phonenum_error));
				phone_error_msg.setVisibility(View.VISIBLE);
				break;
			case NET_ERROR:
				dialog.dismiss();
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
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
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mContext = this.getApplicationContext();
		prepareView();
	}

	public void prepareView() {
		dialog = new LoadingAnim(RegisterActivity.this, R.style.my_dialog);
		userNameView = (EditText) findViewById(R.id.register_user_name);
		passwordView = (EditText) findViewById(R.id.register_user_password);
		againPasswordView = (EditText) findViewById(R.id.register_user_password_again);
		yanchengmaView = (EditText) findViewById(R.id.register_user_yzm);
		phone_error_msg = (TextView) findViewById(R.id.phone_error_msg);
		pass_error_msg = (TextView) findViewById(R.id.password_error_msg);
		pass_empty_msg = (TextView) findViewById(R.id.pass_empty_msg);
		checkcode_error_msg = (TextView) findViewById(R.id.checkcode_error_msg);
		userNameView.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		yanchengmaView.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		userNameView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				phone_error_msg.setVisibility(View.GONE);
			}
		});
		passwordView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pass_empty_msg.setVisibility(View.GONE);
			}
		});
		againPasswordView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pass_error_msg.setVisibility(View.GONE);
			}
		});
		yanchengmaView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkcode_error_msg.setVisibility(View.GONE);
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
		againPasswordView
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							checkAgainPass();
							return true;
						}
						return false;
					}
				});
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
		phone_error_msg.setVisibility(View.GONE);
	}

	public void clearPasswordAgain(View v) {
		againPasswordView.setText("");
		pass_error_msg.setVisibility(View.GONE);
	}

	public void clearPassword(View v) {
		passwordView.setText("");
	}

	public void clearYanzhengma(View v) {
		yanchengmaView.setText("");
	}

	public void register(View v) {
		checkPhone();
		checkPass();
		checkAgainPass();
		checkCheckCode();
		if (phone_error_msg.getVisibility() == View.GONE
				&& pass_error_msg.getVisibility() == View.GONE
				&& pass_empty_msg.getVisibility() == View.GONE
				&& checkcode_error_msg.getVisibility() == View.GONE) {
			if (deviceId == null) {
				checkcode_error_msg.setText(mContext.getResources().getString(
						R.string.str_qzqsryzm));
				checkcode_error_msg.setVisibility(View.VISIBLE);
			} else {
				dialog.show();
				String checkCode = yanchengmaView.getText().toString();

				username = userNameView.getText().toString();
				password = passwordView.getText().toString();
				checkCode = yanchengmaView.getText().toString();
				final String passMd5 = MD5Utils.getInstance().getStringHash(
						password);
				RegisterData registerData = new RegisterData();
				registerData.setUsername(username);
				registerData.setPassword(passMd5);
				registerData.setCheckCode(checkCode);
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
											.obtainMessage(REGISTER_COMPLETE);
									mHandler.sendMessage(updateViewMsg);
									break;
								case 408:
									updateViewMsg = mHandler
											.obtainMessage(CHECKCODE_TIMEOUT);
									mHandler.sendMessage(updateViewMsg);
									break;
								case 403:
									updateViewMsg = mHandler
											.obtainMessage(CHECKCODE_ERROR);
									mHandler.sendMessage(updateViewMsg);
									break;
								case 406:
									updateViewMsg = mHandler
											.obtainMessage(PHONE_NUM_ERROR);
									mHandler.sendMessage(updateViewMsg);
									break;
								case 409:
									updateViewMsg = mHandler
											.obtainMessage(ALREADY_REGISTER);
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
						.register(deviceId, registerData);
			}
		}
	}

	public void checkPhone() {
		if (!CheckMobileAndEmailAndPost.isMobileNO(userNameView.getText() + "")) {
			phone_error_msg.setText(mContext.getResources().getString(
					R.string.str_wrong_phone));
			phone_error_msg.setVisibility(View.VISIBLE);
		} else {
			phone_error_msg.setVisibility(View.GONE);
		}
	}

	public void checkPass() {
		if (passwordView.getText().toString() == null
				|| passwordView.getText().toString().equals("")) {
			pass_empty_msg.setText(mContext.getResources().getString(
					R.string.str_pass_empty));
			pass_empty_msg.setVisibility(View.VISIBLE);
		} else {
			pass_empty_msg.setVisibility(View.GONE);
		}
	}

	public void checkAgainPass() {
		if (passwordView.getText().toString()
				.equals(againPasswordView.getText().toString())) {
			pass_error_msg.setVisibility(View.GONE);
		} else {
			pass_error_msg.setText(mContext.getResources().getString(
					R.string.str_register_password_nomatch));
			pass_error_msg.setVisibility(View.VISIBLE);
		}
	}

	public void checkCheckCode() {
		if (yanchengmaView.getText().toString() == null
				|| yanchengmaView.getText().toString().equals("")) {
			checkcode_error_msg.setText(mContext.getResources().getString(
					R.string.str_qzqsryzm));
			checkcode_error_msg.setVisibility(View.VISIBLE);
		} else {
			checkcode_error_msg.setVisibility(View.GONE);
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

	public void getCheckCode(View v) {
		checkPhone();
		if (phone_error_msg.getVisibility() == View.GONE) {
			String phone = userNameView.getText() + "";
			ControllerManager.getInstance().getLoginController()
					.unRegisterAll();
			ControllerManager.getInstance().getLoginController()
					.registerNotification(new ViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								DeviceData deviceData = GsonTool.getGsonTool()
										.fromJson(obj.getData(),
												DeviceData.class);
								deviceId = deviceData.getUuid();
								break;
							case 409:
								updateViewMsg = mHandler
										.obtainMessage(ALREADY_REGISTER);
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
					.getCheckCode(phone);
		}
	}
}
