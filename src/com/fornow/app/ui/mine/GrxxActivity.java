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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.datapool.ClientData;
import com.fornow.app.model.UserInfo;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.service.IViewListener;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.customdialog.LoginDialog;
import com.fornow.app.utils.CheckUtils;
import com.fornow.app.utils.JSONHelper;

/**
 * @author Simon Lv 2013-11-5
 */
public class GrxxActivity extends Activity implements OnFocusChangeListener {

	private TextView zhanghaoView, phoneView, emailView, nameView, sexView,
			birthdayView, phoneError, nameError, emailError;
	private UserInfo user;
	private int CHARMAXLIMIT = 140, CHARMINLIMIT = 2;
	//START_UPDATE = 0x00, 
	private static final int NET_ERROR = 0x01,
			UUID_TIMEOUT = 0x02;
	private LinearLayout afterEditProfilePhoneContainer,
			afterEditProfileNameContainer, afterEditProfileEmailContainer;
	private EditText editProfilePhone, editProfileName, editProfileEmail;
	private Handler mHandler;
	private Context mContext;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gerenxinxi);
		mContext = this.getApplicationContext();
		initView();
		initValue();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case UUID_TIMEOUT:
					dialog.dismiss();
					LoginDialog loginDialog = new LoginDialog(
							GrxxActivity.this, mContext, mContext
									.getResources().getString(
											R.string.str_tishi), mContext
									.getResources().getString(
											R.string.str_uuid_timeout), 0);
					loginDialog.build();
					break;
				case NET_ERROR:
					dialog.dismiss();
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

	private void initView() {
		dialog = new LoadingAnim(GrxxActivity.this, R.style.my_dialog);
		zhanghaoView = (TextView) findViewById(R.id.grxx_zhanghao);
		phoneView = (TextView) findViewById(R.id.grxx_phone);
		emailView = (TextView) findViewById(R.id.grxx_email);
		nameView = (TextView) findViewById(R.id.grxx_name);
		sexView = (TextView) findViewById(R.id.grxx_sex);
		birthdayView = (TextView) findViewById(R.id.grxx_age);
		phoneError = (TextView) findViewById(R.id.after_edit_profile_phone_error);
		nameError = (TextView) findViewById(R.id.after_edit_profile_name_error);
		emailError = (TextView) findViewById(R.id.after_edit_profile_email_error);
		afterEditProfilePhoneContainer = (LinearLayout) findViewById(R.id.after_edit_profile_phone_container);
		afterEditProfileNameContainer = (LinearLayout) findViewById(R.id.after_edit_profile_name_container);
		afterEditProfileEmailContainer = (LinearLayout) findViewById(R.id.after_edit_profile_email_container);
		editProfilePhone = (EditText) findViewById(R.id.edit_profile_phone);
		editProfileName = (EditText) findViewById(R.id.edit_profile_name);
		editProfileEmail = (EditText) findViewById(R.id.edit_profile_email);
		editProfilePhone.setOnFocusChangeListener(this);
		editProfileName.setOnFocusChangeListener(this);
		editProfileEmail.setOnFocusChangeListener(this);
		editProfilePhone
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							storeData2Text(afterEditProfilePhoneContainer,
									editProfilePhone, phoneView);
							return true;
						}
						return false;
					}
				});

		editProfileName.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					storeData2Text(afterEditProfileNameContainer,
							editProfileName, nameView);
					return true;
				}
				return false;
			}
		});
		editProfileEmail
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							storeData2Text(afterEditProfileEmailContainer,
									editProfileEmail, emailView);
							return true;
						}
						return false;
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case 0:
				break;
			default:
				break;
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	public void editPhone(View v) {
		getFoucus(v);
		phoneError.setVisibility(View.GONE);
		editProfilePhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		inputData2EditText(afterEditProfilePhoneContainer, phoneView,
				editProfilePhone);
	}

	public void editEmail(View v) {
		getFoucus(v);
		emailError.setVisibility(View.GONE);
		inputData2EditText(afterEditProfileEmailContainer, emailView,
				editProfileEmail);
	}

	public void editName(View v) {
		getFoucus(v);
		nameError.setVisibility(View.GONE);
		inputData2EditText(afterEditProfileNameContainer, nameView,
				editProfileName);
	}

	public void getFoucus(View v) {
		// InputMethodManager imm = (InputMethodManager) v
		// .getContext().getSystemService(
		// Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		// v.setFocusable(true);
		// v.setFocusableInTouchMode(true);
		v.requestFocus();
	}

	public void clearFoucus() {
		editProfilePhone.clearFocus();
		editProfileEmail.clearFocus();
		editProfileName.clearFocus();
	}

	public void inputData2EditText(LinearLayout container, TextView from,
			EditText to) {
		container.setVisibility(View.GONE);
		to.setVisibility(View.VISIBLE);
		to.setText(from.getText() + "");
		to.requestFocus();
		Spannable spanText = (Spannable) to.getText();
		Selection.setSelection(spanText, to.getText().length());
	}

	public void storeData2Text(LinearLayout container, EditText from,
			TextView to) {
		container.setVisibility(View.VISIBLE);
		from.setVisibility(View.GONE);
		to.setText(from.getText() + "");
		if (to == phoneView) {
			if (!to.getText().toString().equals("")
					&& !CheckUtils
							.isMobileNO(to.getText() + "")) {
				phoneError.setText(mContext.getResources().getString(
						R.string.str_wrong_phone));
				phoneError.setVisibility(View.VISIBLE);
			} else {
				phoneError.setVisibility(View.GONE);
			}
		} else if (to == nameView) {
			if (!to.getText().toString().equals("")) {
				if (nameView.getText().toString().length() > CHARMAXLIMIT
						|| nameView.getText().toString().length() < CHARMINLIMIT) {
					nameError.setText(mContext.getResources().getString(
							R.string.str_wrong_address_name));
					nameError.setVisibility(View.VISIBLE);
				} else {
					nameError.setVisibility(View.GONE);
				}
			} else {
				nameError.setVisibility(View.GONE);
			}

		} else if (to == emailView) {
			if (!to.getText().toString().equals("")
					&& !CheckUtils
							.checkEmail(to.getText() + "")) {
				emailError.setText(mContext.getResources().getString(
						R.string.str_email_error));
				emailError.setVisibility(View.VISIBLE);
			} else {
				emailError.setVisibility(View.GONE);
			}
		}
	}

	public void initValue() {
		String userInfo = ClientData.getInstance().getUser();
		if (userInfo != null) {
			try {
				user = JSONHelper
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

	public void saveProfile(View v) {
		getFoucus(v);
		clearFoucus();
		if (phoneError.getVisibility() == View.GONE
				&& nameError.getVisibility() == View.GONE
				&& emailError.getVisibility() == View.GONE) {
			dialog.show();
			user.setPhone(phoneView.getText() + "");
			user.setEmail(emailView.getText() + "");
			user.setUser_name(nameView.getText() + "");
			user.setSex(sexView.getText() + "");
			user.setBirthday(birthdayView.getText() + "");

			ControllerManager.getInstance().getLoginController()
					.unRegisterAll();
			ControllerManager.getInstance().getLoginController()
					.registerNotification(new IViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								dialog.dismiss();
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
					.updateUser(user);
		}
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
								
								final String sex = array[which];
								user.setSex(sex);
								ControllerManager.getInstance()
										.getLoginController().unRegisterAll();
								ControllerManager
										.getInstance()
										.getLoginController()
										.registerNotification(
												new IViewListener() {

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
								.registerNotification(new IViewListener() {

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

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if (v == editProfilePhone) {
			if (!hasFocus) {
				storeData2Text(afterEditProfilePhoneContainer,
						editProfilePhone, phoneView);
			}
		} else if (v == editProfileName) {
			if (!hasFocus) {
				storeData2Text(afterEditProfileNameContainer, editProfileName,
						nameView);
			}
		} else if (v == editProfileEmail) {
			if (!hasFocus) {
				storeData2Text(afterEditProfileEmailContainer,
						editProfileEmail, emailView);
			}
		}
	}

}
