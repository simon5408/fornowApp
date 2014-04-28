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
package com.fornow.app.ui.addressmanager;

import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.ShipAddressData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.ui.customdialog.LoginDialog;
import com.fornow.app.utils.CheckMobileAndEmailAndPost;
import com.fornow.app.utils.GsonTool;
import com.fornow.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * @author Simon Lv 2013-11-7
 */
public class EditShipAddressActivity extends Activity implements
		OnFocusChangeListener {
	private ShipAddressData address = null;
	private boolean isDefault = false;
	private int CHARMAXLIMIT = 140, CHARMINLIMIT = 2;
	private String addressId;
	private Handler mHandler;
	private ImageButton defaultCheck;
	private TextView addressDetail, addressDetailError, addressName,
			addressNameError, addressPhone, addressPhoneError, addressPostCode,
			addressPostCodeError;
	private LinearLayout afterEditAddressDetailContainer,
			afterEditAddressNameContainer, afterEditAddressPhoneContainer,
			afterEditAddressPostCodeContainer;
	private EditText editAddressDetail, editAddressName, editAddressPhone,
			editAddressPostCode;
	private static final int NET_ERROR = 0x01, UUID_TIMEOUT = 0x02,
			UPDATE_ADDRESS_ERROR = 0x03;
	private Context mContext;
	private Dialog dialog;
	ShipAddressData newAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this.getApplicationContext();
		setContentView(R.layout.edit_address);
		prepareView();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				Toast toast = new Toast(EditShipAddressActivity.this);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(view);
				switch (msg.what) {
				case NET_ERROR:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_net_error));
					toast.show();
					break;
				case UPDATE_ADDRESS_ERROR:
					dialog.dismiss();
					toastText.setText(getResources().getString(
							R.string.str_updatedizi_not_exists));
					toast.show();
					break;
				case UUID_TIMEOUT:
					dialog.dismiss();
					LoginDialog loginDialog = new LoginDialog(
							EditShipAddressActivity.this, mContext, mContext
									.getResources().getString(
											R.string.str_tishi), mContext
									.getResources().getString(
											R.string.str_uuid_timeout), 0);
					loginDialog.build();
					break;
				default:
					break;
				}
			}
		};
	}

	public void prepareView() {
		dialog = new LoadingAnim(EditShipAddressActivity.this,
				R.style.my_dialog);
		defaultCheck = (ImageButton) findViewById(R.id.edit_address_default_check);
		addressDetail = (TextView) findViewById(R.id.after_edit_address_detail);
		addressDetailError = (TextView) findViewById(R.id.after_edit_address_detail_error);
		addressName = (TextView) findViewById(R.id.after_edit_address_name);
		addressNameError = (TextView) findViewById(R.id.after_edit_address_name_error);
		addressPhone = (TextView) findViewById(R.id.after_edit_address_phone);
		addressPhoneError = (TextView) findViewById(R.id.after_edit_address_phone_error);
		addressPostCode = (TextView) findViewById(R.id.after_edit_address_postcode);
		addressPostCodeError = (TextView) findViewById(R.id.after_edit_address_postcode_error);
		afterEditAddressDetailContainer = (LinearLayout) findViewById(R.id.after_edit_address_detail_container);
		afterEditAddressNameContainer = (LinearLayout) findViewById(R.id.after_edit_address_name_container);
		afterEditAddressPhoneContainer = (LinearLayout) findViewById(R.id.after_edit_address_phone_container);
		afterEditAddressPostCodeContainer = (LinearLayout) findViewById(R.id.after_edit_address_postcode_container);
		editAddressDetail = (EditText) findViewById(R.id.edit_address_detail);
		editAddressName = (EditText) findViewById(R.id.edit_address_name);
		editAddressPhone = (EditText) findViewById(R.id.edit_address_phone);
		editAddressPostCode = (EditText) findViewById(R.id.edit_address_postcode);
		editAddressDetail.setOnFocusChangeListener(this);
		editAddressName.setOnFocusChangeListener(this);
		editAddressPhone.setOnFocusChangeListener(this);
		editAddressPostCode.setOnFocusChangeListener(this);
		editAddressDetail
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							storeData2Text(afterEditAddressDetailContainer,
									editAddressDetail, addressDetail);
							return true;
						}
						return false;
					}
				});
		editAddressName.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					storeData2Text(afterEditAddressNameContainer,
							editAddressName, addressName);
					return true;
				}
				return false;
			}
		});
		editAddressPhone
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							storeData2Text(afterEditAddressPhoneContainer,
									editAddressPhone, addressPhone);
							return true;
						}
						return false;
					}
				});
		editAddressPostCode
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							storeData2Text(afterEditAddressPostCodeContainer,
									editAddressPostCode, addressPostCode);
							return true;
						}
						return false;
					}
				});

		Intent intent = getIntent();
		if (intent.getExtras() != null
				&& intent.getExtras().get("addressData") != null) {
			try {
				address = GsonTool.getGsonTool().fromJson(
						intent.getExtras().get("addressData").toString(),
						ShipAddressData.class);
				if (address.getName() != null) {
					addressName.setText(address.getName());
				}
				if (address.getPhone() != null) {
					addressPhone.setText(address.getPhone());
				}
				if (address.getAddress() != null) {
					addressDetail.setText(address.getAddress());
				}
				if (address.getPostcode() != null) {
					addressPostCode.setText(address.getPostcode());
				}

				if (address.isIsdefault()) {
					isDefault = true;
					defaultCheck.setBackgroundResource(R.drawable.checkbox_yes);
				} else {
					isDefault = false;
					defaultCheck.setBackgroundResource(R.drawable.checkbox_no);
				}
				addressId = address.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		newAddress = (address == null) ? new ShipAddressData() : address;
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

	public void selectRegion(View v) {
		Intent intent = new Intent(EditShipAddressActivity.this,
				SelectProvince.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Activity.RESULT_OK:
			String area_id = data.getExtras().getString("areaId");
			newAddress.setArea_id(area_id);
			setResult(Activity.RESULT_OK, data);
			break;
		default:
			break;
		}
	}

	public void editAddressDetail(View v) {
		getFoucus(v);
		addressDetailError.setVisibility(View.GONE);
		inputData2EditText(afterEditAddressDetailContainer, addressDetail,
				editAddressDetail);
	}

	public void editAddressName(View v) {
		getFoucus(v);
		addressNameError.setVisibility(View.GONE);
		inputData2EditText(afterEditAddressNameContainer, addressName,
				editAddressName);
	}

	public void editAddressPhone(View v) {
		getFoucus(v);
		addressPhoneError.setVisibility(View.GONE);
		editAddressPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		inputData2EditText(afterEditAddressPhoneContainer, addressPhone,
				editAddressPhone);
	}

	public void editAddressPostCode(View v) {
		getFoucus(v);
		addressPostCodeError.setVisibility(View.GONE);
		editAddressPostCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		inputData2EditText(afterEditAddressPostCodeContainer, addressPostCode,
				editAddressPostCode);
	}

	public void softBack(View v) {
		this.finish();
	}

	public void boolDefault(View v) {
		getFoucus(v);
		if (isDefault) {
			isDefault = false;
			defaultCheck.setBackgroundResource(R.drawable.checkbox_no);
		} else {
			isDefault = true;
			defaultCheck.setBackgroundResource(R.drawable.checkbox_yes);
		}
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
		editAddressDetail.clearFocus();
		editAddressName.clearFocus();
		editAddressPhone.clearFocus();
		editAddressPostCode.clearFocus();
	}

	public void deleteAddress(View v) {
		dialog.show();
		getFoucus(v);
		ControllerManager.getInstance().getAddressManageController()
				.unRegisterAll();
		ControllerManager.getInstance().getAddressManageController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						// TODO Auto-generated method stub
						Message updateViewMsg;
						switch (obj.getCode()) {
						case 200:
							dialog.dismiss();
							EditShipAddressActivity.this.finish();
							break;
						case 408:
							updateViewMsg = mHandler
									.obtainMessage(UUID_TIMEOUT);
							mHandler.sendMessage(updateViewMsg);
							break;
						case 404:
							updateViewMsg = mHandler
									.obtainMessage(UPDATE_ADDRESS_ERROR);
							mHandler.sendMessage(updateViewMsg);
							break;
						default:
							updateViewMsg = mHandler.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
							break;
						}
					}
				});
		ControllerManager.getInstance().getAddressManageController()
				.deleteShipingAddress(addressId);
	}

	public void saveAddress(View v) {
		getFoucus(v);
		clearFoucus();
		checkNecessaryData();
		if (addressNameError.getVisibility() == View.GONE
				&& addressPhoneError.getVisibility() == View.GONE
				&& addressDetailError.getVisibility() == View.GONE) {
			dialog.show();
			newAddress.setPhone(addressPhone.getText().toString());
			newAddress.setName(addressName.getText().toString());
			newAddress.setAddress(addressDetail.getText().toString());
			newAddress.setIsdefault(isDefault);
			if (addressId != null && address.getArea_id() != null) {
				newAddress.setId(addressId);
				newAddress.setArea_id(address.getArea_id());
			}

			ControllerManager.getInstance().getAddressManageController()
					.unRegisterAll();
			ControllerManager.getInstance().getAddressManageController()
					.registerNotification(new ViewListener() {

						@Override
						public void updateView(ViewUpdateObj obj) {
							// TODO Auto-generated method stub
							Message updateViewMsg;
							switch (obj.getCode()) {
							case 200:
								EditShipAddressActivity.this.finish();
								break;
							case 408:
								updateViewMsg = mHandler
										.obtainMessage(UUID_TIMEOUT);
								mHandler.sendMessage(updateViewMsg);
								break;
							case 404:
								updateViewMsg = mHandler
										.obtainMessage(UPDATE_ADDRESS_ERROR);
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
			ControllerManager.getInstance().getAddressManageController()
					.updateShippingAddress(newAddress);
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (v == editAddressDetail) {
			if (!hasFocus) {
				storeData2Text(afterEditAddressDetailContainer,
						editAddressDetail, addressDetail);
			}
		} else if (v == editAddressName) {
			if (!hasFocus) {
				storeData2Text(afterEditAddressNameContainer, editAddressName,
						addressName);
			}
		} else if (v == editAddressPhone) {
			if (!hasFocus) {
				storeData2Text(afterEditAddressPhoneContainer,
						editAddressPhone, addressPhone);
			}
		} else if (v == editAddressPostCode) {
			if (!hasFocus) {
				storeData2Text(afterEditAddressPostCodeContainer,
						editAddressPostCode, addressPostCode);
			}
		}
	}

	public void storeData2Text(LinearLayout container, EditText from,
			TextView to) {
		container.setVisibility(View.VISIBLE);
		from.setVisibility(View.GONE);
		to.setText(from.getText() + "");
		if (to == addressDetail) {
			if (addressDetail.getText().toString().length() > CHARMAXLIMIT) {
				addressDetailError.setText(mContext.getResources().getString(
						R.string.str_wrong_address_detail));
				addressDetailError.setVisibility(View.VISIBLE);
			} else {
				addressDetailError.setVisibility(View.GONE);
			}
		} else if (to == addressName) {
			if (addressName.getText().toString().length() > CHARMAXLIMIT
					|| addressName.getText().toString().length() < CHARMINLIMIT) {
				addressNameError.setText(mContext.getResources().getString(
						R.string.str_wrong_address_name));
				addressNameError.setVisibility(View.VISIBLE);
			} else {
				addressNameError.setVisibility(View.GONE);
			}
		} else if (to == addressPhone) {
			if (!CheckMobileAndEmailAndPost.isMobileNO(to.getText() + "")) {
				addressPhoneError.setText(mContext.getResources().getString(
						R.string.str_wrong_phone));
				addressPhoneError.setVisibility(View.VISIBLE);
			} else {
				addressPhoneError.setVisibility(View.GONE);
			}
		} else if (to == addressPostCode) {
			if (!to.getText().toString().equals("")
					&& !CheckMobileAndEmailAndPost.checkPost(to.getText() + "")) {
				addressPostCodeError.setText(mContext.getResources().getString(
						R.string.str_wrong_postCode));
				addressPostCodeError.setVisibility(View.VISIBLE);
			} else {
				addressPostCodeError.setVisibility(View.GONE);
			}
		}
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

	public void checkNecessaryData() {
		if (addressName.getText() == null || addressName.getText().equals("")) {
			addressNameError.setText(mContext.getResources().getString(
					R.string.str_empty_error_address_name));
			addressNameError.setVisibility(View.VISIBLE);
		}
		if (addressPhone.getText() == null || addressPhone.getText().equals("")) {
			addressPhoneError.setText(mContext.getResources().getString(
					R.string.str_empty_error_phone));
			addressPhoneError.setVisibility(View.VISIBLE);
		}
		if (addressDetail.getText() == null
				|| addressDetail.getText().equals("")) {
			addressDetailError.setText(mContext.getResources().getString(
					R.string.str_empty_error_address_detail));
			addressDetailError.setVisibility(View.VISIBLE);
		}
	}

}
