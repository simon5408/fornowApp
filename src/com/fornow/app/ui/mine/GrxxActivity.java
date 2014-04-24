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

import java.util.Calendar;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.UserInfo;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.util.GsonTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class GrxxActivity extends Activity {

	private TextView zhanghaoView, phoneView, emailView, nameView, sexView,
			birthdayView;
	private UserInfo user;
	private static final int START_UPDATE = 0x00, NET_ERROR = 0x01;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gerenxinxi);
		zhanghaoView = (TextView) findViewById(R.id.grxx_zhanghao);
		phoneView = (TextView) findViewById(R.id.grxx_phone);
		emailView = (TextView) findViewById(R.id.grxx_email);
		nameView = (TextView) findViewById(R.id.grxx_name);
		sexView = (TextView) findViewById(R.id.grxx_sex);
		birthdayView = (TextView) findViewById(R.id.grxx_age);
		initValue();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NET_ERROR:
					View view = getLayoutInflater().inflate(R.layout.my_toast,
							null);
					TextView toastText = (TextView) view
							.findViewById(R.id.toast_text);
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					Toast toast = new Toast(GrxxActivity.this);
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
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void initValue() {
		String userInfo = ClientData.getInstance().getUser();
		if (userInfo != null) {
			try {
				user = GsonTool.getGsonTool()
						.fromJson(userInfo, UserInfo.class);
				if (user.getUser_account() != null) {
					zhanghaoView.setText(user.getUser_account());
				}
				if (user.getPhone() != null) {
					phoneView.setText(user.getPhone());
				}
				if (user.getEmail() != null) {
					emailView.setText(user.getEmail());
				}
				if (user.getUser_name() != null) {
					nameView.setText(user.getUser_name());
				}
				if (user.getSex() != null) {
					sexView.setText(user.getSex());
				}
				if (user.getBirthday() != null) {
					birthdayView.setText(String.valueOf(user.getBirthday()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case 0:// editPhone
				if (data.getExtras() != null
						&& data.getExtras().get("data") != null) {
					user.setPhone(data.getExtras().get("data").toString());
					ControllerManager.getInstance().getLoginController()
							.unRegisterAll();
					ControllerManager.getInstance().getLoginController()
							.registerNotification(new ViewListener() {

								@Override
								public void updateView(ViewUpdateObj obj) {
									// TODO Auto-generated method stub
									if (obj.getCode() == 200) {
										phoneView.post(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												phoneView
														.setText(data
																.getExtras()
																.get("data")
																.toString());
											}
										});
									} else {
										Message updateViewMsg = mHandler
												.obtainMessage(NET_ERROR);
										mHandler.sendMessage(updateViewMsg);
									}
								}
							});

					ControllerManager.getInstance().getLoginController()
							.updateUser(user);
				}
				break;
			case 1:// editEmail
				if (data.getExtras() != null
						&& data.getExtras().get("data") != null) {
					user.setEmail(data.getExtras().get("data").toString());
					ControllerManager.getInstance().getLoginController()
							.unRegisterAll();
					ControllerManager.getInstance().getLoginController()
							.registerNotification(new ViewListener() {

								@Override
								public void updateView(ViewUpdateObj obj) {
									// TODO Auto-generated method stub
									if (obj.getCode() == 200) {
										emailView.post(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												emailView
														.setText(data
																.getExtras()
																.get("data")
																.toString());
											}
										});
									} else {
										Message updateViewMsg = mHandler
												.obtainMessage(NET_ERROR);
										mHandler.sendMessage(updateViewMsg);
									}
								}
							});

					ControllerManager.getInstance().getLoginController()
							.updateUser(user);
				}
				break;
			case 2:// editName
				if (data.getExtras() != null
						&& data.getExtras().get("data") != null) {
					user.setUser_name(data.getExtras().get("data").toString());
					ControllerManager.getInstance().getLoginController()
							.unRegisterAll();
					ControllerManager.getInstance().getLoginController()
							.registerNotification(new ViewListener() {

								@Override
								public void updateView(ViewUpdateObj obj) {
									// TODO Auto-generated method stub
									if (obj.getCode() == 200) {
										nameView.post(new Runnable() {
											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												nameView.setText(data
														.getExtras()
														.get("data").toString());
											}
										});
									} else {
										Message updateViewMsg = mHandler
												.obtainMessage(NET_ERROR);
										mHandler.sendMessage(updateViewMsg);
									}
								}
							});

					ControllerManager.getInstance().getLoginController()
							.updateUser(user);
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	public void editPhone(View v) {
		Intent intent = new Intent(GrxxActivity.this, EditGrxxActivity.class);
		intent.putExtra("title", getResources().getString(R.string.str_sjhm));
		if (user.getPhone() != null) {
			intent.putExtra("data", user.getPhone());
		}
		startActivityForResult(intent, 0);
	}

	public void editEmail(View v) {
		Intent intent = new Intent(GrxxActivity.this, EditGrxxActivity.class);
		intent.putExtra("title", getResources().getString(R.string.str_dzyx));
		if (user.getEmail() != null) {
			intent.putExtra("data", user.getEmail());
		}
		startActivityForResult(intent, 1);
	}

	public void editName(View v) {
		Intent intent = new Intent(GrxxActivity.this, EditGrxxActivity.class);
		intent.putExtra("title", getResources()
				.getString(R.string.str_xingming));
		if (user.getUser_name() != null) {
			intent.putExtra("data", user.getUser_name());
		}
		startActivityForResult(intent, 2);
	}

	public void editSex(View v) {
		final String[] array = new String[] {
				getResources().getString(R.string.str_male),
				getResources().getString(R.string.str_female) };
		String currentSex = sexView.getText().toString();
		int defaultIndex = 0;
		if (currentSex.equals(array[0])) {
			defaultIndex = 0;
		} else if (currentSex.equals(array[1])) {
			defaultIndex = 1;
		}
		Dialog alertDialog = new AlertDialog.Builder(GrxxActivity.this)
				.setSingleChoiceItems(array, defaultIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								final String sex = array[which];
								user.setSex(sex);
								ControllerManager.getInstance()
										.getLoginController().unRegisterAll();
								ControllerManager
										.getInstance()
										.getLoginController()
										.registerNotification(
												new ViewListener() {

													@Override
													public void updateView(
															ViewUpdateObj obj) {
														// TODO Auto-generated
														// method stub
														if (obj.getCode() == 200) {
															sexView.post(new Runnable() {

																@Override
																public void run() {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	sexView.setText(sex);
																}
															});
														} else {
															Message updateViewMsg = mHandler
																	.obtainMessage(NET_ERROR);
															mHandler.sendMessage(updateViewMsg);
														}
													}
												});
								ControllerManager.getInstance()
										.getLoginController().updateUser(user);
								dialog.dismiss();
							}
						}).create();
		alertDialog.show();
	}

	public void editBirthday(View v) {
		Calendar c = Calendar.getInstance();
		Dialog dateDialog = new DatePickerDialog(
				GrxxActivity.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker dp, int year, int month,
							int dayOfMonth) {
						final String birthday = year + "-" + (month + 1) + "-"
								+ dayOfMonth;
						user.setBirthday(birthday);
						ControllerManager.getInstance().getLoginController()
								.unRegisterAll();
						ControllerManager.getInstance().getLoginController()
								.registerNotification(new ViewListener() {

									@Override
									public void updateView(ViewUpdateObj obj) {
										// TODO Auto-generated
										// method stub
										if (obj.getCode() == 200) {
											birthdayView.post(new Runnable() {

												@Override
												public void run() {
													// TODO
													// Auto-generated
													// method
													// stub
													birthdayView
															.setText(birthday);
												}
											});
										} else {
											Message updateViewMsg = mHandler
													.obtainMessage(NET_ERROR);
											mHandler.sendMessage(updateViewMsg);
										}
									}
								});
						ControllerManager.getInstance().getLoginController()
								.updateUser(user);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		dateDialog.show();
	}

	public void softBack(View v) {
		this.finish();
	}

}
